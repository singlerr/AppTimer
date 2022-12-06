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

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;
import javax.inject.Inject;
import kr.apptimer.R;
import kr.apptimer.base.InjectedAppCompatActivity;
import kr.apptimer.dagger.android.AppAnalyticsHandler;
import kr.apptimer.dagger.context.ActivityContext;
import kr.apptimer.database.data.ApplicationStats;

public class Statistics extends InjectedAppCompatActivity {

    @Inject
    AppAnalyticsHandler analyticsHandler;

    /***
     * Called after calling {@link ActivityContext#inject(any extends InjectedAppCompatActivity)} in context of {@link #onCreate(Bundle)}
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_statistics);
        // Package uri 받아오기
        String packageUri = null;

        ArrayList<BarEntry> entry_chart = new ArrayList<>();
        BarChart chart = findViewById(R.id.statistics);

        analyticsHandler.getAppInformation(
                packageUri,
                new OnSuccessListener<ApplicationStats>() {
                    @Override
                    public void onSuccess(ApplicationStats applicationStats) {
                        int val1 = applicationStats
                                .getDueTimeCounts()
                                .get(ApplicationStats.DueCategory.SHORT); // 30분 미만 삭제 예약한 횟수
                        int val2 = applicationStats
                                .getDueTimeCounts()
                                .get(ApplicationStats.DueCategory.MEDIUM); // 30분~1시간?
                        int val3 = applicationStats.getDueTimeCounts().get(ApplicationStats.DueCategory.LONG); // 그 이상

                        entry_chart.add(new BarEntry(ApplicationStats.DueCategory.SHORT.getTypeId(), val1));
                        // 이렇게 추가
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 데이터 없음 혹은 불러올 때 오류 생김
                    }
                });

        entry_chart.add(new BarEntry(1, 10)); // entry_chart에 좌표 데이터를 담는다.y값이 데이터넣는값
        entry_chart.add(new BarEntry(2, 20));
        entry_chart.add(new BarEntry(3, 30));

        BarDataSet dataSet = new BarDataSet(entry_chart, "시간");

        BarData data = new BarData(dataSet);

        dataSet.setColor(Color.parseColor("#80c2fe"));

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);
        xAxis.setLabelCount(3);
        xAxis.setValueFormatter(xAxisFormatter);

        chart.setData(data);
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

    public class DayAxisValueFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;

        public DayAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
            if (value == 1) return "30분미만";
            else if (value == 2) return "30분이상 2시간이하";
            else if (value == 3) return "2시간 초과";
            return null;
        }
    }
}
