package com.example.accidentdetectionapp;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_relative_register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_relative_register extends Fragment {
    private String token,id;
    EditText firstname,lastname,cellno,alternativecellno,relation;
    Button register;
    public String url;
    private String postUrl;
    JSONObject jsonObject = new JSONObject();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_relative_register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_relative_register.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_relative_register newInstance(String param1, String param2) {
        fragment_relative_register fragment = new fragment_relative_register();
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
        postUrl = url + "api/rider/relative/add/";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        token = getArguments().getString("token");
        id = getArguments().getString("id");
        View view =  inflater.inflate(R.layout.fragment_relative_register, container, false);
        firstname = view.findViewById(R.id.fn);
        lastname = view.findViewById(R.id.ln);
        cellno = view.findViewById(R.id.cno);
        alternativecellno = view.findViewById(R.id.alternativecno);
        relation = view.findViewById(R.id.rel);

        register = view.findViewById(R.id.reg);
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
                        Toast.makeText(getActivity(),"Please Try Again!",Toast.LENGTH_SHORT).show();                            }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("success","response is"+response.body().string());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Relative Added Successfully!",Toast.LENGTH_SHORT).show();                            }
                });
            }
        });
            }});
        return view;
    }

}