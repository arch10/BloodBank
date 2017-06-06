package com.gigaworks.bloodbankbeta.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gigaworks.bloodbankbeta.BloodRequest;
import com.gigaworks.bloodbankbeta.R;
import com.gigaworks.bloodbankbeta.RequestAdaptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Home extends Fragment {


    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private RequestAdaptor mRequestAdaptor;
    private LinearLayoutManager mLayoutManager;
    private static final String RESPONSE_URL="https://gigaworks.000webhostapp.com/fetch_request.php";
    private Context ctx;
    private SharedPreferences sharedPreferences;
    private ArrayList<BloodRequest> list;
    private ImageView error;


    public Home() { }

    public static Home newInstance() {
        Home fragment = new Home();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv_requests);
        error=(ImageView)view.findViewById(R.id.iv_error_page);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list=new ArrayList<>();
        sharedPreferences= getContext().getSharedPreferences("bloodbank.pref",MODE_PRIVATE);
        mLayoutManager=new LinearLayoutManager(ctx);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.hasFixedSize();
        mRequestAdaptor=new RequestAdaptor(list,ctx);
        mRecyclerView.setAdapter(mRequestAdaptor);
        fetchView();

    }


    void fetchView(){
        final ProgressDialog pd=new ProgressDialog(ctx);
        pd.setTitle("Getting Requests");
        pd.setMessage("Please Wait");
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,RESPONSE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response!=null){
                            JSONArray jsonArray;
                            try {
                                jsonArray=new JSONArray(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                jsonArray=new JSONArray();
                            }
                            for(int i=0;i<response.length();i++){
                                try {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    BloodRequest bloodRequest=new BloodRequest();
                                    bloodRequest.setName(jsonObject.getString("patient_name"));
                                    bloodRequest.setAge(jsonObject.getString("age"));
                                    bloodRequest.setHospital(jsonObject.getString("hospital"));
                                    bloodRequest.setPhone(jsonObject.getString("phone"));
                                    bloodRequest.setReqId(jsonObject.getString("reqid"));
                                    list.add(bloodRequest);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            pd.dismiss();
                            mRequestAdaptor.notifyDataSetChanged();
                        }
                        else {
                            pd.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(ctx,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("email",sharedPreferences.getString("email","abc@xyz.com"));
                params.put("blood",sharedPreferences.getString("bloodgroup","Z+"));
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        ctx=context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
