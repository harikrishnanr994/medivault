package com.carehack.medivault;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.carehack.medivault.custom_font.MyTextView;


/**
 * Created by Bineesh on 25/11/2017.
 */

public class HomeFragment extends Fragment {
    MyTextView edit_profile_text;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        edit_profile_text = v.findViewById(R.id.edit_profile_text);
        edit_profile_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ProfileDetails.class));
            }
        });
        return v;
    }
}
