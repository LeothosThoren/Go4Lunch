package com.leothosthoren.go4lunch.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.WorkmateAdapter;
import com.leothosthoren.go4lunch.model.firebase.WorkmateAndRestaurant;
import com.leothosthoren.go4lunch.utils.App;
import com.leothosthoren.go4lunch.utils.DataConverterHelper;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkmateViewHolder extends RecyclerView.ViewHolder implements DataConverterHelper {

    // Widgets
    @BindView(R.id.item_workmates_choice)
    TextView mTextViewWorkmateName;
    @BindView(R.id.item_workmates_photo)
    ImageView mImageViewWorkmatePhoto;

    public WorkmateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithWorkmateItem(WorkmateAndRestaurant workmateAndRestaurant, String currentUserId,
                                       RequestManager glide) {
        //Update workmate profile picture
        if (workmateAndRestaurant.getUsers() != null) {
            glide.load(workmateAndRestaurant.getUsers().getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.mImageViewWorkmatePhoto);
        }

        //Update workmate name
        assert workmateAndRestaurant.getRestaurants() != null;
        if (workmateAndRestaurant.getUsers().getUid().equals(workmateAndRestaurant.getRestaurants().getWorkmate().getUid())) {
            this.mTextViewWorkmateName.setText(App.getContext().getResources()
                    .getString(R.string.workmate_is_eating,
                            formatFullName(workmateAndRestaurant.getUsers().getUsername()),
                            workmateAndRestaurant.getRestaurants().getPlaceDetail().getResult().getName()));
        } else {
            this.mTextViewWorkmateName.setText(App.getContext().getResources()
                    .getString(R.string.workmate_default_decision, formatFullName(workmateAndRestaurant.getUsers().getUsername())));
        }

    }

}
