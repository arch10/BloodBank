package com.gigaworks.bloodbankbeta;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arch on 13-04-2017.
 */

public class GetUserData  {
    private static String email;
    private static String ResponseURL,ResponseURL1;
    private static Context ctx;

    public GetUserData(String email,String url,String url1, Context context){
        this.email=email;
        ResponseURL=url;
        ResponseURL1=url1;
        ctx=context;
    }

    void GetJsonResponse(final MapCallback mapCallback){

        StringRequest jsonObjectRequest= new StringRequest(Request.Method.POST, ResponseURL
                ,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            ArrayList<String> info=new ArrayList<>();
                            info.add(jsonObject.getString("name"));
                            info.add(jsonObject.getString("phone"));
                            info.add(jsonObject.getString("place"));
                            info.add(jsonObject.getString("bloodgroup"));
                            mapCallback.onSuccess(info);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx,error.getMessage(),Toast.LENGTH_SHORT).show();
                mapCallback.onFail(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params= new HashMap<>();
                params.put("email",email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(jsonObjectRequest);
    }

    public void getImageUrl(final MapCallback mapCallback){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ResponseURL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            ArrayList<String> info=new ArrayList<>();
                            info.add(jsonObject.getString("url"));
                            mapCallback.onSuccess(info);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mapCallback.onFail(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("email",email);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }

}
