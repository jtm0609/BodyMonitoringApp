package com.jtmcompany.waist_guard_project.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Map;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{
    private Context context;

    public FriendAdapter(Context context) {
        this.context = context;
    }

    ArrayList<User> items=new ArrayList<User>();
    Map<String,String> friend_name_uid=new HashMap<>();
    private friendRecyclerListener mListener;

    public interface friendRecyclerListener{
        void onSensorButtonClicked(int position, String name, String uid);
        void onCallButtonClicked(int position,String uid);
    }

    public void setOnclickListener(friendRecyclerListener listener){
        mListener=listener;
    }

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {



        Log.d("TAK","TEST: "+"onBindViewHolder");
        User user=items.get(position);
        holder.setItem(user);

        final String name=user.getName();
        if(mListener!=null) {
            final String uid=friend_name_uid.get(name);
            holder.sensorInfoBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mListener.onSensorButtonClicked(holder.getAdapterPosition(),name,uid);
                }
            });

            holder.callBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onCallButtonClicked(holder.getAdapterPosition(),uid);
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
    public void addItemHash(String name,String uid){
        friend_name_uid.put(name,uid);
    }


     class ViewHolder extends RecyclerView.ViewHolder{
        TextView name_text;
        Button sensorInfoBt;
        Button callBt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_text=itemView.findViewById(R.id.Item_Name);
            sensorInfoBt=itemView.findViewById(R.id.sensorInfoBt);
            callBt=itemView.findViewById(R.id.callBt);
            Log.d("TAK","TEST: "+"ViewHolder");

            SharedPreferences sharedPref=context.getSharedPreferences("user", Activity.MODE_PRIVATE);
            boolean isGuardian=sharedPref.getBoolean("isGuardian",false);
            //사용자라면
            if(!isGuardian) sensorInfoBt.setVisibility(View.GONE);

        }
        public void setItem(User item){
            name_text.setText(item.getName());
            Log.d("TAK","TEST: "+"ViewHolder-setItem");

        }

    }

}
