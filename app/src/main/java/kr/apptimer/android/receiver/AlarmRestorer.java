package kr.apptimer.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import kr.apptimer.base.InjectApplicationContext;
import kr.apptimer.dagger.android.TaskScheduler;

/***
 * Called by android when a device boots.
 * Searches {@link kr.apptimer.database.data.InstalledApplication} from {@link kr.apptimer.database.LocalDatabase} and re-schedule removal task to {@link kr.apptimer.dagger.android.TaskScheduler}
 * @author Singlerr
 */
public final class AlarmRestorer extends BroadcastReceiver {

    @Inject
    TaskScheduler scheduler;

    public AlarmRestorer(){
        super();
        InjectApplicationContext.getInstance().getContext().inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){

        }
    }
}
