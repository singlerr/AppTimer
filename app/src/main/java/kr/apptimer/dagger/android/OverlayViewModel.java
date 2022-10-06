package kr.apptimer.dagger.android;

import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;

/***
 * This class stores {@link android.view.WindowManager.LayoutParams} and layout id
 * Also, with {@link javax.inject.Inject} can be injected
 * @author Singlerr
 */
@Singleton
@Getter
public final class OverlayViewModel {

    private final WindowManager.LayoutParams layoutParams;

    private int layoutId;

    @Inject
    public OverlayViewModel() {
        this.layoutParams = new WindowManager.LayoutParams(
                300,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                150, 150,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );
        this.layoutParams.gravity = Gravity.CENTER;
    }
}
