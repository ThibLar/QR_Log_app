package com.example.qrcodeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Accueil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        Button mButtonp01 = (Button) findViewById(R.id.buttonfini01);

        mButtonp01.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent lanceActivityIntent = new Intent(Accueil.this, ChoisirGroupe.class);
                startActivity(lanceActivityIntent);
            }
        });
    }



}