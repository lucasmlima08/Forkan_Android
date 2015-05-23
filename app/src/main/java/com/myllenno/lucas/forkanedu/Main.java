package com.myllenno.lucas.forkanedu;

/* Author: Lucas Myllenno S M Lima. 2015 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.*;

public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Defined attributes important.
        Sound.context = this;
        Functions.context = this;
        Functions.assets = getResources().getAssets();
        // Play music background.
        Sound.playBackgroundMusic();
        // Create file records when starting first categoryActual.
        try {
            Functions.createFirstFileRecord(this);
        } catch (Exception e){
            Functions.messengerIteration("Problemas na criação dos recordes.");
        }
        // Chamar Banner.
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        // Stop music background.
        Sound.stopBackgroundMusic();
        Sound.playSoundEffect("efeito_final");
    }

    public void eventosMenuInicial(View view){
        Sound.playSoundEffect("beep");
        // Alternate activities.
        Intent intent;
        if (view.getId() == R.id.novoJogo){
            intent = new Intent(this, Categories.class);
            intent.putExtra("infoExtra", false);
            intent.putExtra("screenPrevious", "Main");
            startActivity(intent);
        } else if (view.getId() == R.id.recorde)
            startActivity(new Intent(this, Record.class));
        else if (view.getId() == R.id.creditos)
            startActivity(new Intent(this, Credits.class));
    }
}
