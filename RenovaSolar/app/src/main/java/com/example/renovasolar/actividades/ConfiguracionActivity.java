package com.example.renovasolar.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.renovasolar.MainActivity;
import com.example.renovasolar.R;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConfiguracionActivity extends AppCompatActivity {
    static String[] values;
    private View btnConnect;
    static EditText Ip;
    static EditText Port;
    static String SERVER_IP;
    static Integer PORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        btnConnect = (Button) findViewById(R.id.btnConnectServer);
        Ip = (EditText) findViewById(R.id.IP);
        Port = (EditText) findViewById(R.id.port);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //VARIABLES SERVIDOR
                SERVER_IP = Ip.getText().toString(); // The SERVER_IP must be the same in server and client
                PORT = Integer.parseInt(Port.getText().toString()); // You can put any arbitrary PORT value


                //CONECTA SERVIDOR TCP IP
                ConnectPyTask task = new ConnectPyTask();
                ConnectPyTask.context = getApplicationContext();
                //ENVIA TRAMA CON PAYLOAD TIPO 1(CONSULTA TIEMPO REAL PANEL SOLAR)
                //task.execute("3");
                Toast.makeText(getApplicationContext(), "Coneccion Satisfactoria", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ConfiguracionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
    public static class ConnectPyTask extends AsyncTask<String, Void, String>
    {
        public static Context context = null;
        static float startTime = 0, endTime = 0;
        @Override
        protected String doInBackground(String... data) {
            try {
                startTime = System.currentTimeMillis();
                Socket socket = new Socket(SERVER_IP, PORT); //Server IP and PORT
                Scanner sc = new Scanner(socket.getInputStream());
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                printWriter.write(data[0]); // Send Data
                printWriter.flush();

                String receive = sc.next();//MENSAJE RECIBIDO
                values = agregarLista(receive);//AGREGA A LISTA VALORES
                String sv = values[0];//VOLTAJE EN TEXTO
                String si = values[1];//CORRIENTE EN TEXTO
                double dv = Double.parseDouble(sv);//VOLTAJE EN DECIMAL
                double dc = Double.parseDouble(si);//CORRIENTE EN DECIMAL



            }catch (Exception e){
                Log.d("Exception", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            endTime = System.currentTimeMillis();
            String execTime = String.valueOf((endTime - startTime)/1000.0f);
            //Toast.makeText(context, "Time execution is: " + execTime + "s", Toast.LENGTH_SHORT).show();
        }
        private String[] agregarLista(String input){
            String[] param = input.split(",");//Splitting using whitespace
            return param;
        }
    }
}