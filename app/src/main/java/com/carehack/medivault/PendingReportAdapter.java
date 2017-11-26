package com.carehack.medivault;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sachin on 26/11/17.
 */

public class PendingReportAdapter extends RecyclerView.Adapter<PendingReportAdapter.MyViewHolder> {

    private List<DataClass> reportList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subtitle, date;
        CardView main_container_card;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            subtitle = (TextView) view.findViewById(R.id.subtitle);
            main_container_card = (CardView) view.findViewById(R.id.main_container_card);
            main_container_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataClass dataClass = reportList.get(getLayoutPosition());
                    Intent intent = new Intent(v.getContext(),AddLabReport.class);
                    intent.putExtra("data",dataClass);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }


    public PendingReportAdapter(List<DataClass> reportList) {
        this.reportList = reportList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_lab_pending_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataClass hospital = reportList.get(position);
        holder.title.setText(hospital.getTitle());
        holder.subtitle.setText(hospital.getSubtitle());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}