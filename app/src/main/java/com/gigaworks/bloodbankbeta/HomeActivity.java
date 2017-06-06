package com.gigaworks.bloodbankbeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gigaworks.bloodbankbeta.Fragments.About;
import com.gigaworks.bloodbankbeta.Fragments.AcceptedRequests;
import com.gigaworks.bloodbankbeta.Fragments.Contact;
import com.gigaworks.bloodbankbeta.Fragments.History;
import com.gigaworks.bloodbankbeta.Fragments.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Home.OnFragmentInteractionListener,
        About.OnFragmentInteractionListener,Contact.OnFragmentInteractionListener,History.OnFragmentInteractionListener,
        AcceptedRequests.OnFragmentInteractionListener{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String emailAddress;
    private static final String ResponseURL="https://gigaworks.000webhostapp.com/getUser.php";
    private static final String LOGIN_STATUS_URL="https://gigaworks.000webhostapp.com/change_status.php";
    private static final String GET_IMAGE_URL="https://gigaworks.000webhostapp.com/downloadImage.php";
    private boolean doubleBackToExitPressedOnce=false;
    private CoordinatorLayout coordinatorLayout;
    private NavigationView navigationView;
    private View header;
    private TextView hName,hEmail;
    private ImageView profilePic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Fragment f=new Contact();
        View v=f.getView();
        navigationView=(NavigationView)findViewById(R.id.nav_view);
        header=navigationView.getHeaderView(0);
        hName=(TextView) header.findViewById(R.id.header_name);
        hEmail=(TextView)header.findViewById(R.id.header_email);
        profilePic=(ImageView)header.findViewById(R.id.iv_nav_profile);
        coordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinatorLayout);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent i=new Intent(HomeActivity.this,NewRequest.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user==null){
                    emailAddress="";
                    Intent i = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    emailAddress=user.getEmail();
                    hEmail.setText(emailAddress);
                    GetUserData data=new GetUserData(emailAddress,ResponseURL,GET_IMAGE_URL,HomeActivity.this);
                            data.GetJsonResponse(
                            new MapCallback() {
                                @Override
                                public void onSuccess(ArrayList<String> info) {
                                    SharedPreferences sharedPreferences = getApplicationContext()
                                            .getSharedPreferences("bloodbank.pref",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("name",info.get(0));
                                    editor.putString("phone",info.get(1));
                                    editor.putString("place",info.get(2));
                                    editor.putString("email",emailAddress);
                                    editor.putString("bloodgroup",info.get(3));
                                    editor.commit();
                                    hName.setText(info.get(0));

                                    //Fragment transaction for Home Fragment
                                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    Fragment fragment=new Home();
                                    fragmentTransaction.replace(R.id.fl_container,fragment);
                                    fragmentTransaction.commit();
                                }
                                @Override
                                public void onFail(String message) {

                                }
                            }
                    );

                    data.getImageUrl(new MapCallback() {
                        @Override
                        public void onSuccess(ArrayList<String> info) {
                            SharedPreferences sharedPreferences = getApplicationContext()
                                    .getSharedPreferences("bloodbank.pref",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("url",info.get(0));
                            editor.commit();

                            new DownloadImageTask(profilePic).execute(info.get(0));
                        }

                        @Override
                        public void onFail(String message) {

                        }
                    });
                }
            }
        };

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            finish();
            finishAffinity();
                return;
            }

            doubleBackToExitPressedOnce = true;

        Snackbar.make(coordinatorLayout, "Click Back again to Exit", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            FirebaseAuth.getInstance().signOut();
            changeUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (id) {
                case R.id.nav_home:
                    Fragment fragment=new Home();
                    fragmentTransaction.replace(R.id.fl_container,fragment);
                    break;
                case R.id.nav_profile:
                    Intent i=new Intent(HomeActivity.this,ProfileActivity.class);
                    startActivity(i);
                    break;
                case R.id.nav_history:
                    fragment = new History();
                    fragmentTransaction.replace(R.id.fl_container, fragment);
                    break;
                case R.id.nav_about:
                    fragment = new About();
                    fragmentTransaction.replace(R.id.fl_container, fragment);
                    break;
                case R.id.nav_share:
                    Toast.makeText(HomeActivity.this, "Share", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT,"I recommend you to use Blood Bank App which can save " +
                            "people's life. Donate Blood with Blood Bank");
                    intent.setType("text/plain");
                    startActivity(Intent.createChooser(intent,"Blood Bank"));
                    break;
                case R.id.nav_contact:
                    fragment = new Contact();
                    fragmentTransaction.replace(R.id.fl_container, fragment);
                    break;
                case R.id.nav_accepted_record:
                    fragment= new AcceptedRequests();
                    fragmentTransaction.replace(R.id.fl_container,fragment);
                    break;
                default:
                    break;
            }
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);

    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    void changeUserStatus() {
       SharedPreferences sharedPreferences=getApplicationContext()
               .getSharedPreferences("bloodbank.pref",MODE_PRIVATE);

            final String emailAddress = sharedPreferences.getString("email","abc@xyz.com");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_STATUS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", emailAddress);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

    }
}
