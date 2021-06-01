package com.example.skillyouneed.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.skillyouneed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// Esta actividad se encarga de loguear a los usuarios
public class LoginActivity extends AppCompatActivity {

    //Varibles
    private FirebaseUser currentUser = null;
    private FirebaseAuth mAuth;
    private TextView textViewLoginEmail, textViewLoginPassword;
    private Button buttonLoginLogin, buttonLoginRegister, buttonLoginForguetPassword;
    private ImageButton imageButtonLoginGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLayout();
        initDataBase();
        events();
    }

    private void initLayout() {
        textViewLoginEmail = (TextView) findViewById(R.id.editTextLoginEmail);
        textViewLoginPassword = (TextView) findViewById(R.id.editTextLoginPassword);
        buttonLoginLogin = (Button) findViewById(R.id.buttonLoginLogin);
        buttonLoginRegister = (Button) findViewById(R.id.buttonLoginRegister);
        buttonLoginForguetPassword = (Button) findViewById(R.id.buttonLoginForguetPassword);
    }

    private void initDataBase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void events(){
        buttonLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textLoginEmail = "";
                String textPassword = "";

                textLoginEmail = textViewLoginEmail.getText().toString();
                textPassword = textViewLoginPassword.getText().toString();

                if (!textLoginEmail.isEmpty() && !textPassword.isEmpty()){
                    mAuth.signInWithEmailAndPassword(textLoginEmail,textPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        currentUser = mAuth.getCurrentUser();
                                        updateUI(currentUser);

                                    } else {
                                        updateUI(null);
                                        Snackbar.make(v, R.string.LoginErrorWithEmailOrPassword, Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else {
                    textViewLoginPassword.setError(getText(R.string.ErrorEmptyField));
                    textViewLoginEmail.setError(getText(R.string.ErrorEmptyField));
                }
            }
        });

        buttonLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        buttonLoginForguetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}