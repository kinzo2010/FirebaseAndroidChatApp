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
import kiki.chat.firebase.com.firebasechat.databinding.ItemLatestMessageBinding;
import kiki.chat.firebase.com.firebasechat.models.LatestMessage;
import kiki.chat.firebase.com.firebasechat.viewmodels.LatestMessageItemViewModel;

public class LatestMessageListAdapter extends RecyclerView.Adapter<LatestMessageListAdapter.ViewHolder> {

    List<LatestMessageItemViewModel> listItem = new ArrayList<>();

    public void addItem(LatestMessage latestMessage) {

        listItem.add(new LatestMessageItemViewModel(latestMessage));
        notifyItemInserted(getItemCount() - 1);

    }

    public void updateTime() {

        for(LatestMessageItemViewModel latestMessageItemViewModel : listItem) {

            latestMessageItemViewModel.updateTime();

        }

    }

    public void changeData(LatestMessage latestMessage) {

        int index;
        for (index = 0; index < getItemCount(); index++) {

            if (latestMessage.getUid().equals(listItem.get(index).getUid())) {
                listItem.set(index, new LatestMessageItemViewModel(latestMessage));
                if (latestMessage.getUid().equals(latestMessage.getOwnerUid()))
                    listItem.get(index).newMessageVisibility.set(View.VISIBLE);
                notifyItemChanged(index);
                break;
            }


        }

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
    public int getItemViewType(int position) {
        return R.layout.item_latest_message;
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemLatestMessageBinding itemLatestMessageBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            itemLatestMessageBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(LatestMessageItemViewModel latestMessageItemViewModel) {

            itemLatestMessageBinding.setItemViewModel(latestMessageItemViewModel);

        }
    }

}
