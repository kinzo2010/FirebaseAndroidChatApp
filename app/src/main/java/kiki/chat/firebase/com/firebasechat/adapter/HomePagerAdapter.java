package kiki.chat.firebase.com.firebasechat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kiki.chat.firebase.com.firebasechat.ui.home.group.GroupChatFragment;
import kiki.chat.firebase.com.firebasechat.ui.home.message.MessageListFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragmentList;

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);

        fragmentList = new Fragment[] {
                new MessageListFragment(),
                new GroupChatFragment()
        };
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList[position];
    }

    @Override
    public int getCount() {
        return fragmentList.length;
    }
}
