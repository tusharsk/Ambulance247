package com.example.tusharsk.ambulance247;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login_Activity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    String email;
    String password;
    Button bt,btsign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        etEmail=(EditText)findViewById(R.id.et_email);
        etPassword=(EditText)findViewById(R.id.et_password);
        bt=(Button) findViewById(R.id.login);
        btsign=(Button) findViewById(R.id.signup);
    }


    public void login(View view) {

        email=etEmail.getText().toString();
        password=etPassword.getText().toString();
        if(!email.matches("")&&!password.matches(""))
        {
            String url="https://anubhavaron000001.000webhostapp.com/login.php?name="+email+"&password="+password;
            bt.setEnabled(false);
            btsign.setEnabled(false);
            new MyAsyncTaskgetNews().execute(url);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"PLEASE ENTER THE DETAILS",Toast.LENGTH_SHORT).show();
        }


    }


    public void register(View view) {
        Intent i= new Intent(Login_Activity.this,SignUp.class);
        startActivity(i);


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
                if (json.getString("msg").equalsIgnoreCase("Pass Login")) {
                    Toast.makeText(getApplicationContext(), json.getString("msg"), Toast.LENGTH_LONG).show();
                    //login

                    JSONArray UserInfo=new JSONArray( json.getString("info"));
                    JSONObject UserCreintal= UserInfo.getJSONObject(0);

                    SaveSettings saveSettings= new SaveSettings(getApplicationContext());
                    saveSettings.SaveData(UserCreintal.getString("name"),UserCreintal.getString("email"),UserCreintal.getString("flag"));
                    Intent i= new Intent(Login_Activity.this,MainActivity.class);
                    startActivity(i);
                    finish();

                }

                if (json.getString("msg").equalsIgnoreCase("cannot login")) {
                    Toast.makeText(getApplicationContext(),"WRONG EMAIL OR PASSWORD",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                //Log.d("er",  ex.getMessage());
            }


        }

        protected void onPostExecute(String  result2){


        }

    }



}
