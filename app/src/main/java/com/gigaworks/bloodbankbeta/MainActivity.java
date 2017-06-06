package com.gigaworks.bloodbankbeta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final static String TAG="Archit";
    private final static String REQUEST_URL="https://gigaworks.000webhostapp.com/update_token.php";
    private EditText etUser;
    private EditText etPass;
    private Button signin;
    private Button signup;
    private ProgressDialog progressDialog;
    private int retries=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = (EditText)findViewById(R.id.et_username);
        etPass = (EditText)findViewById(R.id.et_password);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getDisplayName());

                    retries=0;
                    sendToken(user.getEmail());
                    //Sign user in
                    Toast.makeText(MainActivity.this,"Success, Redirecting",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this,HomeActivity.class);
                    //progressBar.setVisibility(View.GONE);
                    progressDialog.cancel();
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this,"Not signed in",Toast.LENGTH_SHORT).show();
                }
            }
        };

        signin = (Button)findViewById(R.id.btn_login);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=etUser.getText().toString().trim();
                String password=etPass.getText().toString().trim();
                if(email.equals("")||password.equals("")){
                    Toast.makeText(MainActivity.this,"Please Enter email and password",Toast.LENGTH_SHORT);
                }
                else {
                    signInUser(email, password);
                    progressDialog.show();
                }
            }
        });

        signup = (Button)findViewById(R.id.btn_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"SignUp Clicked",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(MainActivity.this,PhoneOtp.class);
                startActivity(i);
            }
        });


        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.CALL_PHONE},
                    5);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        finishAffinity();
    }

    void signInUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            progressDialog.cancel();
                            Toast.makeText(MainActivity.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Success...",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    void sendToken(final String email){
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("bloodbank.pref",MODE_PRIVATE);
        final String userToken = sharedPreferences.getString("token","");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("failed")&&retries<=2){
                            Toast.makeText(MainActivity.this,"Token not sent",Toast.LENGTH_SHORT).show();
                            retries++;
                            sendToken(email);
                        }
                        if(response.equalsIgnoreCase("success")){
                            Toast.makeText(MainActivity.this,"Token sent",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("token",userToken);
                params.put("email",email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
