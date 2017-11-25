package com.carehack.medivault;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sachin on 26/11/17.
 */

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.MyViewHolder> {

    private List<DataClass> hospitalList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subtitle, date;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            subtitle = (TextView) view.findViewById(R.id.subtitle);
            date = (TextView) view.findViewById(R.id.date);
        }
    }


    public HospitalAdapter(List<DataClass> hospitalList) {
        this.hospitalList = hospitalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataClass hospital = hospitalList.get(position);
        holder.title.setText(hospital.getTitle());
        holder.subtitle.setText(hospital.getSubtitle());
        holder.date.setText(hospital.getDate());
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }
}