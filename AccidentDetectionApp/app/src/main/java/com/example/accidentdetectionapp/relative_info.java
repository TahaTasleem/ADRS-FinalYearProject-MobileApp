package com.example.accidentdetectionapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link relative_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class relative_info extends Fragment {
    public String id,token,relativeid;
    public String firstName,lastName,cell,alternativeCell,relation2;
    EditText firstname,lastname,cellno,alternativecellno,relation;
    Button updatebtn,delbtn;
    public String url;
    private String getUrl,putUrl;
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public relative_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment relative_info.
     */
    // TODO: Rename and change types and number of parameters
    public static relative_info newInstance(String param1, String param2) {
        relative_info fragment = new relative_info();
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
        url =getResources().getString(R.string.my_url);
        getUrl = url + "api/rider/relative/all/";
        putUrl = url + "api/rider/relative/update/";
//        delUrl = url + "api/rider/relative/delete/";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        id = getArguments().getString("id");
        token = getArguments().getString("token");
        View view =  inflater.inflate(R.layout.fragment_relative_info, container, false);
        firstname = view.findViewById(R.id.firstname);
        lastname = view.findViewById(R.id.lastname);
        cellno = view.findViewById(R.id.cellno);
        alternativecellno = view.findViewById(R.id.alternativeno);
        relation = view.findViewById(R.id.rel);
        updatebtn = view.findViewById(R.id.updatebtn);
        delbtn = view.findViewById(R.id.delbtn);

        Request request = new Request.Builder().header("Cookie", "token="+token).url(getUrl+id).build();
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    JSONObject json = new JSONObject(responseBody.string());
                    JSONArray json2 = json.getJSONArray("message");
                    for (int i = 0; i < json2.length(); i++) {
                        JSONObject jsonobject = json2.getJSONObject(i);
                        relativeid = jsonobject.getString("_id");
                        firstName = jsonobject.getString("firstName");
                        lastName = jsonobject.getString("lastName");
                        cell = jsonobject.getString("cell");
                        alternativeCell = jsonobject.getString("alternativeCell");
                        relation2 = jsonobject.getString("relation");
                        setText(firstname,firstName);
                        setText(lastname,lastName);
                        setText(cellno,cell);
                        setText(alternativecellno,alternativeCell);
                        setText(relation,relation2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObject1 = new JSONObject();
                try {
                    jsonObject1.put("firstName",firstname.getText().toString());
                    jsonObject1.put("lastName",lastname.getText().toString());
                    jsonObject1.put("cell",cellno.getText().toString());
                    jsonObject1.put("alternativeCell",alternativecellno.getText().toString());
                    jsonObject1.put("relation",relation.getText().toString());
                    jsonObject.put("updates",jsonObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));
                okhttp3.Request request = new Request.Builder().header("Cookie", "token="+token).url(putUrl+relativeid).put(body).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        call.cancel();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),"Data Updated Successfully!",Toast.LENGTH_SHORT).show();                            }
                        });
                        Log.i("Success","response is"+response.body().string());
                    }
                });
            }
        });
//        delbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                OkHttpClient client = new OkHttpClient();
//                okhttp3.Request request = new Request.Builder().header("Cookie", "token="+token).url(delUrl+relativeid).delete().build();
//                client.newCall(request).enqueue(new Callback() {
//                    @Override public void onFailure(Call call, IOException e) {
//                        e.printStackTrace();
//                    }
//                    @Override public void onResponse(Call call, Response response) throws IOException {
//                        new Handler(Looper.getMainLooper()).post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getActivity(),"Relative Deleted Successfully!",Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }});
//            }
//        });

//        addbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent2 = new Intent(getActivity(),Relative_register.class);
//                intent2.putExtra("token",token);
//                intent2.putExtra("id",relativeid);
//                startActivity(intent2);
//            }
//        });
        return view;
    }
    private void setText(final EditText text,final String value) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }
}