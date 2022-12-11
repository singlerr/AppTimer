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
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import javax.inject.Inject;
import kr.apptimer.R;
import kr.apptimer.base.InjectApplicationContext;
import kr.apptimer.dagger.android.ApplicationRemovalExecutor;
import kr.apptimer.dagger.android.NotificationHelper;
import kr.apptimer.dagger.android.RemovalNotificationViewModel;

/***
 * A service that shows overlay which tells user that it will remove the reserved app
 * @author Singlerr
 */
public final class RemovalNotificationService extends Service {

    private String packageUri;

    private String name;

    private View view;

    private WindowManager windowManager;

    @Inject
    RemovalNotificationViewModel viewModel;

    @Inject
    ApplicationRemovalExecutor removalExecutor;

    @Inject
    NotificationHelper notificationHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        packageUri = intent.getStringExtra(InjectApplicationContext.KEY_PACKAGE_URI);
        name = intent.getStringExtra(InjectApplicationContext.KEY_NAME);

        ImageView iconView = view.findViewById(R.id.appicon);

        try {
            Drawable iconImage = getPackageManager().getApplicationIcon(packageUri);
            iconView.setImageDrawable(iconImage);
        } catch (PackageManager.NameNotFoundException e) {
            // No-op
        }
        TextView appNameView = view.findViewById(R.id.app_name);
        appNameView.setText(name);

        return START_NOT_STICKY;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        InjectApplicationContext.getInstance().getContext().inject(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        view = inflater.inflate(viewModel.getLayoutId(), null);

        windowManager.addView(view, viewModel.getLayoutParams());

        Button confirmButton = view.findViewById(R.id.ok);
        Button cancelButton = view.findViewById(R.id.cancel_btn);

        confirmButton.setOnClickListener(v -> {
            view.setVisibility(View.INVISIBLE);
            removalExecutor.requestRemoval(packageUri);
            exit();
        });

        cancelButton.setOnClickListener(v -> {
            exit();
        });

        startForeground(1, notificationHelper.buildNotification("AppTimer", "오버레이 실행 중"));
    }

    private void exit() {
        windowManager.removeView(view);
        stopService(new Intent(getApplicationContext(), RemovalNotificationService.class));
    }
}
