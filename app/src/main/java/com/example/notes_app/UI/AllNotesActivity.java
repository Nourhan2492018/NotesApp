package com.example.notes_app.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.notes_app.Model.Note;
import com.example.notes_app.ViewMadel.NoteViewModel;
import com.example.notes_app.R;
import com.example.notes_app.databinding.ActivityAllNotesBinding;

import java.util.List;

public class AllNotesActivity extends AppCompatActivity {
    public static final int ADD_NOTE_RQUEST=1;
    public static final int EIDT_NOTE_RQUEST=2;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_all_notes);

        ActivityAllNotesBinding activityAllNotesBinding=
                DataBindingUtil.setContentView(this,R.layout.activity_all_notes);

        //FloatingActionButton buttonAddNote=findViewById(R.id.button_add_note);
        activityAllNotesBinding.buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AllNotesActivity.this, AddOrUpdateNoteActivity.class);
                startActivityForResult(intent,ADD_NOTE_RQUEST);
            }
        });


        //RecyclerView recyclerView=findViewById(R.id.recycler_view_notes);
        activityAllNotesBinding.recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        //fixed size
        activityAllNotesBinding.recyclerViewNotes.setHasFixedSize(true);
        //
        final NoteAdapter noteAdapter=new NoteAdapter(this);
        activityAllNotesBinding.recyclerViewNotes.setAdapter(noteAdapter);
        LayoutAnimationController controller=null;
        Context context=activityAllNotesBinding.recyclerViewNotes.getContext();

        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_animation_slide_bottom);
        activityAllNotesBinding.recyclerViewNotes.setLayoutAnimation(controller);
        activityAllNotesBinding.recyclerViewNotes.getAdapter().notifyDataSetChanged();
        activityAllNotesBinding.recyclerViewNotes.scheduleLayoutAnimation();
        //

        noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //Update
               // Toast.makeText(MainActivity.this,"onChanged",Toast.LENGTH_SHORT)
                // .show();
                noteAdapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                 noteViewModel.deleteNote(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(AllNotesActivity.this," Delete This Note",Toast.LENGTH_SHORT)
                        .show();
            }
        }).attachToRecyclerView(activityAllNotesBinding.recyclerViewNotes);
        noteAdapter.setonItemClicklistener(new NoteAdapter.onItemClicklistener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent=new Intent(AllNotesActivity.this, AddOrUpdateNoteActivity.class);
                intent.putExtra(AddOrUpdateNoteActivity.EXTRA_ID,note.getNoteID());
                intent.putExtra(AddOrUpdateNoteActivity.EXTRA_TITLE,note.getNoteTitle());
                intent.putExtra(AddOrUpdateNoteActivity.EXTRA_DESCRIPTION,note.getNoteDescription());
                intent.putExtra(AddOrUpdateNoteActivity.EXTRA_PRIORITY,note.getNotPriority());
                startActivityForResult(intent,EIDT_NOTE_RQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADD_NOTE_RQUEST&&resultCode==RESULT_OK)
        {
            String title=data.getStringExtra(AddOrUpdateNoteActivity.EXTRA_TITLE);
            String description=data.getStringExtra(AddOrUpdateNoteActivity.EXTRA_DESCRIPTION);
            int priority=data.getIntExtra(AddOrUpdateNoteActivity.EXTRA_PRIORITY,1);
            Note note=new Note(title,description,priority);
            noteViewModel.insertNote(note);
            Toast.makeText(AllNotesActivity.this,"Save This Note",Toast.LENGTH_SHORT)
                    .show();

        }
        else if(requestCode==EIDT_NOTE_RQUEST&&resultCode==RESULT_OK)
        {
            int id=data.getIntExtra(AddOrUpdateNoteActivity.EXTRA_ID,-1);

            if(id==-1)
            {
                Toast.makeText(AllNotesActivity.this,"Note Can Not Update",Toast.LENGTH_SHORT)
                        .show();
                    return;
            }
            String title=data.getStringExtra(AddOrUpdateNoteActivity.EXTRA_TITLE);
            String description=data.getStringExtra(AddOrUpdateNoteActivity.EXTRA_DESCRIPTION);
            int priority=data.getIntExtra(AddOrUpdateNoteActivity.EXTRA_PRIORITY,1);
            Note  note =new Note(title,description,priority);
            note.setNoteID(id);
            noteViewModel.updataNote(note);
            Toast.makeText(AllNotesActivity.this," Update This Note",Toast.LENGTH_SHORT)
                    .show();

        }

        else
        {
            Toast.makeText(AllNotesActivity.this," Not Save This Note",Toast.LENGTH_SHORT)
                    .show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
                return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:

                noteViewModel.getAllNotes();
                Toast.makeText(AllNotesActivity.this,"Delete All Notes",Toast.LENGTH_SHORT)
                    .show();
                return true;
            default:
                    return super.onOptionsItemSelected(item);
        }


    }
}
