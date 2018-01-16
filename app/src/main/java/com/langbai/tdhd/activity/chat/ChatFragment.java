package com.langbai.tdhd.activity.chat;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenu;
import com.hyphenate.util.PathUtil;
import com.langbai.tdhd.Constant;
import com.langbai.tdhd.DemoHelper;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.activity.ChatActivity;
import com.langbai.tdhd.activity.FriendsInfoActivity;
import com.langbai.tdhd.activity.GroupDetailsActivity;
import com.langbai.tdhd.activity.ImageGridActivity;
import com.langbai.tdhd.activity.InfoActivity;
import com.langbai.tdhd.cache.UserCacheManager;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.domain.EmojiconExampleGroupData;
import com.langbai.tdhd.domain.RobotUser;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ChatFragment extends EaseChatFragment implements EaseChatFragmentHelper {
    // constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;
    private static final int ITEM_FILE = 12;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    private static final int MESSAGE_TYPE_SENT_CARD = 5;
    private static final int MESSAGE_TYPE_RECV_CARD = 6;
    private PictureSelectionModel picModel;
    private PictureSelectionModel videoModel;

    /**
     * if it is chatBot
     */
    private boolean isRobot;
    private List<LocalMedia> selectMedia = new ArrayList<LocalMedia>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            Map<String, RobotUser> robotMap = DemoHelper.getInstance().getRobotList();
            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
                isRobot = true;
            }
        }
        super.setUpView();
        // set click listener
        //        titleBar.setLeftLayoutClickListener(new OnClickListener() {
        //
        //            @Override
        //            public void onClick(View v) {
        //                if (EasyUtils.isSingleActivity(getActivity())) {
        //                    Intent intent = new Intent(getActivity(), MainActivity.class);
        //                    startActivity(intent);
        //                }
        //                onBackPressed();
        //            }
        //        });

        ((EaseEmojiconMenu) inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 1 && "@".equals(String.valueOf(s.charAt(start)))) {
                        //TODO 跳转到选择用户列表的地方
                        //                        startActivityForResult(new Intent(getActivity(), PickAtUserActivity
                        // .class).
                        //                                putExtra("groupId", toChatUsername), 
                        // REQUEST_CODE_SELECT_AT_USER);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            meetingGroup = fragmentArgs.getStringArrayList(EaseConstant.MEETING_GROUP);
            titleBar.setRightImageIsVisible(View.INVISIBLE);
        }
    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
        //extend menu items
        //TODO 修改图标
        //        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, 
        // ITEM_VIDEO, 
        //                extendMenuItemClickListener);
        //        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_video_selector, ITEM_FILE, 
        //                extendMenuItemClickListener);
        //        if (chatType == Constant.CHATTYPE_SINGLE) {
        //            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_video_selector, 
        //                    ITEM_VOICE_CALL, extendMenuItemClickListener);
        //            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_selector, 
        //                    ITEM_VIDEO_CALL, extendMenuItemClickListener);
        //        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // copy
                    clipboard.setPrimaryClip(ClipData.newPlainText(null, ((EMTextMessageBody) contextMenuMessage
                            .getBody()).getMessage()));
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                    break;

                case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
                    //TODO 转发功能
                    Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
                    intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System
                                .currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_AT_USER:
                    if (data != null) {
                        String username = data.getStringExtra("username");
                        inputAtUsername(username, false);
                    }
                    break;
                case GroupDetailsActivity.REQUEST_CODE:
                    getActivity().finish();
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    // 多选回调
                    for (LocalMedia media : PictureSelector.obtainMultipleResult(data)) {
                        sendImageMessage(media.getPath());
                    }
                    break;
                case PictureConfig.TYPE_VIDEO:
                    // 多选回
                    for (LocalMedia media : PictureSelector.obtainMultipleResult(data)) {
                        //                        int duration = data.getIntExtra("dur", 0);
                        //                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System
                                .currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(media.getPath(), 3);
                            ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(media.getCompressPath(), file.getAbsolutePath(), (int) media.getDuration
                                    ());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if (isRobot) {
            //set message extension
            message.setAttribute("em_robot_message", isRobot);
        }

        // 设置消息的扩展属性，携带昵称头像
        UserCacheManager.setMsgExt(message);
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }


    @Override
    public void onEnterToChatDetails() {
        if (chatType == Constant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            /**
             * 这里使用回调,保证退出群或者删除群后,当前聊天界面销毁
             */
            Intent itt = new Intent(getActivity(), GroupDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.FROM, TAG);
            bundle.putString("GROUP_EMID", toChatUsername);
            itt.putExtras(bundle);
            startActivityForResult(itt, GroupDetailsActivity.REQUEST_CODE);
        } else if (chatType == Constant.CHATTYPE_CHATROOM) {
            //            startActivityForResult(new Intent(getActivity(), ChatRoomDetailsActivity.class).putExtra
            // ("roomId", 
            //                    toChatUsername), REQUEST_CODE_GROUP_DETAIL);
        }
    }

    @Override
    public void onAvatarClick(String username) {
        if (username.contains(UserHelper.getInstance().getLogUser().getPhone())) {
            InfoActivity.startAction(getActivity(), ChatActivity.TAG);
        } else {
            FriendsInfoActivity.startAction(getActivity(), username, chatType, ChatActivity.TAG);
        }
    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }


    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
        // no message forward when in chat room
        //        startActivityForResult((new Intent(getActivity(), ContextMenuActivity.class)).putExtra("message", 
        // message)
        //                .putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM), 
        // REQUEST_CODE_CONTEXT_MENU);
    }

    /**
     * 扩展菜单d点击时间
     */
    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case 2:
                selectedPic();
                break;
            case 3:
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                //selectVideoFromLocal();
                break;
            case ITEM_FILE: //file
                selectFileFromLocal();
                break;
            case ITEM_VOICE_CALL:
                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
                startVideoCall();
                break;
            case ITEM_TAKE_PICTURE:
                selectPicFromCamera();
                break;

            //case ITEM_LOCATION:
            //startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
            //break;
            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i : grantResults) {
            if (i == PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "获取到了", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "没有", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
            //            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", 
            // toChatUsername)
            //                    .putExtra("isComingCall", false));
            //            // voiceCallBtn.setEnabled(false);
            //            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            //            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", 
            // toChatUsername)
            //                    .putExtra("isComingCall", false));
            //            // videoCallBtn.setEnabled(false);
            //            inputMenu.hideExtendMenuContainer();
        }
    }


    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 10;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL :
                            MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL :
                            MESSAGE_TYPE_SENT_VIDEO_CALL;
                }/* else if (
                        
                        Constant.CARD_NAME.equals(message.getStringAttribute(Constant.CARD_NAME, ""))) {
                    //发送名片
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_CARD : 
                            MESSAGE_TYPE_SENT_CARD;
                }*/
                //end of red packet code
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            //            if (Constant.CARD_NAME.equals(message.getStringAttribute(Constant.CARD_NAME, ""))) {
            //                //发送名片
            //                return new CardChatRow(getActivity(), message, position, adapter);
            //            }
            return null;
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
                    .maxSelectNum(10)// 最大图片选择数量 int
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
            //.compressMaxKB(100)//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效 int 
            //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效  int 
            //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int 
            //.rotateEnabled() // 裁剪是否可旋转图片 true or false
            //.scaleEnabled()// 裁剪是否可放大缩小图片 true or false
            //.videoQuality()// 视频录制质量 0 or 1 int
            //.videoSecond()// 显示多少秒以内的视频or音频也可适用 int 
            //.recordVideoSecond()//视频秒数录制 默认60s int
        }

        picModel.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code     
    }

    private void selectVideoFromLocal() {
        if (videoModel == null) {
            videoModel = PictureSelector.create(this)//
                    .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                    .theme(R.style.picture_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                    .maxSelectNum(10)// 最大图片选择数量 int
                    .minSelectNum(0)// 最小选择数量 int
                    .imageSpanCount(4)// 每行显示个数 int
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                    .previewImage(true)// 是否可预览图片 true or false
                    .previewVideo(true)// 是否可预览视频 true or false
                    .enablePreviewAudio(false) // 是否可播放音频 true or false
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
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                    //.compressMaxKB(100)//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效 int 
                    //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效  int 
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int 
                    //.rotateEnabled() // 裁剪是否可旋转图片 true or false
                    //.scaleEnabled()// 裁剪是否可放大缩小图片 true or false
                    .videoQuality(0)// 视频录制质量 0 or 1 int
                    .videoMaxSecond(10)// 显示多少秒以内的视频or音频也可适用 int 
                    .recordVideoSecond(60);//视频秒数录制 默认60s int
        }
        videoModel.forResult(PictureConfig.TYPE_VIDEO);
    }

}
