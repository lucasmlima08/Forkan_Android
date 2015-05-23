package com.myllenno.lucas.forkanedu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;

public class Functions {

    public static final String[] directories = {
            "Animais","Capitais_do_Brasil","Capitais_do_Mundo","Cores","Cursos_Superiores",
            "Estados_do_Brasil","Frutas","Paises"};

    public static final String[] categories = {
            "Animais","Capitais do Brasil","Capitais do Mundo","Cores","Cursos Superiores",
            "Estados Brasileiros","Frutas","Países"};

    public static Context context;
    public static AssetManager assets;

    public static void messengerIteration(String erro){
        AlertDialog.Builder messenger = new AlertDialog.Builder(context);
        messenger.setTitle("Desculpa!");
        messenger.setMessage(erro);
        messenger.setNeutralButton("OK", null);
        messenger.show();
    }

    /* Funções da leitura de palavras e categories. */

    private static BufferedReader openFile(String fileRead) throws IOException {
        InputStream inputStream = assets.open(fileRead+".txt");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF8");
        BufferedReader leitura = new BufferedReader(inputStreamReader);
        return leitura;
    }

    // Raffle word the game.
    public static String raffleWord(String fileRead) throws Exception {
        BufferedReader bufferedReader = openFile(fileRead);
        // Raffle a number aleatory in this limit.
        int numPalavras = counterRegister(fileRead);
        Random random = new Random();
        int numRandom = random.nextInt(numPalavras);
        // Read the lines the file.
        String line = bufferedReader.readLine();
        if (numRandom > 1)
            for (int i=1; i < numRandom; i++)
                line = bufferedReader.readLine();
        // Remove first letter.
        else
            line = line.substring(1);
        bufferedReader.close();
        return line;
    }

    private static int counterRegister(String fileRead) throws IOException {
        BufferedReader bufferedReader = openFile(fileRead);
        // Count the number the register.
        int acm = 0;
        String line = bufferedReader.readLine();
        while (line != null){
            acm++;
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        return acm;
    }

    /* Functions to file of scores */

    private static File preparedFileRecord(Context context) throws Exception {
        String filePath = context.getFilesDir().getPath().toString() + "Recorde.txt";
        File file = new File(filePath);
        return file;
    }

    public static void createFileRecord(Context context) throws Exception {
        File file = preparedFileRecord(context);
        file.createNewFile();
    }

    public static void removeFileRecord(Context context) throws Exception {
        File file = preparedFileRecord(context);
        file.delete();
    }

    public static void createFirstFileRecord(Context context) throws Exception {
        File file = preparedFileRecord(context);
        // Create the file Record if not exist.
        if(!file.exists()) {
            file.createNewFile();
            Writer write = preparedFileRecordWrite(context);
            // Write records on file.
            String stringAux = "0";
            for (int i=1; i<directories.length; i++)
                stringAux += "\n"+"0";
            write.write(stringAux);
            write.close();
        }
    }

    private static BufferedReader preparedFileRecordRead(Context context) throws Exception {
        File file = preparedFileRecord(context);
        InputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return bufferedReader;
    }

    private static Writer preparedFileRecordWrite(Context context) throws Exception {
        File file = preparedFileRecord(context);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        Writer writer = new BufferedWriter(outputStreamWriter);
        return writer;
    }

    public static String[] readFileRecord(Context context) throws Exception {
        BufferedReader bufferedReader = preparedFileRecordRead(context);
        String scores[] = new String[directories.length];
        // Read file lines.
        int position = 0;
        String line = bufferedReader.readLine();
        while (line != null){
            scores[position] = line;
            position++;
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        return scores;
    }

    public static void changeScore(String directory, int pontuacao, Context context) throws Exception {
        String scores[] = readFileRecord(context);
        // Search position on directory.
        int position = 0;
        for (position=0; position<directories.length; position++)
            if (directories[position].equals(directory))
                break;
        // If the new score if larger that current.
        if (Integer.parseInt(scores[position]) < pontuacao){
            // Trade off score to the larger.
            scores[position] = ""+pontuacao;
            // Recreate the file the scores.
            removeFileRecord(context);
            createFileRecord(context);
            // Recreate the scores on file.
            Writer writer = preparedFileRecordWrite(context);
            // Write to scores on files.
            String stringAux = scores[0];
            for (int i=1; i<scores.length; i++){
                stringAux += "\n";
                stringAux += scores[i];
            }
            writer.write(stringAux);
            writer.close();
        }
    }
}
