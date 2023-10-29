package com.example.noteme;

import android.annotation.SuppressLint;
import android.content.Context;
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
    private LayoutInflater inflater;
    private List<Note> notes;

    private OnNoteDeleteListener deleteListener;

    /**
     * Constructor for the Adapter.
     * @param context Context of the app
     * @param notes List of notes to display
     */
    Adapter(Context context, List<Note> notes, OnNoteDeleteListener deleteListener) {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
        this.deleteListener= deleteListener;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Inflate the custom list view and return a new ViewHolder
        View view = inflater.inflate(R.layout.custom_list_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        // Get the note at the current position
        String title = notes.get(position).getTitle();
        String date = notes.get(position).getDate();
        String description = notes.get(position).getContent();
        String time = notes.get(position).getTime();
        String colorOfNote = notes.get(position).getColor();

        // Set the values to the respective views
        holder.nTitle.setText(title);
        holder.nDesc.setText(description);
        holder.nDate.setText(date);
        holder.nTime.setText(time);
        holder.dNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Note noteToDelete = notes.get(position);
                deleteListener.onNoteDelete(noteToDelete);
            }
        });

        // Set background color based on the note's color value
        if (colorOfNote.equalsIgnoreCase("orange")) {
            holder.nColor.setBackgroundResource(R.color.orange);
        } else if (colorOfNote.equalsIgnoreCase("yellow")) {
            holder.nColor.setBackgroundResource(R.color.yellow);
        } else if (colorOfNote.equalsIgnoreCase("skyBlue")) {
            holder.nColor.setBackgroundResource(R.color.skyBlue);
            Log.d("selection", "color will be: " + colorOfNote);
        }

        // Log color information for debugging purposes
        Log.d("Color", "Color -> " + colorOfNote);
    }

    @Override
    public int getItemCount() {
        // Return the number of notes
        return notes.size();
    }

    /**
     * ViewHolder class that represents each row of the note item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nTitle, nDesc, nDate, nTime;
        ImageButton dNote;
        ConstraintLayout nColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Bind the views from the layout to the ViewHolder's member variables
            nTitle = itemView.findViewById(R.id.nTitle);
            nDesc = itemView.findViewById(R.id.nDesc);
            nDate = itemView.findViewById(R.id.nDate);
            nTime = itemView.findViewById(R.id.nTime);
            nColor = itemView.findViewById(R.id.boxToFill);
            dNote= itemView.findViewById(R.id.deleteNote);
        }
    }

    /**
     * Update the list of notes and notify the Adapter about the changes.
     * @param newNotes Updated list of notes
     */
    public void updateNotes(List<Note> newNotes) {
        notes = newNotes;
        notifyDataSetChanged();
    }

    public interface OnNoteDeleteListener{
        void onNoteDelete(Note note);
    }
}
