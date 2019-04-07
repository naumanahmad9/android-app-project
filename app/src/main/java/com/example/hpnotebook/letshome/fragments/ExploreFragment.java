package com.example.hpnotebook.letshome.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hpnotebook.letshome.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment {

    CardView cvHome;
    FragmentManager manager;
    FragmentTransaction transaction;

    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        cvHome = view.findViewById(R.id.cvHome);

        cvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new HomesFragment());
            }
        });
        return view;
    }

    private void replaceFragment(Fragment fragmentObject) {

        // Second fragment = new Second();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentContainer_main, fragmentObject);
        transaction.commit();
    }
}
