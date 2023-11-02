package com.example.noteme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter for the RecyclerView to display a list of notes.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    // Member variables
    private LayoutInflater inflater; // Used to inflate the layout of each item in the RecyclerView
    private List<Note> notes; // List containing the notes data

    // Callback listener for delete operation
    private OnNoteDeleteListener deleteListener;
    private OnNoteEditLister editListener;

    /**
     * Constructor for the Adapter.
     *
     * @param context        Context of the app
     * @param notes          List of notes to display
     * @param deleteListener Listener for delete note actions
     */
    Adapter(Context context, List<Note> notes, OnNoteDeleteListener deleteListener, OnNoteEditLister editListener) {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
        this.deleteListener = deleteListener;
        this.editListener=editListener;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Inflate the custom list view layout and return a new ViewHolder
        View view = inflater.inflate(R.layout.custom_list_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        // Extract data for the note at the current position
        String title = notes.get(position).getTitle();
        String date = notes.get(position).getDate();
        String description = notes.get(position).getContent();
        String time = notes.get(position).getTime();
        String colorOfNote = notes.get(position).getColor();

        // Populate the ViewHolder views with the note data
        holder.nTitle.setText(title);
        holder.nDesc.setText(description);
        holder.nDate.setText(date);
        holder.nTime.setText(time);

        // Set a click listener on the delete button to trigger the delete callback
        holder.dNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note noteToDelete = notes.get(position);
                deleteListener.onNoteDelete(noteToDelete);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note noteToEdit = notes.get(position);
                editListener.onNoteEdit(noteToEdit);
            }
        });

        // Set the background color of the note based on its color attribute
        if (colorOfNote.equalsIgnoreCase("orange")) {
            holder.nColor.setBackgroundResource(R.color.orange);
        } else if (colorOfNote.equalsIgnoreCase("yellow")) {
            holder.nColor.setBackgroundResource(R.color.yellow);
        } else if (colorOfNote.equalsIgnoreCase("skyBlue")) {
            holder.nColor.setBackgroundResource(R.color.skyBlue);
            Log.d("selection", "color will be: " + colorOfNote);
        }

        // Log for debugging
        Log.d("Color", "Color -> " + colorOfNote);
    }

    @Override
    public int getItemCount() {
        // Return the total number of notes
        return notes.size();
    }

    /**
     * ViewHolder class that represents each row of the note item in the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        // UI elements within each note item
        TextView nTitle, nDesc, nDate, nTime; // Text views for note details
        ImageButton dNote; // Button for deleting a note
        ConstraintLayout nColor; // Container layout for the note (to change its background color)

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Bind the views from the layout to the ViewHolder's member variables
            nTitle = itemView.findViewById(R.id.nTitle);
            nDesc = itemView.findViewById(R.id.nDesc);
            nDate = itemView.findViewById(R.id.nDate);
            nTime = itemView.findViewById(R.id.nTime);
            nColor = itemView.findViewById(R.id.boxToFill);
            dNote = itemView.findViewById(R.id.deleteNote);
        }
    }

    /**
     * Update the list of notes and notify the Adapter about the changes.
     *
     * @param newNotes Updated list of notes
     */
    public void updateNotes(List<Note> newNotes) {
        notes = newNotes;
        notifyDataSetChanged(); // Notify the RecyclerView about data changes to refresh the view
    }

    /**
     * Interface for a callback to be triggered when a note is deleted.
     */
    public interface OnNoteDeleteListener {
        void onNoteDelete(Note note);
    }
    public interface OnNoteEditLister{
        void onNoteEdit(Note note);
    }
}

