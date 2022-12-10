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
package kr.apptimer.dagger.android;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Date;
import javax.inject.Inject;
import javax.inject.Singleton;
import kr.apptimer.dagger.android.task.SerializableTask;
import kr.apptimer.dagger.android.task.TaskExecutor;
import kr.apptimer.database.data.InstalledApplication;
import kr.apptimer.database.data.InstalledApplicationParcelable;

/***
 * Schedule a task at a specific time(or date) by
 * {@link android.app.AlarmManager}
 *
 * @author Singlerr
 */
@Singleton
public final class TaskScheduler {

    private final Context context;

    private final AlarmManager alarmManager;

    @Inject
    IntentCache cache;

    @Inject
    public TaskScheduler(Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            throw new IllegalStateException("Only android version >= 23");
        }

        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /***
     * Schedule a task at a specific time
     *
     * @param task
     *            task
     * @param time
     *            time at the task to be executed
     * @deprecated Cannot pass {@link SerializableTask} within intent
     */
    @Deprecated
    @SuppressLint("NewApi")
    public void scheduleTask(SerializableTask task, Date time) {
        /*
        Intent intent = new Intent(context, TaskExecutor.class);
        intent.putExtra(TaskExecutor.TASK_EXECUTOR_BUNDLE, task);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTime(), pendingIntent);

         */
    }
    /***
     * Schedule a task at a specific time
     *
     * @param application
     *            application
     * @param time
     *            time at the task to be executed
     */
    @SuppressLint("NewApi")
    public void scheduleApplicationRemoval(InstalledApplication application, Date time) {
        Intent intent = new Intent(context, TaskExecutor.class);

        intent.putExtra(TaskExecutor.EXECUTOR_NAME, application.getName());
        intent.putExtra(TaskExecutor.EXECUTOR_PACKAGE_URI,application.getPackageUri());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        cache.putCache(application.getPackageUri(), pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTime(), pendingIntent);
        Log.d("alarm", time.toString());
    }

    /***
     * Schedule a task at a specific time
     *
     * @param packageUri package uri of application to be uninstalled later
     *
     * @param task
     *            task
     * @param time
     *            time at the task to be executed
     * @deprecated Cannot pass {@link SerializableTask} within intent
     */
    @SuppressLint("NewApi")
    public void scheduleTask(String packageUri, SerializableTask task, Date time) {
        /*
        Intent intent = new Intent(context, TaskExecutor.class);
        intent.putExtra(TaskExecutor.TASK_EXECUTOR_BUNDLE, task);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTime(), pendingIntent);
        cache.putCache(packageUri, pendingIntent);

         */
    }
}
