package com.leothosthoren.go4lunch.controler.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
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
import com.leothosthoren.go4lunch.services.ScheduledNotificationSender;

import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingActivity extends BaseActivity {

    // CONSTANT
    public static final int DELETE_USER_TASK = 68; //ASCII 'D'
    public static final int UPDATE_USERNAME = 85; //ASCII 'U'
    public static final int UPDATE_EMAIL = 86; //Simple ;)
    public static final int UPDATE_NOTIFICATION = 87; //Lazy ...
    // STATIC DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;
    // WIDGET
    @BindView(R.id.placeholder)
    ImageView mImageViewProfile;
    @BindView(R.id.setting_username)
    TextInputEditText mTextInputEditTextUsername;
    @BindView(R.id.setting_email)
    TextInputEditText mTextInputEditTextEmail;
    @BindView(R.id.switch_button)
    Switch mNotificationSwitch;
    // Uri of image selected by user
    private Uri uriImageSelected;
    // VAR
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.updateUIOnCreation();
        this.notificationSwitchHandler();
        this.configureAlarmManager(this);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_setting;
    }

    // --------------------
    // ACTIONS
    // --------------------

    @OnClick(R.id.update_username)
    public void onClickUpdateUsernameButton(View view) {
        this.updateUserNameInFireBase();
    }

    @OnClick(R.id.update_email)
    public void onClickUpdateEmailButton(View view) {
        this.updateEmailInFireBase();
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

    public void notificationSwitchHandler() {
        mNotificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // do some stuff
                updateNotificationSwitchOnFirebase(true, "Notification enabled");
                startAlarm(this);
            } else {
                // do other stuff
                updateNotificationSwitchOnFirebase(false, "Notification disabled");
                stopAlarm(this);
            }
        });

    }


    // --------------------
    // REST REQUESTS
    // --------------------


    private void deleteUserFromFireBase() {
        if (this.getCurrentUser() != null) {
            UserHelper.deleteUser(this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener(this));

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
                    UserHelper.updateProfilePicture(linkImageSavedInFireBase,
                            Objects.requireNonNull(this.getCurrentUser()).getUid())
                            .addOnFailureListener(this.onFailureListener(this));
                })
                .addOnFailureListener(this.onFailureListener(this));
    }

    //Create or update username on Firestore
    private void updateUserNameInFireBase() {
        String username = this.mTextInputEditTextUsername.getText().toString();

        if (this.getCurrentUser() != null) {
            if (!username.isEmpty() && !username.equals(getString(R.string.info_no_user_name_found))) {
                UserHelper.updateUsername(username, this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener(this))
                        .addOnSuccessListener(this.updateUiAfterHttpRequestsCompleted(UPDATE_USERNAME));
            }
        }
    }

    //Create or update username on Firestore
    private void updateEmailInFireBase() {
        String email = this.mTextInputEditTextEmail.getText().toString();

        if (this.getCurrentUser() != null) {
            if (!email.isEmpty() && !email.equals(getString(R.string.info_no_email_found))) {
                UserHelper.updateEmail(email, this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener(this))
                        .addOnSuccessListener(this.updateUiAfterHttpRequestsCompleted(UPDATE_EMAIL));
            }
        }
    }

    // Create or update notification on Firestore
    private void updateNotificationSwitchOnFirebase(boolean check, String s) {
        if (this.getCurrentUser() != null) {
            UserHelper.updateNotification(Objects.requireNonNull(getCurrentUser()).getUid(), check)
                    .addOnCompleteListener(this.onCompleteListener(this, s))
                    .addOnFailureListener(this.onFailureListener(this));
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
                //Get username
                String username = TextUtils.isEmpty(getCurrentUser().getDisplayName()) ?
                        getString(R.string.info_no_user_name_found) : currentUser.getUsername();
                //Get email
                String email = TextUtils.isEmpty(getCurrentUser().getEmail()) ?
                        getString(R.string.info_no_email_found) : getCurrentUser().getEmail();
                //Get notification switch position
                Boolean isSwitchChecked = false;
                if (currentUser.getNotificationEnabled() != null) {
                    isSwitchChecked = currentUser.getNotificationEnabled();
                    Log.d(TAG, "updateUIOnCreation: switch = " + currentUser.getNotificationEnabled());
                }
                //Set Widgets
                this.mNotificationSwitch.setChecked(isSwitchChecked);
                this.mTextInputEditTextUsername.setText(username);
                this.mTextInputEditTextEmail.setText(email);
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
                case UPDATE_EMAIL:
                    Toast.makeText(this, getString(R.string.dowloading), Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_NOTIFICATION:
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
        // Calling the appropriate method after activity result
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
        // Launch an "Selection Image" Activity
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RC_CHOOSE_PHOTO);
    }

    // Handle activity response (after user has chosen or not a picture)
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


    // --------------------
    // SCHEDULED ALARM
    // --------------------

    private void configureAlarmManager(Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ScheduledNotificationSender.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private void startAlarm(Context context) {
        // Set the alarm to start at approximately 12:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);

        // SetInexactRepeating()
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        //Enable notification even after rebooting
        ComponentName receiver = new ComponentName(context, ScheduledNotificationSender.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Toast.makeText(context, "Alarm set !", Toast.LENGTH_SHORT).show();
    }

    private void stopAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);

            //Disable notification even after rebooting
            ComponentName receiver = new ComponentName(context, ScheduledNotificationSender.class);
            PackageManager pm = context.getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);

            Toast.makeText(this, "Alarm Canceled !", Toast.LENGTH_SHORT).show();
        }
    }


}
