package com.projectmanage.Notes;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.projectmanage.R;


public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mNoteText;
    public TextView mNoteDate;

    public NoteViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mNoteText = itemView.findViewById(R.id.noteText);
        mNoteDate = itemView.findViewById(R.id.noteDate);


    }

    @Override
    public void onClick(View v) {

    }
}
