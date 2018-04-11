package com.example.tusharsk.ambulance247;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by pc on 4/10/2018.
 */

public class driver_list_Adapter extends RecyclerView.Adapter<driver_list_Adapter.NUMBERVIEWHOLDER>{


    String driver_name[];
    String driver_position[];
    String cab_no[];
    String driver_phone_number[];
    String special[];
    int rating[];
    double time[];
    private Context mcontext;

    private driver_list_AdapterOnClickHandler mClickHandler;
    public interface driver_list_AdapterOnClickHandler
    {
        void onClick(int x);
    }
    private Context context;
    public driver_list_Adapter(driver_list_AdapterOnClickHandler clickHandler)
    {

        mClickHandler=clickHandler;

    }




    @Override
    public driver_list_Adapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_driver_list,parent,false);
        return new driver_list_Adapter.NUMBERVIEWHOLDER(view);

    }

    @Override
    public void onBindViewHolder(driver_list_Adapter.NUMBERVIEWHOLDER holder, int position) {



        holder.name.setText(driver_name[position]);
        holder.phone.setText(driver_phone_number[position]);
        holder.cab_n.setText(cab_no[position]);
        holder.cab_p.setText(driver_position[position]);
        holder.spec.setText(special[position]);
        holder.rate.setRating(rating[position]/2);
        holder.rate.setFocusableInTouchMode(false);
        holder.rate.setClickable(false);
        holder.rate.setIsIndicator(true);
        holder.timing.setText(String.format(" %.2f", time[position])+" minutes away");




    }

    @Override
    public int getItemCount() {

        if(cab_no==null)
            return 0;
        else
            return cab_no.length;
    }



    public class NUMBERVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name;
        TextView phone;
        TextView cab_n;
        TextView cab_p;
        TextView  spec;
        TextView timing;
        RatingBar rate;
        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            name=(TextView)view.findViewById(R.id.drivername_3);
            phone=(TextView)view.findViewById(R.id.driverphonenumber_3);
            cab_n=(TextView) view.findViewById(R.id.cab_no_3);
            cab_p=(TextView)view.findViewById(R.id.Position_3);
            spec=(TextView)view.findViewById(R.id.special_3);
            timing=(TextView)view.findViewById(R.id.time_3);
            rate=(RatingBar)view.findViewById(R.id.rating_3);
            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());

        }
    }
    public void swapCursor(Context context,String driver_name_a[],String driver_position_a[], String cab_no_a[],String driver_phone_number_a[],String special_a[],int rating_a[],double time_a[]) {
        // Always close the previous mCursor first

        if (driver_name_a != null) {
            // Force the RecyclerView to refresh
            driver_name=driver_name_a;
            driver_position=driver_position_a;
            cab_no=cab_no_a;
            driver_phone_number=driver_phone_number_a;
            special=special_a;
            rating=rating_a;
            time=time_a;
            this.context=context;
            this.notifyDataSetChanged();
        }
    }







}