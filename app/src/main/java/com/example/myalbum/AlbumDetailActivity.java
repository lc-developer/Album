package com.example.myalbum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AlbumDetailActivity extends AppCompatActivity {

    DBmanager dbManager;

    ContentValues album;

    EditText titoloET;
    EditText autoreET;
    EditText annoET;
    EditText supportoET;
    EditText coverET;



    void saveAlbum() {

        boolean isEditMode = album != null;

        if (album == null) {

            album = new ContentValues();
        }

        album.put("titolo", titoloET.getText().toString());
        album.put("autore", autoreET.getText().toString());
        album.put("anno", annoET.getText().toString());
        album.put("supporto", supportoET.getText().toString());
        album.put("cover", coverET.getText().toString());

        if (isEditMode) {
            dbManager.updateAlbum(album);
        } else {
            dbManager.insertAlbum(album);
        }

        setResult(RESULT_OK);

        Toast.makeText(this, "Salvataggio effettuato", Toast.LENGTH_SHORT).show();

        finish();
    }

    void loadData(){
        if (album != null) {
            titoloET.setText(album.getAsString("titolo"));
            autoreET.setText(album.getAsString("autore"));
            annoET.setText(album.getAsString("anno"));
            supportoET.setText(album.getAsString("supporto"));
            coverET.setText(album.getAsString("cover"));
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        album = getIntent().getParcelableExtra("album");

        dbManager = new DBmanager(this, DBmanager.DATABASE_NAME, null, DBmanager.DATABASE_VERSION);

        titoloET = findViewById(R.id.titoloAlbumEditText);
        autoreET = findViewById(R.id.autoreAlbumEditText);
        annoET = findViewById(R.id.annoUscitaEditText);
        supportoET = findViewById(R.id.tipoSupportoEditText);
        coverET = findViewById(R.id.immagineCoverEditText);

        Button saveBtn = findViewById(R.id.saveButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveAlbum();
            }
        });

        loadData();
    }
}


