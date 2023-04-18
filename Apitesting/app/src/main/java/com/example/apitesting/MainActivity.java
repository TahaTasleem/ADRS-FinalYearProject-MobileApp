package com.example.apitesting;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private String sensorList = "[";
    private List<Float> GyroList = new ArrayList<>();
//    public String url1 = "http://127.0.0.1:8000/detectAccident";

    public void callApi() {
//        url1 += sensorList + "]";


        String url1 = "http://192.168.18.6:3000/detectAccident";

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url1).newBuilder();
        urlBuilder.addQueryParameter("input", "[[1,1,1,1,1,1],[2,2,2,2,2,2],[3,3,3,3,3,3]]");

//        String url = urlBuilder.build().toString();
        okhttp3.Request request = new Request.Builder().url(url1).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.i("a","a");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.i("a",response.message());

//        url1 = "http://192.168.18.6:3002/detectAccident/?input=";
//        sensorList = "[";
            }});
            }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> callApi(), 0, 5, TimeUnit.SECONDS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager != null){
//          Getting Sensor
            Sensor acceleroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//          Registering Sensor
            if(acceleroSensor != null){
                sensorManager.registerListener(this, acceleroSensor, SensorManager.SENSOR_DELAY_NORMAL );
            }
            if(gyroscope != null){
                sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL );
            }

        } else {
            Toast.makeText(this, "Sensor service is not detected.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        String AcceleroList = "[";

        if(sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            GyroList.add(sensorEvent.values[0]);
            GyroList.add(sensorEvent.values[1]);
            GyroList.add(sensorEvent.values[2]);
//            ((TextView)findViewById(R.id.txtValues)).setText("Gyroscope x: " + sensorEvent.values[0] + ", y:" + sensorEvent.values[1] + ", z: " + sensorEvent.values[2]);
        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            if(GyroList.isEmpty() == false) {
                AcceleroList += sensorEvent.values[0] + ",";
                AcceleroList += sensorEvent.values[1] + ",";
                AcceleroList += sensorEvent.values[2] + ",";
                AcceleroList += GyroList.get(0) + ",";
                AcceleroList += GyroList.get(1) + ",";
                AcceleroList += GyroList.get(2) + "],";

                GyroList.clear();
                sensorList += AcceleroList;
                //Log.i("Accele", String.valueOf(sensorList));
            }
//            ((TextView)findViewById(R.id.txtValues)).setText("Accelerometer x: " + sensorEvent.values[0] + ", y:" + sensorEvent.values[1] + ", z: " + sensorEvent.values[2]);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}


/*
* package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private List<List<Float>> sensorList = new ArrayList<>();
    private List<Float> GyroList = new ArrayList<>();

    public String url = "http://127.0.0.1:8000/detectAccident/?input=";

    public static void callApi() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> callApi(), 0, 5, TimeUnit.SECONDS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager != null){
//          Getting Sensor
            Sensor acceleroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//          Registering Sensor
            if(acceleroSensor != null){
                sensorManager.registerListener(this, acceleroSensor, SensorManager.SENSOR_DELAY_NORMAL );
            }
            if(gyroscope != null){
                sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL );
            }

        } else {
            Toast.makeText(this, "Sensor service is not detected.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        List<Float> AcceleroList = new ArrayList<>();

        if(sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            GyroList.add(sensorEvent.values[0]);
            GyroList.add(sensorEvent.values[1]);
            GyroList.add(sensorEvent.values[2]);
            ((TextView)findViewById(R.id.txtValues)).setText("Gyroscope x: " + sensorEvent.values[0] + ", y:" + sensorEvent.values[1] + ", z: " + sensorEvent.values[2]);
        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            if(GyroList.isEmpty() == false) {
                AcceleroList.add(sensorEvent.values[0]);
                AcceleroList.add(sensorEvent.values[1]);
                AcceleroList.add(sensorEvent.values[2]);
                AcceleroList.add(GyroList.get(0));
                AcceleroList.add(GyroList.get(1));
                AcceleroList.add(GyroList.get(2));
                GyroList.clear();
                sensorList.add(AcceleroList);
                //Log.i("Accele", String.valueOf(sensorList));
            }
            ((TextView)findViewById(R.id.txtValues)).setText("Accelerometer x: " + sensorEvent.values[0] + ", y:" + sensorEvent.values[1] + ", z: " + sensorEvent.values[2]);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
* */







/*


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url1).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.i("Api Failed","a");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("resp","response is"+response.body().string());
            }
        });
 */