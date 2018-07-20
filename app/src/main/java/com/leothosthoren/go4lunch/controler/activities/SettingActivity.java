package com.leothosthoren.go4lunch.controler.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.api.UserHelper;
import com.leothosthoren.go4lunch.base.BaseActivity;
import com.leothosthoren.go4lunch.model.firebase.Users;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    public static final int DELETE_USER_TASK = 68; //ASCII 'D'
    public static final int UPDATE_USERNAME = 85; //ASCII 'U'

    @BindView(R.id.placeholder)
    ImageView mImageViewProfile;
    @BindView(R.id.username)
    TextInputEditText mTextInputEditTextUsername;
//    @BindView(R.id.email_user) TextInputEditText mTextInputEditTextEmail




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
        this.updateUserNameInFireBase();
    }

    @OnClick(R.id.delete_account)
    public void onClickDeleteButton(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.popup_title)
                .setMessage(R.string.popup_message_confirmation_delete_account)
                .setPositiveButton(R.string.popup_message_choice_yes, (dialog, which) -> deleteUserFromFireBase())
                .setNegativeButton(R.string.popup_message_choice_no, null)
                .show();
    }

    // --------------------
    // REST REQUESTS
    // --------------------

    private void deleteUserFromFireBase() {
        if (this.getCurrentUser() != null) {
            UserHelper.deleteUser(this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());

            AuthUI.getInstance()
                    .delete(this)
                    .addOnSuccessListener(this, updateUiAfterHttpRequestsCompleted(DELETE_USER_TASK));
        }
    }

    //Create or update on Firestore
    private void updateUserNameInFireBase(){
        String username = this.mTextInputEditTextUsername.getText().toString();

        if (this.getCurrentUser() != null) {
            if (!username.isEmpty() && !username.equals(getString(R.string.info_no_user_name_found))) {
                UserHelper.updateUsername(username, this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener())
                        .addOnSuccessListener(this.updateUiAfterHttpRequestsCompleted(UPDATE_USERNAME));
            }
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

//            //Get user 's name
//            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ?
//                    getString(R.string.info_no_user_name_found) : this.getCurrentUser().getDisplayName();
//
//            //Update view with data
//            this.mTextInputEditTextUsername.setText(username);

            //data from firestore
            UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {
                Users currentUser = documentSnapshot.toObject(Users.class);
                assert currentUser != null;
                String username = TextUtils.isEmpty(currentUser.getUsername()) ?
                getString(R.string.info_no_user_name_found) : currentUser.getUsername();
                mTextInputEditTextUsername.setText(username);
            });
        }
    }

    private OnSuccessListener<Void> updateUiAfterHttpRequestsCompleted(final int taskId) {
        return aVoid -> {
            switch (taskId) {
                case UPDATE_USERNAME:
                    Toast.makeText(this, getString(R.string.dowloading), Toast.LENGTH_SHORT).show();
                    break;
                case DELETE_USER_TASK:
                    startMainActivity();
                    break;
                default:
                    break;
            }
        };
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
