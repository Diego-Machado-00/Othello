package com.example.othello;

import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.example.logica.Tablero;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class PintarTablero implements Observer {
    private Game game;
    private GridLayout tablero;
    private LinearLayout layoutTablero;
    private ImageView caudros[][];

    public PintarTablero(Game game) {
        this.game = game;
        iniciarTablero();
    }

    public void iniciarTablero() {
        this.layoutTablero= this.game.findViewById(R.id.linearLayoutTablero);
        this.tablero = new GridLayout(this.game);
        this.tablero.setColumnCount(Tablero.COLUMNAS);
        this.tablero.setRowCount(Tablero.FILAS);
        GridLayout.LayoutParams parame = new GridLayout.LayoutParams();
        parame.setMargins(0,100,0,0);
        this.tablero.setLayoutParams(parame);
        this.caudros = new ImageView [Tablero.FILAS][Tablero.COLUMNAS];
        Display display = this.game.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        for(int i=0; i<Tablero.FILAS; i++){
            for(int j=0; j<Tablero.COLUMNAS; j++){
                this.caudros[i][j] = new ImageView(this.game);
                this.caudros[i][j].setLayoutParams(new LinearLayout.LayoutParams((int)(width/8),(int)(width/8)));
                this.caudros[i][j].setImageResource(R.drawable.cuadro_vacio);
                int pos[]= new int[2];
                pos[0]=i;
                pos[1]=j;
                this.caudros[i][j].setTag(pos);
                this.tablero.addView(this.caudros[i][j]);
            }
        }

        this.caudros[3][3].setImageResource(R.drawable.ficha_blanca);
        this.caudros[3][4].setImageResource(R.drawable.ficha_negra);
        this.caudros[4][3].setImageResource(R.drawable.ficha_negra);
        this.caudros[4][4].setImageResource(R.drawable.ficha_blanca);
        this.layoutTablero.addView(this.tablero);
    }
    public void escribirPosiciones(int i, int j){
        this.game.escribirPosiciones(i,j);
    }
    @Override
    public void update(Observable o, Object arg) {
        try{
            ArrayList<int[]> posiciones = (ArrayList<int[]>) arg;
            for(int i=0; i<posiciones.size(); i++){
                this.caudros [posiciones.get(i)[0]][posiciones.get(i)[1]].setImageResource(posiciones.get(i)[2]);
                if(posiciones.get(i)[2]==R.drawable.cuadro_disponible){
                    this.caudros [posiciones.get(i)[0]][posiciones.get(i)[1]].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos[] = (int[]) v.getTag();
                            escribirPosiciones(pos[0],pos[1]);

                        }
                    });
                }
                if(posiciones.get(i)[2]==R.drawable.cuadro_vacio){
                    this.caudros [posiciones.get(i)[0]][posiciones.get(i)[1]].setOnClickListener(null);
                }

            }

        }catch (Exception e){
            System.out.println("Observador 1 no notificado");
        }
    }
}
