package com.example.lab06;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public final class PackageAddedDetectionService extends Service {
    private PackageAddedReceiver receiver;
    boolean isRunning=false;
    public PackageAddedDetectionService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(!isRunning) {
            Log.d(getPackageName(), "Service onCreate");
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
            intentFilter.addDataScheme("package");
            receiver = new PackageAddedReceiver();
            this.registerReceiver(receiver, intentFilter);
            isRunning=true;
        }
        else Log.d(getPackageName(),"Service is Already Running");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isRunning) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
            intentFilter.addDataScheme("package");
            receiver = new PackageAddedReceiver();
            this.registerReceiver(receiver, intentFilter);
            isRunning=true;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        isRunning=false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}