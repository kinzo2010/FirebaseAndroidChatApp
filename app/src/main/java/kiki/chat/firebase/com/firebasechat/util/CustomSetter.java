package kiki.chat.firebase.com.firebasechat.util;

import android.databinding.BindingAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class CustomSetter {

    @BindingAdapter(value =  {"app:adapterVertical"}, requireAll = false)
    public static void setAdapterVertical(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @BindingAdapter(value =  {"app:gripAdapter"}, requireAll = false)
    public static void setGripAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
        adapter.notifyDataSetChanged();
    }

    @BindingAdapter(value = {"app:imageUrl"}, requireAll = false)
    public static void loadImage(ImageView imageView, String url) {
        if(url.length() <= 0) return;
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }

}
