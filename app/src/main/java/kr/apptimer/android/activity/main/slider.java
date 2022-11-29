package kr.apptimer.android.activity.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import kr.apptimer.R;
import kr.apptimer.android.page.PagerAdater;


public class slider extends AppCompatActivity {
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_slider);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            startActivity(new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:" + getPackageName())));
        }
        permissionCheck();
        TabLayout tabLayout=findViewById(R.id.tabLayout);
        ViewPager viewPager=findViewById(R.id.vpPager);
        PagerAdater pagerAdater=new PagerAdater(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdater);
        tabLayout.setupWithViewPager(viewPager,true);
        Button StartButton = findViewById(R.id.start);
        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
            }
        }
    }
}