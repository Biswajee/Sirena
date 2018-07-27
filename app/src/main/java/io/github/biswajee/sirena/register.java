package io.github.biswajee.sirena;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Biswajit Roy on 28-07-2018.
 */

public class register extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Button reg_button = (Button)findViewById(R.id.btn_reg);
        final TextView name = (TextView)findViewById(R.id.input_name);
        final TextView email = (TextView)findViewById(R.id.input_email);
        final TextView password = (TextView)findViewById(R.id.input_password);
        final TextView pass_again = (TextView)findViewById(R.id.input_pass_verify);

        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().equals(pass_again.getText())!= true){
                    Snackbar.make(v, "Passwords do not match !",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(name.getText().toString().isEmpty()){
                    Snackbar.make(v,"Please enter your full name", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(email.getText().toString().isEmpty()){
                    Snackbar.make(v,"Please enter your E-mail ID", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                

            }
        });

    }
}
