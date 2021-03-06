package com.example.notes_app.ViewMadel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notes_app.Model.Note;
import com.example.notes_app.Repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;


    private LiveData<List<Note>>allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository=new NoteRepository(application);
        allNotes=noteRepository.getAllNotes();
    }
    public void insertNote(Note note)
    {
        noteRepository.insertNote(note);
    }
    public void updataNote(Note note)
    {
        noteRepository.updataNote(note);
    }
    public void deleteNote(Note note)
    {
        noteRepository.deleteNote(note);
    }
    public void deleteAllNotes()
    {
        noteRepository.deleteAllNotes();
    }
    public LiveData<List<Note>>getAllNotes()
    {
        return allNotes;
    }
}
