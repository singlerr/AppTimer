package kr.apptimer.android.activity.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Named;

import kr.apptimer.R;
import kr.apptimer.dagger.android.AppAnalyticsHandler;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class Statistics extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ArrayList<BarEntry> entry_chart = new ArrayList<>();
        BarChart chart = findViewById(R.id.statistics);


        entry_chart.add(new BarEntry(1, 10)); //entry_chart에 좌표 데이터를 담는다.y값이 데이터넣는값
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
    public class DayAxisValueFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;
        public DayAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }
        @Override
        public String getFormattedValue(float value) {
            if (value==1)
                return "30분미만";
            else if(value==2)
                return "30분이상 2시간이하";
            else if(value==3)
                return "2시간 초과";
            return null;
        }
    }
}
