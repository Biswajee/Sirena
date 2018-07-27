package io.github.biswajee.sirena;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

/**
 * Created by Biswajit Roy on 27-07-2018.
 */

public class sign_worker extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_screen);
        mAuth = FirebaseAuth.getInstance();
        Button login_button = (Button)findViewById(R.id.btn_login);
        final TextView email = (TextView)findViewById(R.id.input_email);
        final TextView password = (TextView)findViewById(R.id.input_password);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Authenticating you in. Please wait...",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(sign_worker.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN STATUS:", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent homeIntent = new Intent(sign_worker.this, MainActivity.class);
                            startActivity(homeIntent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LOGIN STATUS: ", "signInWithEmail:failure", task.getException());
                            Toast.makeText(sign_worker.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


}
