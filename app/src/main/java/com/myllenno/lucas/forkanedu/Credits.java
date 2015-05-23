package com.myllenno.lucas.forkanedu;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.gms.ads.*;

public class Credits extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creditos);

        // Chamar Banner.
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}


