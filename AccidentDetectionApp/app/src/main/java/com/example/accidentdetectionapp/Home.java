package com.example.accidentdetectionapp;

import static android.content.Context.SENSOR_SERVICE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements SensorEventListener, LocationListener {
    Button startride;
    private String sensorList = "[";
    private List<Float> GyroList = new ArrayList<>();
    public String url,id,token;
    private String endpointUrl;
    ScheduledExecutorService executor;
    private float[] gravity = new float[3];
    private float[] linearAcceleration = new float[3];
    boolean isVertical = true;
    float speed = 0;
    private LocationManager locationManager;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        url =getResources().getString(R.string.modelapi_url);
        endpointUrl = url +"detectAccident?input=";
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        id = getArguments().getString("id");
        token = getArguments().getString("token");
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        startride = view.findViewById(R.id.startride);
        startride.setText("Start Ride");


        startride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startride.getText().toString()=="Start Ride")
                {
                    executor = Executors.newSingleThreadScheduledExecutor();
                    executor.scheduleAtFixedRate(() -> callApi(), 0, 5, TimeUnit.SECONDS);
                    SensorManager sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
                    if (sensorManager != null){
                        // Getting Sensor
                        Sensor acceleroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                        // Registering Sensor
                        if(acceleroSensor != null){
                            sensorManager.registerListener(Home.this,acceleroSensor, SensorManager.SENSOR_DELAY_NORMAL );
                        }
                        if(gyroscope != null){
                            sensorManager.registerListener(Home.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL );
                        }

                    } else {
                        Toast.makeText(getActivity(), "Sensor service is not detected.", Toast.LENGTH_SHORT).show();
                    }
                    startride.setText("Stop Ride");
                    startride.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.btnred));
                }
                else if (startride.getText().toString()=="Stop Ride"){
                    startride.setText("Start Ride");
                    startride.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.button));
                }
            }
        });
        return view;
    }
    public void onSensorChanged(SensorEvent sensorEvent) {

        String AcceleroList = "[";

        if(sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            GyroList.add(sensorEvent.values[0]);
            GyroList.add(sensorEvent.values[1]);
            GyroList.add(sensorEvent.values[2]);
        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            final float alpha = 1f;
            gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2];
            linearAcceleration[0] = sensorEvent.values[0] - gravity[0];
            linearAcceleration[1] = sensorEvent.values[1] - gravity[1];
            linearAcceleration[2] = sensorEvent.values[2] - gravity[2];

            // Determine the orientation of the device
            if (Math.abs(linearAcceleration[0]) > Math.abs(linearAcceleration[1])) {
//                if (linearAcceleration[0] > 0) {
//                    System.out.println("The device is in landscape mode with the top of the device to the right");
//                } else {
//                    System.out.println("The device is in landscape mode with the top of the device to the left");
//                }
                //Ye landscape ha
                isVertical=false;
            } else {
                isVertical=true;
//                if (linearAcceleration[1] > 0) {
//                    System.out.println("The device is in portrait mode with the top of the device pointing up");
//                } else {
//                    System.out.println("The device is in portrait mode with the top of the device pointing down");
//                }
                //Ye portrait hay
            }
            if(GyroList.isEmpty() == false) {
                AcceleroList += sensorEvent.values[0] + ",";
                AcceleroList += sensorEvent.values[1] + ",";
                AcceleroList += sensorEvent.values[2] + ",";
                AcceleroList += GyroList.get(0) + ",";
                AcceleroList += GyroList.get(1) + ",";
                AcceleroList += GyroList.get(2) + "],";

                GyroList.clear();
                sensorList += AcceleroList;
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public void callApi(){
        endpointUrl += sensorList + "]";
//        // Api should be called from here
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(endpointUrl).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            // called if server is unreachable
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("Failed", "Could not connect to Server");
                        }
                    });
                }catch (Exception f){
                    throw f;
                }

            }

            @Override
            // called if we get a
            // response from the server
            public void onResponse(
                    @NotNull Call call,
                    @NotNull Response response)
                    throws IOException {
                String responseBody= response.body().string();
//                Log.i("url2", responseBody );
                try {
                    JSONObject json = new JSONObject(responseBody);
                    String output = json.getString("Output");
                    Log.i("o",output);
//                    if( isVertical==true || output.equals("no") ){
//                        Log.i("acc","no accident occur");
//                    }
                    if(speed<5.55 || output.equals("no")){
                        Log.i("acc","no accident occur");
                    }
                    else{
                        executor.shutdownNow();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        bundle.putString("token",token);
                        Timer obj = new Timer();
                        obj.setArguments(bundle);
                        replaceFragement(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        endpointUrl = url + "detectAccident?input=";
        sensorList = "[";

    }
    private void replaceFragement(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onLocationChanged(Location location) {
        speed = location.getSpeed(); // get the speed in meters/second
        Log.d("speed", String.format("%.2f m/s", speed));
    }
    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
}

