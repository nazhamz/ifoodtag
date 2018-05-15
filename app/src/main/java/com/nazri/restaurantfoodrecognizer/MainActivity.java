package com.nazri.restaurantfoodrecognizer;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nazri.restaurantfoodrecognizer.helpers.NetworkHelper;
import com.nazri.restaurantfoodrecognizer.helpers.RequestPackage;
import com.nazri.restaurantfoodrecognizer.models.Predictions;
import com.nazri.restaurantfoodrecognizer.models.IrisData;
import com.nazri.restaurantfoodrecognizer.services.IrisService;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_CLICK_REQUEST_CODE = 3;
    private boolean networkOn;

    TextView resultTV;
    ImageButton photoButton, btnGoHome;
    CropImageView image;
    Bitmap bitmap;
    Drawable d;
    Button recognition;
    Predictions food_result;
    LocalBroadcastManager broadcastManager;


    public final String IMAGE = "image";
    public static final String TAG = "IRIS_LOGGER";
    //TODO
    private final String ENDPOINT = "https://southcentralus.api.cognitive.microsoft.com/customvision/v1.1/Prediction/7225b3c9-c844-43b4-9a37-cd869aed45ef/%s?iterationId=f037977e-ea02-477d-9f17-c1ad16d278c7";
    public static final String IRIS_REQUEST = "IRIS_REQUEST";
    private Activity thisActivity;


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            resultTV.setVisibility(View.GONE);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private BroadcastReceiver irisReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (intent.getExtras().containsKey(IrisService.IRIS_SERVICE_ERROR)) {
                        String msg = intent.getStringExtra(IrisService.IRIS_SERVICE_ERROR);
                        resultTV.setText(msg);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    } else if (intent.getExtras().containsKey(IrisService.IRIS_SERVICE_PAYLOAD)) {
                        IrisData irisData = (IrisData) intent
                                .getParcelableExtra(IrisService.IRIS_SERVICE_PAYLOAD);
                        food_result = irisData.getPredictions().get(0);
                        clearText();
                        String msg = String.format("I am %.00f%% confident that this is %s \n", food_result.getProbability() * 100, food_result.getTag());
                        resultTV.append(msg);

                        for (int i = 0; i < irisData.getPredictions().size(); i++) {
                            Log.i(TAG, "onReceive: " + irisData.getPredictions().get(i).getTag());
                        }
                    }
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoButton = (ImageButton) findViewById(R.id.photoButon);
        image = (CropImageView) findViewById(R.id.imageView);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        btnGoHome = (ImageButton) findViewById(R.id.btnGoHome);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHome = new Intent(MainActivity.this,Home.class);
                startActivity(goHome);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        thisActivity = this;

        broadcastManager = LocalBroadcastManager.getInstance(this);
        networkOn = NetworkHelper.hasNetworkAccess(this);

        image = (CropImageView) findViewById(R.id.imageView);
        try {
            d = Drawable.createFromStream(getAssets().open("example.jpg"), null);
            image.setImageBitmap(((BitmapDrawable) d).getBitmap());
            bitmap = ((BitmapDrawable) d).getBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultTV = (TextView) findViewById(R.id.resultText);
        photoButton = (ImageButton) findViewById(R.id.photoButon);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(thisActivity,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_CLICK_REQUEST_CODE);
                } else {
                    resultTV.setVisibility(View.GONE);
                    takePhoto();
                }

            }
        });


        recognition = (Button) findViewById(R.id.start_recognition);
        recognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCrop(v);
            }
        });

        broadcastManager.registerReceiver(irisReceiver, new IntentFilter(IrisService.IRIS_SERVICE_NAME));
    }


    private void clearText() {
        resultTV.setText("");
    }

    private void progressLoader() {
        resultTV.setVisibility(View.VISIBLE);
        resultTV.setText("Recognizing.. please wait..");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(irisReceiver);
    }

    private void requestIrisService(final String type) {

        final Bitmap croppedImage = image.getCroppedImage();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                RequestPackage requestPackage = new RequestPackage();
                Intent intent = new Intent(MainActivity.this, IrisService.class);
                requestPackage.setParam(IRIS_REQUEST, "IRIS");

                if (type.equals(IMAGE)) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    croppedImage.compress(Bitmap.CompressFormat.PNG,100, stream);
                    byte[] byteArray = stream.toByteArray();
                    Log.d(TAG, "requestIrisService: byte array size = " + byteArray.length);
                    requestPackage.setEndPoint(String.format(ENDPOINT, IMAGE));
                    intent.putExtra(IrisService.REQUEST_IMAGE, byteArray);
                }

                requestPackage.setMethod("POST");
                intent.putExtra(IrisService.REQUEST_PACKAGE, requestPackage);

                try {
                    startService(intent);
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultTV.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Image too large.", Toast.LENGTH_LONG).show();
                        }
                    });

                    e.printStackTrace();
                }
            }
        });


    }

    public void takePhoto() {
        clearText();
        if (networkOn) {
            if (Build.MODEL.contains("x86")) {
                requestIrisService(IMAGE);
                progressLoader();
            } else {
                dispatchTakePictureIntent();
            }
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
            Bundle extras = data.getExtras();
            image.setImageUriAsync(data.getData());
            bitmap = image.getCroppedImage();
            bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CAMERA_CLICK_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            }
        }
    }

    public void getCrop(View v) {
        requestIrisService(IMAGE);
        progressLoader();
    }
}
