package com.example.renovasolar.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renovasolar.MainActivity;
import com.example.renovasolar.R;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Function;

import android.util.Log;
import android.widget.Button;
import java.util.Arrays;

public class PanelActivity extends AppCompatActivity {
    //VARIABLES DE LA VISTA
    static String[] values;
    static TextView energia;
    static TextView voltaje;
    static TextView clima;

    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        //IMPORTA ID DE LOS TEXT VIEW
        energia = (TextView) findViewById(R.id.energiaPanel);
        voltaje = (TextView) findViewById(R.id.voltajePanel);
        clima = (TextView) findViewById(R.id.clima);

        counter = 1;
        final Handler handler2 = new Handler();
        final int delay = 3000; //3 segundos

        handler2.postDelayed(new Runnable(){
            public void run(){

                handler2.postDelayed(this, delay);
                System.out.println("Loop: Conteo " + counter);
                try {
                    //CONECTA SERVIDOR TCP IP
                    //energia.setText("Waiting python reply");
                    ConfiguracionActivity.ConnectPyTask task = new ConfiguracionActivity.ConnectPyTask();
                    ConfiguracionActivity.ConnectPyTask.context = getApplicationContext();

                    //ENVIA TRAMA CON PAYLOAD TIPO 2(CONSULTA TIEMPO REAL PANEL SOLAR)
                    task.execute("1");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String[] valuesPanel = ConfiguracionActivity.values;
                            String sv = valuesPanel[0];//VOLTAJE EN TEXTO
                            String si = valuesPanel[1];//CORRIENTE EN TEXTO

                            double dv = Double.parseDouble(sv);//VOLTAJE EN DECIMAL
                            double dc = Double.parseDouble(si);//CORRIENTE EN DECIMAL
                            String e = String.valueOf(calEnergia(dv,dc)+" WATT");//CALCULA LA ENERGIA
                            String c = calClima(dv,dc);//CALCULA ESTADO DE RADIACION SOLAR

                            //ENVIA LOS VALORES CALCULADOS A MOSTRAR EN PANTALLA
                            energia.setText(e);
                            voltaje.setText(sv+" V");
                            clima.setText(c);
                            //Toast.makeText(getApplicationContext(), "info", Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);



                }catch (Exception e){
                    Log.d("Exception", e.toString());
                }
                counter++;
                if(counter > 100){
                    System.out.println("Loop: Termina proceso.");
                    handler2.removeCallbacksAndMessages(null);
                }

            }
        }, delay);

    }

    public static double calEnergia (double v, double i) {
            double t = 1; //VARIABLE DE TIEMPO(1 SEGUNDO)
            return v*i; //CALCULA ENERGIA
        }
        public static String calClima (double v, double i) {
            int umbralMinVoltaje = 7; // UMBRAL MINIMO
            int umbralMaxVoltaje = 10;// UMBRAL MAXIMO
            String output = "";//DECLARA VARIABLE DE SALIDA
            if (v>umbralMaxVoltaje){
                output = "Alto";
            }else if (v>umbralMinVoltaje){
                output = "Medio";
            }else {
                output = "Bajo";
            }
            return output;
        }



}

