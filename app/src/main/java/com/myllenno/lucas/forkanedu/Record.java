package com.myllenno.lucas.forkanedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Record extends Activity {

    LinearLayout layoutScores;
    TextView showCategoryScore;
    private int touchScreen = 6;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recorde);

        try {
            layoutScores = (LinearLayout) findViewById(R.id.layoutRecordes);
            showScore();
        } catch (Exception e){
            Functions.messengerIteration("Problemas na leitura dos recordes.");
            super.finish();
        }

        Toast.makeText(this,"Toque 6 vezes aqui para limpar o recorde.",Toast.LENGTH_LONG).show();
    }

    private void showScore() throws Exception {
        String record[] = Functions.readFileRecord(this);
        ContextThemeWrapper styleButton = new ContextThemeWrapper(this, R.style.styleCategoriesRecord);
        for (int i=0; i<record.length; i++){
            showCategoryScore = new TextView(styleButton);
            showCategoryScore.setText(Functions.categories[i].toUpperCase() + " ¬ " + record[i]);
            layoutScores.addView(showCategoryScore);
        }
    }

    public void eventLayoutRecord(View view){
        // If touch screen 6x, remove file record and restart activity.
        touchScreen--;
        if (touchScreen == 0)
            try{
                Functions.removeFileRecord(this);
                Functions.createFirstFileRecord(this);
                startActivity(new Intent(this, Record.class));
                super.finish();
            } catch (Exception e){
                Functions.messengerIteration("Problemas ao remover os recordes.");
            }
        else
            // Read touch screen.
            if (touchScreen == 1)
                Toast.makeText(this,"Toque mais "+touchScreen+" vez aqui para limpar o recorde.",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"Toque mais "+touchScreen+" vezes aqui para limpar o recorde.",Toast.LENGTH_SHORT).show();
    }
}


