package com.example.qrcodeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



public class Accueil extends AppCompatActivity
{
private Button mButton01;//boutton de  p0 vers p1


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        mButton01 = (Button) findViewById(R.id.buttonAccueil);//lien entre XML et Java
        //Qd on clique sur le bouton de la p0 on va vers la p1
        mButton01.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // The user j
                Intent MenuIntent = new Intent(Accueil.this, ChoisirGroupe.class);
                startActivity(MenuIntent);
            }

        });
    }

}