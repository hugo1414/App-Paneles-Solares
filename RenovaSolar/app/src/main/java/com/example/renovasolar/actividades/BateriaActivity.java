package com.example.renovasolar.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renovasolar.MainActivity;
import com.example.renovasolar.R;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class BateriaActivity extends AppCompatActivity {
    //VARIABLES DE LA VISTA
    static TextView porcentaje;
    static TextView estado;
    static TextView voltaje;
    static TextView corriente;
    static TextView potencia2;
    //static TextView maxVolt; //OJO VARIABLE POR VERSE
    //static TextView minVolt; //OJO VARIABLE POR VERSE
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bateria);

        //IMPORTA ID DE LOS TEXT VIEW
        estado = (TextView) findViewById(R.id.estadoBat);
        porcentaje = (TextView) findViewById(R.id.porcentajeBat);
        voltaje = (TextView) findViewById(R.id.voltajeBat);
        corriente = (TextView) findViewById(R.id.corrienteBat);
        potencia2 = (TextView) findViewById(R.id.potBat2);
        //maxVolt = (TextView) findViewById(R.id.maxVoltBat);
        //minVolt = (TextView) findViewById(R.id.minVoltBat);


        counter = 1;
        final Handler handler2 = new Handler();
        final int delay = 3000; //3 segundos

        handler2.postDelayed(new Runnable(){
            public void run(){

                handler2.postDelayed(this, delay);
                //Toast.makeText(getApplicationContext(), "dentro", Toast.LENGTH_SHORT).show();
                try {
                    //CONECTA SERVIDOR TCP IP
                    ConfiguracionActivity.ConnectPyTask task = new ConfiguracionActivity.ConnectPyTask();
                    ConfiguracionActivity.ConnectPyTask.context = getApplicationContext();

                    //ENVIA TRAMA CON PAYLOAD TIPO 2(CONSULTA TIEMPO REAL PANEL SOLAR)
                    task.execute("2");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String[] valuesPanel = ConfiguracionActivity.values;
                            String sv = valuesPanel[2];//VOLTAJE EN TEXTO
                            String si = valuesPanel[3];//CORRIENTE EN TEXTO

                            double dv = Double.parseDouble(sv);//VOLTAJE EN DECIMAL
                            double dc = Double.parseDouble(si);//CORRIENTE EN DECIMAL
                            double dp = dv*dc;

                            String p = String.valueOf(calPorcentaje(dv, dc)) + "%";//CALCULA PORCENTAJE DE BATERIA
                            String e = calEstado(dv, dc);//CALCULA EL ESTADO DE CARGA
                            String pot = String.valueOf(dp);

                            //ENVIA LOS VALORES CALCULADOS A MOSTRAR EN PANTALLA
                            porcentaje.setText(p);
                            estado.setText(e);
                            voltaje.setText(sv + " V");
                            corriente.setText(si + " A");
                            potencia2.setText(pot + " W");


                            //maxVolt.setText(sv);
                            //minVolt.setText(sv);
                        }
                    }, 1000);


                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }

                counter++;
                if(counter > 100){
                    System.out.println("Loop: Termina proceso.");
                    handler2.removeCallbacksAndMessages(null);
                    //Toast.makeText(getApplicationContext(), "fin", Toast.LENGTH_SHORT).show();
                }

            }
        }, delay);



    }
    public static double calPorcentaje (double v, double i) {
        double aux;
        aux =Math.round((v*100)/42);//REGLA DE 3 SIMPLE, POR DEFINIR FORMULA
        return aux; //CALCULA PORCENTAJE
    }
    public static String calEstado (double v, double i) {
        double umbralCorriente = 0.2; // UMBRAL DE CORRIENTE PARA CARGA
        String output = "";//DECLARA VARIABLE DE SALIDA
        if (i>umbralCorriente){
            output = "En reposo";;
        }else {
            output = "Descargando";
        }
        return output;
    }
}