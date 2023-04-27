package com.example.accidentdetectionapp;

import androidx.fragment.app.Fragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link relatives#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KeyValuePair {
//    public String key;
    public List<String> value;
    public KeyValuePair( List<String> value) {
//        this.key = key;
        this.value = value;
    }
}
