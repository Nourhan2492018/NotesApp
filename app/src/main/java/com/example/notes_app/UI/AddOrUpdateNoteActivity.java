package com.example.notes_app.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.notes_app.R;
import com.example.notes_app.databinding.ActivityAddUpdateNoteBinding;

public class AddOrUpdateNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID="com.example.todolist_app.EXTRA_ID";

    public static final String EXTRA_TITLE="com.example.todolist_app.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION="com.example.todolist_app.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY="com.example.todolist_app.EXTRA_PRIORITY";

    private  ActivityAddUpdateNoteBinding activityBinding;
    /*private EditText eidtTextTitle;
      private EditText eidtTextDescription;
      private NumberPicker numberPickerPriority;
    */
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_update_note);

         activityBinding=
                DataBindingUtil.setContentView(this,R.layout.activity_add_update_note);

        //eidtTextTitle=findViewById(R.id.edit_text_title);
        //eidtTextDescription=findViewById(R.id.edit_text_description);
        //numberPickerPriority=findViewById(R.id.number_picker_priority);

        activityBinding.numberPickerPriority.setMinValue(0);
        activityBinding.numberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        //update
        Intent intent=getIntent();
        //
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            activityBinding.editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            activityBinding.editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            activityBinding.numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));

        }
        else {
            setTitle("Add Note");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                SaveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void SaveNote()
    {
     String title=activityBinding.editTextTitle.getText().toString();
     String description=activityBinding.editTextDescription.getText().toString();
     int priority =activityBinding.numberPickerPriority.getValue();
     if(title.trim().isEmpty()||description.trim().isEmpty())
     {
          Toast.makeText(AddOrUpdateNoteActivity.this,"You Should Insert Title And Description To Save This Note",Toast.LENGTH_SHORT)
                  .show();
          return;

     }
     Intent data=new Intent();
     data.putExtra(EXTRA_TITLE,title);
     data.putExtra(EXTRA_DESCRIPTION,description);
     data.putExtra(EXTRA_PRIORITY,priority);
    //for Update
     int id=getIntent().getIntExtra(EXTRA_ID,-1);
      if(id!=-1)
      {
          data.putExtra(EXTRA_ID,id);
      }

     setResult(RESULT_OK,data);
      finish();
    }

}
