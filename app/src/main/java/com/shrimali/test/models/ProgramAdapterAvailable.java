package com.shrimali.test.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shrimali.test.DisplayDetailsActivity;
import com.shrimali.test.R;
import com.shrimali.test.SubmitDetailsActivity;

import java.util.ArrayList;

public class ProgramAdapterAvailable extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> shedStatus;
    ArrayList<String> shedName;
    ArrayList<String> arriveTime;

    public ProgramAdapterAvailable(Context context, ArrayList<String> shedStatus, ArrayList<String> shedName,ArrayList<String> arriveTime) {
        super(context, R.layout.activity_fuel_available_card, R.id.shed_av_status, shedStatus);
        this.context = context;
        this.shedStatus = shedStatus;
        this.shedName = shedName;
        this.arriveTime = arriveTime;
    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View singleItem = convertView;
        ProgramViewHolderAvailable holder = null;

        if (singleItem == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
            singleItem = layoutInflater.inflate(R.layout.activity_fuel_available_card, parent, false);
            holder = new ProgramViewHolderAvailable(singleItem);
            singleItem.setTag(holder);
        } else {
            holder = (ProgramViewHolderAvailable) singleItem.getTag();
        }
        holder.shedNameAvailable.setText(shedName.get(position));
        System.out.println(shedName.get(position));
        holder.shedIdAvailable.setText(shedStatus.get(position));


        holder.viewAv.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), DisplayDetailsActivity.class);
                        intent.putExtra("name",shedName.get(position));
                        intent.putExtra("id",shedStatus.get(position));
                        intent.putExtra("arrivalTime",arriveTime.get(position));
                        context.startActivity(intent);

                        Toast.makeText(context, shedName.get(position).toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        return singleItem;

    }
}
