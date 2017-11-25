package com.carehack.medivault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.carehack.medivault.custom_font.MyTextView;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Bineesh on 25/11/2017.
 */

public class HomeFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    MyTextView name_txt,dob_txt,height_txt,weight_txt;

    CardView connect,hospital;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        name_txt = v.findViewById(R.id.name);
        dob_txt = v.findViewById(R.id.dob);
        height_txt = v.findViewById(R.id.height);
        weight_txt = v.findViewById(R.id.weight);
        sharedPreferences = getActivity().getSharedPreferences(Utils.pref,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String name = sharedPreferences.getString("name","");
        String dob = sharedPreferences.getString("dob","");
        String height = sharedPreferences.getString("height","");
        String weight = sharedPreferences.getString("weight","");
        String allergies = sharedPreferences.getString("allergies","");
        name_txt.setText(name);
        dob_txt.setText(dob);
        height_txt.setText(height);
        weight_txt.setText(weight);
        connect = v.findViewById(R.id.card_view_connect);
        hospital = v.findViewById(R.id.card_view_hospital);
        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),HospitalViewActivity.class));
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),EmitActivity.class));
            }
        });

        return v;
    }
}
