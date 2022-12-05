package com.example.accidentdetectionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    String url1 = "http://192.168.18.6:3000/api/rider";
    OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Request request = new Request.Builder().url(url1).build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                e.printStackTrace();
//            }
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (response.isSuccessful()){
//                    String responseBody =response.body().string();
//                    JSONObject json = null;
//                    try {
//                        json = new JSONObject(responseBody);
//                        //get Array of message i.e two users Ali and Taha
//                        JSONArray jsonArray =json.getJSONArray("message");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            //From object 1
//                            JSONObject jsonobject = jsonArray.getJSONObject(i);
//                            //get FirstName that is Taha
//                            String name = jsonobject.getString("firstName");
//                            Log.d("Field", "response" + name);
//                        }
//                        }
//                    catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

    }
}