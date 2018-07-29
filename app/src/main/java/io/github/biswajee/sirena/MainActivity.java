package io.github.biswajee.sirena;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        final EditText post_data = (EditText)findViewById(R.id.input_post);
        MenuItem profile_pic = (MenuItem)findViewById(R.id.menu_profile_pic);

        // ACCESS LOGGED IN USER'S ID (FIREBASE)
        final register reg = new register();

        //Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //BOTTOM RIGHT FLOATING BUTTON ACTION
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello There !", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Float Action Button ");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //CODES FOR SEND BUTTON ACTION BEGINS

        FloatingActionButton sender = (FloatingActionButton)findViewById(R.id.post);
        sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                ImageView msg_sent = (ImageView)findViewById(R.id.sent_gif);
                Glide.with(MainActivity.this).load(R.drawable.sent).into(msg_sent);
                */
                if(post_data.getText().toString().isEmpty()){
                    Snackbar.make(v,"Nothing to post ! Please write something", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                else if(!post_data.getText().toString().isEmpty()){
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference post_id = mRootRef.child("posts").push();
                    post_id.child("post").setValue(post_data.getText().toString());
                    post_id.child("sender").setValue(reg.uid);
                    post_id.child("post_date").setValue(Calendar.getInstance().getTime().toString());
                    Snackbar.make(v,"Your post is published now !", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Snackbar.make(v, "There is some connectivity issue. Verify internet connection.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static final int PICK_IMAGE = 1;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_profile_pic) {
            // Handle the Update profile pic action
            Intent intent = new Intent(MainActivity.this, );
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
            Log.w("STATUS INTENT: ",intent.getData().toString());
            if(imageUri != null){
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                storageRef.putFile(imageUri);
            }
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
