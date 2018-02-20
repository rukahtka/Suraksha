package com.example.hp.suraksha;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hp on 10/10/17.
 */

public class ReceivedMessagesFragment extends Fragment {
    RecyclerView mRecyclerView;
    SentMessageAdapter sentMessageAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ProblemSet> problemArrayList;
    RequestQueue requestQueue;
    String url;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.lay_receivemessages,null);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.receiveRecycler);
        requestQueue= Volley.newRequestQueue(getContext());
        problemArrayList=new ArrayList<>();
        url="https://akthakur0422.000webhostapp.com/takeadminmessage.php";
        sentMessageAdapter=new SentMessageAdapter(getContext(),problemArrayList);
        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(sentMessageAdapter);
        getAdminMessages();
        return view;
    }









    private void getAdminMessages() {


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray=response.getJSONArray("result");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        ProblemSet m=new ProblemSet();
                        m.setProblem(jsonObject.getString("Message"));
                        m.setTime(jsonObject.getString("time"));
                        problemArrayList.add(m);
                        sentMessageAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("Successful","Value parsed");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Unsuccessful","Value not parsed"+error);
            }
        });

        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));




    }
}
