package kiki.chat.firebase.com.firebasechat.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import kiki.chat.firebase.com.firebasechat.R;
import kiki.chat.firebase.com.firebasechat.databinding.ItemChatFromRowBinding;
import kiki.chat.firebase.com.firebasechat.databinding.ItemChatToRowBinding;
import kiki.chat.firebase.com.firebasechat.models.ChatMessage;
import kiki.chat.firebase.com.firebasechat.models.GroupChatMessage;
import kiki.chat.firebase.com.firebasechat.viewmodels.UserChatMessageItemViewModel;

public class UserChatMessageListAdapter extends RecyclerView.Adapter<UserChatMessageListAdapter.ViewHolder>{

    List<UserChatMessageItemViewModel> listItem = new ArrayList<>();
    private String avatarUrl = "";

    public void addItem(ChatMessage chatMessage) {
        listItem.add(new UserChatMessageItemViewModel(chatMessage, avatarUrl));
        notifyItemInserted(listItem.size() - 1);
    }

    public void addItemGroupChat(GroupChatMessage groupChatMessage) {

        listItem.add(new UserChatMessageItemViewModel(groupChatMessage));
        notifyItemInserted(listItem.size() - 1);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listItem.get(position));
    }

    @Override
    public int getItemViewType(int position) {

        String currentUserUid = FirebaseAuth.getInstance().getUid();


        if(listItem.get(position).getFromId().equals(currentUserUid)) {
            return R.layout.item_chat_to_row;
        } else {
            return R.layout.item_chat_from_row;
        }

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        ItemChatFromRowBinding itemChatFromRowBinding;
        ItemChatToRowBinding itemChatToRowBinding;
        MessageType messageType;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            switch (viewType) {

                case R.layout.item_chat_to_row: {
                    itemChatToRowBinding = DataBindingUtil.bind(itemView);
                    messageType = MessageType.TO;
                    break;
                }
                case R.layout.item_chat_from_row: {
                    itemChatFromRowBinding = DataBindingUtil.bind(itemView);
                    messageType = MessageType.FROM;
                    break;
                }

            }
        }

        public void bind(UserChatMessageItemViewModel userChatMessageItemViewModel) {

            switch (messageType) {

                case TO: {
                    itemChatToRowBinding.setItemViewModel(userChatMessageItemViewModel);

                    break;
                }
                case FROM: {
                    itemChatFromRowBinding.setItemViewModel(userChatMessageItemViewModel);
                    break;
                }

            }
        }
    }

    enum MessageType {
        TO, FROM
    }

}
