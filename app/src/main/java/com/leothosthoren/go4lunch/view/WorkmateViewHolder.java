package com.leothosthoren.go4lunch.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.model.firebase.Restaurants;
import com.leothosthoren.go4lunch.model.firebase.Users;
import com.leothosthoren.go4lunch.utils.App;
import com.leothosthoren.go4lunch.utils.DataConverterHelper;

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

    public void updateWithWorkmateItem(Users workmateItem, String currentUserId,
                                       Restaurants restaurant, RequestManager glide) {
        //Update workmate profile picture
        if (workmateItem.getUrlPicture() != null) {
            glide.load(workmateItem.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.mImageViewWorkmatePhoto);
        }

        //Update workmate name
        if (currentUserId != null) {
//            assert restaurant.getWorkmate() != null;
            if (workmateItem.getUid().equals(restaurant.getWorkmate().getUid())) {
                this.mTextViewWorkmateName.setText(App.getContext().getResources()
                        .getString(R.string.workmate_is_eating, formatFullName(workmateItem.getUsername()), restaurant.getPlaceDetail().getResult().getName()));
            } else {
                this.mTextViewWorkmateName.setText(App.getContext().getResources()
                        .getString(R.string.workmate_default_decision, formatFullName(workmateItem.getUsername())));
            }

        }
    }
}
