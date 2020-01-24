package com.example.myalbum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBmanager dbManager;

    BaseAdapter baseAdapter;

    ArrayList<ContentValues> albums = new ArrayList<>();

    final int ADD_EDIT_CODE = 100;

    void initializeAdapter() {

        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {

                return albums.size();
            }

            @Override
            public ContentValues getItem(int i) {

                return albums.get(i);
            }

            @Override
            public long getItemId(int i) {

                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {

                if (view == null) {

                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                    view = layoutInflater.inflate(R.layout.album_item_layout, viewGroup, false);
                }

                ContentValues album = getItem(i);

                TextView titoloAlbum = view.findViewById(R.id.titoloAlbumTextView);
                TextView autoreAlbum = view.findViewById(R.id.autoreAlbumTextView);
                TextView annoSupporto = view.findViewById(R.id.annoSupportoTextView);

                titoloAlbum.setText(album.getAsString("titolo"));
                autoreAlbum.setText(album.getAsString("autore"));
                annoSupporto.setText(album.getAsString("anno") + " - " + album.getAsString("supporto") + " - " + album.getAsString("cover"));

                return view;
            }
        };
    }

    void loadAlbums() {

        albums.clear();

        albums.addAll(dbManager.getAlbums());

        baseAdapter.notifyDataSetChanged();
    }

    void openAlbumDetailActivity(ContentValues album) {
        Intent intent = new Intent(MainActivity.this, AlbumDetailActivity.class);

        intent.putExtra("album", album);

        startActivityForResult(intent, ADD_EDIT_CODE);
    }

    void openDelationConfirmationDialog(final ContentValues album) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Attenzione");
        alert.setMessage("Sei sicuro di volere cancellare?");
        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbManager.deleteAlbum(album);
                Toast.makeText(MainActivity.this, "Elemento eliminato!", Toast.LENGTH_SHORT).show();
                loadAlbums();

            }
        });

        alert.setNegativeButton("NO", null);
        alert.show();
        //alert.setCancelable()
    }


    void openDeleteAllConfirmationDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Attenzione");
        alert.setMessage("Sei sicuro di volere eliminare tutti gli elemnti?");
        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbManager.deleteAllAlbums();
                Toast.makeText(MainActivity.this, "Elementi eliminati!", Toast.LENGTH_SHORT).show();
                loadAlbums();

            }
        });

        alert.setNegativeButton("NO", null);
        alert.show();
        //alert.setCancelable()
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EDIT_CODE) {
            if (resultCode == RESULT_OK) {
                loadAlbums();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBmanager(this, DBmanager.DATABASE_NAME, null, DBmanager.DATABASE_VERSION);

        initializeAdapter();
        loadAlbums();

        ListView albumsLV = findViewById(R.id.albumsListView);

        albumsLV.setAdapter(baseAdapter);
        albumsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContentValues album = albums.get(i);
                openAlbumDetailActivity(album);
            }
        });

        albumsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                ContentValues album = albums.get(i);

                openDelationConfirmationDialog(album);

                return true;
            }
        });

        ImageView addBtn = findViewById(R.id.addButton);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAlbumDetailActivity(null);
            }
        });

        Button deleteBtn = (Button) findViewById(R.id.deleteButton);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDeleteAllConfirmationDialog();
            }
        });
    }
}

