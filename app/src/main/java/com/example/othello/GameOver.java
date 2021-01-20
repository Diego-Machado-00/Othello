package com.example.othello;

import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import java.util.Observable;
import java.util.Observer;

public class GameOver implements Observer {
    private Game game;

    public GameOver(Game game) {
        this.game = game;
    }

    public void pintar(int gana){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this.game);
        dialogo.setCancelable(false);
        LinearLayout layout = new LinearLayout(this.game);
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        Display display = this.game.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        //layout.setLayoutParams(new LinearLayout.LayoutParams( width-30, height));
        ImageView imagen = new ImageView(this.game);
        if(gana==1){
            imagen.setImageResource(R.drawable.win);
        }else{
            if(gana==2){
                imagen.setImageResource(R.drawable.lose);
            }else{
                imagen.setImageResource(R.drawable.draw);
            }
        }
        LinearLayout.LayoutParams margen = new LinearLayout.LayoutParams(width-200 , 200 );
        margen.setMargins(0, 0, 0, 30);
        imagen.setLayoutParams(margen);
        layout.addView(imagen);
        layout.setGravity(Gravity.CENTER);
        Button cerrar = new Button(this.game);
        layout.addView(cerrar);
        dialogo.setView(layout);
        final AlertDialog titulo = dialogo.create();
        cerrar.setText("Aceptar");
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrar();
            }
        });
        LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(width-200 , 75);
        parametros.setMargins(0,0,0,30);
        cerrar.setLayoutParams(parametros);
        // titulo.setTitle("Pausa");

        titulo.show();
        titulo.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //titulo.getWindow().setNavigationBarColor(Color.RED);
        titulo.getWindow().setLayout(width,height);
    }
    public void cerrar(){
        this.game.cerrar();
    }
    @Override
    public void update(Observable o, Object arg) {
        try{
            int gana = (int)arg;
            System.out.println("Entro la reconcha");
            pintar(gana);
        }catch (Exception e){
            System.out.println("Observador no notificado");
        }
    }
}
