package kr.apptimer.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import kr.apptimer.base.InjectApplicationContext;
import kr.apptimer.database.LocalDatabase;

/***
 * Receiver class for installing application
 * @author Singlerr
 */
public final class ApplicationInstallationReceiver extends BroadcastReceiver {

    @Inject
    LocalDatabase database;

    public ApplicationInstallationReceiver(){
        super();
        InjectApplicationContext.getInstance().getContext().inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
