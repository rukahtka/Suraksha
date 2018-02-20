package com.example.hp.suraksha;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by hp on 24/10/17.
 */

public class UploadImage extends AsyncTask<String, Void, String> {
    Context context;

    public UploadImage(Context context) {
        this.context = context;
    }

    StringBuilder sb = new StringBuilder();
    ProgressDialog loading;
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(context,"Please wait...","uploading",false,false);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        loading.dismiss();
        try {
            //converting response to json object

            Log.d("php", s);
            JSONObject obj = new JSONObject(s);

            //if no error in response
            if (obj.getBoolean("status")) {

                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String collegeID = params[0];
        String image=params[1];


        try {

            URL url = new URL("https://akthakur0422.000webhostapp.com/upload_image.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputstream = httpURLConnection.getOutputStream();
            BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
            String post_data = URLEncoder.encode("collegeID", "UTF-8") + "=" + URLEncoder.encode(collegeID, "UTF-8") + "&"
                    + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8");
            bufferwriter.write(post_data);

            bufferwriter.flush();
            bufferwriter.close();
            outputstream.close();
            Log.d("data", "image post Done" + "         " + post_data);

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == httpURLConnection.HTTP_OK) {
                Log.e("datasentstatus", "" + SubmitClass.dataSentSattus);
                Log.d("con", "Data Sent" + httpURLConnection.getResponseCode() + "  " + httpURLConnection.getResponseMessage());

                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                sb = new StringBuilder();
                String response;

                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }


                httpURLConnection.disconnect();
            } else {
                Log.d("con", httpURLConnection.getResponseCode() + "   " + httpURLConnection.getResponseMessage());
                httpURLConnection.disconnect();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }



}
