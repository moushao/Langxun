package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.GroupMember;
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.circle.ui.photo.ImageLoadMnanger;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.GroupPresenter;
import com.langbai.tdhd.mvp.view.GroupView;
import com.langbai.tdhd.utils.TimeUtil;
import com.langbai.tdhd.widget.CircleImageView;
import com.langbai.tdhd.widget.Dialog.AlertDialog;
import com.langbai.tdhd.widget.Dialog.IOSDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * 类名: {@link GroupCreateActivity}
 * <br/> 功能描述:创建群聊的界面
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/27
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class GroupCreateActivity extends BaseActivity implements GroupView {
    public static final String TAG = "GroupCreateActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.more) TextView mMore;
    @BindView(R.id.select_friends) TextView mSelectFriends;
    @BindView(R.id.has_select) TextView mHasSelect;
    @BindView(R.id.group_name) EditText mGroupName;
    @BindView(R.id.group_head) CircleImageView mGroupHead;
    @BindView(R.id.brife) EditText mBrife;
    private Context mContext;
    private long[] userArray;
    List<LocalMedia> list = new ArrayList<LocalMedia>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_create;
    }

    @Override
    public BasePresenter getPresenter() {
        return new GroupPresenter();
    }

    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        initWidget();
    }

    private void initWidget() {
        mTitleBack.setImageResource(R.drawable.icon_back);
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("新建群组");
        mMore.setText("完成");
        mMore.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.title_back, R.id.more, R.id.select_friends, R.id.layout, R.id.select_group_head})
    public void onViewClicked(View view) {
        if (!(view instanceof EditText))
            hideKeyboard();
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.more:
                if (checkData())
                    //                    ((GroupPresenter) mPresenter).createGroup(
                    //                            mGroupName.getText().toString(), userArray, mBrife
                    //                                    .getText()
                    //                                    .toString());
                    requestCreateGroup();
                break;
            case R.id.select_friends:
                Intent itt = new Intent(mContext, PickUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FROM, TAG);
                itt.putExtras(bundle);
                startActivityForResult(itt, PickUserActivity.REQUEST_CODE);
                break;
            case R.id.select_group_head:
                showIOSDialog();
                break;
        }
    }


    private void requestCreateGroup() {
        Map<String, RequestBody> params = new HashMap<>();
        File file = new File(list.get(0).getCompressPath());
        RequestBody requestBody = getRequestFle(MediaType.parse("multipart/" + list.get(0).getPictureType().replace
                ("image/", "")), file);
        String name = UserHelper.getUserId() + TimeUtil.getCurrentTime() + "[0]";
        params.put("file\"; name =" + name + ";filename=" + name + "\"", requestBody);

        params.put("owner", toRequestBody(UserHelper.getInstance().getLogUser().getPhone()));
        params.put("name", toRequestBody(mGroupName.getText().toString()));
        params.put("descriptions", toRequestBody(mBrife.getText().toString()));
        params.put("maxusers", toRequestBody(2000 + ""));
        params.put("type", toRequestBody(UserHelper.getUserType() + ""));
        ((GroupPresenter) mPresenter).createGroup(mContext, userArray, params);

    }


    @NonNull
    private RequestBody getRequestFle(MediaType parse, File picFile) {
        return RequestBody.create(parse, picFile);
    }

    public static RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;

    }


    /**
     * 当初选择图片来源的dialog
     */
    private void showIOSDialog() {
        new IOSDialog(mContext).builder().setCareme("照相机", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoTakePhote();
            }
        }).setAlbum("相册", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPhotoSelector();
            }
        }).setCancle("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PickUserActivity.REQUEST_CODE:
                    userArray = data.getLongArrayExtra(PickUserActivity.USER_SELECTED);
                    mHasSelect.setText("已经选择" + userArray.length + "个好友");
                    break;
                // 图片选择结果回调
                case PictureConfig.CHOOSE_REQUEST:
                case PictureConfig.REQUEST_CAMERA:

                    list = PictureSelector.obtainMultipleResult(data);
                    ImageLoadMnanger.INSTANCE.loadImage(mGroupHead, list.get(0).getPath());
                    break;
            }
        }

    }

    private boolean checkData() {
        if (TextUtils.isEmpty(mGroupName.getText().toString())) {
            showBaseMessageDialog("群名称不能为空");
            return false;
        }
        if (TextUtils.isEmpty(mBrife.getText().toString())) {
            showBaseMessageDialog("群简介不能为空");
            return false;
        }
        if (userArray == null || userArray.length == 0) {
            showBaseMessageDialog("请选择群组成员");
            return false;
        }
        if (list.size() == 0) {
            showBaseMessageDialog("请选择群头像");
            return false;
        }
        return true;
    }


    @Override
    public void showLoadProgressDialog(String str) {
        showLoading(str);
    }

    @Override
    public void disDialog() {
        disLoadDialog();
    }

    @Override
    public void createGroupSuccess() {

        new AlertDialog(mContext).setWidthRatio(0.7f).builder()
                .hideTitleLayout().setMsg("群聊创建成功").setNegativeButton(("确 定"), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).setMessageGravity(Gravity.CENTER).show();
    }

    @Override
    public void getGroupListSuccess(ArrayList<GroupResponseBean> mData) {

    }

    @Override
    public void getGroupMenberListSuccess(ArrayList<GroupMember> mData) {

    }

    @Override
    public void getGroupInfoSuccess(GroupResponseBean mData) {

    }

    @Override
    public void addGroupMembersSuccess() {

    }

    @Override
    public void deleteGroupMembersSuccess() {

    }

    @Override
    public void deleteOrQuitGroupSuccess() {

    }

    @Override
    public void onFailed(String message) {
        showBaseMessageDialog(message);
    }

    private void gotoTakePhote() {
        PictureSelector.create(GroupCreateActivity.this).openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private void initPhotoSelector() {
        PictureSelector.create(GroupCreateActivity.this)//
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll
                .theme(R.style.picture_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(0)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .selectionMedia(list)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code     
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, GroupCreateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
