package com.example.client;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.itemViewHolder>{
    private FileData[] files;
    public RecyclerAdapter(FileData[] f) {
        files=f;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int layId=R.layout.storage_item;

        LayoutInflater inflater=LayoutInflater.from(context);

        View view=inflater.inflate(layId,parent,false);

        itemViewHolder viewHolder=new itemViewHolder(view);

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        holder.bind(files[position].getName(),false);
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    class itemViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        ImageView typeImage;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView=itemView.findViewById(R.id.fileName);
            typeImage=itemView.findViewById(R.id.iconView);
        }

        void bind(String name,boolean type){
            nameTextView.setText(name);
        }
    }

}
