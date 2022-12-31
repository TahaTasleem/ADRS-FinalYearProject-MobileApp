package com.example.accidentdetectionapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileUser extends Fragment {
    public String id,token,userName,firstName,lastName,Address,Nic,CellNo,VehicleNo,VehicleType;
    public static int i;
    EditText username,firstname,lastname,address,nic,cell,vehicleno;
    Spinner vehicletype;Button update;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public String getUrl= "http://192.168.18.6:3000/api/rider/specific/";
    public String putUrl= "http://192.168.18.6:3000/api/rider/update/";
    private final OkHttpClient client = new OkHttpClient();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileUser.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileUser newInstance(String param1, String param2) {
        ProfileUser fragment = new ProfileUser();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        id = getArguments().getString("id");
        token = getArguments().getString("token");
        View view =  inflater.inflate(R.layout.fragment_profile_user, container, false);
        username = view.findViewById(R.id.username);
        firstname = view.findViewById(R.id.firstname);
        lastname = view.findViewById(R.id.lastname);
        address = view.findViewById(R.id.address);
        nic = view.findViewById(R.id.nic);
        cell = view.findViewById(R.id.cellno);
        vehicleno = view.findViewById(R.id.vehicleno);
        vehicletype = view.findViewById(R.id.vehicletype);
        update = view.findViewById(R.id.updatebtn);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.vehicleType, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        vehicletype.setAdapter(adapter);
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
                    JSONObject json2 = json.getJSONObject("credentials");
                    userName = json2.getString("userName");
                    firstName = json.getString("firstName");
                    lastName = json.getString("lastName");
                    Address = json.getString("address");
                    Nic = json.getString("NIC");
                    CellNo = json.getString("cell");
                    VehicleNo = json.getString("vehicleRegistrationNumber");
                    VehicleType = json.getString("vehicleType");
                    if (VehicleType == "commercial") i=0;
                    else if(VehicleType == "private") i=1;
                    setText(username,userName);
                    setText(firstname,firstName);
                    setText(lastname,lastName);
                    setText(address,Address);
                    setText(nic,Nic);
                    setText(cell,CellNo);
                    setText(vehicleno,VehicleNo);
                    setSpinner(vehicletype,i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObject1 = new JSONObject();
                try {
                    jsonObject1.put("firstName",firstname.getText().toString());
                    jsonObject1.put("lastName",lastname.getText().toString());
                    jsonObject1.put("address",address.getText().toString());
                    jsonObject1.put("vehicleRegistrationNumber",vehicleno.getText().toString());
                    jsonObject1.put("NIC",nic.getText().toString());
                    jsonObject1.put("cell",cell.getText().toString());
                    jsonObject1.put("vehicleType",vehicletype.getSelectedItem().toString());
                    jsonObject.put("updates",jsonObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));
                okhttp3.Request request = new Request.Builder().header("Cookie", "token="+token).url(putUrl+id).put(body).build();
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
    private void setSpinner(final Spinner text,final int value) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setSelection(1);
            }
        });
    }
}