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

public class MainActivity extends AppCompatActivity {

    // UI components
    Toolbar toolbar;
    RecyclerView recyclerView;
    SearchView searchView;

    // Adapter for RecyclerView and list to hold notes data
    Adapter adapter;
    List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding UI components
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchBar);

        // Setting up the toolbar
        setSupportActionBar(toolbar);

        // Getting notes from the database
        NoteDatabase db = new NoteDatabase(this);
        notes = db.getNotes();

        // Setting up RecyclerView
        recyclerView = findViewById(R.id.listOfNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, notes);
        recyclerView.setAdapter(adapter);

        // Setting listener for searchView to filter notes
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle when the user presses search button (if needed)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the notes based on the user's query
                filter(newText);
                return true;
            }
        });
    }

    // Inflate the menu options for the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    // Handle option item selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            // Start the AddNote activity when 'add' option is selected
            Intent i = new Intent(this, AddNote.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to filter notes based on a query
    private void filter(String query) {
        List<Note> filteredNotes = new ArrayList<>();

        for (Note note : notes) {
            // Check if the title of the note contains the query string
            if (note.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredNotes.add(note);
            }
        }

        // Update the RecyclerView with the filtered notes
        adapter.updateNotes(filteredNotes);
    }
}
