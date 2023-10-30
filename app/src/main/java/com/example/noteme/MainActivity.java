package com.example.noteme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity is the primary screen of the app where users can see a list of their notes.
 * This activity provides functionalities to search notes and navigate to the AddNote screen.
 */
public class MainActivity extends AppCompatActivity implements Adapter.OnNoteDeleteListener {

    // UI components
    private Toolbar toolbar;         // Top toolbar
    private RecyclerView recyclerView; // RecyclerView to display the notes list
    private SearchView searchView;   // Search bar to filter notes

    // Adapter for RecyclerView and list to hold notes data
    private Adapter adapter;
    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding UI components
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchBar);

        // Setting up the toolbar
        setSupportActionBar(toolbar);

        // Fetching notes from the database
        NoteDatabase db = new NoteDatabase(this);
        notes = db.getNotes();

        // Configuring the RecyclerView
        recyclerView = findViewById(R.id.listOfNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, notes, this); // 'this' is passed for the delete note callback
        recyclerView.setAdapter(adapter);

        // Setting a listener on the search bar to filter notes
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle when the user presses the search button on the keyboard (if needed)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the notes list based on the user's search query
                filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options for the activity
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        if (item.getItemId() == R.id.add) {
            // Navigate to the AddNote activity when 'add' option is selected
            Intent i = new Intent(this, AddNote.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNoteDelete(Note note) {
        // Callback method to handle note deletion

        NoteDatabase db = new NoteDatabase(this); // Database instance
        db.deleteNote(note); // Delete the note from the database

        notes = db.getNotes(); // Refresh the list after deletion
        adapter.updateNotes(notes); // Notify the adapter to refresh the RecyclerView
    }

    // Method to filter the notes list based on a search query
    private void filter(String query) {
        List<Note> filteredNotes = new ArrayList<>();

        // Loop through all notes and check if the title contains the search query
        for (Note note : notes) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredNotes.add(note);
            }
        }

        // Update the RecyclerView with the filtered notes
        adapter.updateNotes(filteredNotes);
    }
}

