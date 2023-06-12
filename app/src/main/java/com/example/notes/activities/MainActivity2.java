package com.example.notes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.notes.R;
import com.example.notes.database.AppDatabase;
import com.example.notes.model.Note;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    public static final int RESULT_ADD = 101;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;
    private static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy HH:mm";
    private Note note;
    private AppDatabase database;
    private TextInputEditText titleEditText;
    private TextInputEditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        database = AppDatabase.getInstance(this);
        int id = getIntent().getIntExtra("id", 0);
        titleEditText = findViewById(R.id.tiet_title);
        descriptionEditText = findViewById(R.id.tiet_description);
        MaterialButton saveButton = findViewById(R.id.mb_save);
        MaterialButton removeButton = findViewById(R.id.mb_remove);
        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);

        materialToolbar.setNavigationOnClickListener(v -> finish());

        if (id == 0) {
            note = new Note();

            materialToolbar.setTitle(R.string.add_a_note);

            saveButton.setOnClickListener(v -> addNote());
        } else {
            note = database.noteDao().getNote(id);
            titleEditText.setText(note.getTitle());
            descriptionEditText.setText(note.getDescription());

            materialToolbar.setTitle(R.string.edit_your_note);
            removeButton.setVisibility(View.VISIBLE);

            saveButton.setOnClickListener(v -> editNote());
            removeButton.setOnClickListener(v -> removeNote());
        }
    }

    private void addNote() {

        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        Date currentDate = new Date();
        DateFormat df = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault());
        String date = getString(R.string.created_at) + " " + df.format(currentDate);
        Intent intent = new Intent();

        if (title.isEmpty()) {

            titleEditText.setError(getString(R.string.title_required));
            return;
        }

        note.setTitle(title);
        note.setDescription(description);
        note.setDate(date);

        database.noteDao().insert(note);
        intent.putExtra("id", note.getId());
        setResult(RESULT_ADD, intent);
        finish();
    }

    private void editNote() {

        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        Date currentDate = new Date();
        DateFormat df = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault());
        String date = getString(R.string.edited_at) + " " + df.format(currentDate);
        Intent intent = new Intent();

        if (title.isEmpty()) {

            titleEditText.setError(getString(R.string.title_required));
            return;
        }

        note.setTitle(title);
        note.setDescription(description);
        note.setDate(date);

        database.noteDao().update(note);
        intent.putExtra("id", note.getId());
        setResult(RESULT_UPDATE, intent);
        finish();
    }

    private void removeNote() {

        database.noteDao().delete(note);
        setResult(RESULT_DELETE, new Intent());
        finish();
    }
}