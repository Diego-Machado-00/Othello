package com.example.logica;

import android.graphics.Color;
import android.graphics.Point;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.othello.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Observable;

public class Tablero extends Observable {
    public final static int COLUMNAS=8;
    public final static int FILAS=8;
    private String llaveSala;
    private FirebaseUser usuario;
    private DatabaseReference referenciaBase;
    private int tablero[][];
    private int turnoActual;
    private int miTurno;
    private  int inicio;
    private Sala sala;
    public Tablero(String llaveSala) {
        this.llaveSala = llaveSala;
        this.tablero = new int[FILAS][COLUMNAS];
        this.inicio = 0;
    }

    public void jugar(int i, int j){
        this.sala.setPoscicionX(i);
        this.sala.setPosciciony(j);
        if(turnoActual==1){
           this.sala.setTurno(2);

        }else{
            this.sala.setTurno(1);
        }
        referenciaBase.child("Salas").child(llaveSala).setValue(this.sala);
    }
    public void menu(Sala sala){
        this.sala = sala;
        if(inicio==1){
           pintarJugada(sala.getPoscicionX(),sala.getPosciciony(),sala.getTurno());
        }else{
            ObtenerFotos(sala.getJugador1(), sala.getJugador2(), sala.getTurno());
            this.inicio=1;
        }
    }
    public void iniciar(){
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        referenciaBase = FirebaseDatabase.getInstance().getReference();
        this.tablero[3][3]=1;
        this.tablero[3][4]=2;
        this.tablero[4][3]=2;
        this.tablero[4][4]=1;
        ValueEventListener listenerjuego = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Sala sala = dataSnapshot.getValue(Sala.class);
                menu(sala);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Lectura Fallida");
            }
        };
        referenciaBase.child("Salas").child(llaveSala).addValueEventListener(listenerjuego);

    }

    public void pintarJugada(int i, int j, int turno){
            eliminarPosibles();
            ArrayList<int []> elementos = new ArrayList<>();
            int poscisiones[]= new int[3];
            poscisiones[0]=i;
            poscisiones[1]=j;
            if(turnoActual==1){
                poscisiones[2]=R.drawable.ficha_blanca;
            }else{
                poscisiones[2]=R.drawable.ficha_negra;
            }
            elementos.add(poscisiones);
            this.setChanged();
            this.notifyObservers(elementos);
            pintarFichas(i,j);
            this.turnoActual=turno;
            if(miTurno==turnoActual){
                validarPosibles();
            }

    }

    private void eliminarPosibles() {
        ArrayList<int []> elementos = new ArrayList<>();
        for(int i=0; i<FILAS; i++){
            for(int j=0; j<COLUMNAS; j++){
                if(tablero[i][j]==3){
                    tablero[i][j]=0;
                    int poscisiones[]= new int[3];
                    poscisiones[0]=i;
                    poscisiones[1]=j;
                    poscisiones[2]=R.drawable.cuadro_vacio;
                    elementos.add(poscisiones);
                }
            }
        }
        this.setChanged();
        this.notifyObservers(elementos);
    }

    public void hacerMovimiento(int i, int j, int movimientoI, int movimientoJ){
        ArrayList<int []> elementos = new ArrayList<>();
        while (tablero[i][j] != turnoActual && tablero[i][j]!=0) {
            tablero[i][j] = turnoActual;
            int poscisiones[]= new int[3];
            poscisiones[0]=i;
            poscisiones[1]=j;
            if(turnoActual==1){
                poscisiones[2]=R.drawable.ficha_blanca;
            }else{
                poscisiones[2]=R.drawable.ficha_negra;
            }
            elementos.add(poscisiones);
            i += movimientoI;
            j += movimientoJ;
        }
        this.setChanged();
        this.notifyObservers(elementos);
    }
    private void pintarFichas(int i, int j) {
        //colocar la ficha en el tablero
        tablero[i][j] = turnoActual;
        //validarIzquierda
        if(validarPosicionPosible(i,j-1,0,-1)){
            hacerMovimiento(i,j-1,0,-1);
        }
        //validarDerecha
        if(validarPosicionPosible(i,j+1,0,1)){
            hacerMovimiento(i,j+1,0,1);;
        }
        //validarAbajo
        if(validarPosicionPosible(i+1,j,1,0)){
            hacerMovimiento(i+1,j,1,0);
        }
        //validarArriba
        if(validarPosicionPosible(i-1,j,-1,0)){
             hacerMovimiento(i-1,j,-1,0);
        }
        //validarAbajo-Derecha
        if(validarPosicionPosible(i+1,j+1,1,1)){
            hacerMovimiento(i+1,j+1,1,1);
        }
        //validarAbajo-Izquierda
        if(validarPosicionPosible(i+1,j-1,1,-1)){
            hacerMovimiento(i+1,j-1,1,-1);
        }
        //validarArriba-Derecha
        if(validarPosicionPosible(i-1,j+1,-1,1)){
            hacerMovimiento(i-1,j+1,-1,1);
        }
        //validarArriba-Izquierda
        if(validarPosicionPosible(i-1,j-1,-1,-1)){
            hacerMovimiento(i-1,j-1,-1,-1);
        }

    }

    private void validarPosibles() {
        ArrayList<int []> movimientosPosibles = new ArrayList<>();
        for(int i=0; i<FILAS; i++){
            for(int j=0; j<COLUMNAS; j++){
                if(validarFicha(i,j)){
                    tablero[i][j]=3;
                    int poscision []= new int[3];
                    poscision[0]=i;
                    poscision[1]=j;
                    poscision[2]=R.drawable.cuadro_disponible;
                    movimientosPosibles.add(poscision);
                }
            }
        }
        this.setChanged();
        this.notifyObservers(movimientosPosibles);
    }

    private boolean validarFicha(int i, int j) {
        if(tablero[i][j]!=0){
            return false;
        }
        //validarIzquierda
        if(validarPosicionPosible(i,j-1,0,-1)){
            return true;
        }
        //validarDerecha
        if(validarPosicionPosible(i,j+1,0,1)){
            return true;
        }
        //validarAbajo
        if(validarPosicionPosible(i+1,j,1,0)){
            return true;
        }
        //validarArriba
        if(validarPosicionPosible(i-1,j,-1,0)){
            return true;
        }
        //validarAbajo-Derecha
        if(validarPosicionPosible(i+1,j+1,1,1)){
            return true;
        }
        //validarAbajo-Izquierda
        if(validarPosicionPosible(i+1,j-1,1,-1)){
            return true;
        }
        //validarArriba-Derecha
        if(validarPosicionPosible(i-1,j+1,-1,1)){
            return true;
        }
        //validarArriba-Izquierda
        if(validarPosicionPosible(i-1,j-1,-1,-1)){
            return true;
        }
        return false;
    }

    private boolean validarPosicionPosible(int i, int j, int poscisionNuevai , int poscisionNuevaj) {
        if((i >= 0) && (i< FILAS) && (j>= 0) && (j< COLUMNAS)){
            if(tablero[i][j]!=miTurno && tablero[i][j]!=0 && tablero[i][j]!=3){
                while ((i >= 0) && (i< FILAS) && (j>= 0) && (j< COLUMNAS)) {
                    i += poscisionNuevai;
                    j += poscisionNuevaj;
                    if((i >= 0) && (i< FILAS) && (j>= 0) && (j< COLUMNAS)){
                        if (tablero[i][j] == 0) {
                            return false; //no es consecutiva
                        }
                        if (tablero[i][j] == miTurno){
                            return true;// posible ficha que se puede voltear
                        }
                    }
                }
            }
        }
        return false; //si golpea el borde
    }

    public void ObtenerFotos(String foto1, String foto2, int turno){
        this.turnoActual = turno;
        ArrayList<String> fotos = new ArrayList<>();
        if(usuario.getPhotoUrl().toString().equals(foto1)){
            fotos.add(foto1);
            fotos.add(foto2);
            fotos.add("Tu Turno");
            fotos.add(Integer.toString(Color.WHITE));
            this.miTurno = 1;
        }else{
            fotos.add(foto2);
            fotos.add(foto1);
            fotos.add("Turno Del Oponente");
            fotos.add(Integer.toString(Color.BLACK));
            this.miTurno = 2;
        }
        this.setChanged();
        this.notifyObservers(fotos);
        if(miTurno==turnoActual) {
            validarPosibles();
        }
    }
}
