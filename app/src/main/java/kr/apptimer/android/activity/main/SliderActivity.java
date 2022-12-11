/*
Copyright 2022 singlerr

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

   * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
   * Neither the name of singlerr nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package kr.apptimer.android.activity.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import javax.inject.Inject;
import kr.apptimer.R;
import kr.apptimer.android.page.PagerAdapter;
import kr.apptimer.base.InjectApplicationContext;
import kr.apptimer.base.InjectedAppCompatActivity;
import kr.apptimer.dagger.android.TaskScheduler;
import kr.apptimer.dagger.context.ActivityContext;
import kr.apptimer.utils.CancellableRunnable;

public final class SliderActivity extends InjectedAppCompatActivity {
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;

    @Inject
    TaskScheduler scheduler;

    public void permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                scheduler.scheduleAsyncRepeatingTask(
                        new CancellableRunnable() {
                            @Override
                            public void run() {
                                if (Settings.canDrawOverlays(SliderActivity.this)) {
                                    InjectApplicationContext.getMainHandler().post(() -> {
                                        Intent intent = new Intent(getApplicationContext(), SliderActivity.class);
                                        startActivity(intent);
                                        setCancelled(true);
                                    });

                                }
                            }
                        },
                        1L);
                Intent intent =
                        new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);

            }
        }
    }

    /***
     * Called after calling {@link ActivityContext#inject(any extends InjectedAppCompatActivity)} in context of {@link #onCreate(Bundle)}
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_slider);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM)
                    == PackageManager.PERMISSION_DENIED)
                startActivity(new Intent(
                        android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                        Uri.parse("package:" + getPackageName())));
        }
        permissionCheck();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.vpPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager, true);
        Button StartButton = findViewById(R.id.start);

        StartButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PermissionPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    /***
     * Fill the method body to inject subclass of this using {@param context}
     * @param context {@link ActivityContext}
     */
    @Override
    protected void inject(ActivityContext context) {
        context.inject(this);
    }
}
