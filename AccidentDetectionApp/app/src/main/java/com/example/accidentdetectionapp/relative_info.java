package com.example.accidentdetectionapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link relative_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class relative_info extends Fragment {
    public String id,token;
    EditText firstname,lastname,cellno,alternativecellno,relation;
    Button updatebtn;
    public String getUrl= "http://192.168.18.6:3000/api/rider/relative/all/";
    private final OkHttpClient client = new OkHttpClient();

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
                        String firstName = jsonobject.getString("firstName");
                        String lastName = jsonobject.getString("lastName");
                        String cell = jsonobject.getString("cell");
                        String alternativeCell = jsonobject.getString("alternativeCell");
                        String relation2 = jsonobject.getString("relation");
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