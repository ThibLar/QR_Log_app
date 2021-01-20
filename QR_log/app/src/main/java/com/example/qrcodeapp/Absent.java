package com.example.qrcodeapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


public class Absent extends AppCompatActivity {

    public static ArrayList<String> listeA;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent);

        Button mButtonp32 = (Button) findViewById(R.id.rescanner);
        mButtonp32.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent lanceActivityIntent = new Intent(Absent.this, Scanner.class);
                startActivity(lanceActivityIntent);
            }
        });

        Button mButtonSave = (Button) findViewById(R.id.enregistrer);
        mButtonSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                enregistreListeA();
            }
        });


        majListeA();
        TextView absent= (TextView)findViewById(R.id.Aff);
        absent.setText(creationAff());


    }


    public String creationAff()//Créée un STring contenant les absents
    {
        String str="";
        for(int i=0; i<listeA.size(); i++)
        {
            String e=listeA.get(i);
            String s = e.split("@")[0];
            //recupere le prenom et met la premiere lettre en maj
            String prenom = s.split(Pattern.quote("."))[0];
            prenom=prenom.substring(0, 1).toUpperCase().concat(prenom.substring(1));
            //recupere le nom et met la premiere lettre en maj
            String nom=s.split(Pattern.quote("."))[1];
            nom=nom.substring(0, 1).toUpperCase().concat(nom.substring(1));
            //ajoute le nom et le prenom dans le texte
            String prenomNom = prenom.concat(" ".concat(nom));
            str=str.concat(prenomNom+"\n");
        }
        if(str.length()==0)
        {
            str="Aucun absent";
        }
        return str;
    }




    public void majListeA()
    {
        listeA=new ArrayList<String>();
        ArrayList<String> listeG=ChoisirGroupe.listeG;
        ArrayList<String> listeP=Scanner.listeP;


        for(int i=0; i<listeG.size();i++)
        {
            String nom=listeG.get(i);
            if(rechercheStr(nom,listeP)==false)
            {
                listeA.add(nom);
            }
        }
    }


    public boolean rechercheStr(String str, ArrayList<String> L)
    //Renvoie True si trouve un string dans une ArrayList
    {
        if(L.size()==0){return false;}
        boolean rep= false;
        for(int i=0; i<L.size();i++)
        {
            if(str.equals(L.get(i))) {rep=true;}
        }
        return rep;
    }

    private void enregistreListeA()
    {
        //enregistre la liste des absents sous le format csv dans STockage Interne> Documents> QR_LOG> Absents et au nom "ECAM<n° promo><{E,A}> <TD ou CM>_<date>
        String path = Environment.getExternalStorageDirectory().toString().concat("/").concat(Environment.DIRECTORY_DOCUMENTS).concat("/QR_LOG/Absences/");
        try
        {

            String nomFichier=ChoisirGroupe.mPromoChoisie.split(Pattern.quote("."))[0].concat("_").concat(ChoisirGroupe.mGroupeChoisi).concat("_").concat(aujourdhui()).concat(".txt");
            nomFichier=nomFichier.replace(" ","").replace("-","_");
            File file = new File(path.concat(nomFichier));

            // créer le fichier s'il n'existe pas
            if (!file.exists()) {
                file.createNewFile();
            }
            //écrit dans le fichier l'adresse email des absents
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i=0; i<listeA.size();i++)
            {
                bw.write(listeA.get(i));
                bw.newLine();
            }
            bw.close();
            Toast.makeText(this,"Enregistré", Toast.LENGTH_LONG).show();
        } catch (IOException e) {e.printStackTrace();}
    }

    public String aujourdhui() {
        //retourne la date du jour
        //https://www.developpez.net/forums/d1536724/java/general-java/obtenir-date-jour-format-jour-mois-annee-heure/
        final Date date = new Date();
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }



}