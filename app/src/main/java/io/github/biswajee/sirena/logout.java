package io.github.biswajee.sirena;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Biswajit Roy on 02-08-2018.
 */

public class logout extends AppCompatActivity {

    protected Dialog onCreateDialog(int id) {
        //return super.onCreateDialog(id);

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage(R.string.confirmLogout)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        getSharedPreferences("Avatar", MODE_PRIVATE).edit().clear().commit();
                        getSharedPreferences("Login", MODE_PRIVATE).edit().clear().commit();
                        finish();


                    }
                })
                .setNegativeButton(R.string.notConfirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
