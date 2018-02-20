package com.example.hp.suraksha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hp on 30/9/17.
 */

public class SubmitClass extends Activity {
    static Context mContext;
    static int dataSentSattus;
    int progressBarStatus = 0;
    long fileSize = 0;
    Uri mUri;
    String CollegeID, Name, Branch, MobileNo, Address, ParentNo, imageToSent, year;
    String div;
    String DEFAULT = "Not Selected";
    private final int REQUEST_CODE = 1;
    Button submitBtn;
    ImageView image;
    static int backButtonCount;
    EditText mNameTextView, mCollegeIDText, mMobileText, mAddressText, mParentNumberText;
    Spinner mBranchSpinner, mYearSpinner, mDivisionSpinner;
    Bitmap imageBitmap = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_registrationform);
        mNameTextView = (EditText) findViewById(R.id.nameEditText);
        mCollegeIDText = (EditText) findViewById(R.id.collegeIdEditText);
        mBranchSpinner = (Spinner) findViewById(R.id.branchSpinner);
        mYearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        mDivisionSpinner = (Spinner) findViewById(R.id.divSpinner);
        mMobileText = (EditText) findViewById(R.id.mobileNoEdit);
        mAddressText = (EditText) findViewById(R.id.addEdit);
        mParentNumberText = (EditText) findViewById(R.id.parentMob);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        final String[] yearList = new String[]{DEFAULT, "FE", "SE", "TE", "BE"};
        final ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);
        //yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final String[] branchList = new String[]{DEFAULT
                , "Mechanical", "Civil", "Computer", "IT", "E&TC"};
        final ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, branchList);
        //branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Character[] divList = new Character[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
        final ArrayAdapter<Character> divAdapter = new ArrayAdapter<Character>(this, android.R.layout.simple_spinner_item, divList);
        // divAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBranchSpinner.setAdapter(branchAdapter);
        mDivisionSpinner.setAdapter(divAdapter);
        mYearSpinner.setAdapter(yearAdapter);
        mBranchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //    Toast.makeText(SubmitClass.this, branchList[position], Toast.LENGTH_SHORT).show();
                if (branchList[position] != DEFAULT)
                    Branch = branchList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SubmitClass.this, "Select Branch", Toast.LENGTH_SHORT).show();
            }
        });
        mYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //    Toast.makeText(SubmitClass.this, yearList[position], Toast.LENGTH_SHORT).show();
                if (branchList[position] != DEFAULT)
                    year = yearList[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SubmitClass.this, "Select Year", Toast.LENGTH_SHORT).show();
            }
        });
        mDivisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(SubmitClass.this, "" + divList[position], Toast.LENGTH_SHORT).show();
                div = "" + divList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SubmitClass.this, "Select Division", Toast.LENGTH_SHORT).show();
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext = SubmitClass.this;
                CollegeID = mCollegeIDText.getText().toString();
                Name = mNameTextView.getText().toString();
                Address = mAddressText.getText().toString();
                Toast.makeText(SubmitClass.this, Address, Toast.LENGTH_SHORT).show();
                MobileNo = mMobileText.getText().toString();
                ParentNo = mParentNumberText.getText().toString();
                if (CollegeID != null && CollegeID.length() == 7 && Name != null && Address != null && MobileNo != null && ParentNo != null && Branch != DEFAULT && year != DEFAULT) {


                    new ThreadClass().execute("https://akthakur0422.000webhostapp.com/app_db_connection.php", CollegeID, Name, Branch, year, div, Address, MobileNo, ParentNo);
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }


    class ThreadClass extends AsyncTask<String, Void, String> {

        private ProgressBar progressBar;
        StringBuilder sb = new StringBuilder();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String login_url = params[0];
            String collegeID = params[1];
            String name = params[2];
            String branch = params[3];
            String year = params[4];
            char division = params[5].charAt(0);
            String address = params[6];
            String mobile = params[7];
            String parentno = params[8];


            try {

                URL url = new URL("https://akthakur0422.000webhostapp.com/app_db_connection.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("collegeID", "UTF-8") + "=" + URLEncoder.encode(collegeID, "UTF-8") + "&"
                        + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                        + URLEncoder.encode("branch", "UTF-8") + "=" + URLEncoder.encode(branch, "UTF-8") + "&"
                        + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(year, "UTF-8") + "&"
                        + URLEncoder.encode("division", "UTF-8") + "=" + URLEncoder.encode("" + division, "UTF-8") + "&"
                        + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8") + "&"
                        + URLEncoder.encode("mobileNo", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8") + "&"
                        + URLEncoder.encode("parentNo", "UTF-8") + "=" + URLEncoder.encode(parentno, "UTF-8");
                bufferwriter.write(post_data);

                bufferwriter.flush();
                bufferwriter.close();
                outputstream.close();
                Log.d("data", "post Done" + "         " + post_data);

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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);


            progressBar.setVisibility(View.GONE);

            try {
                //converting response to json object

                Log.d("php", s);
                JSONObject obj = new JSONObject(s);

                //if no error in response
                if (obj.getBoolean("status")) {

                    Toast.makeText(SubmitClass.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    SharedPreferences sh = SubmitClass.this.getSharedPreferences("shared", Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEdt = sh.edit();

                    mEdt.putInt("register", 1);
                    mEdt.commit();
                    mEdt.putString("collegeID", CollegeID);
                    mEdt.commit();
                    mEdt.putString("Name", Name);
                    mEdt.commit();
                    startActivity(new Intent(SubmitClass.this, MainActivity.class));

                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}





