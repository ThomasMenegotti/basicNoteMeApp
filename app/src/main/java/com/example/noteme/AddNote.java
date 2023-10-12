package com.example.noteme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;

/**
 * Activity class for adding a new note.
 */
public class AddNote extends AppCompatActivity {

    // UI Elements
    Toolbar toolbar;
    EditText noteTitle, noteDescription;
    Calendar c;
    ChipGroup chipGroup;
    Chip colorYellow, colorOrange, colorSkyBlue;

    // Variables for storing note details
    String selectColor;
    String todaysDate;
    String currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Set up the toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Connect UI elements to their XML IDs
        noteTitle = findViewById(R.id.noteTitle);
        noteDescription = findViewById(R.id.noteDescription);
        chipGroup = findViewById(R.id.chipGroup);
        colorYellow = findViewById(R.id.colorYellow);
        colorOrange = findViewById(R.id.colorOrange);
        colorSkyBlue = findViewById(R.id.colorSkyBlue);

        // Add text change listener to the note title
        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Dynamically set the toolbar title based on the note's title input
                if (s.length() != 0) {
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Get current date and time
        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(c.get(Calendar.HOUR)) + ":" + pad(c.get(Calendar.MINUTE));
        Log.d("calendar", "Date and Time: " + todaysDate + " and " + currentTime);

        // Set a color change listener to the ChipGroup
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                int color = getColorForChipId(checkedId);
                if (color != 0) {
                    noteTitle.setBackgroundResource(color);
                    noteDescription.setBackgroundResource(color);
                }
            }
        });

        if (colorYellow.isChecked()) {
            selectColor = "yellow";
            noteTitle.setBackgroundResource(R.color.yellow);
            noteDescription.setBackgroundResource(R.color.yellow);
        }
    }

    // Determine the color resource based on the selected chip's ID
    private int getColorForChipId(int chipId) {
        if (chipId == colorYellow.getId()) {
            selectColor = "yellow";
            return R.color.yellow;
        } else if (chipId == colorOrange.getId()) {
            selectColor = "orange";
            return R.color.orange;
        } else if (chipId == colorSkyBlue.getId()) {
            selectColor = "skyBlue";
            return R.color.skyBlue;
        }
        return 0;
    }

    // Pad single digit numbers with leading zero
    private String pad(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return String.valueOf(i);
    }

    // Inflate the save menu for the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    // Handle actions on the options items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            Toast.makeText(this, "Note Was Not Saved.", Toast.LENGTH_SHORT).show();
            goToMain();
        }
        if (item.getItemId() == R.id.save) {
            // Save the note to the database
            Note note = new Note(noteTitle.getText().toString(), noteDescription.getText().toString(), todaysDate, currentTime, selectColor);
            NoteDatabase db = new NoteDatabase(this);
            db.addNote(note);
            Toast.makeText(this, "Note Has Been Saved.", Toast.LENGTH_SHORT).show();
            goToMain();
        }
        return super.onOptionsItemSelected(item);
    }

    // Navigate back to the main activity
    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
