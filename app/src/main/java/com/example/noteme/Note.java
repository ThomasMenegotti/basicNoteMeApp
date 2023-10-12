package com.example.noteme;

/**
 * Model class to represent a single note.
 */
public class Note {

    // Instance variables
    private long ID;         // Unique identifier for the note
    private String title;    // Title of the note
    private String content;  // Content or body of the note
    private String date;     // Date the note was created or last modified
    private String time;     // Time the note was created or last modified
    private String color;    // Color identifier for the note, used for UI display

    // Default constructor
    Note() {}

    /**
     * Constructor without ID parameter. Useful for creating a new note.
     */
    Note(String title, String content, String date, String time, String color) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.color = color;
    }

    /**
     * Full constructor. Useful when retrieving a note from a database.
     */
    Note(long id, String title, String content, String date, String time, String color) {
        this.ID = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.color = color;
    }

    // Getter and Setter methods for the instance variables

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
