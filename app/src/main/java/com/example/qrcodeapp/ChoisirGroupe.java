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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChoisirGroupe extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_groupe);

        //attibuts affichage
        Button mButtonp12 = (Button) findViewById(R.id.groupe_choisi);
        Spinner mSpinnerG = (Spinner) findViewById(R.id.spinnergroupe);
        Spinner mSpinnerP = (Spinner) findViewById(R.id.spinnerpromo);

        //creation de la liste des Promotions
        String [] listePromo;
        try {
            listePromo = choixPromo();
        } catch (IOException e) {
            e.printStackTrace();
            listePromo= new String[]{"erreur"};
        }
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        mSpinnerP.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the TD list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listePromo);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mSpinnerP.setAdapter(aa);

        //creation de la liste TD
        String[] listeTD = choixGroupe("promotion/ECAM4E.txt").toArray(new String[0]);
        //String[] listeTD ={"A","B","C"};
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        mSpinnerG.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the TD list
        ArrayAdapter aaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listeTD);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mSpinnerG.setAdapter(aaa);




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
        //prend en entré le nom du fichier de la promotion dans assets, et retourne une ArrayList contenant les groupes de TD possibles
        List<String> liste= new ArrayList<String>();
        liste.add("CM");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(nomFichier), "UTF-8"));
            // do reading, usually loop until end of file reading
            String mLine;
            int cpt=0;
            while ((mLine = reader.readLine()) != null) {
                //process line
                //récupération des groupes TD
                String[] donnees = mLine.split(";");
                String TD = donnees[3];
                if(cpt>0){liste.add("TD"+TD);}
                cpt++;
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                    liste.add("erreur");
                }
            }
        }
        return supDoublons(liste);
    }



    private String [] choixPromo() throws IOException
    {
        String[] nomFichier = this.getAssets().list("promotion");
        return nomFichier;
    }


    private List<String> supDoublons(List<String> L)
    {
        Set set = new HashSet() ;
        set.addAll(L) ;
        ArrayList Lbis = new ArrayList(set) ;
        return Lbis;
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