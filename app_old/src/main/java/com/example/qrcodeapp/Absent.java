package com.example.qrcodeapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Absent extends AppCompatActivity {

    public List<String> listAffiche = new ArrayList<>();
    ListView liste1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent);

        String file_in = "listelecture.csv";
        String personne;
        String cvsSplitBy = ",";


        try {

            String ligne_lue;

            FileInputStream fin = openFileInput(file_in);
            DataInputStream in = new DataInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((ligne_lue = br.readLine()) != null) {
                String[] champ = ligne_lue.split(cvsSplitBy);

                personne =  (champ[2]);
                listAffiche.add(ligne_lue);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listAffiche);
        liste1.setAdapter(adapter);
    }


}