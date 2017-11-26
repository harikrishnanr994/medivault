package com.carehack.medivault;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Bineesh on 25/11/2017.
 */

public class PendingFragment extends Fragment {
    private RecyclerView pendingRV;
    private PendingRVAdapter pendingRVAdapter;
    private ArrayList<String> pending;

    private DatabaseReference mRef;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    ProgressBar progressBar;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        pending=new ArrayList<>();

        return inflater.inflate(R.layout.fragment_pending, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utils.pref, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mRef = FirebaseDatabase.getInstance().getReference();
        pendingRV=view.findViewById(R.id.recyclerview);

        progressBar =  view.findViewById(R.id.progressbar);
        textView = view.findViewById(R.id.textview);
        pendingRVAdapter=new PendingRVAdapter(pending);
        pendingRV.setLayoutManager(new LinearLayoutManager(getContext()));
        pendingRV.setAdapter(pendingRVAdapter);
        loadData();
    }

    private void loadData() {
        String phone = sharedPreferences.getString("phone","");
        mRef.child("Pending Reports").child("By Phone").child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    String lab_name = postSnapshot.child("Lab Name").getValue(String.class);
                    pending.add(lab_name);
                }
                progressBar.setVisibility(View.GONE);
                if(pending.size()!=0)
                {
                    pendingRV.setVisibility(View.VISIBLE);
                    pendingRVAdapter.notifyDataSetChanged();
                }
                else
                {
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
