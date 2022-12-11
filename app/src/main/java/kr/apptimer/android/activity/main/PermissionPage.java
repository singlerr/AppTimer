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
package kr.apptimer.android.activity.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import javax.inject.Inject;
import kr.apptimer.R;
import kr.apptimer.android.activity.intro.SliderActivity;
import kr.apptimer.android.activity.main.recycler.AppViewAdapter;
import kr.apptimer.android.activity.main.recycler.AppViewHolder;
import kr.apptimer.android.activity.misc.StatisticsActivity;
import kr.apptimer.base.InjectApplicationContext;
import kr.apptimer.base.InjectedAppCompatActivity;
import kr.apptimer.dagger.android.IntentCache;
import kr.apptimer.dagger.context.ActivityContext;
import kr.apptimer.database.LocalDatabase;
import kr.apptimer.database.data.InstalledApplication;

public class PermissionPage extends InjectedAppCompatActivity {
    private static final int SPAN_COUNT = 5;

    @Inject
    LocalDatabase database;

    @Inject
    IntentCache cache;

    private RecyclerView recyclerView;
    /***
     * Called after calling {@link ActivityContext#inject(any extends InjectedAppCompatActivity)} in context of {@link #onCreate(Bundle)}
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);

        if (!first) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst", true);
            editor.apply();
            setContentView(R.layout.activity_permission_request);
            Button CheckButton = findViewById(R.id.check);
            CheckButton.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), SliderActivity.class);
                startActivity(intent);
            });
        } else {
            setContentView(R.layout.activity_main);
            Button statisticsButton = findViewById(R.id.statistics);
            statisticsButton.setOnClickListener(v -> {
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                    if (viewHolder instanceof AppViewHolder) {
                        AppViewHolder app = (AppViewHolder) viewHolder;
                        if (app.isSelected()) {
                            Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                            intent.putExtra(InjectApplicationContext.KEY_PACKAGE_URI, app.getPackageUri());
                            intent.putExtra(InjectApplicationContext.KEY_NAME, app.getName());
                            startActivity(intent);
                        }
                    }
                }
            });

            recyclerView = findViewById(R.id.app);
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), SPAN_COUNT));
            AppViewAdapter appViewAdapter = new AppViewAdapter(
                    database.installedApplicationDao(), getApplicationContext().getPackageManager());
            recyclerView.setAdapter(appViewAdapter);

            Button cancelButton = findViewById(R.id.reservationNo);

            cancelButton.setOnClickListener(view -> {
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                    if (viewHolder instanceof AppViewHolder) {
                        AppViewHolder app = (AppViewHolder) viewHolder;

                        if (app.isSelected()) {
                            AlertDialog dialog = new AlertDialog.Builder(this)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("알림")
                                    .setMessage("삭제 예정을 취소하시겠어요?")
                                    .setPositiveButton("예", (dialogInterface, i1) -> {
                                        ListenableFuture<InstalledApplication> future =
                                                database.installedApplicationDao()
                                                        .findByPackageUri(app.getPackageUri());

                                        Futures.addCallback(
                                                future,
                                                new FutureCallback<InstalledApplication>() {
                                                    @Override
                                                    public void onSuccess(InstalledApplication result) {
                                                        InjectApplicationContext.getMainHandler()
                                                                .post(() -> {
                                                                    cancel(result);

                                                                    database.installedApplicationDao()
                                                                            .delete(result)
                                                                            .addListener(
                                                                                    () -> {},
                                                                                    InjectApplicationContext
                                                                                            .getExecutorService());
                                                                    appViewAdapter.reload();
                                                                    Toast toast = Toast.makeText(
                                                                            PermissionPage.this,
                                                                            "예약이 취소되었습니다.",
                                                                            Toast.LENGTH_SHORT);
                                                                    toast.show();
                                                                });
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable t) {}
                                                },
                                                InjectApplicationContext.getExecutorService());
                                    })
                                    .setNegativeButton("아니요", null)
                                    .create();

                            dialog.show();
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        AppViewAdapter viewAdapter = (AppViewAdapter) recyclerView.getAdapter();
        viewAdapter.reload();
        super.onRestart();
    }

    private void cancel(InstalledApplication installedApplication) {
        PendingIntent pendingIntent = cache.getCachedIntent(installedApplication.getPackageUri());

        if (pendingIntent == null) return;

        pendingIntent.cancel();
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
