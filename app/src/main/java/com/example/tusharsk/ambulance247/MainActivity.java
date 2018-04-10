package com.example.tusharsk.ambulance247;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,driver_list_Adapter.driver_list_AdapterOnClickHandler {

    private GoogleMap mMap;
    Fragment fragment;
    private EditText mSearchText;
    SlidingUpPanelLayout slidingUpPanelLayout;
    double latitude=0; // latitude
    double longitude=0; // longitude
    Button t;
    Marker marker;
    String driver_name[];
    String driver_position[];
    String cab_no[];
    String driver_phone_number[];
    String special[];
    int rating[];
    double time[];
    RecyclerView recyclerView;
    driver_list_Adapter driver_list_adapter;
    RatingBar ratingBar;
    SaveSettings saveSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*saveSettings= new SaveSettings(getApplicationContext());
        saveSettings.LoadData();
        if(!saveSettings.UserPresent())
        {
            finish();
        }*/

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.recycler2);


        //fragment = new MapFragment();
        //FragmentManager fm=
        mSearchText = (EditText) findViewById(R.id.input_search);
        slidingUpPanelLayout=(SlidingUpPanelLayout)findViewById(R.id.sliding_layout);

        slidingUpPanelLayout.setDragView(findViewById(R.id.button2));
        ratingBar=(RatingBar)findViewById(R.id.rating_3);


        t=(Button) findViewById(R.id.button2);






        CheckUserPermsions();

        String url="https://anubhavaron000001.000webhostapp.com/cab_dummy_info.php";
        new MyAsyncTaskgetNews().execute(url);


        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Background_cab_list background_cab_list=new Background_cab_list();
                background_cab_list.execute();

                LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                driver_list_adapter=new driver_list_Adapter(MainActivity.this);
                recyclerView.setAdapter(driver_list_adapter);

                t.setText("SLIDE ME");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void init(){
        //Log.d(TAG, "init: initializing");
        Toast.makeText(this,"searching ",Toast.LENGTH_SHORT);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });
    }

    private void geoLocate(){
        //Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            //Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            //Log.d(TAG, "geoLocate: found a location: " + address.toString());
            if(marker!=null)
            {
                marker.remove();
            }
            latitude=address.getLatitude();
            longitude=address.getLongitude();
            LatLng sydney = new LatLng(address.getLatitude(),address.getLongitude());
            marker=mMap.addMarker(new MarkerOptions().position(sydney).title(address.getAddressLine(0)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f ));


        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;



        getDeviceLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(false);
        init();


    }


    private void getDeviceLocation(){
     //   Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{


                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                           // Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            LatLng sydney = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                            latitude=currentLocation.getLatitude();
                            longitude=currentLocation.getLongitude();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f ));
                            //moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),DEFAULT_ZOOM);

                        }else{
                            //Log.d(TAG, "onComplete: current location is null");
                           // Toast.makeText(this,"unable to get current location",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        }catch (SecurityException e){
           // Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
       // Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    void CheckUserPermsions() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
        getLastLocation();

    }

    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();// init the contact list
                } else {
                    // Permission Denied
                    Toast.makeText(this, "your message", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location


    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1; // 1 minute


    void getLastLocation() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




    }

    @Override
    public void onClick(final int x) {

        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
        View mView=getLayoutInflater().inflate(R.layout.dialog_history,null);
        final EditText destination=(EditText)mView.findViewById(R.id.destination_5);
        Button add=(Button)mView.findViewById(R.id.add_5);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               background_adding_history background_task_add_batches=new background_adding_history(MainActivity.this);
                background_task_add_batches.execute("ANU",cab_no[x],destination.getText().toString());
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog=mBuilder.create();
        dialog.show();
    }

    class Background_cab_list extends AsyncTask<Void,Void,String>
    {   String json_url="https://anubhavaron000001.000webhostapp.com/cab_dummy_info.php";

        @Override
        protected void onPreExecute() {
            //   Toast.makeText(login_signup.this,"Hey",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String JSON_STRING) {

            JSONObject jsonObject;
            JSONArray jsonArray;
            try {
                jsonObject=new JSONObject(JSON_STRING);
                int count=0;
                jsonArray=jsonObject.getJSONArray("server response");
                int size=jsonArray.length();

                double latitude_a=0;
                double longitude_a=0;


                String cab_no_a;
                String special_a;
                String driver_name_a;
                String driver_phone_a;
                String driver_position_a;
                int rating_a;
                float time_a;
                double disttance_check=15;
                int c=0;

                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);

                    latitude_a=JO.getDouble("latitude");
                    longitude_a=JO.getDouble("longitude");
                    if(distance(latitude,longitude,latitude_a,longitude_a)<=disttance_check)
                    {
                        c++;
                    }



                    count++;
                }

                count=0;
                cab_no=new String[c];
                special=new String[c];
                driver_name=new String[c];
                driver_phone_number=new String[c];
                driver_position=new String[c];
                rating=new int[c];
                time=new double[c];
                c=0;
                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);

                    latitude_a=JO.getDouble("latitude");
                    longitude_a=JO.getDouble("longitude");
                    double dist=distance(latitude,longitude,latitude_a,longitude_a);
                    if(dist<=disttance_check)
                    {   cab_no[c]=JO.getString("cab_no");
                        driver_position[c]=JO.getString("cab_position");
                        driver_name[c]=JO.getString("driver_name");
                        rating[c]=JO.getInt("rating");
                        driver_phone_number[c]=JO.getString("driver_number");
                        special[c]=JO.getString("specialisation");
                        time[c]=2.5*dist;

                        c++;
                    }

                    count++;
                }
                driver_list_adapter.swapCursor(getApplicationContext(),driver_name,driver_position,cab_no,driver_phone_number,special,rating,time);






            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(JSON_STRING);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {
        String json_string;
            try {
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((json_string=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(json_string+"\n");

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }




    // get ambulances from server
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

                    JSONArray AmbulanceInfo=new JSONArray( json.getString("server response"));
                    int i;
                    for(i=0;i<AmbulanceInfo.length();i=i+1) {

                        JSONObject UserCreintal = AmbulanceInfo.getJSONObject(i);
                        double Ambulance_latitude=UserCreintal.getDouble("latitude");
                        double Ambulance_longitude=UserCreintal.getDouble("longitude");
                        LatLng sydney = new LatLng(Ambulance_latitude,Ambulance_longitude);
                        MarkerOptions options=new MarkerOptions().position(sydney).title("CAB"+i+1).icon(BitmapDescriptorFactory.fromResource(R.mipmap.cab));
                        mMap.addMarker(options);
                    }

                    //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)

            } catch (Exception ex) {
                //Log.d("er",  ex.getMessage());
            }


        }

        protected void onPostExecute(String  result2){


        }




    }


}
