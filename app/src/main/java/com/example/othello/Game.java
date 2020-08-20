package com.example.othello;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.logica.Sala;
import com.example.logica.Tablero;
import com.google.firebase.database.DatabaseReference;

public class Game extends AppCompatActivity {
    private String salaActual;
    private String fotoJActual;
    private String fotoJContrincante;
    private PintarPuntaje pintarPuntaje;
    private PintarTablero pintarTablero;
    private Tablero tablero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.salaActual = getIntent().getStringExtra("salaActual");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        Iniciar();
    }

    public void Iniciar() {
        this.pintarTablero = new PintarTablero(this);
        this.pintarPuntaje = new PintarPuntaje(this);
        this.tablero = new Tablero(this.salaActual);
        this.tablero.addObserver(this.pintarTablero);
        this.tablero.addObserver(this.pintarPuntaje);
        this.tablero.iniciar();
    }

    public void escribirPosiciones(int i, int j) {
        this.tablero.jugar(i,j);
    }
}