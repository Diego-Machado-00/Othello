package com.example.othello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.logica.Sala;
import com.example.logica.Tablero;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Game extends AppCompatActivity {
    private String salaActual;
    private String fotoJActual;
    private String fotoJContrincante;
    private PintarPuntaje pintarPuntaje;
    private PintarTablero pintarTablero;
    private GameOver gameOver;
    private Tablero tablero;
    private DatabaseReference referenciaBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.salaActual = getIntent().getStringExtra("salaActual");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        Iniciar();
    }

    public void Iniciar() {
        this.gameOver = new GameOver(this);
        this.pintarTablero = new PintarTablero(this);
        this.pintarPuntaje = new PintarPuntaje(this);
        this.tablero = new Tablero(this.salaActual);
        this.tablero.addObserver(this.pintarTablero);
        this.tablero.addObserver(this.pintarPuntaje);
        this.tablero.addObserver(this.gameOver);
        this.tablero.iniciar();
    }
    public void cerrar(){
        referenciaBase = FirebaseDatabase.getInstance().getReference();
        referenciaBase.child("Salas").child(salaActual).removeValue();
        Intent intent = new Intent(this, inicioSecion.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void escribirPosiciones(int i, int j) {
        this.tablero.jugar(i,j);
    }
}