package com.example.accidentdetectionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    TextView redirect;
    EditText username,password;
    Button login;
    public static int i;
    public String url;
    private String getUrl,postUrl;
    //public String postUrl="http://192.168.18.6:3000/api/rider/login";
    //private String getUrl= "http://192.168.18.6:3000/api/rider/relative/all/";
//    private String getUrl="http://127.0.0.1:8000/detectAccident/?input=[[1,1,1,1,1,1],[2,2,2,2,2,2],[3,3,3,3,3,3]]";
    JSONObject jsonObject = new JSONObject();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        url =getResources().getString(R.string.my_url);
        postUrl= url+"api/rider/login";
        getUrl = url+"api/rider/relative/all/";

        username= findViewById(R.id.username);
        password= findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("msg","msg is "+username.getText().toString());
                try{
                    jsonObject.put("userName",username.getText().toString());
                    jsonObject.put("password",password.getText().toString());
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));
                Request request = new Request.Builder().url(postUrl).post(body).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        call.cancel();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBody= response.body().string();
                        String name = "",token1= "",id = "";
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            JSONObject json2 = json.getJSONObject("validObj");
                            name =json2.getString("firstName");
                            id = json2.getString("_id");
                            token1 = json.getString("token");
                            String finalName = name,finalToken=token1,finalId= id;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Relativeexits(finalToken,finalId);
                                    if(i==1){
                                        Intent intent = new Intent(Login.this,MainActivity.class);
                                        intent.putExtra("token",finalToken);
                                        intent.putExtra("id",finalId);
                                        startActivity(intent);
                                    }
                                    else{
                                        Intent intent = new Intent(Login.this,Relative_register.class);
                                        intent.putExtra("token",finalToken);
                                        intent.putExtra("id",finalId);
                                        startActivity(intent);
                                    }
                                    Toast.makeText(getApplicationContext(),"Welcome "+ finalName,Toast.LENGTH_SHORT).show();                            }
                            });
                            username.getText().clear();
                            password.getText().clear();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"Wrong username or password!",Toast.LENGTH_SHORT).show();                            }
                            });
                        }

                    }
                });
            }
        });

        redirect = findViewById(R.id.signupredirect);
        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });
    }

    public void Relativeexits(String token, String id) {
        Request request = new Request.Builder().header("Cookie", "token="+token).url(getUrl+id).build();
//        Request request = new Request.Builder().url(getUrl).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();Log.i("s",responseBody);
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        JSONArray json2 = json.getJSONArray("message");
                        Log.i("len",json2.length()+" 1");
                        if (json2.length() >= 1) {
                            i =1;
                        } else {
                            i =0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}