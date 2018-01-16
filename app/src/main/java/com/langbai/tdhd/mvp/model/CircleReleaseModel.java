package com.langbai.tdhd.mvp.model;

import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.CircleReleaseApi;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.utils.RetrofitErrorUtlis;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Moushao on 2017/10/26.
 */

public class CircleReleaseModel {
    //只有文字
    public void releaseText(String content, Map<String, RequestBody> params, final MVPCallBack<Long> mBack) {
        CircleReleaseApi service = ApiManager.getInstance().getCircleReleaseApi();
        Call<BaseResponseBean<Long>> call = service.getReleaseTextApi(UserHelper.getUserId(), content);
        call.enqueue(new Callback<BaseResponseBean<Long>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<Long>> call, Response<BaseResponseBean<Long>> response) {
                if (response.body() == null) {
                    mBack.failed("");
                    return;
                }
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(response.body().getData());
                } else {
                    mBack.failed(response.body().message);
                }

            }

            @Override
            public void onFailure(Call<BaseResponseBean<Long>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    public void releasePicture(String content, Map<String, RequestBody> params, final MVPCallBack<Long> mBack) {
        CircleReleaseApi service = ApiManager.getInstance().getCircleReleaseApi();
        Call<BaseResponseBean<Long>> call = service.getReleasePictureApi(/*UserHelper.getUserId(), s,*/ params);
        call.enqueue(new Callback<BaseResponseBean<Long>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<Long>> call, Response<BaseResponseBean<Long>> response) {
                if (response.body() == null) {
                    mBack.failed("");
                    return;
                }
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(response.body().getData());
                } else {
                    mBack.failed(response.body().message);
                }

            }

            @Override
            public void onFailure(Call<BaseResponseBean<Long>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    public void releaseVideo(String content, Map<String, RequestBody> params, final MVPCallBack<Long> mBack) {
        CircleReleaseApi service = ApiManager.getInstance().getCircleReleaseApi();
        Call<BaseResponseBean<Long>> call = service.getReleaseVideoApi(/*UserHelper.getUserId(), s,*/ params);
        call.enqueue(new Callback<BaseResponseBean<Long>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<Long>> call, Response<BaseResponseBean<Long>> response) {
                if (response.body() == null) {
                    mBack.failed("");
                    return;
                }
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(response.body().getData());
                } else {
                    mBack.failed(response.body().message);
                }

            }

            @Override
            public void onFailure(Call<BaseResponseBean<Long>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }
}
