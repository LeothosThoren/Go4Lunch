package com.leothosthoren.go4lunch.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.model.WorkmateItem;
import com.leothosthoren.go4lunch.model.firebase.Users;
import com.leothosthoren.go4lunch.utils.StringHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkmateViewHolder extends RecyclerView.ViewHolder implements StringHelper {

    //VIEW
    @BindView(R.id.item_workmates_choice)
    TextView mTextViewWorkmateName;
    @BindView(R.id.item_workmates_photo)
    ImageView mImageViewWorkmatePhoto;

    public WorkmateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithWorkmateItem(Users workmateItem, String currentUserId, RequestManager glide){
        //Update workmate profil picture
        if (workmateItem.getUrlPicture() != null) {
            glide.load(workmateItem.getUrlPicture())
                    .apply(RequestOptions.centerCropTransform())
                    .into(this.mImageViewWorkmatePhoto);
        }

        //Update workmate name
        if (workmateItem.getUsername() != null) {
            this.mTextViewWorkmateName.setText(displayFirstName(workmateItem.getUsername()));
        }
    }
}
