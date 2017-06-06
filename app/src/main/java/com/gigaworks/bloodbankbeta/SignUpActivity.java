package com.gigaworks.bloodbankbeta;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText name,place,email,password,dob,weight,height,lastDonated;
    private CheckBox bloodDisease;
    private Spinner bloodgroup;
    private String phone;
    private int hasBloodDisease;
    private Button nextButton,regButton;
    private ViewFlipper viewFlipper;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final static String TAG="Archit";
    private ProgressDialog progressDialog;
    private Calendar myCalendar;
    private String emailAddress,pass;
    private int retries=0;

    private final static String REQUEST_URL="https://gigaworks.000webhostapp.com/user_details.php";
    private final static String REG_TOKEN_URL="https://gigaworks.000webhostapp.com/reg_token.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        phone=getIntent().getStringExtra("pno");
        name=(EditText)findViewById(R.id.et_name);
        place=(EditText)findViewById(R.id.et_place);
        password=(EditText)findViewById(R.id.et_password);
        dob=(EditText)findViewById(R.id.et_dob);
        weight=(EditText)findViewById(R.id.et_weight);
        height=(EditText)findViewById(R.id.et_height);
        email=(EditText)findViewById(R.id.et_email);
        lastDonated=(EditText)findViewById(R.id.et_lastdonated);
        bloodDisease=(CheckBox)findViewById(R.id.cb_blood_disease);
        bloodgroup = (Spinner)findViewById(R.id.sp_bloodgroup);
        viewFlipper=(ViewFlipper)findViewById(R.id.vf_flipper);
        nextButton=(Button)findViewById(R.id.btn_next);
        regButton=(Button)findViewById(R.id.btn_reg);

        Animation outLeft= AnimationUtils.loadAnimation(this,R.anim.slide_out_left);
        Animation inRight=AnimationUtils.loadAnimation(this,R.anim.slide_in_right);
        viewFlipper.setInAnimation(inRight);
        viewFlipper.setOutAnimation(outLeft);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        //Firebase Initialization
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getDisplayName());
                    //Sign user in
                    Toast.makeText(SignUpActivity.this,"Success, Redirecting",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this,HomeActivity.class);
                    progressDialog.cancel();
                    startActivity(i);
                    finish();
                }
                else {
//                    Toast.makeText(SignUpActivity.this,"Not signed in",Toast.LENGTH_SHORT).show();
                }
            }
        };


        /* create calndar object */
        myCalendar = Calendar.getInstance();

        /* and copy the fallowing code*/
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

                dob.setText(sdf.format(myCalendar.getTime()));

            }

        };

        final DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

                lastDonated.setText(sdf.format(myCalendar.getTime()));

            }

        };


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUpActivity.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        lastDonated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUpActivity.this, datePickerListener2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
                if(regButton.getVisibility()==View.GONE){
                regButton.setVisibility(View.VISIBLE);
                nextButton.setText("Previous");
                }
                else{
                    regButton.setVisibility(View.GONE);
                    nextButton.setText("Next");
                }
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                emailAddress=email.getText().toString().trim();
                pass=password.getText().toString().trim();
                regUser();
            }
        });
    }


    void sendToken(final String email){
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("bloodbank.pref",MODE_PRIVATE);
        final String userToken = sharedPreferences.getString("token","");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REG_TOKEN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("failedi")&&retries<=2){
                            retries++;
                            sendToken(email);
                            Toast.makeText(SignUpActivity.this,"Token not sent",Toast.LENGTH_SHORT).show();
                        }
                        if(response.equalsIgnoreCase("failed")){
                            Toast.makeText(SignUpActivity.this,"User not registered",Toast.LENGTH_SHORT).show();
                        }
                        if(response.equalsIgnoreCase("success")){
                            Toast.makeText(SignUpActivity.this,"Token sent",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
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



    void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            progressDialog.cancel();
                            Toast.makeText(SignUpActivity.this, "Failed",
                                    Toast.LENGTH_SHORT).show();
                            if(retries<3&&retries>=0){
                                createUser(emailAddress,pass);
                                retries++;
                            }
                        }
                        else {
                            Toast.makeText(SignUpActivity.this,"User created",
                                    Toast.LENGTH_SHORT).show();
                            retries=0;
                            sendToken(emailAddress);
                        }
                    }
                });
    }

    void regUser(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(SignUpActivity.this,"Successfully Registered",Toast.LENGTH_SHORT).show();
                            retries=0;
                            createUser(emailAddress,pass);
                        }
                        if(response.equals("failed")){
                            Toast.makeText(SignUpActivity.this,"Registration failed",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(SignUpActivity.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email",email.getText().toString().trim());
                params.put("place",place.getText().toString().trim());
                params.put("name",name.getText().toString().trim());
                params.put("bloodgroup",bloodgroup.getSelectedItem().toString());
                params.put("dob",dob.getText().toString().trim());
                params.put("weight",weight.getText().toString().trim());
                params.put("height",height.getText().toString().trim());
                params.put("phone",phone);
                params.put("lastdonated",lastDonated.getText().toString().trim());
                hasBloodDisease=(bloodDisease.isChecked())?1:0;
                params.put("bloodDisease",hasBloodDisease+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
