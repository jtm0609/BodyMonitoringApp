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
import java.util.List;

public class Recommend_FriendAdapter extends RecyclerView.Adapter<Recommend_FriendAdapter.ViewHoloder>{

    List<User> items= new ArrayList<User>();
    @NonNull
    @Override
    public ViewHoloder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.recommend_friend_item,parent,false);
        return new ViewHoloder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoloder holder, int position) {
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

    public class ViewHoloder extends RecyclerView.ViewHolder{
        TextView Name_text;
        TextView Phone_text;
        public ViewHoloder(@NonNull View itemView) {
            super(itemView);
            Name_text=itemView.findViewById(R.id.name_text);
            Phone_text=itemView.findViewById(R.id.phone_text);
        }

        public void setItem(User user) {
            Name_text.setText(user.getName());
            Phone_text.setText(user.getPhoneNumber());

        }
    }
}
