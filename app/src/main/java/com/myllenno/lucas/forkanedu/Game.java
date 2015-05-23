package com.myllenno.lucas.forkanedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Game extends Activity {

    // ----------------------------------------------------------------------- //

    private String categoryActualString = "", fileReadString = "", wordString = "";
    private char[] arrayWordHidden = {}, arrayWord = {};

    // Método de sorteio da palavra.
    private void raffleWord(String fileRead) throws Exception {
        String word = Functions.raffleWord(fileRead);
        wordHidden(word);
    }

    // Método tratamento da palavra escondida.
    private void wordHidden(String word) throws Exception {
        wordString       = word;
        arrayWord        = word.toCharArray();
        arrayWordHidden  = word.toCharArray();
        for (int i=0; i<arrayWordHidden.length; i++)
            if (arrayWordHidden[i] != ' ')
                arrayWordHidden[i] = '_';
    }

    // ----------------------------------------------------------------------- //

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jogo);

        definedObjects();
        inheritAttributes();

        try {
            raffleWord(fileReadString);
        } catch (Exception e){
            Functions.messengerIteration("Problemas na leitura da palavra chave. + ");
            super.finish();
        }

        nominateObjects();
        threadRelogio();
    }

    // Método de herança de atributos.
    private void inheritAttributes(){
        if(getIntent().hasExtra("infoExtra")){
            Bundle extras         = getIntent().getExtras();
            categoryActualString  = extras.getString("categoryActual");
            fileReadString        = extras.getString("fileRead");
            acmScore              = extras.getInt("acmScore");
        }
    }

    // ----------------------------------------------------------------------- //

    private TextView category, score, word, timeRemainder, time;
    private ImageView boneco;

    private void definedObjects(){
        word            = (TextView) findViewById(R.id.palavraDoJogo);
        category        = (TextView) findViewById(R.id.categoria);
        score           = (TextView) findViewById(R.id.pontuacao);
        timeRemainder   = (TextView) findViewById(R.id.tempoRestante);
        time            = (TextView) findViewById(R.id.tempoTotal);
        boneco          = (ImageView) findViewById(R.id.boneco);
    }

    private void nominateObjects(){
        category       .setText(categoryActualString.toUpperCase());
        score          .setText("Pontuação: " + String.valueOf(acmScore));
        timeRemainder  .setText("Faltam: "+acmTimeRemainder+"s");
        time           .setText("Tempo: " + acmTime_Min + "m" + acmTime_Seg);
        word           .setText(String.valueOf(arrayWordHidden));
        boneco         .setImageResource(R.drawable.boneco0);
    }

    // ----------------------------------------------------------------------- //

    private void updateLayoutWord(){
        word.setText(String.valueOf(arrayWordHidden));
    }

    private void updateLayoutBoneco(){
        int imagem = getResources().getIdentifier("boneco"+errors,"drawable",getPackageName());
        boneco.setImageResource(imagem);
    }

    // ----------------------------------------------------------------------- //

    private int errors = 0;

    private String checkLetterSelected(char letter){
        // Compara o caracter na palavra inteira, os que não forem iguais compara a acentuação.
        String detect = "not";
        for(int i=0; i<arrayWord.length; i++){
            if(arrayWord[i] == letter){
                if(arrayWordHidden[i] == letter){
                    detect = "repeated";
                } else {
                    detect = "yes";
                    arrayWordHidden[i] = letter;
                }
            } else {
                if (searchAccentuation(letter, arrayWord[i])){
                    arrayWordHidden[i] = arrayWord[i];
                    detect = "yes";
                }
            }
        }
        return detect;
    }

    private boolean searchAccentuation(char letter, char letterWord){
        // Arrays to the compared letters.
        // Arrays para a comparação das letras.
        char[] accentuation  = {'Á','À','Â','Ã','Ä','É','Ê','Í','Ó','Ô','Õ','Ú','Ç'};
        char[] text          = {'A','A','A','A','A','E','E','I','O','O','O','U','C'};
        // Search on array the accentuation to compare with the letter selected.
        // Percorre o array de acentuação para comparar com a letra escolhida.
        for (int j=0; j<accentuation.length; j++)
            if(letterWord == accentuation[j])
                if(letter == text[j])
                    return true;
                else
                    return false;
        return false;
    }

    // ----------------------------------------------------------------------- //

    private int acmScore = 0;
    private DecimalFormat df = new DecimalFormat("00");

    private void conditionEnd(){
        // Compare two arrays and return true or false.
        // Compara os dois arrays e retorna true ou false.
        boolean palavraConcluida = Arrays.equals(arrayWord, arrayWordHidden);
        if((palavraConcluida) || (errors >= 6)){
            // Defined attributes that will send.
            // Define atributos que serão enviados.
            wordString = String.valueOf(arrayWord);
            timeString = df.format(acmTime_Min)+"m"+df.format(acmTime_Seg);
            // Condition victory. (word completed)
            //- Condição de vitória (Palavra Completada).
            Intent intent;
            if(palavraConcluida){
                acmScore++;
                intent = new Intent(this, Hit.class);
            // Condition defeat. (boneco completed)
            //- Condição de derrota (Boneco Completado).
            } else {
                intent = new Intent(this, Error.class);
            }
            intent.putExtra("word", wordString);
            intent.putExtra("acmScore", acmScore);
            intent.putExtra("timeGame", timeString);
            intent.putExtra("categoryActual", categoryActualString);
            intent.putExtra("fileRead", fileReadString);
            startActivity(intent);
            super.finish();
        }
    }

    // ----------------------------------------------------------------------- //

    private void pauseUnpauseMusic(){
        if (acmTimeRemainder <= 5)
            Sound.pauseBackgroundMusic();
        else
            Sound.unpauseBackgroundMusic();
    }

    // ----------------------------------------------------------------------- //

    private boolean threadEmExecucao = true;
    private int acmTimeRemainder = 30, acmTime_Min = 0, acmTime_Seg = 0;
    private String timeString = "";

    private void updateTime(){
        if (acmTime_Seg >= 60){
            acmTime_Min++;
            acmTime_Seg = 0;
        }
    }

    private void conditionEnd_Clock(){
        if (acmTimeRemainder == 0){
            timeString = df.format(acmTime_Min)+"m"+df.format(acmTime_Seg);
            Intent intent = new Intent(this, Error.class);
            intent.putExtra("word", wordString);
            intent.putExtra("acmScore", acmScore);
            intent.putExtra("timeGame", timeString);
            intent.putExtra("categoryActual", categoryActualString);
            intent.putExtra("fileRead", fileReadString);
            startActivity(intent);
            super.finish();
        }
    }

    private Thread thread;

    private void threadRelogio(){
        threadEmExecucao = true;
        thread = new Thread(new Runnable(){
            public void run(){
                try {
                    while (threadEmExecucao){
                        Thread.sleep(1000);
                        acmTimeRemainder--;
                        acmTime_Seg++;

                        timeRemainder.post(new Runnable(){
                            public void run(){
                                timeRemainder.setText("Faltam: " + acmTimeRemainder + "s");
                            }
                        });
                        timeRemainder.post(new Runnable(){
                            public void run(){
                                updateTime();
                                time.setText("Tempo: "+df.format(acmTime_Min)+"m"+df.format(acmTime_Seg));
                            }
                        });
                        if (acmTimeRemainder == 5){
                            pauseUnpauseMusic();
                            Sound.playSoundEffect("relogio");
                        }
                        conditionEnd_Clock();
                    }
                } catch (Exception e){}
            }
        });
        thread.start();
    }

    // ----------------------------------------------------------------------- //

    public void eventLetters(View view){
        Button aux = (Button) view;
        // Recebe a letra do botão.
        char caracter = aux.getText().toString().charAt(0);
        // Analisa se a letra está contida na palavra do jogo.
        if ((caracter != ' ') && (errors < 6)){
            String procura = checkLetterSelected(caracter);
            if (procura.equals("yes") || procura.equals("repeated")){
                Sound.playSoundEffect("letra_certa");
                if (procura.equals("yes")){
                    view.setBackgroundColor(0xff25eb15);
                    updateLayoutWord();
                    acmTimeRemainder = 31;
                }
            } else {
                Sound.playSoundEffect("letra_errada");
                view.setBackgroundColor(0xfff50200);
                errors++;
                updateLayoutBoneco();
                acmTimeRemainder = 31;
            }
            pauseUnpauseMusic();
            conditionEnd();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadEmExecucao = false;
        thread.interrupt();
    }
}
