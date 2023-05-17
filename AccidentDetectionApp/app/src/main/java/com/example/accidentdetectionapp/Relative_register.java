package com.example.accidentdetectionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Relative_register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String token,id,type;
    EditText firstname,lastname,cellno,alternativecellno;
    Button register;
    Spinner relativeType;
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
//        relation = findViewById(R.id.relation);
        relativeType=findViewById(R.id.relativeType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.relativeType, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        relativeType.setAdapter(adapter);
        relativeType.setOnItemSelectedListener(this);
        register = findViewById(R.id.register);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String Firstname = firstname.getText().toString();
                String Lastname = lastname.getText().toString();
//                String rel = relation.getText().toString();
                String Phone = cellno.getText().toString();
                if (Firstname.isEmpty()) {
                    firstname.setError("Firstname field cannot be empty!");
                    return;
                }

                if (Lastname.isEmpty()) {
                    lastname.setError("Lastname field cannot be empty!");
                    return;
                }

                if(Phone.isEmpty()){
                    cellno.setError("Phone No field cannot be empty!");
                    return;
                }
                try {
                    jsonObject.put("firstName",firstname.getText().toString());
                    jsonObject.put("lastName",lastname.getText().toString());
                    jsonObject.put("cell",cellno.getText().toString());
                    jsonObject.put("alternativeCell",alternativecellno.getText().toString());
                    jsonObject.put("relation",relativeType.getSelectedItem().toString());

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
                            finish();

                    }

                });

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

