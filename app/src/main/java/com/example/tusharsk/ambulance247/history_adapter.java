package com.example.tusharsk.ambulance247;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pc on 4/11/2018.
 */

public class history_adapter extends RecyclerView.Adapter<history_adapter.NUMBERVIEWHOLDER>{


    private String[] cab_no_array;
    private String[] destination_array;

    private Context mcontext;
    public history_adapter(Context context)
    {

        this.mcontext=context;



    }




    @Override
    public history_adapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_history,parent,false);
        return new history_adapter.NUMBERVIEWHOLDER(view);

    }

    @Override
    public void onBindViewHolder(history_adapter.NUMBERVIEWHOLDER holder, int position) {

        holder.cab_no.setText(cab_no_array[position]);
        holder.destination.setText(destination_array[position]);



    }

    @Override
    public int getItemCount() {

        if(cab_no_array==null)
            return 0;
        else
            return cab_no_array.length;



    }



    public class NUMBERVIEWHOLDER extends RecyclerView.ViewHolder
    {
        TextView cab_no;
        TextView destination;
        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            cab_no=(TextView)view.findViewById(R.id.cab_no_7);
            destination=(TextView)view.findViewById(R.id.destination_7);




        }



    }
    public void swapCursor(String[] cab,String[] d) {
        // Always close the previous mCursor first

        if (cab != null) {
            // Force the RecyclerView to refresh
            cab_no_array=cab;
            destination_array=d;
            this.notifyDataSetChanged();
        }
    }



}