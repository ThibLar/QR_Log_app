package com.example.qrcodeapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChoisirGroupe extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    static String mPromoChoisie;
    static String mGroupeChoisi;
    static String[] mListeTD;
    static TextView mtextView;
    static public ArrayList<String> listeG;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_groupe);

        // affichage
        Button mButtonp12 = (Button) findViewById(R.id.groupe_choisi);
        //Spinner mSpinnerG = (Spinner) findViewById(R.id.spinnergroupe);
        Spinner mSpinnerP = (Spinner) findViewById(R.id.spinnerpromo);
        mtextView = (TextView)findViewById(R.id.nb);

        //creation de la liste des Promotions
        String [] listePromo;
        try {
            //listePromo = choixPromo();
            listePromo = choixPromoBis();

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
                Intent lanceActivityIntent = new Intent(ChoisirGroupe.this, Scanner.class);
                startActivity(lanceActivityIntent);
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
         //initialie la liste des personnes du groupe ainsi que la liste des personnes présentes
        listeG = new ArrayList<String>();
        Scanner.listeP=new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("promotion/"+ mPromoChoisie), "UTF-8"));
            // do reading, usually loop until end of file reading
            String mLine;
            int cpt=0;
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
                        listeG.add(email);
                    }

                    else
                    {
                        if(mGroupeChoisi=="CM")
                        {
                            listeG.add(email);
                        }
                    }
                }
                cpt++;
            }
        } catch (IOException e)
        {
            //log the exception
            //mtextView.setText("bug dans lecture fichier");
        } finally
        {
            if (reader != null)
            {try { reader.close();} catch (IOException e){}  }
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
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        //On recupere la valeur du string
        //mtextView.setText(item);
        if(item.charAt(0)=='T' || item.charAt(0)=='C' )
        {
            mGroupeChoisi=item;
            creationGroupeBis();
            //creationGroupe();
            mtextView.setText(Integer.toString(listeG.size())+" étudiants");
        }
        if(item.charAt(0)=='E')
        {
            mPromoChoisie=item;
            //choixGroupe();
            choixGroupeBis();
            gestionSpinner2();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // TODO Auto-generated method stub
    }

    private String [] choixPromoBis() throws IOException
    {
        //https://stackoverflow.com/questions/8646984/how-to-list-files-in-an-android-directory
        String path = Environment.getExternalStorageDirectory().toString().concat("/").concat(Environment.DIRECTORY_DOCUMENTS).concat("/QR_LOG/Promotions");
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);

        String[] fichiers =  new String[files.length];
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
            fichiers[i]=files[i].getName();
        }
        return fichiers;
    }


    private void choixGroupeBis()
    {
        //prend en entré le nom du fichier de la promotion dans assets, et retourne une ArrayList contenant les groupes de TD possibles
        List<String> liste= new ArrayList<String>();
        liste.add("CM");
        BufferedReader reader = null;
        try {

            //On créée le chemin e  STring puis on le convertit
            String path = Environment.getExternalStorageDirectory().toString().concat("/").concat(Environment.DIRECTORY_DOCUMENTS).concat("/QR_LOG/Promotions/").concat(mPromoChoisie);
            //on créée le reader (il permet de lire un doc ligne par ligne).
            //Voir https://www.baeldung.com/convert-string-to-input-stream pour ouvrir le fichier à partir du chemin
            reader = new BufferedReader(new FileReader(path));

            // do reading, usually loop until end of file reading
            String mLine;
            int cpt=0;
            while ((mLine = reader.readLine()) != null) {
                //process line
                //récupération des groupes TD
                String[] donnees = mLine.split(";");
                int n = donnees.length;
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

    private void creationGroupeBis()
    {
        //prend en entré le nom du fichier de la promotion dans assets, et le nom du groupe (Cm, TD A, TD B,...)
        // et enregistre un fichier contenant les personnes devant assister au cours
        //initialie la liste des personnes du groupe ainsi que la liste des personnes présentes
        listeG = new ArrayList<String>();
        Scanner.listeP=new ArrayList<String>();


        BufferedReader reader = null;
        try {
            String path = Environment.getExternalStorageDirectory().toString().concat("/").concat(Environment.DIRECTORY_DOCUMENTS).concat("/QR_LOG/Promotions/").concat(mPromoChoisie);
            reader = new BufferedReader(new FileReader(path));
            // do reading, usually loop until end of file reading
            String mLine;
            int cpt=0;
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
                        listeG.add(email);
                    }

                    else
                    {
                        if(mGroupeChoisi=="CM")
                        {
                            listeG.add(email);
                        }
                    }
                }
                cpt++;
            }
        } catch (IOException e)
        {
            //log the exception
            //mtextView.setText("bug dans lecture fichier");
        } finally
        {
            if (reader != null)
            {try { reader.close();} catch (IOException e){}  }
        }
    }

}