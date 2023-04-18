package com.example.accidentdetectionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity  {
    private DrawerLayout drawerLayout;
   /* DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;*/
    public String id,token;
    List<String> usernames = new ArrayList<String>();
    List<String> ids = new ArrayList<String>();
    public String url;
    private String getUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url =getResources().getString(R.string.my_url);
        getUrl = url + "api/rider/";

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        id = intent.getStringExtra("id");
        /*try {
            run(token);
            Log.i("Welcome","welcome");
            Log.v("token",token);
            Log.v("name",username);

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("warn","warn");
            Log.v("token",token);
        }*/
//        for (int i=0;i<usernames.size();i++){
//            Log.i("running","runn");
//            if(usernames.get(i) == username){
//                id=ids.get(i);
//                Log.i("id",id+69);
//            }}

        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawerlayout);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id1 = menuItem.getItemId();
                menuItem.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("token",token);
                switch (id1){
                    case R.id.user_profile:
                        ProfileUser obj = new ProfileUser();
                        obj.setArguments(bundle);
                        replaceFragement(obj);
                        break;
                    case R.id.home:
                        replaceFragement(new Home());
                        break;
                    case R.id.relatives:
                        //relative_info obj2 = new relative_info();
                        relatives obj2 = new relatives();
//                        Timer obj2  = new Timer();
                        obj2.setArguments(bundle);
                        replaceFragement(obj2);
                        break;
                    case R.id.logout:
                        logoutMenu(MainActivity.this);
                        break;
                }
                return true;
            }
        });
        /*drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigationopen,R.string.navigationclose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();*/

    }
    private void logoutMenu(MainActivity mainActivity){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

   /* private final OkHttpClient client = new OkHttpClient();
    public void run(String token) throws Exception {
        Request request = new Request.Builder().header("Cookie", "token="+token)
                .url(getUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    JSONObject json = new JSONObject(responseBody.string());
                    JSONArray jsonarray = json.getJSONArray("message");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        JSONObject json2 = jsonobject.getJSONObject("credentials");
                        String username1 = json2.getString("userName");
                        String id2 = jsonobject.getString("_id");
                        if(username1 == username){
                            Log.v("match",username1+69);
                            id = id2;
                        }
                        //usernames.add(username);ids.add(id2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/
    private void replaceFragement(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();
    }
/*
    public void getRiderData(String id,String token) throws Exception {
        Request request = new Request.Builder().header("Cookie", "token="+token)
                .url( "http://192.168.18.6:3000/api/rider/specific/"+id)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    JSONObject json = new JSONObject(responseBody.string());
                    String firstName = json.getString("firstName");
                    String lastName = json.getString("lastName");
                    String address = json.getString("address");
                    String vehicleRegistrationNumber = json.getString("vehicleRegistrationNumber");
                    String nic = json.getString("NIC");
                    String cell = json.getString("cell");
                    String vehicleType = json.getString("vehicleType");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

   */
}

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