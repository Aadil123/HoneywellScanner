package com.izakgames.honeywellscanner;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.barcode.CaptureRequestBuilderListener;
import com.honeywell.barcode.HSMDecodeComponent;
import com.honeywell.barcode.HSMDecodeResult;
import com.honeywell.barcode.HSMDecoder;
import com.honeywell.barcode.ImageListener;
import com.honeywell.barcode.Symbology;
import com.honeywell.camera.CameraManager;
import com.honeywell.license.ActivationManager;
import com.honeywell.license.ActivationResult;
import com.honeywell.misc.HSMLog;
import com.honeywell.plugins.decode.DecodeResultListener;

public class MainActivity extends AppCompatActivity implements DecodeResultListener {
    private HSMDecoder hsmDecoder;
    EditText textView;
    ImageView imageView;
    int scanCount;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HSMDecodeComponent hsmDecodeComponent = findViewById(R.id.scannerPreview);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        hsmDecoder = HSMDecoder.getInstance(this.getApplicationContext());
        hsmDecoder.setCaptureRequestBuilderListener(new CaptureRequestBuilderListener() {
            @Override
            public void OnCaptureRequestBuilderAvailable(CaptureRequest.Builder builder) {

            }
        });

    }

    @Override
    public void onHSMDecodeResult(HSMDecodeResult[] hsmDecodeResults) {
        HSMDecodeResult result1 = hsmDecodeResults[0];
        hsmDecoder.enableDecoding(false);
        /*hsmDecoder.captureHiResImage(new ImageListener() {
            @Override
            public void onImageAvailable(Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        Bitmap bmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
                        imageView.setImageBitmap(bmp);
                    }
                });
            }
        });*/
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = hsmDecoder.getLastBarcodeImage(result1.getBarcodeBounds());
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap bmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
                imageView.setImageBitmap(bmp);
            }
        });
        String msg = "Scan count: "+ ++scanCount +"\n"+
                        "Result:\n"+
                        "Data: "+result1.getBarcodeData()+"\n"+
                        "Symbology: "+result1.getSymbology()+"\n"+
                "Length: " + result1.getBarcodeDataLength()  + "\n" +
                "Decode Time: " + result1.getDecodeTime() + "ms";
        textView.setText(msg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        scanCount = 0;
        hsmDecoder.addResultListener(this);
        hsmDecoder.enableDecoding(true);

    }

    @Override
    protected void onStop() {
        super.onStop();
        hsmDecoder.removeResultListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hsmDecoder.setCaptureRequestBuilderListener(null);
        CameraManager.getInstance(getApplicationContext()).closeCamera();
        try {
            this.finish();
        } catch (Exception e) {

        }
    }

}