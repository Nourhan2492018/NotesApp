package com.example.notes_app.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes_app.Model.Note;
import com.example.notes_app.R;
import com.example.notes_app.databinding.NoteCardBinding;

public class NoteAdapter extends ListAdapter <Note,NoteAdapter.NoteHolder> {
    //private List<Note> notes ;
    private onItemClicklistener listener;
    private Context context ;
    private static final DiffUtil.ItemCallback<Note>DIFF_CALLBACK=new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getNoteID()==newItem.getNoteID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getNoteTitle().equals(newItem.getNoteTitle())&&
                    oldItem.getNoteDescription().equals(newItem.getNoteDescription())&&
                    oldItem.getNotPriority()==newItem.getNotPriority();
        }
    };
    public NoteAdapter(Context context) {

        super(DIFF_CALLBACK);
        this.context=context;
    }
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        NoteCardBinding noteCardBinding=
                NoteCardBinding.inflate(layoutInflater,parent,false);
        return new NoteHolder(noteCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.onBindNote(getItem(position));
    }

   /*  @Override
    public int getItemCount() {
        return notes.size();
    }

   public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }
    */

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
            private NoteCardBinding noteCardBinding;
        public NoteHolder(@NonNull NoteCardBinding noteCardBinding) {
            super(noteCardBinding.getRoot());
              this.noteCardBinding=noteCardBinding;
            noteCardBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =getAdapterPosition();
                    if (listener!=null&&position!= RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(getItem(position));//notes.get(position)
                    }
                }
            });


        }
        private Animation animationTitle,animationDesctiption,animationPriority;
        public void onBindNote(Note note)
        {
            noteCardBinding.textViewTitle.setText(note.getNoteTitle());
            animationTitle= AnimationUtils.loadAnimation(context,R.anim.scale);
            noteCardBinding.textViewTitle.setAnimation(animationTitle);


            noteCardBinding.textViewDescription.setText(note.getNoteDescription());
            animationDesctiption= AnimationUtils.loadAnimation(context,R.anim.scale);
            noteCardBinding.textViewDescription.setAnimation(animationDesctiption);

            noteCardBinding.textViewPriority.setText(String.valueOf(note.getNotPriority()));
            animationPriority= AnimationUtils.loadAnimation(context,R.anim.transation);
            noteCardBinding.textViewDescription.setAnimation(animationPriority);

        }
    }

    public interface onItemClicklistener {
        public void onItemClick(Note note);
    }

    public void setonItemClicklistener(onItemClicklistener listener)
    {
        this.listener=listener;

    }

}
