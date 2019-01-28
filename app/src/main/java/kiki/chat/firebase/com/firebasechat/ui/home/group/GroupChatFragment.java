package kiki.chat.firebase.com.firebasechat.ui.home.group;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kiki.chat.firebase.com.firebasechat.R;
import kiki.chat.firebase.com.firebasechat.databinding.FragmentGroupChatBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends Fragment {

    FragmentGroupChatBinding fragmentGroupChatBinding;
    GroupChatViewModel groupChatViewModel;

    public GroupChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentGroupChatBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_chat, container, false);

        groupChatViewModel = new GroupChatViewModel(getContext(), getActivity(), fragmentGroupChatBinding);
        fragmentGroupChatBinding.setViewModel(groupChatViewModel);

        return fragmentGroupChatBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupChatViewModel.fetchGroupList();
    }
}
