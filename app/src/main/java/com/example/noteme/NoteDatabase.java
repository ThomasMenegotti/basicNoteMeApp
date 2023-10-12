package com.example.noteme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides database functionalities like creating, reading, and updating the notes.
 */
public class NoteDatabase extends SQLiteOpenHelper {

    // Constants for database version, name and table name
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "notedb4";
    private static final String DATABASE_TABLE = "notestable3";

    // Column names for the database table
    private static final String KEY_ID = "id";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_COLOR = "color";

    // Constructor for the NoteDatabase
    NoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create a table with columns id, title, content, date, time, and color
        String query = "CREATE TABLE " + DATABASE_TABLE + "(" + KEY_ID + " INT PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_CONTENT + " TEXT," + KEY_DATE + " TEXT," + KEY_TIME + " TEXT," + KEY_COLOR + " TEXT" + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If the existing version of the database is up-to-date, no need to proceed
        if (oldVersion >= newVersion) {
            return;
        }

        // If not, drop the older table and recreate
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    // Function to add a new note to the database
    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_TITLE, note.getTitle());
        c.put(KEY_CONTENT, note.getContent());
        c.put(KEY_TIME, note.getTime());
        c.put(KEY_DATE, note.getDate());
        c.put(KEY_COLOR, note.getColor());

        long ID = db.insert(DATABASE_TABLE, null, c);
        Log.d("Inserted", "ID ->" + ID);
        Log.d("Color", "Color ->  " + note.getColor());
        return ID;
    }

    // Function to retrieve a single note based on its ID
    public Note getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_DATE, KEY_TIME, KEY_COLOR}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        // Construct and return a Note object from the cursor data
        return new Note(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
    }

    // Function to retrieve all notes from the database
    public List<Note> getNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> allNotes = new ArrayList<>();

        // SQL query to fetch all records from the table
        String query = "SELECT * FROM " + DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        // Loop through the cursor and populate the list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setID(cursor.getLong(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                note.setColor(cursor.getString(5));

                allNotes.add(note);
            } while (cursor.moveToNext());
        }

        // Return the list of notes
        return allNotes;
    }
}
