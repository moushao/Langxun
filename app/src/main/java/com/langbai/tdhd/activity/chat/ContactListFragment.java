/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.langbai.tdhd.activity.chat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.NetUtils;
import com.langbai.tdhd.Constant;
import com.langbai.tdhd.DemoHelper;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.activity.FriendsInfoActivity;
import com.langbai.tdhd.activity.GroupListActivity;
import com.langbai.tdhd.activity.NewFriendsActivity;
import com.hyphenate.easeui.EventMessage;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.db.InviteMessgeDao;
import com.langbai.tdhd.db.UserDao;
import com.langbai.tdhd.mvp.presenter.EMPresenter;
import com.langbai.tdhd.mvp.presenter.FriendsSettingPresenter;
import com.langbai.tdhd.mvp.view.BaseView;
import com.langbai.tdhd.mvp.view.EMView;
import com.langbai.tdhd.widget.ContactItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * contact list
 */
public class ContactListFragment extends EaseContactListFragment implements EMView {

    public static final String TAG = ContactListFragment.class.getSimpleName();
    private ContactSyncListener contactSyncListener;
    private BlackListSyncListener blackListSyncListener;
    private ContactInfoSyncListener contactInfoSyncListener;
    private View loadingView;
    private ContactItemView applicationItem;
    private InviteMessgeDao inviteMessgeDao;
    protected EMPresenter mPresenter;
    private int toBeProcessPosition;
    private ProgressDialog pd;

    @SuppressLint("InflateParams")
    @Override
    protected void initView() {
        super.initView();
        @SuppressLint("InflateParams") View headerView = LayoutInflater.from(getActivity()).inflate(R.layout
                .em_contacts_header, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        applicationItem.setOnClickListener(clickListener);
        headerView.findViewById(R.id.group_item).setOnClickListener(clickListener);
        headerView.findViewById(R.id.chat_room_item).setOnClickListener(clickListener);
        headerView.findViewById(R.id.robot_item).setOnClickListener(clickListener);
        listView.addHeaderView(headerView);
        //add loading view
        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
        contentContainer.addView(loadingView);
        registerForContextMenu(listView);
        mPresenter = new EMPresenter();
        if (mPresenter != null && this instanceof BaseView) {
            mPresenter.attach(this);
        }
        swipeRefreshLayout.setColorSchemeResources(com.hyphenate.easeui.R.color.holo_blue_bright, com.hyphenate
                .easeui.R.color.holo_green_light, com.hyphenate.easeui.R.color
                .holo_orange_light, com.hyphenate.easeui.R.color.holo_red_light);
    }

    @Override
    public void refresh() {
        Map<String, EaseUser> m = DemoHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            //noinspection unchecked
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>) m).clone();
        }
        setContactsMap(m);
        super.refresh();
        if (inviteMessgeDao == null) {
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        if (inviteMessgeDao.getUnreadMessagesCount() > 0) {
            applicationItem.showUnreadMsgView();
        } else {
            applicationItem.hideUnreadMsgView();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setUpView() {
        titleBar.setRightImageResource(R.drawable.icon_add);
        titleBar.setRightLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //                startActivity(new Intent(getActivity(), AddContactActivity.class));
                NetUtils.hasDataConnection(getActivity());
            }
        });
        //设置联系人数据
        Map<String, EaseUser> m = DemoHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>) m).clone();
        }
        setContactsMap(m);
        super.setUpView();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseUser user = (EaseUser) listView.getItemAtPosition(position);
                if (user != null) {
                    String username = user.getUsername();
                    // demo中直接进入聊天页面，实际一般是进入用户详情页
                    //startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", username));
                    FriendsInfoActivity.startAction(getActivity(), username, Constant.CHATTYPE_SINGLE, TAG);
                }
            }
        });


        // 进入添加好友页
        titleBar.getRightLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });


        contactSyncListener = new ContactSyncListener();
        DemoHelper.getInstance().addSyncContactListener(contactSyncListener);

        blackListSyncListener = new BlackListSyncListener();
        DemoHelper.getInstance().addSyncBlackListListener(blackListSyncListener);

        contactInfoSyncListener = new ContactInfoSyncListener();
        DemoHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(contactInfoSyncListener);

        if (DemoHelper.getInstance().isContactsSyncedWithServer()) {
            loadingView.setVisibility(View.GONE);
        } else if (DemoHelper.getInstance().isSyncingContactsWithServer()) {
            loadingView.setVisibility(View.VISIBLE);
        }
        setRefreshLayoutListener();
        getContractList();
        getGroupList();
    }


    protected void setRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        getContractList();
                    }
                }, 600);
            }
        });
    }

    private void getGroupList() {
        ((EMPresenter) mPresenter).getGroupList(getContext());
    }


    /**
     * 从后台获取好友列表
     */
    public void getContractList() {
        ((EMPresenter) mPresenter).getContractList(getContext());
    }

    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void disDialog() {

    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void onFailed(String message) {
        if (pd != null)
            pd.dismiss();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getContractListSuccess(List<EaseUser> mData) {
        swipeRefreshLayout.setRefreshing(false);
        // sort
        Collections.sort(mData, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if (lhs.getInitialLetter().equals(rhs.getInitialLetter())) {
                    return lhs.getNick().compareTo(rhs.getNick());
                } else {
                    if ("#".equals(lhs.getInitialLetter())) {
                        return 1;
                    } else if ("#".equals(rhs.getInitialLetter())) {
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }

            }
        });
        contactListLayout.init(mData);
        contactListLayout.refresh();
        //        refresh();
    }

    @Override
    public void FindFriendsSuccess(ArrayList<LoginResponseBean> mData) {

    }

    @Override
    public void deleteSuccess() {
        //删除成功后，发送消息，通知回话列表删除此人
        EventBus.getDefault().post(new EventMessage(EventMessage.DELETE_USER, toBeProcessUser.getUsername()));
        try {
            // delete contact
            // contactListLayout.removeItem(toBeProcessPosition - 1);
            deleteContact(toBeProcessUser);
            // remove invitation message
            InviteMessgeDao dao = new InviteMessgeDao(getActivity());
            dao.deleteMessage(toBeProcessUser.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected class HeaderItemClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.application_item:
                    // 进入申请与通知页面
                    NewFriendsActivity.startAction(getActivity(), TAG);
                    break;
                case R.id.group_item:
                    // 进入群聊列表页面
                    GroupListActivity.startAction(getActivity(), TAG);
                    break;
                //                case R.id.chat_room_item:
                //                    //进入聊天室列表页面
                //                    startActivity(new Intent(getActivity(), PublicChatRoomsActivity.class));
                //                    break;
                //                case R.id.robot_item:
                //                    //进入Robot列表页面
                //                    startActivity(new Intent(getActivity(), RobotsActivity.class));
                //                    break;
                //
                default:
                    break;
            }
        }

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        toBeProcessPosition = ((AdapterContextMenuInfo) menuInfo).position;
        toBeProcessUser = (EaseUser) listView.getItemAtPosition(toBeProcessPosition);
        toBeProcessUsername = toBeProcessUser.getUsername();
        getActivity().getMenuInflater().inflate(R.menu.em_context_contact_list, menu);
    }

    //长按监听
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_contact) {
            pd = new ProgressDialog(getActivity());
            pd.setMessage(getResources().getString(R.string.deleting));
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            mPresenter.deleteFirends(UserHelper.getUserId(), toBeProcessUser.getUserID());

            return true;
       /* } else if (item.getItemId() == R.id.add_to_blacklist) {
            moveToBlacklist(toBeProcessUsername);
            return true;*/
        }
        return super.onContextItemSelected(item);
    }


    /**
     * delete contact
     */
    public void deleteContact(final EaseUser tobeDeleteUser) {
        final String st2 = getResources().getString(R.string.Delete_failed);

        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(tobeDeleteUser.getUsername());
                    // remove user from memory and database
                    UserDao dao = new UserDao(getActivity());
                    dao.deleteContact(tobeDeleteUser.getUsername());
                    DemoHelper.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            contactList.remove(tobeDeleteUser);
                            contactListLayout.refresh();
                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                        }
                    });

                }

            }
        }).start();

    }

    class ContactSyncListener implements DemoHelper.DataSyncListener {
        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contact list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (success) {
                                loadingView.setVisibility(View.GONE);
                                refresh();
                            } else {
                                String s1 = getResources().getString(R.string.get_failed_please_check);
                                Toast.makeText(getActivity(), s1, Toast.LENGTH_LONG).show();
                                loadingView.setVisibility(View.GONE);
                            }
                        }

                    });
                }
            });
        }
    }

    class BlackListSyncListener implements DemoHelper.DataSyncListener {

        @Override
        public void onSyncComplete(boolean success) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    refresh();
                }
            });
        }

    }

    class ContactInfoSyncListener implements DemoHelper.DataSyncListener {

        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contactinfo list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingView.setVisibility(View.GONE);
                    if (success) {
                        refresh();
                    }
                }
            });
        }

    }

    @Subscribe/*(threadMode = ThreadMode.MAIN)*/
    public void onEventMainThread(EventMessage message) {
        if (mPresenter == null)
            return;
        if (message.getMessage().equals(EventMessage.USER_CONTRACT)) {
            getContractList();
        } else if (message.getMessage().equals(EventMessage.GROUP_CHANGED)) {
            getGroupList();
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
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (contactSyncListener != null) {
            DemoHelper.getInstance().removeSyncContactListener(contactSyncListener);
            contactSyncListener = null;
        }

        if (blackListSyncListener != null) {
            DemoHelper.getInstance().removeSyncBlackListListener(blackListSyncListener);
        }

        if (contactInfoSyncListener != null) {
            DemoHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(contactInfoSyncListener);
        }
        if (mPresenter != null && this instanceof BaseView) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

}
