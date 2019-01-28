package kiki.chat.firebase.com.firebasechat.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kiki.chat.firebase.com.firebasechat.R;
import kiki.chat.firebase.com.firebasechat.databinding.ItemGroupChatListBinding;
import kiki.chat.firebase.com.firebasechat.models.Group;
import kiki.chat.firebase.com.firebasechat.viewmodels.GroupItemViewModel;

public class GroupChatListAdapter extends RecyclerView.Adapter<GroupChatListAdapter.ViewHolder>{

    private List<GroupItemViewModel> listItem = new ArrayList<>();

    public void addItem(Group group) {

        listItem.add(new GroupItemViewModel(group));
        notifyItemInserted(getItemCount()-1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_group_chat_list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemGroupChatListBinding itemGroupChatListBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            itemGroupChatListBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(GroupItemViewModel groupItemViewModel) {
            itemGroupChatListBinding.setItemViewModel(groupItemViewModel);
        }
    }

}
