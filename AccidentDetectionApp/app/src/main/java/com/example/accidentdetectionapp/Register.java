package com.example.accidentdetectionapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView login_link;
    EditText username, password,firstname,lastname,address,Nic,phone,Vehicleno;
    Button register;
    Spinner Vehicletype;
    public String postUrl= "http://192.168.18.6:3000/api/rider/add";
    JSONObject jsonObject = new JSONObject();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        firstname=findViewById(R.id.firstname);
        lastname=findViewById(R.id.lastname);
        address=findViewById(R.id.address);
        Nic=findViewById(R.id.nic);
        phone=findViewById(R.id.cellno);
        Vehicleno=findViewById(R.id.vehicleno);
        Vehicletype=findViewById(R.id.vehicletype);
        register=findViewById(R.id.register);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.vehicleType, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
            Vehicletype.setAdapter(adapter);

        Vehicletype.setOnItemSelectedListener(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    jsonObject.put("userName",username.getText().toString());
                    jsonObject.put("password",password.getText().toString());
                    jsonObject.put("firstName",firstname.getText().toString());
                    jsonObject.put("lastName",lastname.getText().toString());
                    jsonObject.put("address",address.getText().toString());
                    jsonObject.put("vehicleRegistrationNumber",Vehicleno.getText().toString());
                    jsonObject.put("vehicleType",Vehicletype.getSelectedItem().toString());
                    jsonObject.put("NIC",Nic.getText().toString());
                    jsonObject.put("cell",phone.getText().toString());
                    Log.i("str","msg "+jsonObject);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));
                okhttp3.Request request = new Request.Builder().url(postUrl).post(body).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        call.cancel();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i("resp","response is"+response.body().string());
                    }
                });
            }
        });



        login_link = findViewById(R.id.login_link);
        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });




    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

