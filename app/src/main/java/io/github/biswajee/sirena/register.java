package io.github.biswajee.sirena;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Biswajit Roy on 28-07-2018.
 */

public class register extends AppCompatActivity {
    FirebaseAuth mAuth;
    String uid = "unknown-user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Button reg_button = (Button)findViewById(R.id.btn_reg);
        final TextView name = (TextView)findViewById(R.id.input_name);
        final TextView email = (TextView)findViewById(R.id.input_email);
        final TextView password = (TextView)findViewById(R.id.input_password);
        final TextView pass_again = (TextView)findViewById(R.id.input_pass_verify);
        TextView signin = (TextView)findViewById(R.id.link_signin);

        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String sender_email = email.getText().toString();
                if(password.getText().toString().equals(pass_again.getText().toString())!= true){
                    Snackbar.make(v, "Passwords do not match !",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                else if(name.getText().toString().isEmpty()){
                    Snackbar.make(v,"Please enter your full name", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                else if(email.getText().toString().isEmpty()){
                    Snackbar.make(v,"Please enter your E-mail ID", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Snackbar.make(v,"Please wait. Authenticating...", Snackbar.LENGTH_SHORT).show();
                }

                mAuth = FirebaseAuth.getInstance();

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Date currentTime = Calendar.getInstance().getTime();
                            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference mChildRef = mRootRef.child("user-info").push();
                            mChildRef.child("email").setValue(email.getText().toString());
                            mChildRef.child("name").setValue(name.getText().toString());
                            mChildRef.child("enroll_dt").setValue(currentTime.toString());
                            Toast.makeText(register.this,"Successfully Registered",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent homeIntent = new Intent(register.this, MainActivity.class);
                            startActivity(homeIntent);
                            finish();
                        }
                        else{
                            Snackbar.make(v, "Authentication failed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(register.this, sign_worker.class);
                startActivity(signInIntent);
            }
        });


    }
}
