package kr.apptimer.android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import kr.apptimer.base.InjectApplicationContext;
import kr.apptimer.dagger.android.OverlayViewModel;

/***
 * Android service class for showing setting overlay
 * In the overlay, user can set how long an installed application will last.
 * @author Singlerr
 */
public final class AppExpirationOverlayService extends Service {

    @Inject
    OverlayViewModel viewModel;

    private WindowManager windowManager;

    private View view;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        InjectApplicationContext.getInstance().getContext().inject(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        view = inflater.inflate(viewModel.getLayoutId(), null);

        windowManager.addView(view, viewModel.getLayoutParams());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager == null || view == null)
            return;

        windowManager.removeView(view);
        windowManager = null;
        view = null;
    }
}
