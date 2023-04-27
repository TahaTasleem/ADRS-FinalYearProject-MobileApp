package com.example.accidentdetectionapp;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UserProfile extends Fragment {
    /*TextView username;
    TextView lastname;
    String firstName="",lastName2="",address="";
    public String getUrl= "http://192.168.18.6:3000/api/rider/specific/";
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        lastname = findViewById(R.id.lastname);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String token = intent.getStringExtra("token");
        Request request = new Request.Builder().header("Cookie", "token="+token)
                .url(getUrl+id)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    JSONObject json = new JSONObject(responseBody.string());
                    firstName = json.getString("firstName");
                    lastName2 = json.getString("lastName");
                    address = json.getString("address");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        username.setText(firstName);
        lastname.setText(lastName2);

    }*/
}
