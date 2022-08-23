package com.example.renovasolar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.renovasolar.actividades.BateriaActivity;
import com.example.renovasolar.actividades.ConfiguracionActivity;
import com.example.renovasolar.actividades.ConnServer;
import com.example.renovasolar.actividades.PanelActivity;
import com.example.renovasolar.actividades.ReporteActivity;
import com.example.renovasolar.actividades.SplashActivity;
import com.example.renovasolar.actividades.fragment.InicioFragment;
import com.example.renovasolar.interfaces.IComunicaFragments;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements IComunicaFragments,InicioFragment.OnFragmentInteractionListener {
    Fragment fragmentInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentInicio = new InicioFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFrangments,fragmentInicio).commit();

    }

    public void panel() {
        Intent intent = new Intent(this, PanelActivity.class);
        startActivity(intent);
    }
    public void bateria() {
        Intent intent = new Intent(this, BateriaActivity.class);
        startActivity(intent);
    }
    public void reporte() {
        Intent intent = new Intent(this, ReporteActivity.class);
        startActivity(intent);
    }
    public void conf() {
        Intent intent = new Intent(this, ConfiguracionActivity.class);
        startActivity(intent);
    }
    public void info() {
        Toast.makeText(getApplicationContext(), "info", Toast.LENGTH_SHORT).show();
    }
    public void onFragmentInteraction(Uri uri){

    }
}