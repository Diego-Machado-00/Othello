package com.example.othello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logica.Sala;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class inicioSecion extends AppCompatActivity {
    private FirebaseUser usuario;
    private TextView Nombre;
    private ImageView Foto;
    private Button jugar;
    private DatabaseReference referenciaBase;
    private Button cerrarSesion;
    private ProgressBar espera;
    private boolean juego;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inicio_secion);
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        this.Nombre = findViewById(R.id.nombreUser);
        this.Foto = findViewById(R.id.imageView2);
        this.Nombre.setText(this.usuario.getDisplayName());
        Picasso.get().load(this.usuario.getPhotoUrl()+"?type=large").into(Foto);
        this.cerrarSesion = findViewById(R.id.cerrarSesion);
        this.cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
        this.espera = findViewById(R.id.esperar);
       // this.leerUsuariosActivos();
        this.juego = false;
        this.jugar = findViewById(R.id.jugarboton);
        this.jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevojuego();
            }
        });
    }

    public void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void nuevojuego() {
        this.jugar.setVisibility(View.INVISIBLE);
        this.cerrarSesion.setVisibility(View.INVISIBLE);
        this.espera.setVisibility(View.VISIBLE);

        referenciaBase = FirebaseDatabase.getInstance().getReference();
        referenciaBase.child("Usuarios").child(this.usuario.getUid()).child("estado").setValue("2");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                    boolean nuevo=true;
                    for(DataSnapshot snap: dataSnapshot.getChildren()){
                        Sala sala = snap.getValue(Sala.class);
                        if(!snap.getKey().equalsIgnoreCase("defaul")){
                            if(sala.getJugador2().equalsIgnoreCase("")){
                                    EntrarSala(snap.getKey());
                                    nuevo=false;
                            }
                        }

                    }
                    if(nuevo==true){
                        CrearSala();
                    }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Lectura fallida", Toast.LENGTH_SHORT).show();
            }
        };
        referenciaBase.child("Salas").addListenerForSingleValueEvent(postListener);

    }


    public String getCorreo(){
        return this.usuario.getEmail();
    }
    public String getID(){
        return this.usuario.getUid();
    }
    public void CrearSala(){
        referenciaBase.child("Usuarios").child(this.usuario.getUid()).child("estado").setValue("3");
        Sala sala = new Sala(this.usuario.getPhotoUrl().toString(), "",1,0,0);
        referenciaBase.child("Salas").child(this.usuario.getUid()).setValue(sala);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Sala sala = dataSnapshot.getValue(Sala.class);
                if(!sala.getJugador2().equalsIgnoreCase("")){
                    referenciaBase.child("Salas").child(dataSnapshot.getKey()).removeEventListener(this);
                    Crearjuego(dataSnapshot.getKey());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Lectura fallida", Toast.LENGTH_SHORT).show();
            }
        };
        referenciaBase.child("Salas").child(this.usuario.getUid()).addValueEventListener(postListener);
    }
    public void Crearjuego(String sala){
        Intent intent = new Intent(this, Game.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("salaActual", sala);
        startActivity(intent);
    }
    public void EntrarSala(String llave){
        referenciaBase.child("Salas").child(llave).child("jugador2").setValue(this.usuario.getPhotoUrl().toString());
        Crearjuego(llave);
    }

}