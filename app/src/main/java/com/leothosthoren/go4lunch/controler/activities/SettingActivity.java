package com.leothosthoren.go4lunch.controler.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.api.UserHelper;
import com.leothosthoren.go4lunch.base.BaseActivity;
import com.leothosthoren.go4lunch.model.firebase.Users;

import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingActivity extends BaseActivity {

    public static final int DELETE_USER_TASK = 68; //ASCII 'D'
    public static final int UPDATE_USERNAME = 85; //ASCII 'U'
    // STATIC DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;
    // 2 - STATIC DATA FOR PICTURE
    @BindView(R.id.placeholder)
    ImageView mImageViewProfile;
    @BindView(R.id.username)
    TextInputEditText mTextInputEditTextUsername;
    // 1 - Uri of image selected by user
    private Uri uriImageSelected;
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

    // Ask permission when accessing to this listener
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    @OnClick(R.id.placeholder)
    public void onClickSettingPicture(View view) {
        this.chooseImageFromPhone();

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

    private void uploadPhotoInFireBase() {
        String uuid = UUID.randomUUID().toString();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(uuid);
        storageReference.putFile(this.uriImageSelected)
                .addOnSuccessListener(this, taskSnapshot -> {
                    String linkImageSavedInFireBase = Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString();

                    //Update picture
                    UserHelper.updateProfilPicture(linkImageSavedInFireBase,
                            Objects.requireNonNull(this.getCurrentUser()).getUid())
                            .addOnFailureListener(this.onFailureListener());
                })
                .addOnFailureListener(this.onFailureListener());
    }

    //Create or update on Firestore
    private void updateUserNameInFireBase() {
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

            //data from firestore
            UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {
                Users currentUser = documentSnapshot.toObject(Users.class);
                assert currentUser != null;

                //Get user picture from providers on Firebase
                if (currentUser.getUrlPicture() != null) {
                    Glide.with(this)
                            .load(currentUser.getUrlPicture())
                            .apply(RequestOptions.circleCropTransform())
                            .into(this.mImageViewProfile);
                }
                String username = TextUtils.isEmpty(currentUser.getUsername()) ?
                        getString(R.string.info_no_user_name_found) : currentUser.getUsername();
                mTextInputEditTextUsername.setText(username);
            });
        }
    }


    // --------------------
    // UTILS
    // --------------------

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 6 - Calling the appropriate method after activity result
        this.handleResponse(requestCode, resultCode, data);
    }

    // --------------------
    // FILE MANAGEMENT
    // --------------------

    private void chooseImageFromPhone() {
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_files_access), RC_IMAGE_PERMS, PERMS);
            return;
        }
        // 3 - Launch an "Selection Image" Activity
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RC_CHOOSE_PHOTO);
    }

    // 4 - Handle activity response (after user has chosen or not a picture)
    private void handleResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                this.uriImageSelected = data.getData();
                Glide.with(this) //SHOWING PREVIEW OF IMAGE
                        .load(this.uriImageSelected)
                        .apply(RequestOptions.circleCropTransform())
                        .into(this.mImageViewProfile);
                this.uploadPhotoInFireBase();
            } else {
                Toast.makeText(this, getString(R.string.toast_title_no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
