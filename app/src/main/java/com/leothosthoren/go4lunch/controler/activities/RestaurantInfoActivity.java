package com.leothosthoren.go4lunch.controler.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.widget.Button;
import android.widget.Toast;

import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.base.BaseActivity;

import butterknife.BindView;

public class RestaurantInfoActivity extends BaseActivity {

    //    private Button mButtonCall = (Button) findViewById(R.id.restaurtant_info_phone_button);
//    private FloatingActionButton mFloatingActionButton = findViewById(R.id.floatingActionButton);
    @BindView(R.id.restaurtant_info_phone_button)
    Button mButtonCall;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.restaurant_info_website_button)
    Button mWebsiteButton;
    @BindView(R.id.restaurant_info_like_button)
    Button mLikeButton;
    private boolean isCheckFab = true;
    private boolean isCheckLike = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        click();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_restaurant_info;
    }

    //RESTAURANT SELECTION
    public void click() {
        mFloatingActionButton.setOnClickListener(v -> {

            if (isCheckFab) {
                mFloatingActionButton.setImageResource(R.drawable.ic_check_circle);
                isCheckFab = false;
                Snackbar.make(v, "checkFab true", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                mFloatingActionButton.setImageResource(R.drawable.ic_uncheck_circle);
                isCheckFab = true;
                Snackbar.make(v, "checkFab false", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });

        //LIKE BUTTON
        mLikeButton.setOnClickListener(v -> {

            if (isCheckLike) {
                mLikeButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_full, 0, 0);
                isCheckLike = false;
                Toast.makeText(this, "click on star button true", Toast.LENGTH_SHORT).show();
            } else {
                mLikeButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_border, 0, 0);
                isCheckLike = true;
                Toast.makeText(this, "click on star button false", Toast.LENGTH_SHORT).show();
            }

        });

        //CALLING
        mButtonCall.setOnClickListener(v -> dialPhoneNumber("0649036635"));

        //BROWSE URL
        mWebsiteButton.setOnClickListener(v -> openWebPage("http://google.fr/"));

    }


    private void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}

