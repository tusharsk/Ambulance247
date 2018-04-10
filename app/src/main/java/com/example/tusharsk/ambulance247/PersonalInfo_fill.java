package com.example.tusharsk.ambulance247;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PersonalInfo_fill extends AppCompatActivity {


    SaveSettings saveSettings;
    TextView tvname;
    EditText dob,bg,mobile,age,emergency,gender;
    String dobs,bgs,mobiles,ages,emergencys,genders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        saveSettings=new SaveSettings(getApplicationContext());
        String username=saveSettings.Username();
        String flag=saveSettings.Userflag();
        if(!flag.matches(""))
        {
            Intent intent=new Intent(getApplicationContext(),PersonalInfo_show.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_personal_info_fill);
        tvname.setText(username);
        dob=(EditText) findViewById(R.id.etdob);
        bg=(EditText) findViewById(R.id.etbloodgroup);
        mobile=(EditText) findViewById(R.id.etmobile);
        age=(EditText) findViewById(R.id.etage);
        emergency=(EditText) findViewById(R.id.etemergency);
        gender=(EditText) findViewById(R.id.etgender);
    }

    public void Save(View view) {
        dobs=dob.getText().toString();
        bgs=bg.getText().toString();
        mobiles=mobile.getText().toString();
        ages=age.getText().toString();
        emergencys=emergency.getText().toString();
        genders=gender.getText().toString();
        if(!dobs.matches("")&&!bgs.matches("")&&!mobiles.matches("")&&!ages.matches("")&&!emergencys.matches("")&&genders.matches("")){
            String url="";
            new MyAsyncTaskgetNews().execute(url);
        }
    }


    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
        }
        @Override
        protected String  doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds

                try {
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //convert the stream to string
                    Operations operations=new Operations(getApplicationContext());
                    NewsData = operations.ConvertInputToStringNoChange(in);
                    //send to display data
                    publishProgress(NewsData);
                } finally {
                    //end connection
                    urlConnection.disconnect();
                }

            }catch (Exception ex){}
            return null;
        }
        protected void onProgressUpdate(String... progress) {

            try {
                JSONObject json= new JSONObject(progress[0]);
                //display response data
                if (json.getString("msg")==null)
                    return;
                if (json.getString("msg").equalsIgnoreCase("Yes")) {
                    Toast.makeText(getApplicationContext(), json.getString("Details Filled!"), Toast.LENGTH_LONG).show();
                    //login
                }

                if (json.getString("msg").equalsIgnoreCase("No")) {
                    Toast.makeText(getApplicationContext(),"Email Already Registered!",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                //Log.d("er",  ex.getMessage());
            }


        }

        protected void onPostExecute(String  result2){


        }

    }
}
