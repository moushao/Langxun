package com.langbai.tdhd.activity.chat;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.ui.EaseGroupListener;
import com.hyphenate.util.NetUtils;
import com.langbai.tdhd.Constant;
import com.langbai.tdhd.DemoHelper;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.activity.ChatActivity;
import com.langbai.tdhd.activity.MainActivity;
import com.hyphenate.easeui.EventMessage;
import com.langbai.tdhd.db.InviteMessgeDao;
import com.langbai.tdhd.widget.Dialog.IOSDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class TalkListFragment extends EaseConversationListFragment {
    private GroupListener groupListener;
    private EMContactListener userChageListener;
    //    private TextView errorText;


    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        //        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager
                .LayoutParams.SOFT_INPUT_ADJUST_PAN);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserHelper.getInstance().getLogUser() == null)
                    return;
                EMClient.getInstance().login(UserHelper.getEMID(), UserHelper.getEMID(), new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                conversationList.addAll(loadConversationList());
                                conversationListView.init(conversationList);
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
        groupListener = new GroupListener();
        EMClient.getInstance().groupManager().addGroupChangeListener(groupListener);
        EMClient.getInstance().contactManager().setContactListener(userChageListener);

    }

    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversationType.ChatRoom) {
                            // it's group chat
                            // intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant
                            // .CHATTYPE_CHATROOM);
                            //会议不会存在在对话列表中,所以,进入不能为聊天室,不然会报空指针,而且,这本身就是群聊
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        } else {
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        }

                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });

        /**
         * 长按删除
         */
        conversationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                showIOSDialog(position);
                return true;
            }
        });

        //        DemoHelper.getInstance().setGroupListtener(new DemoHelper.GroupListtener() {
        //            @Override
        //            public void onAutoAcceptInvitationFromGroup() {
        //                refresh();
        //                //conversationListView.refresh();
        //            }
        //        });


        super.setUpView();
        //end of red packet code
    }


    /**
     * 当初选择图片来源的dialog
     */
    private void showIOSDialog(final int position) {
        new IOSDialog(getActivity()).builder().setTitle("提 示")
                .setCareme("删除会话", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteMessage(false, position);
                    }
                }).setAlbum("删除会话和消息", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMessage(true, position);
            }
        }).setCancle("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    public boolean deleteMessage(boolean deleteMessage, int position) {
        EMConversation tobeDeleteCons = conversationListView.getItem(position);
        if (tobeDeleteCons == null) {
            return true;
        }
        deleteConversation(deleteMessage, tobeDeleteCons);
        ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
        } else {
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        //        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu); 
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).updateUnreadLabel();
        super.onResume();
    }

    @Subscribe
    public void onEventMainThread(EventMessage message) {
        if (message.getCode().equals(EventMessage.DELETE_USER)) {
            //联系人被删除后，删除聊天，刷新列表
            deleteConversation(true, message.getMessage());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (groupListener != null) {
            EMClient.getInstance().groupManager().removeGroupChangeListener(groupListener);
        }
        if (userChageListener != null) {
            EMClient.getInstance().contactManager().removeContactListener(userChageListener);
        }

    }

    class GroupListener extends EaseGroupListener {


        @Override
        public void onUserRemoved(String groupId, String groupName) {
            deleGroup(groupId);
        }

        @Override
        public void onGroupDestroyed(String groupId, String groupName) {
            deleGroup(groupId);
        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
            refresh();
        }

    }

    private void deleGroup(String groupId) {
        for (int i = 0; i < conversationList.size(); i++) {
            if (conversationList.get(i).conversationId().equals(groupId)) {
                deleteConversation(true, conversationList.get(i));
                break;
            }
        }
    }

    //删除对话
    private void deleteConversation(boolean deleteMessage, EMConversation tobeDeleteCons) {
        if (tobeDeleteCons.getType() == EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        deleteConversation(deleteMessage, tobeDeleteCons.conversationId());
        refresh();
    }

    //删除对话
    private void deleteConversation(boolean deleteMessage, String conversationId) {
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(conversationId, deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(conversationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();
    }
}
