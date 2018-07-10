package com.leothosthoren.go4lunch.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.leothosthoren.go4lunch.view.GenericViewHolder;

import java.util.ArrayList;

public class GenericRecyclerViewAdapter extends RecyclerView.Adapter<GenericViewHolder> {

    // FOR COMMUNICATION
    private final Listener callback;
    // FOR DATA
    private ArrayList<?> genericList;
    private RequestManager glide;
    public GenericRecyclerViewAdapter(ArrayList<?> genericList, RequestManager glide, Listener callback) {

        this.genericList = genericList;
        this.glide = glide;
        this.callback = callback;
    }



    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Try to check viewtype ok
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(viewType, parent, false);

        return new GenericViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        //Put viewholder infos
    }

    @Override
    public int getItemCount() {
        return this.genericList.size();
    }

    public interface Listener {
        void onClickItemButton(int postion);
    }
}
