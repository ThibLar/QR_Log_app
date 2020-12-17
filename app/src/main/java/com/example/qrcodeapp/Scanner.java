package com.example.qrcodeapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
        Button buttonEnd = (Button) findViewById(R.id.buttonTerminer);
        buttonEnd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent lanceActivityIntent = new Intent(Scanner.this, Absent.class);
                startActivity(lanceActivityIntent);
            }
        });

        Button buttonRetour = (Button) findViewById(R.id.buttonRetour);
        buttonRetour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent lanceActivityIntent = new Intent(Scanner.this, ChoisirGroupe.class);
                startActivity(lanceActivityIntent);
            }
        });

        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
        txtResult = (TextView) findViewById(R.id.txtResult);
        compteur = (TextView) findViewById(R.id.compteur);


        //String file_in = "listelecture.csv";

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

        //nombre de personne dans le fichier personneattendu
        //lire le fichiers des qr code scanner
        String csvFile = "personnesAttendues.csv";
        BufferedReader buff_r = null;
        String line = "";


        try {

            //String ligne_lue;
            //br = new BufferedReader(new FileReader(csvFile));
            FileInputStream fin = openFileInput(csvFile);
            DataInputStream in = new DataInputStream(fin);
            buff_r = new BufferedReader(new InputStreamReader(in));
            while ((line = buff_r.readLine()) != null)
            {
                nb_ligne++;
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }//fin comptage nb personne presente





        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0) {
                    if (qr_lu.equals(qrcodes.valueAt(0).displayValue)) { // si le code lu est égal au précédent :
                                                     // on fait rien
                        }
                    else {
                        qr_lu = qrcodes.valueAt(0).displayValue;
                        txtResult.post(new Runnable() {
                            @Override
                            public void run() {
                                //Create vibrate
                           /* Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);*/
                                txtResult.setText(qrcodes.valueAt(0).displayValue);



                                try { ///data/data/com.example.qrcodeapp/files/listelecture.csv
                                    String file_out = "listelecture.csv";
                                    FileOutputStream fOut = openFileOutput(file_out, Context.MODE_APPEND);
                                    String str = qrcodes.valueAt(0).displayValue + "\n";
                                    fOut.write(str.getBytes());

                                    /* fini : on ferme le fichier */
                                    fOut.close();
                                    //finish();

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                String csvFile2 = "listelecture.csv";
                                BufferedReader buff_r2 = null;
                                String line_bis = "";


                                try {

                                    //String ligne_lue;
                                    //br = new BufferedReader(new FileReader(csvFile));
                                    FileInputStream fin = openFileInput(csvFile2);
                                    DataInputStream in = new DataInputStream(fin);
                                    buff_r2 = new BufferedReader(new InputStreamReader(in));
                                    while ((line_bis = buff_r2.readLine()) != null)
                                    {
                                        c++;
                                        pourcentage= (c/ nb_ligne)*100;
                                        compteur.setText("Pourcentage de présent = "+pourcentage+" %");
                                    }

                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }//fin comptage nb personne attendues



                            }
                        });
                    }
                }
            }
        });
    }
}
