//TODO: FIGURE OUT HOW IN THE FLYING FUCK I AM TO STORE IMAGES IN AN SQLITE DATABASE, PEOPLE SAYING TO STORE USING BYTE ARRAY (HARD)
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
 * NoteDatabase is responsible for providing database functionalities for managing notes.
 * This includes operations like creating, reading, updating, and deleting notes.
 */
public class NoteDatabase extends SQLiteOpenHelper {

    // Constants defining database attributes such as its version, name and table name
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "notedb6";
    private static final String DATABASE_TABLE = "notestable5";

    // Column names for the notes table
    private static final String KEY_ID = "id";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_COLOR = "color";
    private static final String KEY_IMAGE = "image";


    /**
     * Constructor to initialize the database helper with context.
     * @param context Context of the application
     */
    NoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is being created for the first time.
     * Defines the structure of the table(s).
     * @param db The database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DATABASE_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_COLOR + " TEXT,"
                + KEY_IMAGE +" BLOB" +")";
        db.execSQL(query);
    }

    /**
     * Called when the database needs to be upgraded, for example, due to schema changes.
     * @param db The database instance
     * @param oldVersion The old database version
     * @param newVersion The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) return;
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    /**
     * Adds a new note to the database.
     * @param note The note to be added
     * @return ID of the note after insertion
     */
    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_TITLE, note.getTitle());
        c.put(KEY_CONTENT, note.getContent());
        c.put(KEY_TIME, note.getTime());
        c.put(KEY_DATE, note.getDate());
        c.put(KEY_COLOR, note.getColor());
        c.put(KEY_IMAGE, note.getImageByteArray());

        long ID = db.insert(DATABASE_TABLE, null, c);
        Log.d("Inserted", "ID ->" + ID);
        Log.d("Color", "Color ->  " + note.getColor());
        return ID;
    }

    /**
     * Fetches a note based on its ID.
     * @param id ID of the note to retrieve
     * @return The corresponding note
     */
    public Note getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_DATE, KEY_TIME, KEY_COLOR}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        return new Note(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getBlob(6));
    }

    /**
     * Fetches all the notes present in the database.
     * @return List of all notes
     */
    public List<Note> getNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> allNotes = new ArrayList<>();
        String query = "SELECT * FROM " + DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setID(cursor.getLong(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                note.setColor(cursor.getString(5));
                note.setImageByteArray(cursor.getBlob(6));
                allNotes.add(note);
            } while (cursor.moveToNext());
        }
        return allNotes;
    }

    /**
     * Deletes a specific note from the database.
     * @param note The note to be deleted
     */
    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(DATABASE_TABLE, KEY_ID + "=?", new String[]{String.valueOf(note.getID())});
        Log.d("NoteDatabase", "Attempting to delete note with ID: " +note.getID() + ". Rows deleted: " + deletedRows);
        db.close();
    }

    public int updateNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_TIME, note.getTime());
        values.put(KEY_DATE, note.getDate());
        values.put(KEY_COLOR, note.getColor());
        values.put(KEY_IMAGE, note.getImageByteArray());

        // Updating row
        return db.update(DATABASE_TABLE, values, KEY_ID + " = ?", new String[]{String.valueOf(note.getID())});
    }
}
