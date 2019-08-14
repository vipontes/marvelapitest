package br.net.easify.marvelapitest.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "marvel.sqlite";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query;

        query = "CREATE TABLE IF NOT EXISTS data (id INTEGER PRIMARY KEY, data_count INTEGER, data_total INTEGER, data_offset INTEGER, data_limit INTEGER)";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS characters (id INTEGER PRIMARY KEY, name TEXT, description TEXT, thumbnail TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Total getData() {

        Total total = new Total(0,0,0,0);
        String countQuery = "SELECT data_count, data_total, data_offset, data_limit FROM data WHERE id = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if ( cursor.moveToFirst() ) {
            total.setCount(cursor.getInt(cursor.getColumnIndex("data_count")));
            total.setTotal(cursor.getInt(cursor.getColumnIndex("data_total")));
            total.setOffset(cursor.getInt(cursor.getColumnIndex("data_offset")));
            total.setLimit(cursor.getInt(cursor.getColumnIndex("data_limit")));
        }
        cursor.close();
        return total;
    }

    public void setData(int data_count, int data_total, int data_offset, int data_limit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("data_count", data_count);
        values.put("data_total", data_total);
        values.put("data_offset", data_offset);
        values.put("data_limit", data_limit);

        String countQuery = "SELECT id FROM data WHERE id = 1";
        Cursor cursor = db.rawQuery(countQuery, null);
        if ( cursor.getCount() == 0 ) {
            values.put("id", 1);
            db.insert("data", null, values);
        } else {
            db.update("data", values, "id=?", new String[]{String.valueOf(1)});
        }
    }

    public void addCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id", character.getId());
        values.put("name", character.getName());
        values.put("description", character.getDescription());
        values.put("thumbnail", character.getThumbnail());

        db.insert("characters", null, values);
    }

    public List<Character> getCharacterList () {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                "characters",
                null,
                null,
                null,
                null,
                null,
                "name");

        List<Character> res = new ArrayList<>();

        if ( cursor.moveToFirst() ) {
            do {
                Character character = new Character(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("thumbnail")));
                res.add(character);

            } while (cursor.moveToNext());
        }

        db.close();

        return res;
    }

}
