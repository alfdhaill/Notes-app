package com.example.notes.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.activities.MainActivity2;
import com.example.notes.model.Note;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    private List<Note> notes;
    private ActivityResultLauncher<Intent> resultLauncher;

    public NoteAdapter(List<Note> notes, ActivityResultLauncher<Intent> resultLauncher) {
        this.notes = notes;
        this.resultLauncher = resultLauncher;
    }

    public void updateNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Note note = notes.get(position);
        String title = note.getTitle();

        if (title != null && title.length() > 23) {

            title = title.substring(0, 23) + "...";
        } holder.mtvTitle.setText(title);

        holder.mtvDescription.setText(note.getDescription());

        if (holder.mtvDate != null) {
            holder.mtvDate.setText(note.getDate());
        }

        holder.materialCardView.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), MainActivity2.class);

            intent.putExtra("id", note.getId());

            resultLauncher.launch(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView materialCardView;
        MaterialTextView mtvTitle, mtvDescription, mtvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            materialCardView = itemView.findViewById(R.id.mcv_note);
            mtvTitle = itemView.findViewById(R.id.mtv_title);
            mtvDescription = itemView.findViewById(R.id.mtv_description);
            mtvDate = itemView.findViewById(R.id.mtv_date);
        }
    }
}
