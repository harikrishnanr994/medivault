package com.carehack.medivault;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by user on 11/25/2017.
 */


public class PendingRVAdapter extends RecyclerView.Adapter<PendingRVAdapter.MyViewHolder> {

    private FirebaseDatabase firebase;
    private DatabaseReference databaseReference;
    private ArrayList<String> pending;


    PendingRVAdapter(ArrayList<String> pending)
    {
        this.pending=pending;
    }
    @Override
    public PendingRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pending_card, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PendingRVAdapter.MyViewHolder holder, int position) {
        holder.pendingTV.setText(pending.get(position));
    }

    @Override
    public int getItemCount() {
        return pending.size();
    }
    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView pendingTV;
         MyViewHolder(View itemView) {
            super(itemView);
            pendingTV=itemView.findViewById(R.id.pendingTV);
        }
    }
}

