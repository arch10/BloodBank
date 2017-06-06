package com.gigaworks.bloodbankbeta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class NewRequest extends AppCompatActivity implements View.OnClickListener{

    private EditText name,phone,age,hospital;
    private Spinner req,bloodgroup;
    private Button RequestButton;
    private String emailAddress;
    String ReqUrl="https://gigaworks.000webhostapp.com/new_request.php";
    String notificationURL="https://gigaworks.000webhostapp.com/test_notification.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        SharedPreferences sharedPreferences=getApplicationContext().
                getSharedPreferences("bloodbank.pref",MODE_PRIVATE);
        emailAddress=sharedPreferences.getString("email","");

        name=(EditText)findViewById(R.id.et_req_name);
        phone=(EditText)findViewById(R.id.et_req_phone);
        age=(EditText)findViewById(R.id.et_req_age);
        req=(Spinner)findViewById(R.id.sp_req_for);
        bloodgroup=(Spinner)findViewById(R.id.sp_req_bloodgroup);
        hospital=(EditText)findViewById(R.id.et_req_hospital);
        RequestButton=(Button)findViewById(R.id.btn_request_blood);
        RequestButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final ProgressDialog pd=new ProgressDialog(NewRequest.this);
        pd.setTitle("Sending Request");
        pd.setMessage("Please Wait");
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ReqUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){
                            pd.dismiss();
                            Toast.makeText(NewRequest.this,"Request Generated",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(NewRequest.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                        if(response.equalsIgnoreCase("failed")){
                            pd.dismiss();
                            Toast.makeText(NewRequest.this,"Request Query Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(NewRequest.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("email",emailAddress);
                params.put("name",name.getText().toString());
                params.put("phone",phone.getText().toString());
                params.put("age",age.getText().toString());
                params.put("for",req.getSelectedItem().toString());
                params.put("hospital",hospital.getText().toString());
                params.put("blood",bloodgroup.getSelectedItem().toString());
                return params;
            }
        };

        StringRequest request=new StringRequest(Request.Method.POST, notificationURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences sharedPreferences=getApplicationContext()
                        .getSharedPreferences("bloodbank.pref",MODE_PRIVATE);
                Map<String,String> params=new HashMap<>();
                params.put("message",name.getText().toString()+" needs your help");
                params.put("blood",bloodgroup.getSelectedItem().toString());
                params.put("email",sharedPreferences.getString("email","abc@xyz.com"));
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        requestQueue.add(request);
    }
}
