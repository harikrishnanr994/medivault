package com.carehack.medivault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

    MyTextView edit_profile_text,name_txt,dob_txt,height_txt,weight_txt;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        edit_profile_text = v.findViewById(R.id.edit_profile_text);
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
        edit_profile_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ProfileDetails.class));
            }
        });

        return v;
    }
}
