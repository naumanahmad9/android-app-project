package com.example.hpnotebook.letshome.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.activities.AboutUsActivity;
import com.example.hpnotebook.letshome.activities.LoginActivity;
import com.example.hpnotebook.letshome.activities.SelectListingTypeActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView becomeHost, goto_aboutus, profile_signout;
    FirebaseAuth auth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        becomeHost = view.findViewById(R.id.profile_becomeHost);
        goto_aboutus = view.findViewById(R.id.goto_aboutus);
        profile_signout = view.findViewById(R.id.profile_signout);
        auth = FirebaseAuth.getInstance();

        becomeHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), SelectListingTypeActivity.class));
            }
        });

        goto_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), AboutUsActivity.class));
            }
        });

        profile_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));

            }
        });

        return view;

    }

}
