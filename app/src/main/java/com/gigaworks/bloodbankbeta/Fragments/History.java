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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gigaworks.bloodbankbeta.HelpHistory;
import com.gigaworks.bloodbankbeta.HistoryAdaptor;
import com.gigaworks.bloodbankbeta.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class History extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private HistoryAdaptor mHistoryAdaptor;
    private LinearLayoutManager mLayoutManager;
    private static final String RESPONSE_URL="https://gigaworks.000webhostapp.com/fetch_history.php";
    private Context ctx;
    public History() {}

    public static History newInstance() {
        History fragment = new History();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv_history);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ArrayList<HelpHistory> list=new ArrayList<>();
        final SharedPreferences sharedPreferences= getContext().getSharedPreferences("bloodbank.pref",MODE_PRIVATE);
        mLayoutManager=new LinearLayoutManager(ctx);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.hasFixedSize();
        mHistoryAdaptor=new HistoryAdaptor(list,ctx);
        mRecyclerView.setAdapter(mHistoryAdaptor);

        final ProgressDialog pd=new ProgressDialog(ctx);
        pd.setTitle("Getting List");
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
                                    HelpHistory helpHistory=new HelpHistory();
                                    helpHistory.setName(jsonObject.getString("name"));
                                    helpHistory.setDate(jsonObject.getString("date(`date`)"));
                                    list.add(helpHistory);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            pd.dismiss();
                            mHistoryAdaptor.notifyDataSetChanged();
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
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);

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
        this.ctx=context;
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
