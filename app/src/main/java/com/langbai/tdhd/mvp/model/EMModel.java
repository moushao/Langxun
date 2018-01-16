package com.langbai.tdhd.mvp.model;

import android.content.Context;
import android.widget.Toast;

import com.hyphenate.easeui.domain.EaseUser;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.activity.chat.ContactListFragment;
import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.EMApi;
import com.langbai.tdhd.api.FriendsSettingApi;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.FriendsResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.bean.SearchBean;
import com.langbai.tdhd.cache.UserCacheManager;
import com.langbai.tdhd.common.MyApplication;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.utils.DiskCacheManager;
import com.langbai.tdhd.utils.LogUtil;
import com.langbai.tdhd.utils.RetrofitErrorUtlis;
import com.langbai.tdhd.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Moushao on 2017/9/28.
 */

public class EMModel {
    public void getContractList(final Context context, final MVPCallBack<List<EaseUser>> mBack) {

        ApiManager.getInstance()//
                .getFriendsApi()//
                .getFriendsApi(UserHelper.getInstance().getLogUser().getUserInfoID())//
                .map(new Function<BaseResponseBean<ArrayList<FriendsResponseBean>>, ArrayList<FriendsResponseBean>>() {
                    @Override
                    public ArrayList<FriendsResponseBean>//
                    apply(BaseResponseBean<ArrayList<FriendsResponseBean>> response) {
                        if ("100".equals(response.getStatus())) {
                            DiskCacheManager manager = new DiskCacheManager(context, ContactListFragment.TAG);
                            manager.put(ContactListFragment.TAG, response.getData());
                            new UserUtils(context).saveUserInfo(response.getData());
                            return response.getData();

                        }
                        return null;
                    }
                })//
                .flatMap(new Function<ArrayList<FriendsResponseBean>, Observable<FriendsResponseBean>>() {
                    @Override
                    public Observable<FriendsResponseBean> apply(ArrayList<FriendsResponseBean> friends) {
                        return Observable.fromIterable(friends);
                    }
                })//
                .map(new Function<FriendsResponseBean, EaseUser>() {
                    @Override
                    public EaseUser apply(FriendsResponseBean friends) throws Exception {
                        EaseUser baseItem = new EaseUser(UserHelper.getEMID(friends));
                        baseItem.setAvatar(friends.getFriendIcon());
                        baseItem.setNickname(UserHelper.getEMNickName(friends));
                        baseItem.setUserID(friends.getFriendID());
                        UserCacheManager.save(baseItem);
                        return baseItem;
                    }
                })//
                .toList() //
                .subscribeOn(Schedulers.io())//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new SingleObserver<List<EaseUser>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<EaseUser> value) {
                        LogUtil.e("线程打印", "subscribe成功" + Thread.currentThread().getName());
                        mBack.succeed(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
                    }

                });
     /*   EMApi service = ApiManager.getInstance().getFriendsApi();
        Call<BaseResponseBean<ArrayList<FriendsResponseBean>>> call = service.getFriendsApi(UserHelper.getInstance()
                .getLogUser().getUserInfoID());
        call.enqueue(new Callback<BaseResponseBean<ArrayList<FriendsResponseBean>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<FriendsResponseBean>>> call,
                                   Response<BaseResponseBean<ArrayList<FriendsResponseBean>>> response) {

                if ("100".equals(response.body().getStatus())) {
                    DiskCacheManager manager = new DiskCacheManager(context, ContactListFragment.TAG);
                    manager.put(ContactListFragment.TAG, response.body().getData());
                    new UserUtils(context).saveUserInfo(response.body().getData());
                    mBack.succeed(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<FriendsResponseBean>>> call, Throwable t) {

            }
        });*/
    }

    public void searchFriends(String phone, final MVPCallBack<ArrayList<LoginResponseBean>> mBack) {
        EMApi service = ApiManager.getInstance().getFriendsApi();
        Call<SearchBean> call = service.getUserByPhoneOrNameApi(phone, 1, 50);
        call.enqueue(new Callback<SearchBean>() {
            @Override
            public void onResponse(retrofit2.Call<SearchBean> call,
                                   Response<SearchBean> response) {

                if (response.body().getTotal() != 0) {
                    mBack.succeed(response.body().getRows());
                } else {
                    mBack.failed("");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<SearchBean> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    public void deleteFirends(long userId, long friendID, final MVPCallBack<String> mBack) {
        FriendsSettingApi service = ApiManager.getInstance().getFriendsSettingApi();
        Call<BaseResponseBean> call = service.getDeleteFriendsApi(userId, friendID);

        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                if (response.body() != null)
                    if ("100".equals(response.body().getStatus())) {
                        mBack.succeed("");
                    } else {
                        mBack.failed(response.body().getMessage());
                    }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }
}
