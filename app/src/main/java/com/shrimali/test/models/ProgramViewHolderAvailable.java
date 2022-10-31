package com.shrimali.test.models;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shrimali.test.R;

public class ProgramViewHolderAvailable {
    TextView shedNameAvailable;
    TextView shedIdAvailable;
    Button viewAv;

    ProgramViewHolderAvailable(View v){
        shedNameAvailable = v.findViewById(R.id.shed_details_av);
        shedIdAvailable = v.findViewById(R.id.shed_av_status);
        viewAv = v.findViewById(R.id.view_btn_av);
//        btnMinus = v.findViewById(R.id.minusBtn);
    }
}
