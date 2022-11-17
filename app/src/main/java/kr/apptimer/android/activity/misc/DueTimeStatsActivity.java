package kr.apptimer.android.activity.misc;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import kr.apptimer.base.InjectedAppCompatActivity;
import kr.apptimer.dagger.context.ActivityContext;

/***
 * Activity which shows statistics of due time of application to user
 */
public final class DueTimeStatsActivity extends InjectedAppCompatActivity {
    /***
     * Called after calling {@link ActivityContext#inject(any extends InjectedAppCompatActivity)} in context of {@link #onCreate(Bundle)}
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreate(@Nullable Bundle savedInstanceState) {

    }

    /***
     * Register listener for {@link View} here
     */
    @Override
    public void bindListeners() {

    }

    /***
     * Fill the method body to inject subclass of this using {@param context}
     * @param context {@link ActivityContext}
     */
    @Override
    protected void inject(ActivityContext context) {
        context.inject(this);
    }
}
