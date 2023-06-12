package com.example.notes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notes.model.Note;

@Database(entities = Note.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase database;
    private static final String DATABASE_NAME = "NoteDB";

    public synchronized static AppDatabase getInstance(Context context) {

        if (database == null) {

            database = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }

        return database;
    }

    public abstract NoteDao noteDao();
}
