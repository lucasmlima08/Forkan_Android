package com.myllenno.lucas.forkanedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.ads.*;

public class Hit extends Activity implements View.OnClickListener {

    private TextView word, score, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acertou);

        word    = (TextView) findViewById(R.id.palavraChaveAcertou);
        score   = (TextView) findViewById(R.id.pontuacaoAcertou);
        time    = (TextView) findViewById(R.id.tempoAcertou);

        Sound.playSoundEffect("acertou");
        herancaDeAtributos();
        definir();

        // Chamar Banner.
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private String categoryActual = "", fileRead = "", wordString = "", timeString = "";
    private int acmScore = 0;

    private void herancaDeAtributos(){
        Bundle extras   = getIntent().getExtras();
        categoryActual  = extras.getString("categoryActual");
        fileRead        = extras.getString("fileRead");
        wordString      = extras.getString("word");
        acmScore        = extras.getInt("acmScore");
        timeString      = extras.getString("timeGame");
    }

    private void definir(){
        word.setText(wordString);
        score.setText("Pontuação: "+String.valueOf(acmScore));
        time.setText("Tempo: "+timeString);
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
        if (view.getId() == R.id.novaPalavraAcertou){
            intent = new Intent(this, Game.class);
            intent.putExtra("infoExtra", true);
            intent.putExtra("screenPrevious", "Acertou");
            intent.putExtra("categoryActual", categoryActual);
            intent.putExtra("fileRead", fileRead);
            intent.putExtra("acmScore", acmScore);
            startActivity(intent);
        } else if (view.getId() == R.id.newCategorySucced){
            intent = new Intent(this, Categories.class);
            intent.putExtra("infoExtra", true);
            intent.putExtra("acmScore", acmScore);
            updateRecord();
            startActivity(intent);
        } else {
            updateRecord();
        }
        super.finish();
    }
}
