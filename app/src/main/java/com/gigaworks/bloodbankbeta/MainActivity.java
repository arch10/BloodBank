package com.gigaworks.bloodbankbeta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
    private TextInputLayout emailWrapper,passwordWrapper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = (EditText)findViewById(R.id.et_username);
        etPass = (EditText)findViewById(R.id.et_password);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        emailWrapper=(TextInputLayout)findViewById(R.id.emailWrapper);
        passwordWrapper=(TextInputLayout)findViewById(R.id.passwordWrapper);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    sendToken(user.getEmail());
                    Toast.makeText(MainActivity.this,"Success, Redirecting",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        signin = (Button)findViewById(R.id.btn_login);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=etUser.getText().toString().trim();
                String password=etPass.getText().toString().trim();
                if(isValidLogin()){
                    hideKeyboard();
                    signInUser(email, password);
                }
            }
        });

        signup = (Button)findViewById(R.id.btn_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean isValidLogin(){
        if(!validateEmail())
            return false;
        if(!validatePassword())
            return false;
        return true;
    }

    private boolean validatePassword() {
        String password = etPass.getText().toString().trim();

        if(password.isEmpty() || !isValidPassword(password)){
            passwordWrapper.setError("Password must be at least 6 characters");
            requestFocus(etPass);
            return false;
        }
        else {
            passwordWrapper.setErrorEnabled(false);
        }

        return true;
    }

    private boolean isValidPassword(String password) {
        return password.length()>=6;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateEmail() {
        String email = etUser.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            emailWrapper.setError("Please enter a valid email id");
            requestFocus(etUser);
            return false;
        } else {
            emailWrapper.setErrorEnabled(false);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        finishAffinity();
    }

    void signInUser(String email, String password){
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            progressDialog.cancel();
                            Toast.makeText(MainActivity.this, task.getException().toString(),
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
                        if(response.equalsIgnoreCase("failed")){
                            Toast.makeText(MainActivity.this,"Token not sent",Toast.LENGTH_SHORT).show();
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

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
