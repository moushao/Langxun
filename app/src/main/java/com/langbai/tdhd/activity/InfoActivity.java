package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.base.BaseActivity;
import com.hyphenate.easeui.EventMessage;
import com.langbai.tdhd.bean.InfoRequestBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.event.PickerListener;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.InfoPresenter;
import com.langbai.tdhd.mvp.view.InfoView;
import com.langbai.tdhd.utils.DiskCacheManager;
import com.langbai.tdhd.utils.ImageUtils;
import com.langbai.tdhd.utils.PickerUtils;
import com.langbai.tdhd.widget.CircleImageView;
import com.langbai.tdhd.widget.Dialog.IOSDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 类名: {@link InfoActivity}
 * <br/> 功能描述:个人中心,{@link com.langbai.tdhd.fragment.PersonFragment} 的子界面
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/27
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class InfoActivity extends BaseActivity implements InfoView {
    public static final String TAG = "InfoActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.head_info) CircleImageView mHead;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.toolbar_layout) FrameLayout mToolbarLayout;
    @BindView(R.id.tel) TextView mTel;
    @BindView(R.id.name) TextView mName;
    @BindView(R.id.head_layout) LinearLayout mHeadLayout;
    @BindView(R.id.sex) TextView mSex;
    @BindView(R.id.sex_layout) LinearLayout mSexLayout;
    @BindView(R.id.old) TextView mOld;
    @BindView(R.id.old_layout) LinearLayout mOldLayout;
    @BindView(R.id.qr_layout) LinearLayout mQrLayout;
    @BindView(R.id.brief) TextView mBrief;
    @BindView(R.id.brief_layout) LinearLayout mBriefLayout;
    @BindView(R.id.area) TextView mArea;
    @BindView(R.id.platform) TextView mPlatform;
    @BindView(R.id.area_layout) LinearLayout mAreaLayout;
    private Context mContext;
    private PickerUtils mPicker;
    List<LocalMedia> list = new ArrayList<LocalMedia>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_info;
    }

    @Override
    public BasePresenter getPresenter() {
        return new InfoPresenter();
    }

    @Override
    protected void initInjector() {
        mContext = this;
        //CommonImageLoader.getInstance().addGlideRequests(this);
    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        initWidget();
        mPicker = new PickerUtils(mContext);
    }

    private void initWidget() {
        mTitleTv.setText("个人信息");
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleBack.setImageResource(R.drawable.icon_back);
        LoginResponseBean userBean = UserHelper.getInstance().getLogUser();
        if (userBean != null) {
            mName.setText(userBean.getRealName());
            mTel.setText(userBean.getPhone());
            mBrief.setText(userBean.getSignName());
            mSex.setText(userBean.getSex());
            mArea.setText(userBean.getArea());
            mOld.setText(userBean.getAge() == 0 ? "" : userBean.getAge() + "");
            switch (userBean.getType()) {
                case 1:
                    mPlatform.setText("KF");
                    break;
                case 2:
                    mPlatform.setText("TD");
                    break;
                case 3:
                    mPlatform.setText("XN");
                    break;
            }
            if (TextUtils.isEmpty(userBean.getUserIcon())) {
                mHead.setImageResource(R.drawable.login_head);
            } else {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.login_head)
                        .error(R.drawable.login_head)
                        .priority(Priority.HIGH);
                Glide.with(mContext).load(userBean.getUserIcon()).apply(options).into(mHead);
            }
        }


    }

    @OnClick({R.id.title_back, R.id.head_layout, R.id.area_layout, R.id.sex_layout, R.id.old_layout, R.id.qr_layout,
            R.id.brief_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.head_layout:
                showIOSDialog();
                break;
            case R.id.sex_layout:
                showSexPicker();
                break;
            case R.id.area_layout:
                showAreaPicker();
                break;
            case R.id.old_layout:
                gotoEditActivity(EditActivity.AGE, EditActivity.AGE_REQUEST, mOld.getText().toString());
                break;
            case R.id.qr_layout:
                MyQrCodeActivity.startAction(mContext, TAG);
                break;
            case R.id.brief_layout:
                gotoEditActivity(EditActivity.BRIFE, EditActivity.BRIFE_REQUEST, mBrief.getText().toString());
                break;
        }
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

    private void gotoTakePhote() {
        //        PictureSelector.create(InfoActivity.this)//
        //                .openCamera(PictureMimeType.ofImage())//
        //                .forResult(PictureConfig.REQUEST_CAMERA);
        PictureSelector.create(InfoActivity.this).openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                //                .theme(themeId)// 主题样式设置 具体参考 values/styles
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                // LUBAN_COMPRESS_MODE
                //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                //.withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                //.isGif(cb_isGif.isChecked())// 是否显示gif图片
                //.freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
                //.circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
                //.showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                //.showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                //.openClickSound(cb_voice.isChecked())// 是否开启点击声音
                //.selectionMedia(selectList)// 是否传入已选图片
                .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认为100
                //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled() // 裁剪是否可旋转图片
                //.scaleEnabled()// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()////显示多少秒以内的视频or音频也可适用
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private void initPhotoSelector() {
        PictureSelector.create(InfoActivity.this)//
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll
                // ()、图片.ofImage()、视频.ofVideo()
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
                // LUBAN_COMPRESS_MODE
                //.glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                //.withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                //.isGif()// 是否显示gif图片 true or false
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .selectionMedia(list)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效  int 
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int 
                //.rotateEnabled() // 裁剪是否可旋转图片 true or false
                //.scaleEnabled()// 裁剪是否可放大缩小图片 true or false
                //.videoQuality()// 视频录制质量 0 or 1 int
                //.videoSecond()// 显示多少秒以内的视频or音频也可适用 int 
                //.recordVideoSecond()//视频秒数录制 默认60s int
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code     
    }

    private void showSexPicker() {
        mPicker.buildSexPicker(new PickerListener<String>() {
            @Override
            public void pickerData(int position, String data) {
                mSex.setText(data);
                upPersonInfo("");
            }
        });
    }


    private void showAreaPicker() {
        mPicker.buildAreaPicker(new PickerListener<String>() {
            @Override
            public void pickerData(int position, String data) {
                mArea.setText(data);
                upPersonInfo("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case EditActivity.AGE_REQUEST:
                    mOld.setText(data.getStringExtra(EditActivity.CONTENT));
                    upPersonInfo("");
                    break;
                case EditActivity.BRIFE_REQUEST:
                    mBrief.setText(data.getStringExtra(EditActivity.CONTENT));
                    EventBus.getDefault().post(new EventMessage("user_brief", data.getStringExtra(EditActivity
                            .CONTENT)));
                    upPersonInfo("");
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                case PictureConfig.REQUEST_CAMERA:
                    // 图片选择结果回调
                    final List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher).priority(Priority.HIGH);
                    Glide.with(mContext).load(selectList.get(0).getCompressPath()).apply(options).into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            mHead.setImageDrawable(resource);
                        }
                    });
                    //发送通知,更新主界面和个人中心的头像
                    EventBus.getDefault().post(new EventMessage("head_pic", selectList.get(0).getCompressPath()));
                    //保存头像
                    UserHelper.getInstance().getLogUser().setUserIcon(selectList.get(0).getCompressPath());
                    Bitmap bt = ImageUtils.getBitmapByPath(selectList.get(0).getPath());
                    if (bt == null)
                        return;
                    Bitmap sBt = ImageUtils.zoomImage(bt, 60, bt.getHeight() / (bt.getWidth() / 60));
                    upPersonInfo(ImageUtils.bitmapToBaseString(sBt));
                    break;
            }
        }

    }

    /**
     * 更新用户数据
     */
    private void upPersonInfo(String bitString) {
        InfoRequestBean bean = new InfoRequestBean();
        LoginResponseBean logUser = UserHelper.getInstance().getLogUser();
        bean.userInfoID = logUser.getUserInfoID();
        if (!TextUtils.isEmpty(mOld.getText().toString())) {
            bean.age = Integer.valueOf(mOld.getText().toString());
        }
        bean.sex = mSex.getText().toString();
        bean.area = mArea.getText().toString();
        bean.signName = mBrief.getText().toString();
        bean.userIcon = bitString;

        //更新本地数据
        logUser.setAge(bean.age);
        logUser.setSex(bean.sex);
        logUser.setArea(bean.area);
        logUser.setSignName(bean.signName);

        DiskCacheManager manager = new DiskCacheManager(mContext, Constants.LOGIN_USER);
        manager.put(Constants.LOGIN_USER, logUser);
        ((InfoPresenter) mPresenter).changeInfo(mContext, bean);


    }

    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void disDialog() {

    }

    @Override
    public void onFailed(String message) {
        showBaseMessageDialog(message);
    }

    @Override
    public void changeInfoSuccess(String message) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PictureFileUtils.deleteCacheDirFile(InfoActivity.this);
        if (mPicker != null) {
            mPicker = null;
        }
    }

    private void gotoEditActivity(String type, int requestCode, String content) {
        Intent itt = new Intent(mContext, EditActivity.class);
        itt.putExtra(EditActivity.TYPE, type);
        itt.putExtra(EditActivity.CONTENT, content);
        startActivityForResult(itt, requestCode);
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, InfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
