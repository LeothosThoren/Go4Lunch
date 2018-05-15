package com.leothosthoren.go4lunch.controlers;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.placeholder)
    ImageView mImageViewProfile;
    @BindView(R.id.username)
    TextInputEditText mTextInputEditTextUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolbar();
        this.updateUIOnCreation();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_setting;
    }

    // --------------------
    // ACTIONS
    // --------------------

    @OnClick(R.id.update_username)
    public void onClickUpdateButton() {

    }

    @OnClick(R.id.delete_account)
    public void onClickDeleteButton() {

    }

    // --------------------
    // UI
    // --------------------

    private void updateUIOnCreation() {

        if (this.getCurrentUser() != null) {

            //Get user picture from providers on Firebase
            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImageViewProfile);
            }

            //Get user 's name
            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ?
                    getString(R.string.info_no_user_name_found) : this.getCurrentUser().getDisplayName();

            //Update view with data
            this.mTextInputEditTextUsername.setText(username);
        }
    }
}
