package com.leothosthoren.go4lunch.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.model.firebase.Users;
import com.leothosthoren.go4lunch.view.WorkmateViewHolder;


public class WorkmateAdapter extends FirestoreRecyclerAdapter<Users, WorkmateViewHolder> {
    //FOR DATA
    private final RequestManager glide;
    private final String idCurrentUser;

    //FOR COMMUNICATION
    private Listener callback;


    public WorkmateAdapter(@NonNull FirestoreRecyclerOptions<Users> options,
                           RequestManager glide, Listener callback, String idCurrentUser) {
        super(options);
        this.glide = glide;
        this.callback = callback;
        this.idCurrentUser = idCurrentUser;
    }

    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkmateViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_workmates, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position, @NonNull Users model) {
        //add method holder
        holder.updateWithWorkmateItem(model, this.idCurrentUser, this.glide);
    }

    public interface Listener {
        void onDataChanged();
    }
}
