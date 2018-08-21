package com.leothosthoren.go4lunch.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.model.firebase.WorkmateAndRestaurant;
import com.leothosthoren.go4lunch.view.WorkmateViewHolder;

import java.util.List;


public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateViewHolder> {
    //FOR DATA
    private final RequestManager glide;
    private final String idCurrentUser;
    private List<WorkmateAndRestaurant> mWorkmateAndRestaurantList;


    public WorkmateAdapter(List<WorkmateAndRestaurant> workmateAndRestaurantList, RequestManager glide, String idCurrentUser) {
        this.mWorkmateAndRestaurantList = workmateAndRestaurantList;
        this.glide = glide;
        this.idCurrentUser = idCurrentUser;
    }

    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkmateViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_workmates, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position) {
        //add method holder
        if (!mWorkmateAndRestaurantList.get(position).getUsers().getUid().equals(idCurrentUser)) {
            holder.updateWithWorkmateItem(mWorkmateAndRestaurantList.get(position), this.idCurrentUser, this.glide);
        }
    }

    @Override
    public int getItemCount() {
        return mWorkmateAndRestaurantList.size();
    }

    //Handle click on recycler view list
    public WorkmateAndRestaurant getWorkamteRestaurantChoice(int position) {
        return this.mWorkmateAndRestaurantList.get(position);
    }

//    @Override
//    public void onDataChanged() {
//        super.onDataChanged();
//        this.callback.onDataChanged();
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position, @NonNull WorkmateAndRestaurant workmateAndRestaurant) {
//        //add method holder
//        if (!workmateAndRestaurant.getUsers().getUid().equals(idCurrentUser)) {
//            holder.updateWithWorkmateItem(workmateAndRestaurant, this.idCurrentUser, this.glide);
//        }
//
//    }

//    public interface Listener {
//        void onDataChanged();
//    }
}
