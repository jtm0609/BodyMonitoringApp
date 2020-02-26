package com.jtmcompany.waist_guard_project.RecrclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.waist_guard_project.R;
import com.jtmcompany.waist_guard_project.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    ArrayList<User> items=new ArrayList<User>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.user_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user=items.get(position);
        holder.setItem(user);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(User item){
        items.add(item);
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name_text;
        TextView phone_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_text=itemView.findViewById(R.id.Item_Name);
            phone_text=itemView.findViewById(R.id.Item_Phone);

        }
        public void setItem(User item){
            name_text.setText(item.getName());
            phone_text.setText(item.getPhoneNumber());

        }

    }

}
