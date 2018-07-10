package com.leothosthoren.go4lunch.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.leothosthoren.go4lunch.adapter.GenericRecyclerViewAdapter;

import java.lang.ref.WeakReference;

public class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private WeakReference<GenericRecyclerViewAdapter.Listener> callbackWeakRef;

    public GenericViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onClick(View v) {

    }
}
