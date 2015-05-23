package com.myllenno.lucas.forkanedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Categories extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorias);

        listButtons();
    }

    private void listButtons(){
        LinearLayout layoutCategories = (LinearLayout) findViewById(R.id.layoutMenucategories);
        // Call style defined to buttons the categories.
        ContextThemeWrapper styleButton = new ContextThemeWrapper(this, R.style.estiloBotaocategories);
        // Print categories the array on game.
        Button buttonCategory;
        for (int i=0; i< Functions.directories.length; i++){
            buttonCategory = new Button(styleButton);
            buttonCategory.setId(i);
            buttonCategory.setBackground(getResources().getDrawable(R.drawable.tema_botoes_categorias));
            buttonCategory.setText(Functions.categories[i]);
            buttonCategory.setOnClickListener(this);
            layoutCategories.addView(buttonCategory);
        }
    }

    private void categorySelected(int id){
        // Send info the category selected.
        Intent game = new Intent(this, Game.class);
        game.putExtra("infoExtra", true);
        game.putExtra("categoryActual", Functions.categories[id]);
        game.putExtra("fileRead", Functions.directories[id]);
        game.putExtra("acmScore", 0);
        startActivity(game);
        super.finish();
    }

    @Override
    public void onClick(View view){
        Sound.playSoundEffect("beep");
        categorySelected(view.getId());
    }
}