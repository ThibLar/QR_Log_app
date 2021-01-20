package com.example.qrcodeapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Scanner extends AppCompatActivity {

    SurfaceView cameraPreview;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    String qr_lu = "";
    TextView compteur;
    float c=0;
    float pourcentage;
    int nb_ligne=0;
    static public ArrayList<String> listeP;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        Button mButton23 = (Button) findViewById(R.id.buttonTerminer);
        mButton23.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent lanceActivityIntent = new Intent(Scanner.this, Absent.class);
                startActivity(lanceActivityIntent);
            }
        });

        Button mButton21 = (Button) findViewById(R.id.buttonRetour);
        mButton21.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent lanceActivityIntent = new Intent(Scanner.this, ChoisirGroupe.class);
                startActivity(lanceActivityIntent);
            }
        });

        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
        txtResult = (TextView) findViewById(R.id.txtResult);
        compteur = (TextView) findViewById(R.id.textPourcent);

        pourcentage = (listeP.size()*100)/ChoisirGroupe.listeG.size();
        compteur.setText("Présence : "+pourcentage+" %");

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(500, 500)//640, 480
                .build();
        //Add Event
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions(Scanner.this,
                            new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections)
            {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0)
                {
                    if (qr_lu.equals(qrcodes.valueAt(0).displayValue)) { // si le code lu est égal au précédent :
                        // on fait rien
                    }
                    else
                        {
                        qr_lu = qrcodes.valueAt(0).displayValue;
                        txtResult.post(new Runnable() {
                            @Override
                            public void run() {
                                //Create vibrate
                           /* Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);*/
                                txtResult.setText(qrcodes.valueAt(0).displayValue);

                                if(!rechercheStr(qrcodes.valueAt(0).displayValue, listeP))
                                {
                                    //Ajoute le nom scanner à la liste des présents
                                    listeP.add(qrcodes.valueAt(0).displayValue);
                                }
                                //affiche le % de présence
                                pourcentage = (listeP.size()*100)/ChoisirGroupe.listeG.size();
                                compteur.setText("Présence : "+pourcentage+" %");

                                MediaPlayer player= MediaPlayer.create(Scanner.this,R.raw.validation);
                                player.start();

                            }
                        });
                    }
                }
            }

        });




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

}
