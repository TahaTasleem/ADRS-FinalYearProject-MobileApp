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
    EditText username,email, password,firstname,lastname,address,Nic,phone,Vehicleno;
    Button register;
    Spinner Vehicletype;
    public String url;
    private String postUrl;
    JSONObject jsonObject = new JSONObject();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        url =getResources().getString(R.string.my_url);
        postUrl = url + "api/rider/add";

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        firstname=findViewById(R.id.firstname);
        lastname=findViewById(R.id.lastname);
        address=findViewById(R.id.address);
        email = findViewById(R.id.email);
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
                String Username = username.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String Firstname = firstname.getText().toString();
                String Lastname = lastname.getText().toString();
                String Address = address.getText().toString();
                String nic = Nic.getText().toString();
                String Phone = phone.getText().toString();
                String VehicleNo = Vehicleno.getText().toString();

                if (Username.isEmpty()) {
                    username.setError("Username field cannot be empty!");
                    return;
                }
                if(Email.isEmpty()){
                    email.setError("Email field cannot be empty!");
                    return;
                }

                if (Password.isEmpty()) {
                    password.setError("Password field cannot be empty!");
                    return;
                }
                if (Firstname.isEmpty()) {
                    firstname.setError("Firstname field cannot be empty!");
                    return;
                }

                if (Lastname.isEmpty()) {
                    lastname.setError("Lastname field cannot be empty!");
                    return;
                }
                if (Address.isEmpty()) {
                    address.setError("Address field cannot be empty!");
                    return;
                }

                if (nic.isEmpty()) {
                    Nic.setError("NIC field cannot be empty!");
                    return;
                }
                if (Phone.isEmpty()) {
                    phone.setError("Phone No field cannot be empty!");
                    return;
                }

                if (VehicleNo.isEmpty()) {
                    Vehicleno.setError("Vehicle No field cannot be empty!");
                    return;
                }

                try{
                    jsonObject.put("email",email.getText().toString());
                    jsonObject.put("userName",username.getText().toString());
                    jsonObject.put("password",password.getText().toString());
                    jsonObject.put("firstName",firstname.getText().toString());
                    jsonObject.put("lastName",lastname.getText().toString());
                    jsonObject.put("address",address.getText().toString());
                    jsonObject.put("vehicleRegistrationNumber",Vehicleno.getText().toString());
                    jsonObject.put("vehicleType",Vehicletype.getSelectedItem().toString());
                    jsonObject.put("NIC",Nic.getText().toString());
                    jsonObject.put("cell",phone.getText().toString());
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
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Error in Registration. Please Try Again!",Toast.LENGTH_SHORT).show();                           }
                        });

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code()==201){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"Rider Registered Successfully ",Toast.LENGTH_SHORT).show();                            }
                            });
                        }
                        else if(response.code()==500){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"User Already Exists! ",Toast.LENGTH_SHORT).show();                            }
                            });
                        }
                        else{
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"Error in Registration. Please Try Again!",Toast.LENGTH_SHORT).show();                            }
                            });
                        }

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

