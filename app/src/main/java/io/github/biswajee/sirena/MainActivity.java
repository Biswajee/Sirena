package io.github.biswajee.sirena;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAnalytics mFirebaseAnalytics;
    private String MENU_IMAGE = "profile_pic";
    private RecyclerView postRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //INITIALIZATION OF COMPONENTS BEGINS

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.hideOverflowMenu();
        //setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);
        final TextView user_name_view = (TextView)v.findViewById(R.id.user_name_view);
        TextView user_mail_view = (TextView)v.findViewById(R.id.user_mail_view);

        //Get data from shared preferences...
        SharedPreferences loginData = getSharedPreferences("Login", MODE_PRIVATE);
        final String user_mail = loginData.getString("email","aliaa08@twitter.com");
        final String user_name = loginData.getString("user", "Alia Bhatt");

        // Get name from Firebase database after successful login...

        DatabaseReference userInfoRef = FirebaseDatabase.getInstance().getReference("user-info");
        userInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> userInfoName = dataSnapshot.getChildren();
                for(DataSnapshot nameAssigner : userInfoName){
                    if (nameAssigner.child("email").getValue().toString().equalsIgnoreCase(user_mail)){
                        user_name_view.setText(nameAssigner.child("name").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postRecycler = (RecyclerView) findViewById(R.id.postRecycler);


        //Get Avatar image details and call firebase storage in case avatar image file name is found
        //Else do nothing...
        SharedPreferences avatarData = getSharedPreferences("Avatar", MODE_PRIVATE);
        String avatarFileURL = avatarData.getString("firebaseStorageURL","");

        final EditText post_data = (EditText) findViewById(R.id.input_post);
        MenuItem profile_pic = (MenuItem) findViewById(R.id.menu_profile_pic);


        // ACCESS LOGGED IN USER'S ID (FIREBASE)
        final register reg = new register();

        //Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //INITIALIZATION OF COMPONENTS ENDS



        if(!avatarFileURL.isEmpty()){
            ImageView avatar_pic_view = (ImageView) v.findViewById(R.id.profile_pic_view);
            Glide.with(getApplicationContext()).load(avatarFileURL).override(80,80).into(avatar_pic_view);
        }


        //Set user values into Side nav...
        user_name_view.setText(user_name);
        user_mail_view.setText(user_mail);


        post_data.clearFocus();


        //BOTTOM RIGHT FLOATING BUTTON ACTION
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello There !", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Float Action Button Mail ");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Mail Button");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);


        //CODES FOR SEND BUTTON ACTION BEGINS

        FloatingActionButton sender = (FloatingActionButton) findViewById(R.id.post);
        sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                ImageView msg_sent = (ImageView)findViewById(R.id.sent_gif);
                Glide.with(MainActivity.this).load(R.drawable.sent).into(msg_sent);
                */
                if (post_data.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Nothing to post ! Please write something", Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (!post_data.getText().toString().isEmpty()) {
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference post_id = mRootRef.child("posts").push();
                    post_id.child("post").setValue(post_data.getText().toString());

                    System.out.println(reg.uid);
                    post_id.child("post_date").setValue(Calendar.getInstance().getTime().toString());
                    post_data.setText("");
                    post_data.clearFocus();

                    Snackbar.make(v, "Your post is published now !", Snackbar.LENGTH_SHORT).show();
                    return;
                } else {
                    Snackbar.make(v, "There is some connectivity issue. Verify internet connection.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });



        //POPULATING LIST VIEW WITH POSTS...
        final String[] postList = new String[256];

        //Use Firebase to populate data in postList Array...
        DatabaseReference postDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = postDatabase.child("posts");
        // TODO Above line can be merged with single Databasereference...

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                Iterable<DataSnapshot> postText = dataSnapshot.getChildren();
                for (DataSnapshot postSnap : postText) {
                    //Toast.makeText(getApplicationContext(),postSnap.child("sender").getValue().toString(),Toast.LENGTH_LONG).show();
                    postList[i++] = postSnap.child("sender").getValue().toString() + " : " + postSnap.child("post").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Couldn't gather posts !", Toast.LENGTH_SHORT).show();
            }
        });


        postRecycler.setLayoutManager(new LinearLayoutManager(this));
        postRecycler.setAdapter(new postAdapter(postList));

        //END LIST POPULATING...
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
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
        else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.logout){
            AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(this);
            logoutBuilder.setCancelable(true);
            logoutBuilder.setMessage("Logout and exit application ?");
            logoutBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),"You clicked logout", Toast.LENGTH_SHORT).show();
                    getSharedPreferences("Avatar", MODE_PRIVATE).edit().clear().commit();
                    getSharedPreferences("Login", MODE_PRIVATE).edit().clear().commit();
                    FirebaseAuth.getInstance().signOut();
                    finish();
                }
            });
            AlertDialog logoutDialog = logoutBuilder.create();
            logoutDialog.setCanceledOnTouchOutside(true);
            logoutDialog.show();
        }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(data != null) {
            if (requestCode == PICK_IMAGE) {
                Random r = new Random();
                int a = r.nextInt((100000 - 100) + 1) + 10;

                final StorageReference storageRef = FirebaseStorage.getInstance().getReference(Integer.toString(a));



                Uri imageUri = data.getData();   //Intent.EXTRA_STREAM
                UploadTask avatarUploadTask = storageRef.putFile(imageUri);
                Toast.makeText(getApplicationContext(),"Uploading...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = avatarUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Upload unsuccessful", Toast.LENGTH_SHORT).show();
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return storageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            Toast.makeText(getApplicationContext(), "Profile picture successfully uploaded", Toast.LENGTH_SHORT).show();

                            //Download Avatar image from Firebase Storage to ImageView...
                            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            View v = navigationView.getHeaderView(0);
                            ImageView avatar_pic_view = (ImageView) v.findViewById(R.id.profile_pic_view);

                            //Test Code
                            //Toast.makeText(getApplicationContext(), downloadUri.toString(), Toast.LENGTH_SHORT).show();

                            //Add image download url to Shared Pref...
                            SharedPreferences avatarData = getSharedPreferences("Avatar", MODE_PRIVATE);
                            SharedPreferences.Editor avatarInf = avatarData.edit();
                            avatarInf.putString("firebaseStorageURL",downloadUri.toString());
                            avatarInf.commit();


                            Glide.with(getApplicationContext()).load(downloadUri).into(avatar_pic_view);


                        } else {
                            // Handle failures
                            Toast.makeText(getApplicationContext(), "No file was selected", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });


            } else {
                Toast.makeText(getApplicationContext(), "File Upload Failed", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    }

