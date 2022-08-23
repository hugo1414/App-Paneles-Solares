package com.example.renovasolar.actividades;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.renovasolar.MainActivity;
import com.example.renovasolar.R;
import android.graphics.Color;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class ReporteActivity extends AppCompatActivity {
    //GRAPHICS VARIABLES
    private PieChart pieChart;
    //CALENDAR VARIABLES
    private TextView mDisplayDate1;
    private TextView mDisplayDate2;
    private TextView res;
    private DatePickerDialog.OnDateSetListener mDateSetListener1;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;
    private static final String TAG1 = "DATE1";
    private static final String TAG2 = "DATE2";
    String date1;
    String date2;
    String sea;
    String scp;
    //BUTTON VARIABLES
    private View btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reporte);
        pieChart = findViewById(R.id.activity_main_piechart);//GRAPHICS
        mDisplayDate1 = (TextView) findViewById(R.id.tvdate1);//CALENDAR
        mDisplayDate2 = (TextView) findViewById(R.id.tvdate2);//CALENDAR
        btn = (Button) findViewById(R.id.buttonConsultar);
        res = (TextView) findViewById(R.id.resultado);





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    //CONECTA SERVIDOR TCP IP
                    ConfiguracionActivity.ConnectPyTask task = new ConfiguracionActivity.ConnectPyTask();
                    ConfiguracionActivity.ConnectPyTask.context = getApplicationContext();

                    //ENVIA TRAMA CON PAYLOAD TIPO 2(CONSULTA TIEMPO REAL PANEL SOLAR)
                    task.execute("3,"+date1+","+date2);

                    //Toast.makeText(ReporteActivity.this,date1, Toast.LENGTH_LONG).show();
                    //Toast.makeText(ReporteActivity.this,date2, Toast.LENGTH_LONG).show();

                    String[] valuesPanel = ConfiguracionActivity.values;
                    String sv = valuesPanel[0];//VOLTAJE EN TEXTO
                    String si = valuesPanel[1];//CORRIENTE EN TEXTO

                    double ea = Double.parseDouble(sv);//VOLTAJE EN DECIMAL
                    double cp = Double.parseDouble(si);//CORRIENTE EN DECIMAL

                    sea = String.valueOf(ea);
                    scp = String.valueOf(cp);



                    //res.setText(p+e);

                }catch (Exception e){
                    Log.d("Exception", e.toString());
                }

                setupPieChart();
                loadPieChartData();

            }
        });

        mDisplayDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ReporteActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener1,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ReporteActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG1, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                date1 = month + "/" + day + "/" + year;
                mDisplayDate1.setText(date1);
            }
        };
        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG2, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                date2 = month + "/" + day + "/" + year;
                mDisplayDate2.setText(date2);
            }
        };
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.4f, "Energia Ahorrada"));
        entries.add(new PieEntry(0.6f, "Consumo Promedio"));
        //entries.add(new PieEntry(0.10f, "Entertainment"));
        //entries.add(new PieEntry(0.25f, "Electricity and Gas"));
        //entries.add(new PieEntry(0.3f, "Housing"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }
}