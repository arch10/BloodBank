package com.gigaworks.bloodbankbeta;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Arch on 15-04-2017.
 */

public class RequestAdaptor extends RecyclerView.Adapter<RequestAdaptor.RequestViewHolder> {

    private ArrayList<BloodRequest> mRequest;
    private Context context;
    private static final String REQUEST_URL = "https://gigaworks.000webhostapp.com/update_request.php";
    private static final String NOTIFICATION_URL = "https://gigaworks.000webhostapp.com/send_notification.php";


    public RequestAdaptor(ArrayList<BloodRequest> mRequest, Context context) {
        this.mRequest = mRequest;
        this.context = context;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        RequestViewHolder mRequestViewHolder = new RequestViewHolder(view);
        return mRequestViewHolder;
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        BloodRequest mBloodRequest = mRequest.get(position);
        String name = mBloodRequest.getName();
        name += " needs your help";
        holder.title.setText(name);
        holder.age.setText(mBloodRequest.getAge());
        holder.hospital.setText(mBloodRequest.getHospital());
    }

    @Override
    public int getItemCount() {
        return mRequest.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title, hospital, age;
        private Button callButton;

        public RequestViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_item_title);
            hospital = (TextView) itemView.findViewById(R.id.tv_item_address);
            age = (TextView) itemView.findViewById(R.id.tv_item_age);
            callButton = (Button) itemView.findViewById(R.id.btn_item_call);
            callButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "item " + getAdapterPosition() + " clicked", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mRequest.get(getAdapterPosition()).getPhone()));

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                sendNotification(mRequest.get(getAdapterPosition()));
                updateRequest(mRequest.get(getAdapterPosition()));
                context.startActivity(intent);
            }
            else {
                Snackbar.make(v,"Please provide call permission",Snackbar.LENGTH_SHORT).show();
                return;
            }
        }
    }

    void updateRequest(BloodRequest bloodRequest){
        final String requestId=bloodRequest.getReqId();
        final SharedPreferences sharedPreferences= context.getSharedPreferences("bloodbank.pref",MODE_PRIVATE);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("reqid",requestId);
                params.put("email",sharedPreferences.getString("email","abc@xyz.com"));
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    void sendNotification(BloodRequest bloodRequest){
        final String reqid=bloodRequest.getReqId();
        final SharedPreferences sharedPreferences= context.getSharedPreferences("bloodbank.pref",MODE_PRIVATE);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, NOTIFICATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("reqid",reqid);
                params.put("email",sharedPreferences.getString("email","abc@xyz.com"));
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
