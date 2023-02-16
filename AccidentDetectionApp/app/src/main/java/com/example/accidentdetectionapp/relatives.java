package com.example.accidentdetectionapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link relatives#newInstance} factory method to
 * create an instance of this fragment.
 */
public class relatives extends Fragment {
    public ListView listView;
    public String id,token,firstName,relativeid;
    public List<String> list = new ArrayList<String>();
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public String getUrl= "http://192.168.18.6:3000/api/rider/relative/all/";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public relatives() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment relatives.
     */
    // TODO: Rename and change types and number of parameters
    public static relatives newInstance(String param1, String param2) {
        relatives fragment = new relatives();
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
        View view =  inflater.inflate(R.layout.fragment_relatives, container, false);
        listView =(ListView) view.findViewById(R.id.listview);
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
                    Log.i("asd",json2.toString());
                    for (int i = 0; i < json2.length(); i++) {
                        JSONObject jsonobject = json2.getJSONObject(i);
                        relativeid = jsonobject.getString("_id");
                        firstName = jsonobject.getString("firstName");
                        list.add("1"+ firstName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("token",token);
                relative_info obj = new relative_info();
                obj.setArguments(bundle);
                replaceFragement(obj);
            }
        });



        return view;
    }
    private void replaceFragement(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();
    }
}
