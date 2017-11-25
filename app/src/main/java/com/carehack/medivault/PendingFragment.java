package com.carehack.medivault;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bineesh on 25/11/2017.
 */

public class PendingFragment extends Fragment {
    private RecyclerView pendingRV;
    private PendingRVAdapter pendingRVAdapter;
    private ArrayList<String> pending;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        pending=new ArrayList<>();

        //Dummy data
        pending.add("Riverrun Test Centre");
        pending.add("Aerys Health Centre");
        pending.add("Freddy's Clinic");
        //
        return inflater.inflate(R.layout.fragment_pending, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pendingRV=view.findViewById(R.id.pendingRV);
        //add onDataChange Listener here

        //

        pendingRVAdapter=new PendingRVAdapter(pending);
        pendingRV.setLayoutManager(new LinearLayoutManager(getContext()));
        pendingRV.setAdapter(pendingRVAdapter);


    }
}
