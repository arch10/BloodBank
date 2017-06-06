package com.gigaworks.bloodbankbeta;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Arch on 15-04-2017.
 */

public class HistoryAdaptor extends RecyclerView.Adapter<HistoryAdaptor.HistoryViewHolder> {

    private ArrayList<HelpHistory> mHelpHistory;
    private Context context;

    public HistoryAdaptor(ArrayList<HelpHistory> mHelpHistory, Context ctx) {
        this.mHelpHistory = mHelpHistory;
        context=ctx;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_history,parent,false);
        HistoryViewHolder holder=new HistoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryAdaptor.HistoryViewHolder holder, int position) {
        HelpHistory helpHistory=mHelpHistory.get(position);
        String name=helpHistory.getName();
        holder.title.setText("You helped "+name);
        holder.date.setText("on      "+helpHistory.getDate());
    }

    @Override
    public int getItemCount() {
        return mHelpHistory.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder{
        TextView title,date;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.tv_itemHistory_title);
            date=(TextView)itemView.findViewById(R.id.tv_itemHistory_date);
        }
    }
}
