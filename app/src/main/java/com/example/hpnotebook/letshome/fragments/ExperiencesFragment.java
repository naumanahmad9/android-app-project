package com.example.hpnotebook.letshome.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hpnotebook.letshome.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExperiencesFragment extends Fragment {


    public ExperiencesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_experiences, container, false);
    }

}
