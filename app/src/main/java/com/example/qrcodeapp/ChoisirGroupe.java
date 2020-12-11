package com.example.qrcodeapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ChoisirGroupe extends AppCompatActivity {

    //a handle to the application's resources
    //a string to output the contents of the files to LogCat
    private String output;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_groupe);
        Button mButtonp12 = (Button) findViewById(R.id.groupe_choisi);

        mButtonp12.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent lanceActivityIntent = new Intent(ChoisirGroupe.this, Scanner.class);
                startActivity(lanceActivityIntent);
            }
        });

        // data/data/com.example.qrcodeapp/files/listelecture.csv
        //get the file as a stream
        String fileName = "Liste_ECAM_4E.txt";
        resources = getResources();
        //Load the file from assets folder - don't forget to INCLUDE the extension
        //output = LoadFile(fileName, false);
        try {

            String ligne_lue;
            String cvsSplitBy = ",";
            String[] champ1;
            //Create a InputStream to read the file into
            InputStream iS;
            //create a buffer that has the same size as the InputStream
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(getApplicationContext().getAssets().open(fileName)));
            while ((ligne_lue = reader.readLine()) != null) {
                champ1 = ligne_lue.split(cvsSplitBy);
                }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}