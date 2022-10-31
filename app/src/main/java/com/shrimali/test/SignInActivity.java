package com.shrimali.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shrimali.test.models.DBHelper;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    Button signInBtn;
    TextView signUpText;
    EditText signInVehicle, passwordSignInVehicle;
    String userName, password, type;
    DBHelper DB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Objects.requireNonNull(getSupportActionBar()).hide();

        signInBtn = findViewById(R.id.signInBtn);
        signUpText = findViewById(R.id.signUpText);
        signInVehicle = findViewById(R.id.signInVehicle);
        passwordSignInVehicle = findViewById(R.id.passwordSignInVehicle);
        DB = new DBHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = signInVehicle.getText().toString();
                password = passwordSignInVehicle.getText().toString();
                System.out.println(userName + password);
                if (userName.isEmpty() && password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "please fill fully", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUser = DB.checkUserNamePassword(userName, password, "1");
                    if (checkUser) {
                        String type = DB.getTypeOfFuel(userName);
                        myEdit.putString("userName", userName);
                        System.out.println(type);
                        myEdit.putString("fType", type);

                        myEdit.commit();

                        Toast.makeText(SignInActivity.this, "Signing", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignInActivity.this, "UserName or Password wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
}