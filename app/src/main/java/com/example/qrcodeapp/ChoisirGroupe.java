package com.example.qrcodeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



public class ChoisirGroupe extends AppCompatActivity
{
    private Button mButton12;//boutton de  p0 vers p1


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_groupe);

        mButton12 = (Button) findViewById(R.id.buttonP12);//lien entre XML et Java
        //Qd on clique sur le bouton de la p0 on va vers la p1
        mButton12.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // The user j
                Intent MenuIntent = new Intent(ChoisirGroupe.this, Scanner.class);
                startActivity(MenuIntent);
            }

        });
    }

}