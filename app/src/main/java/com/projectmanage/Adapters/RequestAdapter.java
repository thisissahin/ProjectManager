package com.projectmanage.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectmanage.Models.RequestObject;
import com.projectmanage.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private ArrayList<RequestObject> mData;
    private Context context;

    public RequestAdapter(ArrayList<RequestObject> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project_request, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.myTextView.setText(mData.get(position).getProjectName());



        holder.myTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(context,view);
                popupMenu.getMenuInflater().inflate(R.menu.request_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String requestKey = mData.get(position).getProjectKey();
                        String requestUser = mData.get(position).getRequestUser();

                        if(item.getItemId() == (R.id.accept)){

                            DatabaseReference checkDatabase = FirebaseDatabase.getInstance().getReference().child("Projects").child(requestKey);
                            checkDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    String requestKey = mData.get(position).getProjectKey();
                                    String requestUser = mData.get(position).getRequestUser();
                                    String projectName = mData.get(position).getProjectName();
                                    if(dataSnapshot.exists()){
                                        Map newUser = new HashMap();
                                        DatabaseReference userDatabase =FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Projects").child(requestKey);
                                        newUser.put("createdBy",requestUser);
                                        newUser.put("projectName",projectName);
                                        userDatabase.setValue(newUser);
                                        DatabaseReference projetDatabase = FirebaseDatabase.getInstance().getReference().child("Projects").
                                                child(requestKey).child("Users").child(currentUserId);
                                        newUser.clear();
                                        newUser.put("user",currentUserId);
                                        newUser.put("Status","Member");
                                        projetDatabase.setValue(newUser);
                                        DatabaseReference deleteRequest = FirebaseDatabase.getInstance().getReference().child("Users").
                                                child(currentUserId).child("Requests").child(requestKey);
                                        deleteRequest.removeValue();
                                        mData.remove(position);
                                        notifyDataSetChanged();
                                    }
                                    else {
                                        Toast.makeText(context,"This project is deleted!",Toast.LENGTH_SHORT).show();
                                        DatabaseReference deleteRequest = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Requests").child(requestKey);
                                        deleteRequest.removeValue();
                                        mData.remove(position);
                                        notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        if (item.getItemId()==(R.id.decline)){
                            DatabaseReference deleteRequest = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Requests").child(requestKey);
                            deleteRequest.removeValue();
                            mData.remove(position);
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
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);


            myTextView = itemView.findViewById(R.id.requestText);

        }


    }




}