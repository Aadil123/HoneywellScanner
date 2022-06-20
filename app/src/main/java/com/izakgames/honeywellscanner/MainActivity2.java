package com.izakgames.honeywellscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.honeywell.barcode.CaptureRequestBuilderListener;
import com.honeywell.barcode.HSMDecodeComponent;
import com.honeywell.barcode.HSMDecodeResult;
import com.honeywell.barcode.HSMDecoder;
import com.honeywell.barcode.Symbology;
import com.honeywell.license.ActivationManager;
import com.honeywell.license.ActivationResult;
import com.honeywell.plugins.decode.DecodeResultListener;

public class MainActivity2 extends AppCompatActivity implements DecodeResultListener {
    private HSMDecoder hsmDecoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button button = findViewById(R.id.button);
        ActivationResult result = ActivationManager.activateEntitlement(this,"entitlement-id");
        Toast.makeText(this,"Activation: "+result,Toast.LENGTH_SHORT).show();
        hsmDecoder = HSMDecoder.getInstance(this.getApplicationContext());
        //set all decoder related settings
        hsmDecoder.enableSymbology(Symbology.UPCA);
        hsmDecoder.enableSymbology(Symbology.CODE128);
        hsmDecoder.enableSymbology(Symbology.CODE39);
        hsmDecoder.enableSymbology(Symbology.QR);
        hsmDecoder.enableFlashOnDecode(false);
        hsmDecoder.enableSound(false);
        hsmDecoder.enableAimer(true);
        hsmDecoder.setAimerColor(Color.RED);
        hsmDecoder.setOverlayText("Place barcode completely inside viewfinder!");
        hsmDecoder.setOverlayTextColor(Color.WHITE);
        hsmDecoder.addResultListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onHSMDecodeResult(HSMDecodeResult[] hsmDecodeResults) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        HSMDecoder.disposeInstance();
    }
}