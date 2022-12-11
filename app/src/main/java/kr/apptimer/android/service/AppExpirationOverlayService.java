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
package kr.apptimer.android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Calendar;
import java.util.HashMap;
import javax.inject.Inject;
import kr.apptimer.R;
import kr.apptimer.base.InjectApplicationContext;
import kr.apptimer.dagger.android.AppAnalyticsHandler;
import kr.apptimer.dagger.android.ApplicationRemovalExecutor;
import kr.apptimer.dagger.android.NotificationHelper;
import kr.apptimer.dagger.android.OverlayViewModel;
import kr.apptimer.dagger.android.TaskScheduler;
import kr.apptimer.database.LocalDatabase;
import kr.apptimer.database.data.ApplicationStats;
import kr.apptimer.database.data.InstalledApplication;

/***
 * Android service class for showing setting overlay In the overlay, user can
 * set how long an installed application will last.
 *
 * @author Singlerr
 */
public final class AppExpirationOverlayService extends Service {

    @Inject
    AppAnalyticsHandler analyticsHandler;

    @Inject
    OverlayViewModel viewModel;

    @Inject
    TaskScheduler taskScheduler;

    @Inject
    ApplicationRemovalExecutor removalExecutor;

    @Inject
    LocalDatabase database;

    @Inject
    NotificationHelper notificationHelper;

    private WindowManager windowManager;

    private View view;

    private NumberPicker pickerDay;
    private NumberPicker pickerHour;
    private NumberPicker pickerMinute;

    private String packageName;

    @Override
    public void onCreate() {
        super.onCreate();

        InjectApplicationContext.getInstance().getContext().inject(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        view = inflater.inflate(viewModel.getLayoutId(), null);

        windowManager.addView(view, viewModel.getLayoutParams());
        // View 연결
        Button buttonPositive = view.findViewById(R.id.ok);
        Button buttonNegative = view.findViewById(R.id.cancel);
        pickerDay = view.findViewById(R.id.pickerDay);
        pickerHour = view.findViewById(R.id.pickerHour);
        pickerMinute = view.findViewById(R.id.pickerMinute);

        pickerDay.setMinValue(0);
        pickerDay.setMaxValue(100);

        pickerHour.setMinValue(0);
        pickerHour.setMaxValue(23);

        pickerMinute.setMinValue(0);
        pickerMinute.setMaxValue(59);

        buttonNegative.setOnClickListener(view -> exit());
        buttonPositive.setOnClickListener(view -> {
            try {
                int day = pickerDay.getValue();
                int hour = pickerHour.getValue();
                int minute = pickerMinute.getValue();

                Calendar calendar = Calendar.getInstance();

                calendar.add(Calendar.DATE, day);
                calendar.add(Calendar.HOUR_OF_DAY, hour);
                calendar.add(Calendar.MINUTE, minute);

                String applicationName = (String) getPackageManager()
                        .getApplicationLabel(
                                getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA));

                InstalledApplication application =
                        new InstalledApplication(packageName, applicationName, calendar.getTime());

                ListenableFuture<Void> future =
                        database.installedApplicationDao().insert(application);

                Futures.addCallback(
                        future,
                        new FutureCallback<Void>() {
                            @Override
                            public void onSuccess(Void result) {
                                Looper.prepare();
                                taskScheduler.scheduleApplicationRemoval(application, calendar.getTime());

                                ApplicationStats stats = new ApplicationStats(application, new HashMap<>());
                                long millis = (long) day * 24 * 60 * 60 * 1000
                                        + (long) hour * 60 * 60 * 1000
                                        + (long) minute * 60 * 1000;
                                ApplicationStats.DueCategory category = ApplicationStats.DueCategory.fromMillis(millis);
                                stats.getDueTimeCounts()
                                        .put(
                                                category.toString(),
                                                stats.getDueTimeCounts().get(category.toString()) + 1);
                                analyticsHandler.submitOrUpdateAppInformation(stats, null, null);
                                Toast.makeText(getApplicationContext(), "예약되었습니다.", Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onFailure(Throwable t) {}
                        },
                        InjectApplicationContext.getExecutorService());
                exit();
            } catch (PackageManager.NameNotFoundException e) {
                Log.d(e.toString(), e.getMessage());
            } catch (NumberFormatException ex) {
                Toast.makeText(getApplicationContext(), "숫자를 입력해주세요.", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        startForeground(1, notificationHelper.buildNotification("AppTimer", "오버레이 실행 중"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        packageName = intent.getStringExtra(InjectApplicationContext.KEY_PACKAGE_URI);
        ImageView appIcon = view.findViewById(R.id.appicon);

        TextView statsView = view.findViewById(R.id.time_text);

        analyticsHandler.getAppInformation(
                packageName,
                applicationStats -> {
                    ApplicationStats.DueCategory dueCategory = applicationStats.getMostCommon();
                    statsView.setText(formatText(dueCategory));
                },
                e -> {});

        try {
            Drawable icon = getPackageManager().getApplicationIcon(packageName);
            appIcon.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(getClass().getSimpleName(), "Cannot load application icon");
        }
        return START_NOT_STICKY;
    }

    private void exit() {
        stopService(new Intent(getApplicationContext(), AppExpirationOverlayService.class));
    }

    private boolean validateTime(int day, int hour, int minute) {
        return day >= 0 && hour >= 0 && hour <= 24 && minute >= 0 && minute <= 60;
    }

    private String formatText(ApplicationStats.DueCategory dueCategory) {
        String format = "이 앱은 보통 %s 삭제돼요.";
        switch (dueCategory) {
            case SHORT:
                return String.format(format, "30분 이내에");
            case MEDIUM:
                return String.format(format, "1시간 이내에");
            case LONG:
                return String.format(format, "2시간 넘어서");
            default:
                return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager == null || view == null) return;

        windowManager.removeView(view);
        windowManager = null;
        view = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
