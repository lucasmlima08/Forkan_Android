package com.myllenno.lucas.forkanedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.ads.*;

public class Error extends Activity implements View.OnClickListener {

    private TextView score, word, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.errou);

        word   = (TextView) findViewById(R.id.palavraChaveErrou);
        score  = (TextView) findViewById(R.id.pontuacaoErrou);
        time   = (TextView) findViewById(R.id.tempoErrou);

        Sound.playSoundEffect("errou");
        Sound.unpauseBackgroundMusic();
        inheritAttributes();
        defined();
        updateRecord();

        // Chamar Banner.
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private String categoryActual = "", fileRead = "", wordString = "", timeString = "";
    private int acmScore = 0;

    private void inheritAttributes(){
        Bundle extras 	= getIntent().getExtras();
        categoryActual 	= extras.getString("categoryActual");
        fileRead        = extras.getString("fileRead");
        wordString 	    = extras.getString("word");
        acmScore 	    = extras.getInt("acmScore");
        timeString 	    = extras.getString("timeGame");
    }

    private void defined(){
        word  .setText(wordString);
        score .setText("Pontuação: "+String.valueOf(acmScore));
        time  .setText("Tempo: "+timeString);
    }

    private void updateRecord(){
        try {
            Functions.changeScore(fileRead, acmScore, this);
        } catch (Exception e){
            Functions.messengerIteration("Problemas na escrita do recorde.");
        }
    }

    @Override
    public void onClick(View view) {
        Sound.playSoundEffect("beep");
        Intent intent;
        if (view.getId() == R.id.novaPalavraErrou){
            intent = new Intent(this, Game.class);
            intent.putExtra("infoExtra", true);
            intent.putExtra("categoryActual", categoryActual);
            intent.putExtra("fileRead", fileRead);
            intent.putExtra("acmScore", 0);
            startActivity(intent);
        } else if (view.getId() == R.id.novaCategoriaErrou){
            intent = new Intent(this, Categories.class);
            intent.putExtra("infoExtra", true);
            intent.putExtra("screenPrevious", "Errou");
            startActivity(intent);
        }
        super.finish();
    }
}
