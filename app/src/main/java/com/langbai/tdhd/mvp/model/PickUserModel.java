package com.langbai.tdhd.mvp.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.easeui.domain.EaseUser;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.activity.chat.ContactListFragment;
import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.MeetingApi;
import com.langbai.tdhd.api.NewFriendsApi;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.FriendsResponseBean;
import com.langbai.tdhd.bean.MeetingFriends;
import com.langbai.tdhd.cache.UserCacheManager;
import com.langbai.tdhd.common.MyApplication;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.utils.DiskCacheManager;
import com.langbai.tdhd.utils.LogUtil;
import com.langbai.tdhd.utils.RetrofitErrorUtlis;

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
 * Created by Moushao on 2017/10/17.
 */

public class PickUserModel {
    public void getGroupUserList(final Context context, final MVPCallBack<List<FriendsResponseBean>> mBack) {
        ApiManager.getInstance()//
                .getFriendsApi()//
                .getFriendsApi(UserHelper.getInstance().getLogUser().getUserInfoID())//
                .map(new Function<BaseResponseBean<ArrayList<FriendsResponseBean>>, ArrayList<FriendsResponseBean>>() {
                    @Override
                    public ArrayList<FriendsResponseBean> apply(BaseResponseBean<ArrayList<FriendsResponseBean>>
                                                                        response) {
                        LogUtil.e("线程打印", "第一步" + Thread.currentThread().getName());
                        if ("100".equals(response.getStatus())) {
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
                .map(new Function<FriendsResponseBean, FriendsResponseBean>() {
                    @Override
                    public FriendsResponseBean apply(FriendsResponseBean friends) throws Exception {
                        EaseUser baseItem = new EaseUser(UserHelper.getEMID(friends));
                        baseItem.setAvatar(friends.getFriendIcon());
                        baseItem.setNickname(UserHelper.getEMNickName(friends));
                        UserCacheManager.save(baseItem);
                        return friends;
                    }
                })//
                .toList() //
                .subscribeOn(Schedulers.io())//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new SingleObserver<List<FriendsResponseBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<FriendsResponseBean> value) {
                        mBack.succeed(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
                    }
                });
    }

    public void getMeetingUserList(Context context, final MVPCallBack<List<MeetingFriends>> mBack) {
        MeetingApi service = ApiManager.getInstance().getMeetingApi();
        Call<BaseResponseBean<List<MeetingFriends>>> call = service.getMeetFriendsList(UserHelper.getInstance()
                .getLogUser().getUserInfoID());
        call.enqueue(new Callback<BaseResponseBean<List<MeetingFriends>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<List<MeetingFriends>>> call,
                                   Response<BaseResponseBean<List<MeetingFriends>>> response) {
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(response.body().getData());
                } else {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<List<MeetingFriends>>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }
}
