package com.example.qrcodeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.text.Text;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChoisirGroupe extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    static String mPromoChoisie;
    static String mGroupeChoisi;
    static String mListeEmail;
    static String[] mListeTD;
    static TextView mtextView;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_groupe);

        // affichage
        Button mButtonp12 = (Button) findViewById(R.id.groupe_choisi);
        //Spinner mSpinnerG = (Spinner) findViewById(R.id.spinnergroupe);
        Spinner mSpinnerP = (Spinner) findViewById(R.id.spinnerpromo);
        mtextView = (TextView)findViewById(R.id.Autres);

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




        mButtonp12.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Intent lanceActivityIntent = new Intent(ChoisirGroupe.this, Scanner.class);
                //startActivity(lanceActivityIntent);
            }
        });
    }


    private void choixGroupe()
    {
        //prend en entré le nom du fichier de la promotion dans assets, et retourne une ArrayList contenant les groupes de TD possibles
        List<String> liste= new ArrayList<String>();
        liste.add("CM");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("promotion/".concat(mPromoChoisie)), "UTF-8"));
            // do reading, usually loop until end of file reading
            String mLine;
            int cpt=0;
            while ((mLine = reader.readLine()) != null) {
                //process line
                //récupération des groupes TD
                String[] donnees = mLine.split(";");
                String TD = donnees[3];
                if(cpt>0){liste.add("TD "+TD);}
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
        //creation de la liste des choix TD
        liste=supDoublons(liste);
        Collections.sort(liste);
        mListeTD = liste.toArray(new String[0]);
    }

    private void creationGroupe()
    {
        //prend en entré le nom du fichier de la promotion dans assets, et le nom du groupe (Cm, TD A, TD B,...)
        // et enregistre un fichier contenant les personnes devant assister au cours
         mListeEmail ="";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("promotion/"+ mPromoChoisie), "UTF-8"));
            // do reading, usually loop until end of file reading
            String mLine;
            int cpt=0;
            mListeEmail="";
            while ((mLine = reader.readLine()) != null) {
                //process line
                //récupération des groupes TD
                String[] donnees = mLine.split(";");
                String TD = donnees[3];
                String email = donnees[5];

                if(cpt>0)
                {
                    if(mGroupeChoisi.equals("TD "+TD))
                    {
                        mListeEmail=mListeEmail.concat(email+"\n");
                    }

                    else
                    {
                        if(mGroupeChoisi=="CM")
                        {
                            mListeEmail=mListeEmail.concat(email+"\n");
                        }
                    }
                }
                cpt++;
            }
        } catch (IOException e)
        {
            //log the exception
            //mListeEmail="bug dans lecture fichier";
            //mtextView.setText("bug dans lecture fichier");
        } finally
        {
            if (reader != null)
            {try { reader.close();} catch (IOException e){}  }
        }
        //on souhaite enregistrer le fichier contenant les email des personnes attendues au cours
        try {
            //mtextView.setText(listeEmail);
            String file_out = "personnesAttendues.csv";
            FileOutputStream fOut = openFileOutput(file_out, Context.MODE_PRIVATE);
            fOut.write(mListeEmail.getBytes());
            /* fini : on ferme le fichier */
            fOut.close();
            //finish();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            mtextView.setText("bug enrigestrement fichier");
        }
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

    private void gestionSpinner2()
    {
        Spinner mSpinnerG = (Spinner) findViewById(R.id.spinnergroupe);
        //creation de la liste des choix TD
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        mSpinnerG.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the TD list
        ArrayAdapter aaa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mListeTD);
        aaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mSpinnerG.setAdapter(aaa);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        //On recupere la valeur du string
        //mtextView.setText(item);
        if(item.charAt(0)=='T' || item.charAt(0)=='C' )
        {
            mGroupeChoisi=item;
            creationGroupe();
            mtextView.setText(mListeEmail);
        }
        if(item.charAt(0)=='E')
        {
            mPromoChoisie=item;
            choixGroupe();
            gestionSpinner2();
        }




    }


    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // TODO Auto-generated method stub
    }
}