package com.projectmanage.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectmanage.Models.CheckListObject;
import com.projectmanage.R;

import java.util.ArrayList;


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




        Boolean bool = checkList.get(i).getCheckBoxBool();
        String checkListText = checkList.get(i).getCheckListText();
        viewHolder.checkBox.setChecked(bool);
        viewHolder.checkListEditText.setText(checkListText);





    }

    @Override
    public int getItemCount() {
        return checkList.size();

    }

    class CheckListViewholder extends RecyclerView.ViewHolder{

        EditText checkListEditText;
        CheckBox checkBox;

        public CheckListViewholder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkListEditText = itemView.findViewById(R.id.checkListText);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkList.get(getAdapterPosition()).setCheckBoxBool(isChecked);
                 }
            });

            checkListEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    checkList.get(getAdapterPosition()).setCheckListText(checkListEditText.getText().toString());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

        }

        public void updateList(String projectKey,String taskKey,String taskState){

        for(int i = 0; i < checkList.size(); i++){
            String checkKey = checkList.get(i).getCheckKey();
            String text = checkList.get(i).getCheckListText();
            Boolean checkBool = checkList.get(i).getCheckBoxBool();

            DatabaseReference updateData = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey)
                    .child("Tasks").child(taskState).child(taskKey).child("checkList").child(checkKey);
            updateData.child("text").setValue(text);
            updateData.child("checkBox").setValue(checkBool);


        }


        }


}
