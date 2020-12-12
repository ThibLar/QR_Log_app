package com.example.qrcodeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ChoisirGroupe extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    //attributs
    List<String> mTD = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_groupe);
        //attibuts affichage
        Button mButtonp12 = (Button) findViewById(R.id.groupe_choisi);
        Spinner mSpinnerG = (Spinner) findViewById(R.id.spinnergroupe);


        //creation de la liste TD
        mTD=choixGroupe("promotion/ECAM4E.txt");
        String[] listeTD = mTD.toArray(new String[0]);
        //String[] listeTD ={"A","B","C"};
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        mSpinnerG.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the TD list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listeTD);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mSpinnerG.setAdapter(aa);



        mButtonp12.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent lanceActivityIntent = new Intent(ChoisirGroupe.this, Scanner.class);
                startActivity(lanceActivityIntent);
            }
        });



    }

    private List<String> choixGroupe(String nomFichier)
    {
        List<String> liste= new ArrayList<String>();
        liste.add("CM");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(nomFichier), "UTF-8"));
            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                //récupération des groupes TD
                String[] donnees = mLine.split(";");
                String TD = donnees[3];
                boolean ajouter;
                ajouter = true;
                for (int i = 0; i <liste.size(); i++)
                {
                    String elem= liste.get(i);
                    if (TD == elem || TD == "TD") {ajouter = false;}
                }
                if (ajouter) {liste.add(TD);}
            }
        } catch (IOException e) {
            //log the exception
            liste.add("bug");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return liste;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // TODO Auto-generated method stub
    }
}