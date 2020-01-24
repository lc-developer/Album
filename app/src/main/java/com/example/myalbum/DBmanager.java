package com.example.myalbum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBmanager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db";
    public static final int DATABASE_VERSION = 1;

    public DBmanager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE myAlbums (id INTEGER PRIMARY KEY AUTOINCREMENT, titolo TEXT, autore TEXT, anno TEXT, supporto TEXT, cover TEXT)";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    ArrayList<ContentValues> getAlbums() {

        ArrayList<ContentValues> albums = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM myAlbums";

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            ContentValues album = new ContentValues();

            album.put("id", cursor.getInt(cursor.getColumnIndex("id")));
            album.put("titolo", cursor.getString(cursor.getColumnIndex("titolo")));
            album.put("autore", cursor.getString(cursor.getColumnIndex("autore")));
            album.put("anno", cursor.getString(cursor.getColumnIndex("anno")));
            album.put("supporto", cursor.getString(cursor.getColumnIndex("supporto")));
            album.put("cover", cursor.getString(cursor.getColumnIndex("cover")));

            albums.add(album);

            cursor.moveToNext();
        }

        db.close();

        return albums;
    }

    void insertAlbum(ContentValues album) {

        SQLiteDatabase db = getWritableDatabase();

        db.insert("myAlbums", null, album);

        db.close();
    }

    void updateAlbum(ContentValues album) {
        SQLiteDatabase db = getWritableDatabase();

        db.update("myAlbums", album, "id=?", new String[] {album.getAsInteger("id").toString()});

        db.close();
    }

    void deleteAlbum(ContentValues album) {

        SQLiteDatabase db = getWritableDatabase();

        db.delete("myAlbums", "id=?", new String[] {album.getAsInteger("id").toString()});

        db.close();

    }

    void deleteAllAlbums() {
        SQLiteDatabase db = getWritableDatabase();

        db.delete("myAlbums", "", null);

        db.close();
    }
}

