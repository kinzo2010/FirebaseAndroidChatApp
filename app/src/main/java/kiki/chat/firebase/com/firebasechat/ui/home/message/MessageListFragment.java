package kiki.chat.firebase.com.firebasechat.ui.home.message;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;

import kiki.chat.firebase.com.firebasechat.R;
import kiki.chat.firebase.com.firebasechat.databinding.FragmentMessageListBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageListFragment extends Fragment {

    FragmentMessageListBinding fragmentMessageListBinding;
    MessageListViewModel messageListViewModel;

    public MessageListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentMessageListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message_list, container, false);
        messageListViewModel = new MessageListViewModel(getContext(), getActivity(), fragmentMessageListBinding);
        fragmentMessageListBinding.setViewModel(messageListViewModel);
        return fragmentMessageListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageListViewModel.fetchLatestMessage();
    }

    @Override
    public void onResume() {
        super.onResume();

        messageListViewModel.updateUI();
    }
}
