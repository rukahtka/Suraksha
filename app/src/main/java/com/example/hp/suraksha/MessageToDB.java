package com.example.hp.suraksha;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by hp on 3/10/17.
 */

public class MessageToDB extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... params) {

            String collegeID = params[0];
            String problem = params[1];
            String lat = params[2];
            String lng = params[3];

        try{
            URL url = new URL("https://akthakur0422.000webhostapp.com/report_problem.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputstream = httpURLConnection.getOutputStream();
            BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(outputstream,"UTF-8"));
            String post_data=null;
            if(lat ==null || lng ==null){
                post_data = URLEncoder.encode("collegeID","UTF-8")+"="+URLEncoder.encode(collegeID,"UTF-8")+"&"
                        +URLEncoder.encode("problem","UTF-8")+"="+URLEncoder.encode(problem,"UTF-8");
            }
            else {
                post_data = URLEncoder.encode("collegeID", "UTF-8") + "=" + URLEncoder.encode(collegeID, "UTF-8") + "&"
                        + URLEncoder.encode("problem", "UTF-8") + "=" + URLEncoder.encode(problem, "UTF-8") + "&"
                        + URLEncoder.encode("lattitude", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8") + "&"
                        + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(lng, "UTF-8");
            }
            bufferwriter.write(post_data);

            bufferwriter.flush();
            bufferwriter.close();
            outputstream.close();
            Log.d("data","post Done"+"         "+post_data);

            int responseCode=httpURLConnection.getResponseCode();
            if(responseCode==httpURLConnection.HTTP_OK){
                Log.d("messageStatus","Sent to Table");
            }
            else
                Log.d("con", httpURLConnection.getResponseCode() + "   " + httpURLConnection.getResponseMessage());
            httpURLConnection.disconnect();




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


            return null;
    }
}
