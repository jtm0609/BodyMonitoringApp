package com.jtmcompany.waist_guard_project.RecrclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.waist_guard_project.R;
import com.jtmcompany.waist_guard_project.Model.User;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{
    ArrayList<User> items=new ArrayList<User>();



    //리사이클러뷰가 아이템을나타내기위한 타입의 새로운 리사이클러뷰의 뷰홀더를 필요로할때 호출됨
    //parent: 새로운뷰가 어댑터포지션에 바운드된후 그새로운뷰가 추가되어질 뷰그룹(리싸이클러뷰)
    //View.getConext()는 현재뷰가 가지고있는 Context반환(일반적으로 Activity의 Context)
    //onCreateViewHolder->ViewHolder->onBindVieHolder->ViewHolder->setItem
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.friend_item,parent,false);
        Log.d("TAK","TEST: "+"onCreateViewHolder");
        return new ViewHolder(itemView);

    }

    //특정위치에 데이터를 나타내기위해 리사이클러뷰에의해 호출됨
    //holder=onCreateViewHolder에서 반환한 ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("TAK","TEST: "+"onBindViewHolder");
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_text=itemView.findViewById(R.id.Item_Name);
            Log.d("TAK","TEST: "+"ViewHolder");

        }
        public void setItem(User item){
            name_text.setText(item.getName());
            Log.d("TAK","TEST: "+"ViewHolder-setItem");

        }

    }

}
