package com.example.accidentdetectionapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class relatives extends Fragment {
    public String id,token,firstName,lastName,relativeid,cell,alternativeCell,relation;
    public List<String> list = new ArrayList<>();
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Button addbtn;
    JSONObject jsonobject;JSONArray json2;
    public String url;
    private String getUrl,delUrl;
    public ListView listView;
//    public ArrayAdapter<List<String>> arrayAdapter;
    public ArrayAdapter<String> arrayAdapter;
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
        url =getResources().getString(R.string.my_url);
        getUrl = url + "api/rider/relative/all/";
        delUrl = url + "api/rider/relative/delete/";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        id = getArguments().getString("id");
        token = getArguments().getString("token");
        View view = inflater.inflate(R.layout.fragment_relatives, container, false);
        addbtn = view.findViewById(R.id.button);
        listView = view.findViewById(R.id.listview);

        Request request = new Request.Builder().header("Cookie", "token=" + token).url(getUrl + id).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    JSONObject json = new JSONObject(responseBody.string());
                    json2 = json.getJSONArray("message");
                    for (int i = 0; i < json2.length(); i++) {
                        jsonobject = json2.getJSONObject(i);
                        Log.i("obj", String.valueOf(jsonobject));
                        relativeid = jsonobject.getString("_id");
                        firstName = jsonobject.getString("firstName");
                        lastName = jsonobject.getString("lastName");
                        cell = jsonobject.getString("cell");
                        alternativeCell = jsonobject.getString("alternativeCell");
                        relation = jsonobject.getString("relation");
                        list.add(firstName);
                        Log.i("l2", String.valueOf(list));
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            } });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        arrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,list);

        arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item_relative, R.id.name, list) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_relative, parent, false);
                }

                TextView nameTextView = convertView.findViewById(R.id.name);
                TextView phoneTextView = convertView.findViewById(R.id.phone);
                ImageView deleteImageView = convertView.findViewById(R.id.delete_button);

                try {
                    JSONObject jsonobject = json2.getJSONObject(position);
                    String firstName = jsonobject.getString("firstName");
                    String lastName = jsonobject.getString("lastName");
                    String cell = jsonobject.getString("cell");
                    String name = firstName + " " + lastName;
                    String rel_id = jsonobject.getString("_id");
                    String alt_cell = jsonobject.getString("alternativeCell");
                    String rel = jsonobject.getString("relation");


                    nameTextView.setText(name);
                    phoneTextView.setText(cell);

                    deleteImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            OkHttpClient client = new OkHttpClient();
                            okhttp3.Request request = new Request.Builder().header("Cookie", "token="+token).url(delUrl+rel_id).delete().build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }
                                @Override public void onResponse(Call call, Response response) throws IOException {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(),"Relative Deleted Successfully!",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }});
                            // Notify the adapter that the data set has changed
                            arrayAdapter.notifyDataSetChanged();
                        }
                    });
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                Bundle bundle = new Bundle();
                                bundle.putString("relativeid",rel_id);
                                bundle.putString("id",id);
                                bundle.putString("token",token);
                                bundle.putString("firstName", firstName);
                                bundle.putString("lastName",lastName);
                                bundle.putString("cell",cell);
                                bundle.putString("alternativeCell",alt_cell);
                                bundle.putString("relation",rel);
                                relativeinfo2 ri = new relativeinfo2();
                                ri.setArguments(bundle);
                                replaceFragement(ri);

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return convertView;
            }
        };

        listView.setAdapter(arrayAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.i("val", (String) adapterView.getItemAtPosition(i));
//                try {
//                    JSONObject jsonobject= json2.getJSONObject(i);
//                    String relativeid2 = jsonobject.getString("_id");
//                    String firstName2 = jsonobject.getString("firstName");
//                    String lastName2 = jsonobject.getString("lastName");
//                    String cell2 = jsonobject.getString("cell");
//                    String alternativeCell2 = jsonobject.getString("alternativeCell");
//                    String relation2 = jsonobject.getString("relation");
//                    Bundle bundle = new Bundle();
//                    bundle.putString("relativeid",relativeid2);
//                    bundle.putString("id",id);
//                    bundle.putString("token",token);
//                    bundle.putString("firstName", firstName2);
//                    bundle.putString("lastName",lastName2);
//                    bundle.putString("cell",cell2);
//                    bundle.putString("alternativeCell",alternativeCell2);
//                    bundle.putString("relation",relation2);
//                    relativeinfo2 ri = new relativeinfo2();
//                    ri.setArguments(bundle);
//                    replaceFragement(ri);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getActivity(),Relative_register.class);
                intent2.putExtra("token",token);
                intent2.putExtra("id",id);
                startActivity(intent2);
            }
        });
        return view;
    }
    private void replaceFragement(Fragment fragment){

//        FragmentManager fragmentManager = getParentFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.framelayout, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();
    }
}
