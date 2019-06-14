package com.projectmanage.Projects.Tasks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.projectmanage.R;

import java.util.ArrayList;
import java.util.List;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.CheckListViewholder> {

    private ArrayList<CheckListObject> checkList;
    private Context context;


    public CheckListAdapter (ArrayList<CheckListObject> checkList,Context context){
        this.checkList = checkList;
        this.context = context;
    }
    @NonNull
    @Override
    public CheckListViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_checklist,null);
        CheckListViewholder  viewHolder = new CheckListViewholder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListAdapter.CheckListViewholder viewHolder, int i) {

        String checkListText = checkList.get(i).getCheckListText();
        viewHolder.checkListEditText.setText(checkListText);


    }

    @Override
    public int getItemCount() {
        return checkList.size();
    }

    class CheckListViewholder extends RecyclerView.ViewHolder{

        EditText checkListEditText;

        public CheckListViewholder(@NonNull View itemView) {
            super(itemView);
            checkListEditText = itemView.findViewById(R.id.checkListText);
        }
    }
}
