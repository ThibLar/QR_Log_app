package com.example.qrcodeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Absent extends AppCompatActivity {
    public List<String> listAffiche = new ArrayList<>();
    ListView liste1 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent);

        liste1 = (ListView) findViewById(R.id.afficheur);

        Button mButtonp42 = (Button) findViewById(R.id.rescanner);

        mButtonp42.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent lanceActivityIntent = new Intent(Absent.this, Scanner.class);
                startActivity(lanceActivityIntent);
            }
        });
/*

        ArrayList<String> nomPresents;
        try
        {
            nomPresents = recupChaine("listelecture.csv");
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
*/

        //lire le fichiers des qr code scanner
        String csvFile = "listelecture.csv";
        BufferedReader buff_r = null;
        String line = "";
        String cvsSplitBy = "@";

        try {

            //String ligne_lue;
            //br = new BufferedReader(new FileReader(csvFile));
            FileInputStream fin = openFileInput(csvFile);
            DataInputStream in = new DataInputStream(fin);
            buff_r = new BufferedReader(new InputStreamReader(in));
            while ((line = buff_r.readLine()) != null) {
                String[] name = line.split(cvsSplitBy);

                listAffiche.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listAffiche);
        liste1.setAdapter(adapter);

    }

    protected ArrayList<String> recupChaine(String nomFichier) throws IOException {
        //Récupere les donnees d'un fichier csv de nom nomFichier, et renvoie la liste des éléments sous forme de Tableau de Str
        String ligne_lue;
        FileInputStream fichier = openFileInput(nomFichier);
        DataInputStream in = new DataInputStream(fichier);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        ArrayList<String> liste = new ArrayList<>();
        while ((ligne_lue = br.readLine()) != null)
        {
            String[] chaine = ligne_lue.split(",");
            List<String> tab = Arrays.asList(chaine);

            for(int i=0; i<chaine.length;i++)
            {
                liste.add(tab.get(i));
            }
        }
        br.close();
        return liste;
    }
}