package com.projectmanage.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import com.projectmanage.Models.UsersObject;
import com.projectmanage.R;

import java.util.ArrayList;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private ArrayList<UsersObject> userList;
    private Context context;
    private OnUserListener onUserListener;


    public UserListAdapter (ArrayList<UsersObject> userList,Context context,OnUserListener onUserListener){
        this.userList = userList;
        this.context = context;
        this.onUserListener = onUserListener;
    }
    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_users,null);
        UserListViewHolder  viewHolder = new UserListViewHolder(view,onUserListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserListViewHolder viewHolder, int i) {

        String userName = userList.get(i).getUserName();
        String userStatus = userList.get(i).getUserStatus();
        viewHolder.userNames.setText(userName);
        viewHolder.userStatus.setText(userStatus);

    }

    @Override
    public int getItemCount() {
        return userList.size();

    }

    public class UserListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView userNames;
        TextView userStatus;
        OnUserListener onUserListener;

        public UserListViewHolder(@NonNull View itemView,OnUserListener onUserListener) {
            super(itemView);
            this.onUserListener = onUserListener;

            userNames = itemView.findViewById(R.id.userListNames);
            userStatus = itemView.findViewById(R.id.userStatus);
            userStatus.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            onUserListener.onUserClick(getAdapterPosition(),view);


        }
    }

    public interface OnUserListener{
        void onUserClick(int position,View view);
    }

}
