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
package kr.apptimer.android.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import javax.inject.Inject;
import kr.apptimer.android.utils.PermissionRequest;
import kr.apptimer.base.InjectedAppCompatActivity;
import kr.apptimer.dagger.android.NotificationHelper;
import kr.apptimer.dagger.context.ActivityContext;
import kr.apptimer.database.LocalDatabase;

/***
 * Activity shown first on start of application
 * Request permission here.
 * @author Singlerr
 */
public final class SettingsActivity extends InjectedAppCompatActivity {

  @Inject LocalDatabase database;

  @Inject NotificationHelper notificationHelper;

  @Override
  public void onActivityCreate(@Nullable Bundle savedInstanceState) {
    //Request permission here with PermissionRequestBuilder
    PermissionRequest request =
        PermissionRequest.builder()
            .context(this)
            .withPermission(
                PermissionRequest.Permission.builder()
                    .permission(Manifest.permission.SCHEDULE_EXACT_ALARM)
                    .when(
                        ctx ->
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                                && ContextCompat.checkSelfPermission(
                                        ctx, Manifest.permission.SCHEDULE_EXACT_ALARM)
                                    != PackageManager.PERMISSION_GRANTED)
                    .build())
            .withPermission(
                PermissionRequest.Permission.builder()
                    .permission(Manifest.permission.SYSTEM_ALERT_WINDOW)
                    .when(
                        ctx ->
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                && Settings.canDrawOverlays(ctx))
                    .build())
            .build();

    request.execute();
  }

  @Override
  public void bindListeners() {}

  @Override
  protected void inject(ActivityContext context) {
    context.inject(this);
  }
}
