package com.example.accidentdetectionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Relative_register extends AppCompatActivity {
    private String token,id;
    EditText firstname,lastname,cellno,alternativecellno,relation;
    Button register;
    public String url;
    private String postUrl;
    JSONObject jsonObject = new JSONObject();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_register);

        url =getResources().getString(R.string.my_url);
        postUrl = url + "api/rider/relative/add/";

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        cellno = findViewById(R.id.cellno);
        alternativecellno = findViewById(R.id.alternativecellno);
        relation = findViewById(R.id.relation);

        register = findViewById(R.id.register);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        id = intent.getStringExtra("id");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jsonObject.put("firstName",firstname.getText().toString());
                    jsonObject.put("lastName",lastname.getText().toString());
                    jsonObject.put("cell",cellno.getText().toString());
                    jsonObject.put("alternativeCell",alternativecellno.getText().toString());
                    jsonObject.put("relation",relation.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));
                okhttp3.Request request = new Request.Builder().header("Cookie", "token="+token).url(postUrl+id).post(body).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        call.cancel();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Please Try Again!",Toast.LENGTH_SHORT).show();                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i("success","response is"+response.body().string());
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Relative Added Successfully!",Toast.LENGTH_SHORT).show();                            }
                        });
                        Intent intent2 = new Intent(Relative_register.this,MainActivity.class);
                        intent2.putExtra("token",token);
                        intent2.putExtra("id",id);
                        startActivity(intent2);
                    }
                });

            }
        });
    }
}

