package com.leothosthoren.go4lunch.controler.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    public static final int DELETE_USER_TASK = 68; //ASCII 'D'

    @BindView(R.id.placeholder)
    ImageView mImageViewProfile;
    @BindView(R.id.username)
    TextInputEditText mTextInputEditTextUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.configureToolbar();
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
    public void onClickUpdateButton(View view) {

    }

    @OnClick(R.id.delete_account)
    public void onClickDeleteButton(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.popup_title)
                .setMessage(R.string.popup_message_confirmation_delete_account)
                .setPositiveButton(R.string.popup_message_choice_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser();
                    }
                })
                .setNegativeButton(R.string.popup_message_choice_no, null)
                .show();
    }

    // --------------------
    // REST REQUESTS
    // --------------------

    private void deleteUser() {
        if (this.getCurrentUser() != null) {
            AuthUI.getInstance()
                    .delete(this)
                    .addOnSuccessListener(this, updateUiAfterHttpRequestsCompleted(DELETE_USER_TASK));
        }
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

    private OnSuccessListener<Void> updateUiAfterHttpRequestsCompleted(final int taskId) {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (taskId) {
                    case DELETE_USER_TASK:
                        startMainActivity();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
