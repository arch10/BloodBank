package com.gigaworks.bloodbankbeta;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Arch on 18-04-2017.
 */

public class AcceptedAdaptor extends RecyclerView.Adapter<AcceptedAdaptor.AcceptedViewHolder> {
    private ArrayList<HelpHistory> mHelpHistory;

    public AcceptedAdaptor(ArrayList<HelpHistory> helpHistory){
        mHelpHistory=helpHistory;
    }

    @Override
    public AcceptedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_history,parent,false);
        AcceptedViewHolder holder=new AcceptedViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AcceptedViewHolder holder, int position) {
        HelpHistory helpHistory=mHelpHistory.get(position);
        holder.name.setText(helpHistory.getName()+" helped you");
        holder.date.setText("on     "+helpHistory.getDate());
    }

    @Override
    public int getItemCount() {
        return mHelpHistory.size();
    }

    class AcceptedViewHolder extends RecyclerView.ViewHolder{

        TextView name,date;
        public AcceptedViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.tv_itemHistory_title);
            date=(TextView)itemView.findViewById(R.id.tv_itemHistory_date);
        }
    }
}
