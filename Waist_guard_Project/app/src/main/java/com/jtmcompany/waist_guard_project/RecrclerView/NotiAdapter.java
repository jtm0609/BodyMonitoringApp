package com.jtmcompany.waist_guard_project.RecrclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.waist_guard_project.R;

import java.util.ArrayList;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.ViewHoler> {

    ArrayList<String> names=new ArrayList<String>();
    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.noti_item,parent,false);
        Log.d("TAAK","TEST: "+"onCreateViewHolder");
        return new ViewHoler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        String name=names.get(position);
        holder.setItem(name);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public void addItem(String item){
        names.add(item);
    }


    public class ViewHoler extends RecyclerView.ViewHolder {
        TextView requestName;
        public ViewHoler(View itemView) {
            super(itemView);
            requestName=itemView.findViewById(R.id.Noti_Item_Name);

        }

        public void setItem(String name) {
            Log.d("TAAK","TEST: "+name);
            requestName.setText(name);
        }
    }
}
