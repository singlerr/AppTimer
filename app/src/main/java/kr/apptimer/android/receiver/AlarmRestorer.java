package kr.apptimer.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import kr.apptimer.base.InjectApplicationContext;
import kr.apptimer.dagger.android.ApplicationRemovalExecutor;
import kr.apptimer.dagger.android.TaskScheduler;
import kr.apptimer.dagger.android.task.SerializableTask;
import kr.apptimer.database.LocalDatabase;
import kr.apptimer.database.data.InstalledApplication;

/***
 * Called by android when a device boots.
 * Searches {@link kr.apptimer.database.data.InstalledApplication} from {@link kr.apptimer.database.LocalDatabase} and re-schedule removal task to {@link kr.apptimer.dagger.android.TaskScheduler}
 * @author Singlerr
 */
public final class AlarmRestorer extends BroadcastReceiver {

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
            List<InstalledApplication> reservedApplications = database.installedApplicationDao().findAll();
            Date currentTime = Calendar.getInstance().getTime();
            for (InstalledApplication reservedApplication : reservedApplications) {
                if(reservedApplication.getTime().before(currentTime))
                    handleOutdatedSchedule(reservedApplication);
                else
                    handleYetSchedule(reservedApplication);
            }
        }
    }

    private void handleOutdatedSchedule(InstalledApplication application) {
        removalExecutor.requestRemoval(application.getPackageUri());
    }

    private void handleYetSchedule(InstalledApplication application) {
        scheduler.scheduleTask((SerializableTask) () -> removalExecutor.requestRemoval(application.getPackageUri()), application.getTime());
    }
}
