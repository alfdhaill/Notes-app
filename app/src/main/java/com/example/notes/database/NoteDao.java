package com.example.notes.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notes.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Query("SELECT * FROM note")
    List<Note> getAll();

    @Query("SELECT * FROM note WHERE id = :id")
    Note getNote(int id);

    @Query("SELECT * FROM note WHERE LOWER(title) LIKE :title OR LOWER(description) LIKE :description")
    List<Note> search(String title, String description);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
}
