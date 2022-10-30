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

import com.shrimali.test.R;
import com.shrimali.test.SubmitDetailsActivity;

import java.util.ArrayList;

public class ProgramAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> shedId;
    ArrayList<String> shedName;


    public ProgramAdapter(Context context, ArrayList<String> shedId, ArrayList<String> shedName) {
        super(context, R.layout.details_item, R.id.shed_id, shedId);
        this.context = context;
        this.shedId = shedId;
        this.shedName = shedName;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View singleItem = convertView;
        ProgramViewHolder holder = null;

        if (singleItem == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
            singleItem = layoutInflater.inflate(R.layout.details_item, parent, false);
            holder = new ProgramViewHolder(singleItem);
            singleItem.setTag(holder);
        } else {
            holder = (ProgramViewHolder) singleItem.getTag();
        }
        holder.shedName.setText(shedName.get(position));
        System.out.println(shedName.get(position));
        holder.shedId.setText(shedId.get(position));


        holder.view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), SubmitDetailsActivity.class);
                        intent.putExtra("name",shedName.get(position));
                        intent.putExtra("id",shedId.get(position));
                        context.startActivity(intent);

                        Toast.makeText(context, shedName.get(position).toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        return singleItem;

    }
}
