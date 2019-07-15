package com.projectmanage.Adapters;






import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectmanage.Models.NoteObject;
import com.projectmanage.Activities.Notes.NoteActivity;
import com.projectmanage.R;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;


public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private List<NoteObject> noteList;
    private Context context;
    private String currentUserId;


    public NoteAdapter(List<NoteObject> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;

    }





    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        NoteViewHolder rcv = new NoteViewHolder((layoutView));
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position) {

        holder.mNoteDate.setText(noteList.get(position).getDate());
        holder.mNoteText.setText(noteList.get(position).getNote());
        holder.mNoteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String editKey = noteList.get(position).getNoteKey();
                Intent i = new Intent(context, NoteActivity.class);
                i.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra("key",editKey);
                context.startActivity(i);



            }
        });
        holder.mNoteText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenuInflater().inflate(R.menu.note_popup_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId() == (R.id.noteDelete)){
                            String key = noteList.get(position).getNoteKey();

                            delete(key);
                            noteList.remove(position);
                            notifyDataSetChanged();
                        }


                        return true;
                    }

                });

                return true;
            }

        });


    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


    public void delete(String key){
        DatabaseReference deletedNote = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Notes").child(key);
        deletedNote.removeValue();

    }


}


