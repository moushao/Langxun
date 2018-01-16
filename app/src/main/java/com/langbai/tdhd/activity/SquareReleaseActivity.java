package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.adapter.GridImageAdapter;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.ffmpeg.FFmpegKit;
import com.langbai.tdhd.ffmpeg.ThreadPoolUtils;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.SquareReleasePresente;
import com.langbai.tdhd.mvp.view.ReleaseView;
import com.langbai.tdhd.utils.BitmapUtils;
import com.langbai.tdhd.utils.FileUtil;
import com.langbai.tdhd.utils.TimeUtil;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class SquareReleaseActivity extends BaseActivity implements ReleaseView {
    public static final String TAG = "SquareReleaseActivity";
    public static final int REQUEST_CODE = 996;
    public static String SELECTE_TYPE = "SELECTE_TYPE";
    public static int TEXT = 1;
    public static int PICTURE = 2;
    public static int VIDEO = 3;
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.more) TextView mMore;
    @BindView(R.id.content) EditText mMessage;
    @BindView(R.id.pic_list) RecyclerView mPicList;
    private Context mContext;
    private GridImageAdapter adapter;
    private List<LocalMedia> selectMedia = new ArrayList<LocalMedia>();
    private PictureSelectionModel picModel;
    private PictureSelectionModel videoModel;
    private int fromType;
    private String outputUrl;
    private Handler mHandler;
    private File videoFile;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_circle_release;
    }

    @Override
    public BasePresenter getPresenter() {
        return new SquareReleasePresente();
    }

    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        initWidget();
        fromType = this.getIntent().getExtras().getInt(SELECTE_TYPE);

        if (fromType == TEXT) {

        } else if (fromType == PICTURE) {
            initRecycleView();
            selectedPic();
        } else if (fromType == VIDEO) {
            initRecycleView();
            selectVideoFromLocal();
        }
    }

    private void initWidget() {
        mTitleBack.setVisibility(View.VISIBLE);
        mMore.setText("发表");
        mMore.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.title_back, R.id.more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.more:
                ReleaseContent();
                break;
        }
    }

    //发布内容
    private void ReleaseContent() {
        switch (fromType) {
            case 1:
                if (TextUtils.isEmpty(mMessage.getText().toString())) {
                    showToast("文字内容不能空");
                } else {
                    ((SquareReleasePresente) mPresenter).releaseText(mMessage.getText().toString());
                }
                break;
            case 2:
                if (selectMedia.size() == 0) {
                    showToast("图片内容不能为空");
                } else {
                    ReleasePicture();
                }
                break;
            case 3:
                if (selectMedia.size() == 0) {
                    showToast("视频内容不能为空");
                } else {
                    mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == 0) {
                                //视频播放的控制
                                ReleaseVideo();
                            }
                        }
                    };
                    compressVideo();
                }
                break;
        }
    }


    File picFile;

    private void compressVideo() {
        showLoading("视频合成中...");
        //获取路径
        String path = Constants.PHONE_PATH + File.separator + "video";
        //定义文件名
        String fileName = new Date().getTime() + ".mp4";
        videoFile = new File(path, fileName);
        //文件夹不存在
        if (!videoFile.getParentFile().exists()) {
            videoFile.getParentFile().mkdirs();
        }

        outputUrl = videoFile.getAbsolutePath();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String[] commands = new String[10];
                commands[0] = "ffmpeg";
                commands[1] = "-i";
                commands[2] = selectMedia.get(0).getPath();
                //                commands[3] = "-vcodec";
                //                commands[4] = "libx264";
                //                commands[5] = "-preset";
                //                commands[6] = "ultrafast";

                commands[3] = "-r";
                commands[4] = "videoFramerate";
                commands[5] = "-b:a";
                commands[6] = "32";
                commands[7] = "-b";        //这个
                commands[8] = "2048k";
                commands[9] = outputUrl;
                FFmpegKit.execute(commands, new FFmpegKit.KitInterface() {
                    @Override
                    public void onStart() {
                        Log.e("FFmpegLog LOGCAT", "FFmpeg 命令行开始执行了...");
                    }

                    @Override
                    public void onProgress(int progress) {
                        Log.e("FFmpegLog LOGCAT", "done com" + "FFmpeg 命令行执行进度..." + progress);
                    }

                    @Override
                    public void onEnd(int result) {
                        Log.e("FFmpegLog LOGCAT", "FFmpeg 命令行执行完成...");
                        Message msg = new Message();
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                        disLoadDialog();
                    }
                });
            }
        };
        ThreadPoolUtils.execute(runnable);
    }


    /**
     * 发布视频
     */
    private void ReleaseVideo() {
        try {
            Map<String, RequestBody> params = new HashMap<>();
            String name = "video_" + UserHelper.getUserId() + TimeUtil.getCurrentTime() + "";

            File file = new File(outputUrl);

            RequestBody requestBody = getRequestFle(MediaType.parse("multipart/" + selectMedia.get
                    (0).getPictureType().replace("video/", "")), file);

            params.put("file\"; name =" + name + ";filename=" + name + "\"", requestBody);
            //获取视频的第一祯作为缩略图
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(outputUrl);
            Bitmap backBitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            //获取播放按钮drawable转为bitmap
            Bitmap frontBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_play);
            //将两个bitmap合成一个bitmap
            picFile = FileUtil.compressImage(BitmapUtils.mergeBitmap(backBitmap, frontBitmap));
            RequestBody requestBodyPic = getRequestFle(MediaType.parse("multipart/png"), picFile);
            params.put("file\"; name =" + name.replace("video", "picture") + ";filename=" + name.replace("video",
                    "picture") + "\"", requestBodyPic);

            params.put("content", toRequestBody(mMessage.getText().toString()));
            params.put("userID", toRequestBody(UserHelper.getUserId() + ""));
            ((SquareReleasePresente) mPresenter).releaseVideo(mMessage.getText().toString(), params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private RequestBody getRequestFle(MediaType parse, File picFile) {

        return RequestBody.create(parse, picFile);
    }

    private void ReleasePicture() {
        Map<String, RequestBody> params = new HashMap<>();
        for (int i = 0; i < selectMedia.size(); i++) {
            File file = new File(selectMedia.get(i).getCompressPath());
            RequestBody requestBody = getRequestFle(MediaType.parse("multipart/" + selectMedia.get
                    (i).getPictureType().replace("image/", "")), file);
            String name = UserHelper.getUserId() + TimeUtil.getCurrentTime() + "[" + i + "]";
            params.put("file\"; name =" + name + ";filename=" + name + "\"", requestBody);
        }
        params.put("content", toRequestBody(mMessage.getText().toString()));
        params.put("userID", toRequestBody(UserHelper.getUserId() + ""));
        ((SquareReleasePresente) mPresenter).releasePicture(mMessage.getText().toString(), params);
    }

    public static RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;

    }

    public void initRecycleView() {
        mPicList.setVisibility(View.VISIBLE);
        GridLayoutManager mgr = new GridLayoutManager(mContext, 4);
        mPicList.setLayoutManager(mgr);
        mPicList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                outRect.set(10, 10, 10, 20);
            }
        });

        adapter = new GridImageAdapter(mContext, onAddPicClickListener);
        adapter.setList(selectMedia);
        adapter.setSelectMax(10);
        mPicList.setAdapter(adapter);

        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (fromType) {
                    case 2:
                        PictureSelector.create(SquareReleaseActivity.this).externalPicturePreview(position,
                                selectMedia);
                        break;
                    case 3:
                        PictureSelector.create(SquareReleaseActivity.this).externalPictureVideo(selectMedia.get(0)
                                .getPath());
                        break;
                }

            }
        });

    }

    /**
     * 删除图片回调接口
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener
            () {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    switch (fromType) {
                        case 2:
                            selectedPic();
                            break;
                        case 3:
                            selectVideoFromLocal();
                            break;
                    }
                    break;
                case 1:
                    // 删除图片
                    selectMedia.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }
    };

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, SquareReleaseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }

    @Override
    public void releaseSuccess(Long userPublishID) {
        if (picFile != null && picFile.exists()) {
            picFile.delete();
        }
        if (videoFile != null && videoFile.exists()) {
            //删除视频
            videoFile.delete();
        }
        setResult(RESULT_OK);
        finish();
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
    public void onFailed(String message) {
        showBaseMessageDialog(message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 多选回调
                    //                    for (LocalMedia media : PictureSelector.obtainMultipleResult(data)) {
                    //                    }
                    selectMedia = PictureSelector.obtainMultipleResult(data);
                    adapter.setList(selectMedia);
                    adapter.notifyDataSetChanged();
                    break;
                case PictureConfig.TYPE_VIDEO:
                    selectMedia = PictureSelector.obtainMultipleResult(data);
                    adapter.setList(selectMedia);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    /**
     * 选择照片
     */
    private void selectedPic() {
        if (picModel == null) {
            picModel = PictureSelector.create(this)//
                    .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                    .theme(R.style.picture_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                    .maxSelectNum(9)// 最大图片选择数量 int
                    .minSelectNum(0)// 最小选择数量 int
                    .imageSpanCount(4)// 每行显示个数 int
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                    .previewImage(true)// 是否可预览图片 true or false
                    .previewVideo(true)// 是否可预览视频 true or false
                    .enablePreviewAudio(true) // 是否可播放音频 true or false
                    // .CUSTOM_GEAR
                    .isCamera(true)// 是否显示拍照按钮 true or false
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                    .enableCrop(false)// 是否裁剪 true or false
                    .compress(true)// 是否压缩 true or false
                    // LUBAN_COMPRESS_MODE
                    //.glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    //.withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                    .isGif(false)// 是否显示gif图片 true or false
                    .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                    .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                    .openClickSound(false)// 是否开启点击声音 true or false
                    .selectionMedia(null)// 是否传入已选图片 List<LocalMedia> list
                    .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                    .cropCompressQuality(90);// 裁剪压缩质量 默认90 int
        }
        picModel.selectionMedia(selectMedia);
        picModel.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code     
    }

    private void selectVideoFromLocal() {
        if (videoModel == null) {
            videoModel = PictureSelector.create(this)//
                    .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                    .theme(R.style.picture_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                    .maxSelectNum(1)// 最大图片选择数量 int
                    .minSelectNum(0)// 最小选择数量 int
                    .imageSpanCount(4)// 每行显示个数 int
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                    .previewImage(true)// 是否可预览图片 true or false
                    .previewVideo(true)// 是否可预览视频 true or false
                    .enablePreviewAudio(false) // 是否可播放音频 true or false
                    .isCamera(true)// 是否显示拍照按钮 true or false
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                    .enableCrop(false)// 是否裁剪 true or false
                    .compress(true)// 是否压缩 true or false
                    .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                    .isGif(false)// 是否显示gif图片 true or false
                    .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                    .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                    .openClickSound(false)// 是否开启点击声音 true or false
                    .selectionMedia(null)// 是否传入已选图片 List<LocalMedia> list
                    .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                    .videoQuality(1)// 视频录制质量 0 or 1 int
                    .videoMaxSecond(10)
                    .recordVideoSecond(10);//视频秒数录制 默认60s int
        }
        videoModel.selectionMedia(selectMedia);
        videoModel.forResult(PictureConfig.TYPE_VIDEO);
    }

    @Override
    protected void onDestroy() {
        if (videoModel != null) {
            videoModel = null;
        }
        if (picModel != null) {
            picModel = null;
        }
        super.onDestroy();
    }
}
