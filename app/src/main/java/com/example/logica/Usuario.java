package com.example.logica;

import android.net.Uri;

public class Usuario {
    public String nombre;
    public String foto;
    public String correo;
    public String estado;

    public Usuario() {
    }

    public Usuario( String nombre, String  foto, String correo, String estado) {
        this.nombre = nombre;
        this.foto = foto;
        this.correo = correo;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
