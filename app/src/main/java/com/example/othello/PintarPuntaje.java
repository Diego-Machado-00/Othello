package com.example.othello;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class PintarPuntaje implements Observer {
    private Game game;
    private ImageView fotoJActual;
    private ImageView fotoJContrincante;
    private TextView puntajeJActual;
    private TextView puntajeJContrincante;
    private TextView turno;

    public PintarPuntaje(Game game) {
        this.game = game;
        this.fotoJActual = this.game.findViewById(R.id.fotoJActual);
        this.fotoJContrincante = this.game.findViewById(R.id.fotoJContrincante);
        this.puntajeJActual = this.game.findViewById(R.id.puntajeJActual);
        this.puntajeJContrincante = this.game.findViewById(R.id.puntajeJContrincante);
        this.turno = this.game.findViewById(R.id.turno);

        this.puntajeJContrincante.setText("0");
        this.puntajeJActual.setText("0");
    }



    @Override
    public void update(Observable o, Object arg) {
        try{
            ArrayList<String> fotos = (ArrayList<String>) arg;
            Picasso.get().load(fotos.get(0)+"?type=large").into(this.fotoJActual);
            Picasso.get().load(fotos.get(1)+"?type=large").into(this.fotoJContrincante);
            this.turno.setText(fotos.get(2));
            this.turno.setTextColor(Integer.parseInt(fotos.get(3)));
        }catch (Exception e){
            System.out.println("Observador no notificado");
        }
        try{
            ArrayList<Integer> elementos = (ArrayList<Integer>) arg;
            this.puntajeJActual.setText(Integer.toString(elementos.get(0)));
            this.puntajeJContrincante.setText(Integer.toString(elementos.get(1)));
            if(elementos.get(2)!=elementos.get(3)){
                this.turno.setText("Tu Turno");
                this.turno.setTextColor(elementos.get(4));
            }else{
                this.turno.setText("Turno Del Oponente");
                this.turno.setTextColor(elementos.get(4));
            }

        }catch (Exception e){
            System.out.println("Observador no notificado");
        }
    }
}
