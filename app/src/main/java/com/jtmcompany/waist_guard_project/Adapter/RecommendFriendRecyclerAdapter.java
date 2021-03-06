package com.jtmcompany.waist_guard_project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.waist_guard_project.Model.User;
import com.jtmcompany.waist_guard_project.R;

import java.util.ArrayList;
import java.util.List;

public class RecommendFriendRecyclerAdapter extends RecyclerView.Adapter<RecommendFriendRecyclerAdapter.ViewHoloder>{

    public interface MyRecyclerViewClickListener{
        void onButtonClicked(int position, String name);
    }

    private MyRecyclerViewClickListener mListener;

    public void setOnClickListener(MyRecyclerViewClickListener listener){
        mListener=listener;
    }

    List<User> items= new ArrayList<User>();
    @NonNull
    @Override
    public ViewHoloder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.recommend_friend_item,parent,false);
        return new ViewHoloder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHoloder holder, int position) {
        User user=items.get(position);
        holder.setItem(user);

        if(mListener!=null){
            //final int pos=position;
            holder.Button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mListener.onButtonClicked(holder.getAdapterPosition(),holder.Name_text.getText().toString());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(User item){
        items.add(item);
    }

    public class ViewHoloder extends RecyclerView.ViewHolder{
        TextView Name_text,Phone_text;
        Button Button;
        public ViewHoloder(@NonNull View itemView) {
            super(itemView);
            Name_text=itemView.findViewById(R.id.name_text);
            Phone_text=itemView.findViewById(R.id.phone_text);
            Button=itemView.findViewById(R.id.button);

        }

        public void setItem(User user) {
            Name_text.setText(user.getName());
            Phone_text.setText(user.getPhoneNumber());

        }
    }
}
