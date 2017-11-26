package com.carehack.medivault;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by user on 11/26/2017.
 */

public class LabReportsRVAdapter extends RecyclerView.Adapter<LabReportsRVAdapter.MyViewHolder> {

    public LabReportsRVAdapter(ArrayList<DataClass> data) {
        this.data = data;
    }

    ArrayList<DataClass> data;
    @Override
    public LabReportsRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_lab_pending_report,parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LabReportsRVAdapter.MyViewHolder holder, int position) {

            holder.labNameTV.setText(data.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView labNameTV;
        public MyViewHolder(View itemView) {
            super(itemView);
            labNameTV=itemView.findViewById(R.id.title);
        }
    }
}
