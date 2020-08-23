package com.example.logica;

public class Sala {
    public String jugador1;
    public String jugador2;
    public int turno;
    public int poscicionX;
    public int posciciony;
    public int gameOver;
    public Sala() {
    }

    public Sala(String jugador1, String jugador2, int turno, int poscicionX, int posciciony, int gameOver) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.turno = turno;
        this.poscicionX = poscicionX;
        this.posciciony = posciciony;
        this.gameOver = gameOver;
    }

    public String getJugador1() {
        return jugador1;
    }

    public void setJugador1(String jugador1) {
        this.jugador1 = jugador1;
    }

    public String getJugador2() {
        return jugador2;
    }

    public void setJugador2(String jugador2) {
        this.jugador2 = jugador2;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public int getPoscicionX() {
        return poscicionX;
    }

    public void setPoscicionX(int poscicionX) {
        this.poscicionX = poscicionX;
    }

    public int getPosciciony() {
        return posciciony;
    }

    public void setPosciciony(int posciciony) {
        this.posciciony = posciciony;
    }


}
