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
package kr.apptimer.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import kr.apptimer.android.service.AppExpirationOverlayService;
import kr.apptimer.base.InjectApplicationContext;
import kr.apptimer.dagger.android.ApplicationRemovalExecutor;
import kr.apptimer.dagger.android.NotificationHelper;
import kr.apptimer.dagger.android.TaskScheduler;
import kr.apptimer.database.LocalDatabase;
import kr.apptimer.database.data.InstalledApplication;

/***
 * Called by android when a device boots.
 * Searches {@link kr.apptimer.database.data.InstalledApplication} from {@link kr.apptimer.database.LocalDatabase} and re-schedule removal task to {@link kr.apptimer.dagger.android.TaskScheduler}
 * Refer to <a href="https://developer.android.com/training/scheduling/alarms?hl=ko#boot"></a>
 * <br>
 * According to link above, all alarms need to be re-registered after boot.
 * @author Singlerr
 */
public final class AlarmRestorer extends BroadcastReceiver {

    @Inject
    NotificationHelper helper;

    @Inject
    TaskScheduler scheduler;

    @Inject
    LocalDatabase database;

    @Inject
    ApplicationRemovalExecutor removalExecutor;

    public AlarmRestorer() {
        super();
        InjectApplicationContext.getInstance().getContext().inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            ListenableFuture<List<InstalledApplication>> future =
                    database.installedApplicationDao().findAll();
            Futures.addCallback(
                    future,
                    new FutureCallback<List<InstalledApplication>>() {
                        @Override
                        public void onSuccess(List<InstalledApplication> result) {
                            Date currentTime = Calendar.getInstance().getTime();
                            for (InstalledApplication reservedApplication : result) {
                                if (reservedApplication.getTime().before(currentTime))
                                    handleOutdatedSchedule(context, reservedApplication);
                                else handleYetSchedule(reservedApplication);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {}
                    },
                    InjectApplicationContext.getExecutorService());
        }
    }

    private void handleOutdatedSchedule(Context context, InstalledApplication application) {
        String packageName = application.getPackageUri();
        Intent serviceIntent = new Intent(context, AppExpirationOverlayService.class);

        serviceIntent.putExtra(InjectApplicationContext.KEY_PACKAGE_URI, packageName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }

    private void handleYetSchedule(InstalledApplication application) {
        scheduler.scheduleApplicationRemoval(application);
    }
}
