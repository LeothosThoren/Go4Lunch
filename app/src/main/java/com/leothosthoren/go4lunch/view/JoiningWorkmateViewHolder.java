package com.leothosthoren.go4lunch.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.model.firebase.Restaurants;
import com.leothosthoren.go4lunch.utils.App;
import com.leothosthoren.go4lunch.utils.DataConverterHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JoiningWorkmateViewHolder extends RecyclerView.ViewHolder implements DataConverterHelper {

    // Widgets
    @BindView(R.id.item_workmates_choice)
    TextView mTextViewJoiningWorkmateName;
    @BindView(R.id.item_workmates_photo)
    ImageView mImageViewJoiningWorkmatePhoto;


    public JoiningWorkmateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithJoiningWorkmateItem(Restaurants joiningWorkmate, String currentUserId,
                                              RequestManager glide) {

        if (joiningWorkmate.getWorkmate() != null) {
            //Update workmate profile picture
            glide.load(joiningWorkmate.getWorkmate().getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.mImageViewJoiningWorkmatePhoto);

            //Update workmate name
            this.mTextViewJoiningWorkmateName.setText(App.getContext().getResources()
                    .getString(R.string.workmate_is_joining, formatFullName(joiningWorkmate.getWorkmate().getUsername())));
        }

    }

}
