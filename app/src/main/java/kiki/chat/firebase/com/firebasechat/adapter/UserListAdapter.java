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
import kiki.chat.firebase.com.firebasechat.databinding.ItemUserListBinding;
import kiki.chat.firebase.com.firebasechat.models.User;
import kiki.chat.firebase.com.firebasechat.ui.search.SearchViewModel;
import kiki.chat.firebase.com.firebasechat.viewmodels.UserItemViewModel;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{

    private List<UserItemViewModel> listItem = new ArrayList<>();

    public void setListItem(List<User> userList) {

        for(User user : userList) {

            listItem.add(new UserItemViewModel(user));

        }

        notifyDataSetChanged();

    }

    public void addItem(User user) {

        listItem.add(new UserItemViewModel(user));
        notifyItemInserted(listItem.size()-1);

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
        return R.layout.item_user_list;
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        ItemUserListBinding itemUserListBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            itemUserListBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(UserItemViewModel userItemViewModel) {

            itemUserListBinding.setItemViewModel(userItemViewModel);

        }
    }

}
