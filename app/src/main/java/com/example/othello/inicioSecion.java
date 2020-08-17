package com.example.othello;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class inicioSecion extends AppCompatActivity {
    private FirebaseUser usuario;
    private TextView Nombre;
    private ImageView Foto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_secion);
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        this.Nombre = findViewById(R.id.nombreUser);
        this.Foto = findViewById(R.id.imageView2);
        this.Nombre.setText(this.usuario.getDisplayName());
        System.out.println(this.usuario.getPhotoUrl());
        Picasso.get().load(this.usuario.getPhotoUrl()+"?type=large").into(Foto);
    }
}