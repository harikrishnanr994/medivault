package com.carehack.medivault;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bineesh on 26/11/2017.
 */

public class AdapterPrescriptionReport extends RecyclerView.Adapter<AdapterPrescriptionReport.MyViewHolder> {

    private List<DataClass> prescriptpionList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subtitle, date;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            subtitle = view.findViewById(R.id.sub_title);
            date = view.findViewById(R.id.date);
        }
    }


    public AdapterPrescriptionReport(List<DataClass> prescriptpionList) {
        this.prescriptpionList = prescriptpionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prescription_card_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataClass pres = prescriptpionList.get(position);
        holder.title.setText(pres.getTitle());
        holder.subtitle.setText(pres.getSubtitle());
        holder.date.setText(pres.getDate());
    }

    @Override
    public int getItemCount() {
        return prescriptpionList.size();
    }
}