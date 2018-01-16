package com.langbai.tdhd.square.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.langbai.tdhd.bean.SquareBaseBean;
import com.langbai.tdhd.circle.holder.BaseRecyclerViewAdapter;
import com.langbai.tdhd.mvp.presenter.SquarePresenter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 朋友圈adapter
 */

public class SquareAdapter extends BaseRecyclerViewAdapter<SquareBaseBean> {


    private SparseArray<ViewHoldernfo> viewHolderKeyArray;
    private SquarePresenter momentPresenter;


    private SquareAdapter(@NonNull Context context,
                          @NonNull List<SquareBaseBean> datas) {
        super(context, datas);
    }

    private SquareAdapter(Builder builder) {
        this(builder.context, builder.datas);
        this.viewHolderKeyArray = builder.viewHolderKeyArray;
        this.momentPresenter = builder.momentPresenter;
    }

    @Override
    protected int getViewType(int position, @NonNull SquareBaseBean data) {
        //返回朋友圈类型
        if (!TextUtils.isEmpty(data.getVideoPicture())) {
            //视频
            return 2;
        } else if (!isListEmpty(data.getThumbnail())) {
            //图片
            return 1;
        } else {
            //只有文本信息
            return 0;
        }
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return 0;
    }

    @Override
    protected SquareBaseViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
        ViewHoldernfo viewHoldernfo = viewHolderKeyArray.get(viewType);
        if (viewHoldernfo != null) {
            SquareBaseViewHolder squareBaseViewHolder = createCircleViewHolder(context, parent, viewHoldernfo);
            if (squareBaseViewHolder != null) {
                squareBaseViewHolder.setPresenter(momentPresenter);
            }
            return squareBaseViewHolder;
        }
        return null;
    }


    public static final class Builder<T> {
        private Context context;
        private SparseArray<ViewHoldernfo> viewHolderKeyArray = new SparseArray<>();
        private List<T> datas;
        private SquarePresenter momentPresenter;


        public Builder(Context context) {
            this.context = context;
            datas = new ArrayList<>();
        }

        public Builder<T> addType(Class<? extends SquareBaseViewHolder> viewHolderClass,
                                  int viewType,
                                  int layoutResId) {
            final ViewHoldernfo info = new ViewHoldernfo();
            info.holderClass = viewHolderClass;
            info.viewType = viewType;
            info.layoutResID = layoutResId;
            viewHolderKeyArray.put(viewType, info);
            return this;
        }

        public Builder<T> setData(List<T> datas) {
            this.datas = datas;
            return this;
        }

        public Builder<T> setPresenter(SquarePresenter presenter) {
            this.momentPresenter = presenter;
            return this;
        }

        public SquareAdapter build() {
            return new SquareAdapter(this);
        }

    }


    /**
     * vh的信息类
     */
    private static final class ViewHoldernfo {
        Class<? extends SquareBaseViewHolder> holderClass;
        int viewType;
        int layoutResID;
    }

    private SquareBaseViewHolder createCircleViewHolder(Context context, ViewGroup viewGroup, ViewHoldernfo
            viewHoldernfo) {
        if (viewHoldernfo == null) {
            throw new NullPointerException("木有这个viewholder信息哦");
        }
        Class<? extends SquareBaseViewHolder> className = viewHoldernfo.holderClass;
        Constructor constructor = null;
        try {
            constructor = className.getConstructor(Context.class, ViewGroup.class, int.class);
            return (SquareBaseViewHolder) constructor.newInstance(context, viewGroup, viewHoldernfo.layoutResID);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException
                | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }

}
