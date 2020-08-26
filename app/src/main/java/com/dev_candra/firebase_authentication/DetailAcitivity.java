package com.dev_candra.firebase_authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class DetailAcitivity extends AppCompatActivity {

    // inisiasi
    private GoogleSignInOptions options;
    private GoogleSignInClient client;
    private Button btn_logut;
    private TextView nama,email;
    private ImageView gambar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detail);

        // assignment
        nama = findViewById(R.id.teksNama);
        email = findViewById(R.id.teksEmail);
        gambar = findViewById(R.id.gambar);
        btn_logut = findViewById(R.id.btn_logout);
        auth = FirebaseAuth.getInstance();
        // membuat authentication logout
        auth.signOut();
        // membuat method aksi
        Aksi();
    }

    private void Aksi(){
        AmbilData();
        requestCredential();

        btn_logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        initToolbar();
    }

    // mengambil api credential
    private void requestCredential() {
        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .build();

        client = GoogleSignIn.getClient(this,options);
    }

    // membuat method untuk mengambil data
    private void AmbilData(){
        Intent ambil = getIntent();
        String namaAnda = ambil.getStringExtra("nama");
        String emailAnda = ambil.getStringExtra("email");
        String photoAnda = ambil.getStringExtra("photo");

        nama.setText(namaAnda);
        email.setText(emailAnda);

        // untuk mengambil fotonya saya pakai library glide
        Glide.with(this)
                .load(photoAnda)
                .into(gambar);

    }

    // membuat method logout
    private void logout(){
        client.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(DetailAcitivity.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(DetailAcitivity.this, "Terjadi kesalahan pada system", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnCanceledListener(this, new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(DetailAcitivity.this, "Gagal keluar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // membuat toolbar
    private void initToolbar(){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Candra Julius Sinaga");
            getSupportActionBar().setSubtitle("Detail Authentication");
        }
    }
}
