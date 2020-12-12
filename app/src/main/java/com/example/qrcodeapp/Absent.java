package com.example.qrcodeapp;

import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent);
        ArrayList<String> nomPresents;
        try {
            nomPresents = recupChaine("listelecture.csv");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
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