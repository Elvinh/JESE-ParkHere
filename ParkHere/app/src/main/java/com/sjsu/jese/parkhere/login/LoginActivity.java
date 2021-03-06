package com.sjsu.jese.parkhere.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sjsu.jese.parkhere.MainActivity;
import com.sjsu.jese.parkhere.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;
    private ProgressBar loginProgressBar;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPassword;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = (EditText) findViewById(R.id.emailText);
        passwordField = (EditText) findViewById(R.id.passwordText);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        findViewById(R.id.forgotPassTextView).setOnClickListener(this);
        findViewById(R.id.signInBtn).setOnClickListener(this);
        //findViewById(R.id.logoutBtn).setOnClickListener(this);
        findViewById(R.id.createAccntTextField).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount() {

        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }
    private void signIn(String email, String password) {
        if(!validateForm(email, password)) {
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                           // If successful sign in, update UI with signed in user's info
                            Toast.makeText(LoginActivity.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // if not successful, display message
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*private void signOut() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            mAuth.signOut();
        } else {
            Toast.makeText(LoginActivity.this, "Not Logged in",
                    Toast.LENGTH_SHORT).show();
        }
        user = mAuth.getCurrentUser();
        updateUI(user);
    }*/
    public  boolean validateForm(String email, String password) {
        boolean valid = true;

        if(TextUtils.isEmpty(email)) {
            //emailField.setError("Required.");
            inputLayoutEmail.setError("Required.");
            valid = false;
        } else {
            //emailField.setError(null);
            inputLayoutEmail.setError(null);

        }

        if(TextUtils.isEmpty(password)) {
            //emailField.setError("Required.");
            inputLayoutPassword.setError("Required.");
            valid = false;
        } else {
            //emailField.setError(null);
            inputLayoutPassword.setError(null);

        }

        return valid;
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            /*findViewById(R.id.emailText).setVisibility(View.GONE);
            findViewById(R.id.passwordText).setVisibility(View.GONE);
            findViewById(R.id.signInBtn).setVisibility(View.GONE);
            findViewById(R.id.createAccountBtn).setVisibility(View.GONE);*/
            toMain();
        } else {
            findViewById(R.id.emailText).setVisibility(View.VISIBLE);
            findViewById(R.id.passwordText).setVisibility(View.VISIBLE);
            findViewById(R.id.signInBtn).setVisibility(View.VISIBLE);
        }
    }
    private void toMain() {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.createAccntTextField) {
            createAccount();
        } else if(id == R.id.signInBtn) {
            signIn(emailField.getText().toString(), passwordField.getText().toString());
        /*} else if(id == R.id.logoutBtn) {
            signOut();*/
        } else if(id == R.id.forgotPassTextView) {
            Toast.makeText(LoginActivity.this, "Forgot Password",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
