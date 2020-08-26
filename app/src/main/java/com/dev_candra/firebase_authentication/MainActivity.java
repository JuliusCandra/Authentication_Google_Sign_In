package com.dev_candra.firebase_authentication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    // inisisasi
    private SignInButton btn_google;
    private GoogleSignInOptions options;
    private GoogleSignInAccount account;
    private GoogleSignInClient client;
    private FirebaseAuth auth;
    private static final int REQUEST_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // assignment
        btn_google = findViewById(R.id.btn_google);
        auth = FirebaseAuth.getInstance();
        // membuat method aksi
        Aksi();

    }

    private void Aksi(){
        initToolbar();
        requestCredential();
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterToGoogle();
            }
        });

    }

    private void requestCredential(){
        // membuat credential firebase google sign innya
        options =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .build();
        client = GoogleSignIn.getClient(this,options);
    }

    // membuat method untuk menghandle btn google
    private void enterToGoogle(){
        Intent masuk = client.getSignInIntent();
        startActivityForResult(masuk,REQUEST_CODE);
    }

    // mengirim data dari intent

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode){
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                account = accountTask.getResult(ApiException.class);
                // membuat method google sign in dengan credential
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    // membuat sign in dengan auth dan credential google
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // membuat firebase user
                    FirebaseUser user = auth.getCurrentUser();
                    // method yang dibuat untuk masuk kemain utama
                    UpdateUi(user);
                }else{
                    UpdateUi(null);
                    Toast.makeText(MainActivity.this, "terjadi kesalahan system", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // membuat UI untuk masuk ke content utama
    private void UpdateUi(FirebaseUser user) {
        if (user != null){
            String userName = user.getDisplayName();
            String userEmail = user.getEmail();
            String userPhoto = Objects.requireNonNull(user.getPhotoUrl()).toString();
            Intent masukKeKontentUtama = new Intent(MainActivity.this,DetailAcitivity.class);
            masukKeKontentUtama.putExtra("nama",userName);
            masukKeKontentUtama.putExtra("email",userEmail);
            masukKeKontentUtama.putExtra("photo",userPhoto);
            startActivity(masukKeKontentUtama);
            finish();

        }else{
            Toast.makeText(this, "Anda gagal login", Toast.LENGTH_SHORT).show();
        }
    }

    private void initToolbar(){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Candra Julius Sinaga");
            getSupportActionBar().setSubtitle("Firebase Authentication");
        }
    }




}
