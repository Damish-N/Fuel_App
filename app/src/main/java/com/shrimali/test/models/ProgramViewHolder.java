package com.shrimali.test.models;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shrimali.test.R;

public class ProgramViewHolder {
    TextView shedName;
    TextView shedId;
    Button view;


    ProgramViewHolder(View v){
        shedId = v.findViewById(R.id.shed_id);
        shedName = v.findViewById(R.id.shed_details);
        view = v.findViewById(R.id.view_btn);
//        btnMinus = v.findViewById(R.id.minusBtn);
    }


}
