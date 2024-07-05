/********************************************************
 * Autor: Alejandro Guarino Muñoz                       *
 *                                                      *
 * Descripcion: fragment que muestra las estadísticas   *
 * del usuario de manera semanal, mensual y anual       *
 ********************************************************/
package com.example.milectura.ui.stats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.milectura.R;
import com.example.milectura.utils.MyFormatter;

public class StatsFragment extends Fragment implements StatsContract.View {

    public static final String TAG = "StatsFragment";
    private static final String WEEKLYCHART = "WEEKL";
    private static final String MONTHLYCHART = "MONTHLY";
    private static final String YEARLYCHART = "YEARLY";

    private StatsContract.Presenter presenter;
    private BarChart barChart;
    private Button btWeekly;
    private Button btMonthly;
    private Button btYearly;
    private TextView tvPagesReaded;
    private TextView tvPagesAverage;
    private TextView tvDaysReaded;
    private TextView tvReadConsecutive;
    private TextView tvLabelReadConsecutive;
    private TextView tvNoPagesReaded;
    private TextView tvLabelDaysReaded;
    private RelativeLayout rlNoData;
    private ArrayList<String> weeklyLabels;
    private ArrayList<String> monthLabels;
    private ArrayList<String> yearLabels;

    public static Fragment newInstance(Bundle bundle) {
        StatsFragment fragment = new StatsFragment();
        if (bundle != null) fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //(setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btWeekly = view.findViewById(R.id.btWeekly);
        btMonthly = view.findViewById(R.id.btMonthly);
        btYearly = view.findViewById(R.id.btYearly);
        barChart = view.findViewById(R.id.chart);
        tvPagesReaded = view.findViewById(R.id.tvPagesReaded);
        tvPagesAverage = view.findViewById(R.id.tvPagesAverage);
        tvDaysReaded = view.findViewById(R.id.tvDaysReaded);
        tvReadConsecutive = view.findViewById(R.id.tvReadConsecutive);
        tvLabelReadConsecutive = view.findViewById(R.id.tvLabelReadConsecutive);
        tvLabelDaysReaded = view.findViewById(R.id.tvLabelDaysReaded);
        rlNoData = view.findViewById(R.id.rlNoData);
        tvNoPagesReaded = view.findViewById(R.id.tvNoPagesReaded);
        initWeekly();
        initMonthly();
        initYearly();
        initLabels();
        btWeekly.performClick();
    }

    private void initLabels() {
        monthLabels = new ArrayList();
        weeklyLabels = new ArrayList<>();
        weeklyLabels.add(getString(R.string.dayMon));
        weeklyLabels.add(getString(R.string.dayTue));
        weeklyLabels.add(getString(R.string.dayWed));
        weeklyLabels.add(getString(R.string.dayThu));
        weeklyLabels.add(getString(R.string.dayFri));
        weeklyLabels.add(getString(R.string.daySat));
        weeklyLabels.add(getString(R.string.daySun));
        yearLabels = new ArrayList<>();
        yearLabels.add(getString(R.string.monthJan));
        yearLabels.add(getString(R.string.monthFeb));
        yearLabels.add(getString(R.string.monthMar));
        yearLabels.add(getString(R.string.monthApr));
        yearLabels.add(getString(R.string.monthMay));
        yearLabels.add(getString(R.string.monthJun));
        yearLabels.add(getString(R.string.monthJul));
        yearLabels.add(getString(R.string.monthAug));
        yearLabels.add(getString(R.string.monthSep));
        yearLabels.add(getString(R.string.monthOct));
        yearLabels.add(getString(R.string.monthNov));
        yearLabels.add(getString(R.string.monthDic));
    }

    private void initWeekly() {
        btWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btWeekly.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_stats_button_selected));
                btMonthly.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_stats_button_unselected));
                btYearly.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_stats_button_unselected));
                tvLabelReadConsecutive.setText(getString(R.string.days_reading));
                tvLabelDaysReaded.setText(getString(R.string.days_read));
                LinkedHashMap<Long, Integer> pagesByDayWeekly = presenter.getPagesWeekly();
                loadChart(pagesByDayWeekly, WEEKLYCHART);
            }
        });
    }

    private void initMonthly() {
        btMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btWeekly.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_stats_button_unselected));
                btMonthly.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_stats_button_selected));
                btYearly.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_stats_button_unselected));
                tvLabelReadConsecutive.setText(getString(R.string.days_reading));
                tvLabelDaysReaded.setText(getString(R.string.days_read));
                LinkedHashMap<Long, Integer> pagesByDayMonthly = presenter.getPagesMonthly();
                loadChart(pagesByDayMonthly, MONTHLYCHART);
            }
        });
    }

    private void initYearly() {
        btYearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btWeekly.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_stats_button_unselected));
                btMonthly.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_stats_button_unselected));
                btYearly.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_stats_button_selected));
                tvLabelReadConsecutive.setText(getString(R.string.months_reading));
                tvLabelDaysReaded.setText(getString(R.string.months_readed));
                LinkedHashMap<Long, Integer> pagesByMonthly = presenter.getPagesYearly();
                loadChart(pagesByMonthly, YEARLYCHART);
            }
        });
    }

    private void loadChart(LinkedHashMap<Long, Integer> data, String type) {
        if (data.size() == 0) {
            rlNoData.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.GONE);
            setTextNoPagesReaded(type);
        } else {
            rlNoData.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
            int totalPages = 0;
            int pagesAverage = 0;
            int daysReading = 0;
            int daysRead = 0;
            int j = 0;
            ArrayList<BarEntry> entries = new ArrayList<>();
            int i = 0;
            for (Map.Entry<Long, Integer> entry : data.entrySet()) {
                entries.add(new BarEntry(i++, entry.getValue()));
                totalPages += entry.getValue();
                if (entry.getValue() > 0) {
                    daysRead++;
                    ++j;
                    if (j > daysReading)
                        daysReading = j;
                } else {
                    j = 0;
                }
            }
            Log.d("a", entries.toString());
            BarDataSet bardataset = new BarDataSet(entries, "Páginas leídas");
            BarData barData = new BarData(bardataset);

            barData.setValueFormatter(new MyFormatter());

            barChart.setData(barData);
            XAxis xAxis = barChart.getXAxis();
            //xAxis.setLabelRotationAngle(-35);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            ArrayList<String> formatter = getFormattter(type);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(formatter));

            barChart.getAxisLeft().setAxisMinValue(0f);
            barChart.getDescription().setEnabled(false);
            barChart.getAxisRight().setEnabled(false);
            barChart.getAxisLeft().setEnabled(false);
            //barChart.getAxisLeft().setLabelCount(5, false);
            barChart.setFitBars(true);
            int color = ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
            bardataset.setColors(color);
            barChart.animateY(500);

            Legend legend = barChart.getLegend();
            legend.setEnabled(false);
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setForm(Legend.LegendForm.CIRCLE);
            legend.setDrawInside(false);

            switch (type) {
                case WEEKLYCHART:
                    xAxis.setCenterAxisLabels(false);
                    pagesAverage = Math.round(totalPages / 7);
                    break;
                case MONTHLYCHART:
                    xAxis.setCenterAxisLabels(true);
                    pagesAverage = Math.round(totalPages / 30);
                    break;
                case YEARLYCHART:
                    xAxis.setCenterAxisLabels(false);
                    pagesAverage = Math.round(totalPages / 365);
                    break;
            }

            tvPagesReaded.setText(String.valueOf(totalPages));
            tvPagesAverage.setText(String.valueOf(pagesAverage));
            tvReadConsecutive.setText(String.valueOf(daysReading));
            tvDaysReaded.setText(String.valueOf(daysRead));
        }
    }

    private void setTextNoPagesReaded(String type) {
        switch (type) {
            case WEEKLYCHART:
                tvNoPagesReaded.setText(getString(R.string.no_read_week));
                break;
            case MONTHLYCHART:
                tvNoPagesReaded.setText(getString(R.string.no_read_month));
                break;
            case YEARLYCHART:
                tvNoPagesReaded.setText(getString(R.string.no_read_year));
                break;
        }
    }

    private ArrayList<String> getFormattter(String chartType) {
        switch (chartType) {
            case WEEKLYCHART:
                return weeklyLabels;
            case MONTHLYCHART:
                monthFormatter();
                return monthLabels;
            case YEARLYCHART:
                return yearLabels;
        }
        return null;
    }

    private void monthFormatter() {
        if (!monthLabels.isEmpty()) {
            monthLabels.clear();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        for (int i = 0; i < cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1; i++) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            String day = sdf.format(cal.getTime());
            monthLabels.add(day);
        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void setPresenter(StatsContract.Presenter presenter) {
        this.presenter = presenter;
    }

}
