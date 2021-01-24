package com.example.MyFirstProject;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyFirstProject.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<Note> list;
    private Context context;

    MyAdapter(ArrayList<Note> list, Context context){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.lv_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Note note = list.get(position);
        holder.tv1.setText(note.getTime());
        holder.tv2.setText(note.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), DescriptionActivity.class);
                intent.putExtra(Note.class.getSimpleName(), note);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1, tv2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv1 = itemView.findViewById(R.id.tv);
            tv2 = itemView.findViewById(R.id.tv2);
        }
    }
}
