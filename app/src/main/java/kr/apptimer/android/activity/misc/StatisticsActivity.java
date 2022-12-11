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
package kr.apptimer.android.activity.misc;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.ArrayList;
import javax.inject.Inject;
import kr.apptimer.R;
import kr.apptimer.base.InjectApplicationContext;
import kr.apptimer.base.InjectedAppCompatActivity;
import kr.apptimer.dagger.android.AppAnalyticsHandler;
import kr.apptimer.dagger.context.ActivityContext;
import kr.apptimer.database.data.ApplicationStats;
import lombok.NoArgsConstructor;

/***
 * Activity that shows usage statistics to user
 * <br>
 * pass {@link InjectApplicationContext#KEY_NAME} and {@link InjectApplicationContext#KEY_PACKAGE_URI} via intent extra
 * @author Singlerr, jaeyoon
 */
public final class StatisticsActivity extends InjectedAppCompatActivity {

    @Inject
    AppAnalyticsHandler analyticsHandler;

    /***
     * Called after calling {@link ActivityContext#inject(any extends InjectedAppCompatActivity)} in context of {@link #onCreate(Bundle)}
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_statistics);

        String packageUri = getIntent().getStringExtra(InjectApplicationContext.KEY_PACKAGE_URI);
        String name = getIntent().getStringExtra(InjectApplicationContext.KEY_NAME);

        TextView appNameView = findViewById(R.id.appname);

        appNameView.setText(name);

        ImageView appImageView = findViewById(R.id.appImage);

        try {
            Drawable icon = getPackageManager().getApplicationIcon(packageUri);
            appImageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            // No-op
        }

        BarChart chart = findViewById(R.id.statistics);

        analyticsHandler.getAppInformation(
                packageUri,
                applicationStats -> {
                    int shortCount =
                            applicationStats.getDueTimeCounts().get(ApplicationStats.DueCategory.SHORT.toString());
                    int mediumCount =
                            applicationStats.getDueTimeCounts().get(ApplicationStats.DueCategory.MEDIUM.toString());
                    int longCount =
                            applicationStats.getDueTimeCounts().get(ApplicationStats.DueCategory.LONG.toString());

                    ArrayList<BarEntry> entry_chart = new ArrayList<>();

                    entry_chart.add(new BarEntry(ApplicationStats.DueCategory.SHORT.getTypeId(), shortCount));
                    entry_chart.add(new BarEntry(ApplicationStats.DueCategory.MEDIUM.getTypeId(), mediumCount));
                    entry_chart.add(new BarEntry(ApplicationStats.DueCategory.LONG.getTypeId(), longCount));
                    InjectApplicationContext.getMainHandler().post(() -> {
                        BarDataSet dataSet = new BarDataSet(entry_chart, "시간");
                        BarData data = new BarData(dataSet);
                        dataSet.setColor(Color.parseColor("#80c2fe"));
                        chart.setData(data);
                        chart.invalidate();
                    });
                },
                e -> {
                    Toast.makeText(this, "아직 이 앱은 통계가 없어요.", Toast.LENGTH_LONG).show();
                });

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter();
        XAxis xAxis = chart.getXAxis();


        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);
        xAxis.setLabelCount(3);
        xAxis.setValueFormatter(xAxisFormatter);

        chart.invalidate();
    }

    /***
     * Fill the method body to inject subclass of this using {@param context}
     * @param context {@link ActivityContext}
     */
    @Override
    protected void inject(ActivityContext context) {
        context.inject(this);
    }

    @NoArgsConstructor
    private static class DayAxisValueFormatter extends ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            if (value == 1) return "30분미만";
            else if (value == 2) return "30분이상 2시간이하";
            else if (value == 3) return "2시간 초과";
            return null;
        }
    }
}
