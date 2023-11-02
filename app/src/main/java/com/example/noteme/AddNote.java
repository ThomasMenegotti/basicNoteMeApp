package com.example.noteme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * AddNote activity provides functionalities to create and save a new note.
 * It allows the user to set note title, description, choose a background color,
 * and either capture or upload an image for the note.
 */
public class AddNote extends AppCompatActivity {

    // UI components
    private Toolbar toolbar;
    private Note editNote;
    private EditText noteTitle, noteDescription;
    private Calendar c;
    private ChipGroup chipGroup;
    private ImageView noteImage;
    private Chip colorYellow, colorOrange, colorSkyBlue;
    private Button capImg, uplImg;

    // Variables for storing note details
    private String selectColor;
    private String todaysDate;
    private String currentTime;

    // Request code constants for intents and permissions
    private final int SELECT_IMAGE = 101;
    private final int CAPTURE_IMAGE = 102;
    private final int REQUEST_PERMISSION = 100;

    // Method to request required permissions from the user
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permissions granted.
            } else {
                // permissions denied. Handle accordingly.
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Set up the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Connect UI elements to their respective views
        noteTitle = findViewById(R.id.noteTitle);
        noteDescription = findViewById(R.id.noteDescription);
        chipGroup = findViewById(R.id.chipGroup);
        colorYellow = findViewById(R.id.colorYellow);
        colorOrange = findViewById(R.id.colorOrange);
        colorSkyBlue = findViewById(R.id.colorSkyBlue);
        capImg = findViewById(R.id.captureImg);
        uplImg = findViewById(R.id.uploadImg);
        noteImage=findViewById(R.id.noteImage);

        // Set up the capture image button
        capImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(AddNote.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions();  // request permissions if not granted
                    return;
                }

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, CAPTURE_IMAGE);
            }
        });

        // Set up the upload image button
        uplImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT);
                pickPhoto.setType("image/*");
                startActivityForResult(pickPhoto, SELECT_IMAGE);
            }
        });

        // Set up a text change listener for the note title
        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Dynamically update the toolbar title based on the note title
                if (s.length() != 0) {
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space is intentionally left blank.
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This space is intentionally left blank.
            }
        });

        // Retrieve current date and time
        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(c.get(Calendar.HOUR)) + ":" + pad(c.get(Calendar.MINUTE));
        Log.d("calendar", "Date and Time: " + todaysDate + " and " + currentTime);

        // Set up the color selection mechanism for the note
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

        // This will check and set the default color after all initialization
        if (colorYellow.isChecked()) {
            selectColor = "yellow";
            noteTitle.setBackgroundResource(R.color.yellow);
            noteDescription.setBackgroundResource(R.color.yellow);
        }

        if(getIntent().hasExtra("editNote")){
            editNote=(Note) getIntent().getSerializableExtra("editNote");
            setEditMode();
        } else{
            //something else
        }

    }
    private void setEditMode(){

        isEditMode=true;
        // Populate UI fields with existing note data for editing.
        noteTitle.setText(editNote.getTitle());
        noteDescription.setText(editNote.getContent());

        // Load and display the image if it exists
        if(editNote.getImageByteArray()!=null){
            Bitmap imageBitmap= BitmapFactory.decodeByteArray(editNote.getImageByteArray(), 0, editNote.getImageByteArray().length);
            noteImage.setImageBitmap(imageBitmap);
        }


        // Enable editing for title and description fields.
        noteTitle.setEnabled(true);
        noteDescription.setEnabled(true);

        // Set the toolbar title to indicate edit mode.
        getSupportActionBar().setTitle("Edit Note");
        selectColor= editNote.getColor();
        if (selectColor.equalsIgnoreCase("orange")) {
            noteTitle.setBackgroundResource(R.color.orange);
            noteDescription.setBackgroundResource(R.color.orange);
        } else if (selectColor.equalsIgnoreCase("yellow")) {
            noteTitle.setBackgroundResource(R.color.yellow);
            noteDescription.setBackgroundResource(R.color.yellow);
        } else if (selectColor.equalsIgnoreCase("skyBlue")) {
            noteTitle.setBackgroundResource(R.color.skyBlue);
            noteDescription.setBackgroundResource(R.color.skyBlue);
        }
    }

    // Utility method to get the color resource based on selected chip
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

    // Utility method to pad single digit numbers with a leading zero
    private String pad(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return String.valueOf(i);
    }

    // Inflate menu options for this activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    private boolean isEditMode = false;

    // Handle actions when a menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO: IF THE NOTE IS IN AN EDIT STATE AND USER PRESSES THE DELETE BUTTON THEN DELETE THE NOTE AS WELL (NOT HARD)
        if (item.getItemId() == R.id.delete) {
            if(isEditMode){
                deleteExistingNote();
            }else {
                Toast.makeText(this, "Note Was Not Saved.", Toast.LENGTH_SHORT).show();
                goToMain();
            }
        } else if (item.getItemId() == R.id.save) {
            if(isEditMode){
                updateExistingNote();
            }else{
                saveNewNote();
            }
//            // Save the note to the database
//            Note note = new Note(noteTitle.getText().toString(), noteDescription.getText().toString(), todaysDate, currentTime, selectColor);
//            NoteDatabase db = new NoteDatabase(this);
//            db.addNote(note);
//            Toast.makeText(this, "Note Has Been Saved.", Toast.LENGTH_SHORT).show();
//            goToMain();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateExistingNote(){
        if(editNote!=null){
            // Update the existing note's details
            editNote.setTitle(noteTitle.getText().toString());
            editNote.setContent(noteDescription.getText().toString());
            editNote.setColor(selectColor);

            // Update the existing note in the database
            NoteDatabase db = new NoteDatabase(this);
            db.updateNote(editNote);

            // Indicate that you've left edit mode
            isEditMode = false;

            // Notify the user that the note has been updated
            Toast.makeText(this, "Note Has Been Updated.", Toast.LENGTH_SHORT).show();

            // Return to the main activity
            goToMain();
        }
    }

    private void deleteExistingNote() {
        if (editNote != null) {
            // Delete the existing note from the database
            NoteDatabase db = new NoteDatabase(this);
            db.deleteNote(editNote);

            // Indicate that you've left edit mode
            isEditMode = false;

            // Notify the user that the note has been deleted
            Toast.makeText(this, "Note Has Been Deleted.", Toast.LENGTH_SHORT).show();

            // Return to the main activity
            goToMain();
        }
    }

    private void saveNewNote() {
        byte[] imageByteArray = null; // Declare imageByteArray before the if statement

        // Create a new note and save it to the database
        if (noteImage.getDrawable() != null && noteImage.getDrawable() instanceof BitmapDrawable) {
            Bitmap imageBitmap = ((BitmapDrawable) noteImage.getDrawable()).getBitmap();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            if (imageBitmap != null) {
                Log.d("NoteMe", "ImageBitmap size before compression: " + imageBitmap.getByteCount());
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Log.d("NoteMe", "ImageBitmap size after compression: " + stream.size());

                // Convert the bitmap to a byte array
                imageByteArray = stream.toByteArray(); // Assign a value to imageByteArray
            } else {
                Log.e("NoteMe", "ImageBitmap is null");
            }
        }

        // Create a new note with or without the imageByteArray
        Note note = new Note(noteTitle.getText().toString(), noteDescription.getText().toString(), todaysDate, currentTime, selectColor, imageByteArray);
        NoteDatabase db = new NoteDatabase(this);
        db.addNote(note);

        // Notify the user that the new note has been saved
        Toast.makeText(this, "Note Has Been Saved.", Toast.LENGTH_SHORT).show();

        // Return to the main activity
        goToMain();
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result was successful
        if (resultCode == RESULT_OK) {
            // Handle result based on request code
            if (requestCode == CAPTURE_IMAGE) {
                if (data != null && data.getExtras() != null) {
                    // Get the captured image as a bitmap from the Intent data
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

                    // Set the captured image to the ImageView
                    if (imageBitmap != null) {
                        noteImage.setImageBitmap(imageBitmap); // Set the image to the ImageView
                    } else {
                        Log.e("NoteMe", "Captured image bitmap is null");
                    }
                } else {
                    Log.e("NoteMe", "Captured image data is null");
                }
            }
            else if (requestCode == SELECT_IMAGE) {
                // Get the URI of the selected image from the Intent data
                Uri selectedImage = data.getData();

                try {
                    // Decode the URI into a bitmap
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                    // Set the selected image to the ImageView
                    noteImage.setImageBitmap(imageBitmap); // Set the image to the ImageView
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    // Utility method to navigate to the main activity
    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    // Handle the back button press event
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
