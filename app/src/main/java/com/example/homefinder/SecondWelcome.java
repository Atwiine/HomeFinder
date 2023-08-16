package com.example.homefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.google.android.material.card.MaterialCardView;

public class SecondWelcome extends AppCompatActivity {


    MaterialCardView hh1,hh2,hh3,hh4,hh5,hh6;
   Spinner division;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_welcome);

        hh1 = findViewById(R.id.hh1);
        hh2 = findViewById(R.id.hh2);
        hh3 = findViewById(R.id.hh3);
        hh4 = findViewById(R.id.hh4);
        hh5 = findViewById(R.id.hh5);
        hh6 = findViewById(R.id.hh6);
        division = findViewById(R.id.division);


        hh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hh1.setCardBackgroundColor(R.color.grey);
//hh1.getResources().getColor(R.color.grey);
                hh1.setBackgroundColor(Color.parseColor("#B3B000"));
            }
        });
        hh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hh1.setCardBackgroundColor(R.color.grey);
//                hh2.getResources().getColor(R.color.grey);
                hh2.setBackgroundColor(Color.parseColor("#B3B000"));
            }
        });
        hh3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hh1.setCardBackgroundColor(R.color.grey);
//                hh3.getResources().getColor(R.color.grey);
                hh3.setBackgroundColor(Color.parseColor("#B3B000"));
            }
        });
        hh4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hh4.setBackgroundColor(Color.parseColor("#B3B000"));
//                hh4.getResources().getColor(R.color.grey);

            }
        });
        hh5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hh1.setCardBackgroundColor(R.color.grey);
//                hh5.getResources().getColor(R.color.grey);
                hh5.setBackgroundColor(Color.parseColor("#B3B000"));
            }
        });
        hh6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hh1.setCardBackgroundColor(R.color.grey);
//                hh6.getResources().getColor(R.color.grey);
                hh6.setBackgroundColor(Color.parseColor("#B3B000"));
            }
        });

    }

    public void sendSearch(View view) {




        startActivity(new Intent(SecondWelcome.this,HouseList.class));
    }
}