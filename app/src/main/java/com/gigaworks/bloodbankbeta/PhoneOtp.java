package com.gigaworks.bloodbankbeta;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PhoneOtp extends AppCompatActivity implements View.OnClickListener{

    Button sendOtp,verifyOtp;
    EditText phoneno, otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_otp);

        sendOtp=(Button)findViewById(R.id.btn_sendOtp);
        phoneno=(EditText)findViewById(R.id.et_mobno);
        sendOtp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sendOtp:
                if(!phoneno.getText().toString().equals("")&&phoneno.getText().toString().trim().length()==10)
                    sendOtp();
                break;
        }
    }

    private void sendOtp() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View confirmDialog=inflater.inflate(R.layout.otp_verify,null);
        verifyOtp=(Button)confirmDialog.findViewById(R.id.btn_confirmOtp);
        otp=(EditText)confirmDialog.findViewById(R.id.et_otp);

        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        final AlertDialog alertDialog=alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().toString().trim().equals("1234")){
                    Toast.makeText(PhoneOtp.this,"Validated",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(PhoneOtp.this,SignUpActivity.class);
                    i.putExtra("pno",phoneno.getText().toString().trim());
                    alertDialog.dismiss();
                    startActivity(i);
                    finish();
                }
                else if(otp.getText().toString().equals("")){
                    Toast.makeText(PhoneOtp.this,"Enter Valid OTP",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PhoneOtp.this,"Failed, Mobile cannot be verified",Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });
    }


}
