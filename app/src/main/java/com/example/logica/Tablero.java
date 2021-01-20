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
    private int puntajeA;
    private int miPuntaje;
    private ValueEventListener listenerjuego;
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
            if(sala.getGameOver()==0){
                if(sala.getPoscicionX()>=0 && sala.getPosciciony()>=0){
                    pintarJugada(sala.getPoscicionX(),sala.getPosciciony(),sala.getTurno());
                }
                this.turnoActual=sala.getTurno();
                if(miTurno==turnoActual){
                    validarPosibles();
                }
            }else{
                validarGanar();
            }

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
         listenerjuego = new ValueEventListener() {
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
    public void eliminarListener(){
        referenciaBase.child("Salas").child(llaveSala).removeEventListener(listenerjuego);
    }
    public void validarGameOVer(int turnosala){
        int perder1 =0;
        int perder2 =0;
        for(int i=0; i<FILAS; i++){
            for(int j=0; j<COLUMNAS; j++){
                if(validarFicha(i,j,1)){
                    perder1++;
                }
                if(validarFicha(i,j,2)){
                    perder2++;
                }
            }
        }
        if(perder1 ==0 && perder2!=0){
            if(turnosala==1){
                turnoActual=1;
                jugar(-1,-1);
            }
        }else{
            if(perder1 !=0 && perder2==0){
                if(turnosala==2){
                    turnoActual=2;
                    jugar(-1,-1);
                }
            }else{
                if(perder1 ==0 && perder2==0){
                    referenciaBase.child("Salas").child(llaveSala).child("gameOver").setValue(1);
                }
            }
        }
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
            pintarFichas(i,j, turnoActual);
            validarGameOVer(turno);
            calcularPuntaje();
    }
    private void validarGanar(){
        this.puntajeA =0;
        this.miPuntaje =0;
        int gana = 0;
        for(int i=0; i<FILAS; i++){
            for (int j=0; j<COLUMNAS; j++){
                if(this.tablero[i][j]==miTurno){
                    this.miPuntaje ++;
                }else{
                    if(this.tablero[i][j]!=0 && this.tablero[i][j]!=3){
                        this.puntajeA++;
                    }
                }
            }
        }
        if(puntajeA==miPuntaje){
            gana=3;
        }else{
            if(puntajeA<miPuntaje){
                gana=1;
            }else{
                gana=2;
            }
        }
        this.setChanged();
        this.notifyObservers(gana);
    }
    private void calcularPuntaje() {
        ArrayList<Integer> elementos = new ArrayList<>();
        this.puntajeA =0;
        this.miPuntaje =0;
        for(int i=0; i<FILAS; i++){
            for (int j=0; j<COLUMNAS; j++){
                if(this.tablero[i][j]==miTurno){
                    this.miPuntaje ++;
                }else{
                    if(this.tablero[i][j]!=0 && this.tablero[i][j]!=3){
                        this.puntajeA++;
                    }
                }
            }
        }
        elementos.add(this.miPuntaje);
        elementos.add(this.puntajeA);
        elementos.add(turnoActual);
        elementos.add(miTurno);
        if(turnoActual==1){
            elementos.add(Color.BLACK);
        }else{
            elementos.add(Color.WHITE);
        }
        this.setChanged();
        this.notifyObservers(elementos);
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
        while (tablero[i][j] != turnoActual && tablero[i][j]!=0 && tablero[i][j]!=3) {
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
    private void pintarFichas(int i, int j, int ActualTurno) {
        //colocar la ficha en el tablero
        tablero[i][j] = turnoActual;
        //validarIzquierda
        if(validarPosicionPosible(i,j-1,0,-1, ActualTurno)){
            hacerMovimiento(i,j-1,0,-1);
        }
        //validarDerecha
        if(validarPosicionPosible(i,j+1,0,1, ActualTurno)){
            hacerMovimiento(i,j+1,0,1);;
        }
        //validarAbajo
        if(validarPosicionPosible(i+1,j,1,0, ActualTurno)){
            hacerMovimiento(i+1,j,1,0);
        }
        //validarArriba
        if(validarPosicionPosible(i-1,j,-1,0, ActualTurno)){
             hacerMovimiento(i-1,j,-1,0);
        }
        //validarAbajo-Derecha
        if(validarPosicionPosible(i+1,j+1,1,1, ActualTurno)){
            hacerMovimiento(i+1,j+1,1,1);
        }
        //validarAbajo-Izquierda
        if(validarPosicionPosible(i+1,j-1,1,-1, ActualTurno)){
            hacerMovimiento(i+1,j-1,1,-1);
        }
        //validarArriba-Derecha
        if(validarPosicionPosible(i-1,j+1,-1,1, ActualTurno)){
            hacerMovimiento(i-1,j+1,-1,1);
        }
        //validarArriba-Izquierda
        if(validarPosicionPosible(i-1,j-1,-1,-1, ActualTurno)){
            hacerMovimiento(i-1,j-1,-1,-1);
        }

    }

    private void validarPosibles() {
        ArrayList<int []> movimientosPosibles = new ArrayList<>();
        for(int i=0; i<FILAS; i++){
            for(int j=0; j<COLUMNAS; j++){
                if(validarFicha(i,j, turnoActual)){
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

    private boolean validarFicha(int i, int j, int ActualTurno ) {
        if(tablero[i][j]!=0 && tablero[i][j]!=3){
            return false;
        }
        //validarIzquierda
        if(validarPosicionPosible(i,j-1,0,-1,ActualTurno)){
            return true;
        }
        //validarDerecha
        if(validarPosicionPosible(i,j+1,0,1,ActualTurno)){
            return true;
        }
        //validarAbajo
        if(validarPosicionPosible(i+1,j,1,0,ActualTurno)){
            return true;
        }
        //validarArriba
        if(validarPosicionPosible(i-1,j,-1,0,ActualTurno)){
            return true;
        }
        //validarAbajo-Derecha
        if(validarPosicionPosible(i+1,j+1,1,1,ActualTurno)){
            return true;
        }
        //validarAbajo-Izquierda
        if(validarPosicionPosible(i+1,j-1,1,-1,ActualTurno)){
            return true;
        }
        //validarArriba-Derecha
        if(validarPosicionPosible(i-1,j+1,-1,1,ActualTurno)){
            return true;
        }
        //validarArriba-Izquierda
        if(validarPosicionPosible(i-1,j-1,-1,-1,ActualTurno)){
            return true;
        }
        return false;
    }

    private boolean validarPosicionPosible(int i, int j, int poscisionNuevai , int poscisionNuevaj, int ActualTurno) {
        if((i >= 0) && (i< FILAS) && (j>= 0) && (j< COLUMNAS)){
            if(tablero[i][j]!=ActualTurno && tablero[i][j]!=0 && tablero[i][j]!=3){
                while ((i >= 0) && (i< FILAS) && (j>= 0) && (j< COLUMNAS)) {
                    i += poscisionNuevai;
                    j += poscisionNuevaj;
                    if((i >= 0) && (i< FILAS) && (j>= 0) && (j< COLUMNAS)){
                        if (tablero[i][j] == 0 || tablero[i][j] == 3) {
                            return false; //no es consecutiva
                        }
                        if (tablero[i][j] == ActualTurno){
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
        if(usuario.getPhotoUrl().toString().equals(foto2)){
            fotos.add(foto2);
            fotos.add(foto1);
            fotos.add("Tu Turno");
            fotos.add(Integer.toString(Color.BLACK));
            this.miTurno = 2;

        }else{
            fotos.add(foto1);
            fotos.add(foto2);
            fotos.add("Turno Del Oponente");
            fotos.add(Integer.toString(Color.BLACK));
            this.miTurno = 1;
        }
        this.setChanged();
        this.notifyObservers(fotos);
        if(miTurno==turnoActual) {
            validarPosibles();
        }
    }
}
