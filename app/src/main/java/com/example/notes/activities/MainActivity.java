package com.example.notes.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.notes.R;
import com.example.notes.adapter.NoteAdapter;
import com.example.notes.database.AppDatabase;
import com.example.notes.model.Note;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ExecutorService service;
    private Handler handler;
    private AppDatabase database;
    private NoteAdapter adapter;
    private List<Note> notes;
    private List<Note> searchResults;
    private SearchView searchView;
    RecyclerView recyclerView;
    private String searchText = "";

    private final ActivityResultLauncher<Intent> resultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getData() != null) {
                            switch (result.getResultCode()) {
                                case MainActivity2.RESULT_ADD:

                                    Snackbar.make(findViewById(android.R.id.content),
                                                    getString(R.string.note_added), Snackbar.LENGTH_SHORT)
                                            .show();
                                    break;

                                case MainActivity2.RESULT_UPDATE:

                                    Snackbar.make(findViewById(android.R.id.content),
                                                    getString(R.string.note_edited), Snackbar.LENGTH_SHORT)
                                            .show();
                                    break;

                                case MainActivity2.RESULT_DELETE:

                                    Snackbar.make(findViewById(android.R.id.content),
                                                    getString(R.string.note_deleted), Snackbar.LENGTH_SHORT)
                                            .show();
                                    break;
                            }
                        }

                        service.execute(() -> {
                            notes = database.noteDao().getAll();
                            handler.post(() -> adapter.updateNotes(notes));
                        });
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchBar searchBar = findViewById(R.id.search_bar);
        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.rv_notes);
        ExtendedFloatingActionButton extendedFloatingActionButton = findViewById(R.id.extended_fab);

        extendedFloatingActionButton.setOnClickListener(v -> resultLauncher.launch
                (new Intent(this, MainActivity2.class)));

        service = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
        database = AppDatabase.getInstance(this);
        searchResults = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        search(searchText);

        searchView
                .getEditText()
                .setOnEditorActionListener(
                        (v, actionId, event) -> {

                            searchText = searchView.getText().toString().toLowerCase();
                            searchBar.setText(searchView.getText());
                            searchView.hide();

                            search(searchText);

                            return false;
                        });
    }

    private void search(String searchText) {

        searchResults = database.noteDao()
                .search("%" + searchText + "%", "%" + searchText + "%");
        adapter = new NoteAdapter(searchResults, resultLauncher);
        recyclerView.setAdapter(adapter);
    }
}