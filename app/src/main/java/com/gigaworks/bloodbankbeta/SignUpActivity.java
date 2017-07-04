package com.gigaworks.bloodbankbeta;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText add1, add2, dob, weight, height, lastDonated;
    private CheckBox bloodDisease;
    private Spinner bloodgroup;
    private String phone;
    private int hasBloodDisease;
    private Button nextButton, regButton;
    private ViewFlipper viewFlipper;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final static String TAG = "Archit";
    private ProgressDialog progressDialog;
    private Calendar myCalendar;
    private String emailAddress, pass;
    private NumberPicker np;
    private Button pickerButton;
    private AutoCompleteTextView city,state;
    private TextInputLayout add1Wrapper,add2Wrapper,cityWrapper,stateWrapper,dobWrapper,heightWrapper,
    weightWrapper,lastdonatedWrapper;
    private Pattern pattern;
    private Matcher matcher;

    private final static String DATE_PATTEN="[0-9]{4}-[0-9]{2}-[0-9]{2}";
    private final static String REQUEST_URL = "https://gigaworks.000webhostapp.com/user_details.php";
    private final static String REG_TOKEN_URL = "https://gigaworks.000webhostapp.com/reg_token.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        phone = getIntent().getStringExtra("pno");
        add1 = (EditText) findViewById(R.id.et_add1);
        add2 = (EditText) findViewById(R.id.et_add2);
        state = (AutoCompleteTextView) findViewById(R.id.et_state);
        dob = (EditText) findViewById(R.id.et_dob);
        weight = (EditText) findViewById(R.id.et_weight);
        height = (EditText) findViewById(R.id.et_height);
        city = (AutoCompleteTextView) findViewById(R.id.et_city);
        lastDonated = (EditText) findViewById(R.id.et_lastdonated);
        bloodDisease = (CheckBox) findViewById(R.id.cb_blood_disease);
        bloodgroup = (Spinner) findViewById(R.id.sp_bloodgroup);
        viewFlipper = (ViewFlipper) findViewById(R.id.vf_flipper);
        nextButton = (Button) findViewById(R.id.btn_next);
        regButton = (Button) findViewById(R.id.btn_reg);

        //Wrapper Initializations
        add1Wrapper=(TextInputLayout)findViewById(R.id.add1Wrapper);
        add2Wrapper=(TextInputLayout)findViewById(R.id.add2Wrapper);
        cityWrapper=(TextInputLayout)findViewById(R.id.cityWrapper);
        stateWrapper=(TextInputLayout)findViewById(R.id.stateWrapper);
        dobWrapper=(TextInputLayout)findViewById(R.id.dobWrapper);
        weightWrapper=(TextInputLayout)findViewById(R.id.weightWrapper);
        heightWrapper=(TextInputLayout)findViewById(R.id.heightWrapper);
        lastdonatedWrapper=(TextInputLayout)findViewById(R.id.lastdonatedWrapper);

        final Animation outLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        final Animation inRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        final Animation outRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        final Animation inLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);


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
                    Toast.makeText(SignUpActivity.this, "Success, Redirecting", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                    progressDialog.cancel();
                    startActivity(i);
                    finish();
                } else {
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
                if (regButton.getVisibility() == View.GONE) {
                    if(validateInput1()) {
                        viewFlipper.setInAnimation(inRight);
                        viewFlipper.setOutAnimation(outLeft);
                        viewFlipper.showNext();
                        regButton.setVisibility(View.VISIBLE);
                        nextButton.setText("Previous");
                    }
                } else {
                    viewFlipper.setInAnimation(inLeft);
                    viewFlipper.setOutAnimation(outRight);
                    viewFlipper.showPrevious();
                    regButton.setVisibility(View.GONE);
                    nextButton.setText("Next");
                }
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateInput2()){
                    Toast.makeText(SignUpActivity.this,"OK",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignUpActivity.this,"Not OK",Toast.LENGTH_SHORT).show();
                }
                //progressDialog.show();
                //emailAddress = email.getText().toString().trim();
                //pass = password.getText().toString().trim();
                //regUser();
            }
        });

        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNumber(R.id.et_weight);
            }
        });
        height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNumber(R.id.et_height);
            }
        });
        weight.setKeyListener(null);
        height.setKeyListener(null);

        String[] cities=getResources().getStringArray(R.array.india_top_places);
        String[] states=getResources().getStringArray(R.array.india_states);

        ArrayAdapter<String> adapterCity= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cities);
        ArrayAdapter<String> adapterState= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,states);

        city.setAdapter(adapterCity);
        state.setAdapter(adapterState);
    }


    void sendToken(final String email) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("bloodbank.pref", MODE_PRIVATE);
        final String userToken = sharedPreferences.getString("token", "");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REG_TOKEN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("failed")) {
                            Toast.makeText(SignUpActivity.this, "User not registered", Toast.LENGTH_SHORT).show();
                        }
                        if (response.equalsIgnoreCase("success")) {
                            Toast.makeText(SignUpActivity.this, "Token sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", userToken);
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            progressDialog.cancel();
                            Toast.makeText(SignUpActivity.this, "Failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "User created",
                                    Toast.LENGTH_SHORT).show();
                            sendToken(emailAddress);
                        }
                    }
                });
    }

    void regUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Toast.makeText(SignUpActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            createUser(emailAddress, pass);
                        }
                        if (response.equals("failed")) {
                            Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("email", email.getText().toString().trim());
                //params.put("place", place.getText().toString().trim());
                //params.put("name", name.getText().toString().trim());
                params.put("bloodgroup", bloodgroup.getSelectedItem().toString());
                params.put("dob", dob.getText().toString().trim());
                params.put("weight", weight.getText().toString().trim());
                params.put("height", height.getText().toString().trim());
                params.put("phone", phone);
                params.put("lastdonated", lastDonated.getText().toString().trim());
                hasBloodDisease = (bloodDisease.isChecked()) ? 1 : 0;
                params.put("bloodDisease", hasBloodDisease + "");
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

    private void getNumber(final int id){

        LayoutInflater layoutInflater=LayoutInflater.from(this);
        View numberPicker=layoutInflater.inflate(R.layout.number_picker_layout,null);
        np=(NumberPicker)numberPicker.findViewById(R.id.np_picker);
        pickerButton=(Button)numberPicker.findViewById(R.id.btn_picker_ok);

        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setView(numberPicker);
        final AlertDialog alertDialog=alert.create();
        alertDialog.show();

        if(id==R.id.et_weight) {
            //NumberPicker
            np.setMinValue(45);
            np.setMaxValue(120);
            np.setWrapSelectorWheel(true);
        }
        else if(id==R.id.et_height){
            //NumberPicker
            np.setMinValue(135);
            np.setMaxValue(190);
            np.setWrapSelectorWheel(true);
        }

        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id==R.id.et_weight) {
                    weight.setText(np.getValue()+"");
                }
                else if(id==R.id.et_height){
                    height.setText(np.getValue()+"");
                }
                alertDialog.dismiss();
            }
        });
    }

    private boolean validateAddress1(){
        if(add1.getText().toString().trim().equals("")){
            add1Wrapper.setError("Complete the address");
            requestFocus(add2);
            return false;
        }
        else {
            add1Wrapper.setErrorEnabled(false);
            return true;
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateAddress2(){
        if(add2.getText().toString().trim().equals("")){
            add2Wrapper.setError("Complete the address");
            requestFocus(add2);
            return false;
        }
        else {
            add2Wrapper.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateCity(){
        if(city.getText().toString().trim().equals("")){
            cityWrapper.setError("Enter city");
            requestFocus(city);
            return false;
        }
        else {
            cityWrapper.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateState(){
        if(state.getText().toString().trim().equals("")){
            stateWrapper.setError("Enter state");
            requestFocus(state);
            return false;
        }
        else {
            stateWrapper.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateDob(){
        if(dob.getText().toString().trim().equals("")){
            dobWrapper.setError("Enter Date of Birth");
            return false;
        }
        else {
            pattern=Pattern.compile(DATE_PATTEN);
            matcher=pattern.matcher(dob.getText().toString().trim());
            if(matcher.matches()){
                dobWrapper.setErrorEnabled(false);
                return true;
            }
            else {
                dobWrapper.setError("Enter Valid DOB");
                return false;
            }
        }
    }

    private boolean validateLast(){
        if(!lastDonated.getText().toString().equals("")) {
            pattern = Pattern.compile(DATE_PATTEN);
            matcher = pattern.matcher(dob.getText().toString().trim());
            if (matcher.matches()) {
                lastdonatedWrapper.setErrorEnabled(false);
                return true;
            } else {
                lastdonatedWrapper.setError("Enter Valid Date");
                return false;
            }
        }
        lastdonatedWrapper.setErrorEnabled(false);
        return true;
    }

    private boolean validateWeight(){
        if(weight.getText().toString().trim().equals("")){
            weightWrapper.setError("Enter weight");
            return false;
        }
        else {
            weightWrapper.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateHeight(){
        if(height.getText().toString().trim().equals("")){
            heightWrapper.setError("Enter height");
            return false;
        }
        else {
            heightWrapper.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateInput1(){
        return validateAddress1()&&validateAddress2()&&validateCity()&&validateState();
    }

    private boolean validateInput2(){
        return validateDob()&&validateHeight()
                &&validateLast()&&validateWeight();
    }

}
