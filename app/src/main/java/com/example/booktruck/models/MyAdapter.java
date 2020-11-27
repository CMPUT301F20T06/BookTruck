package com.example.booktruck.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.booktruck.GlideApp;
import com.example.booktruck.R;
import com.example.booktruck.UrlModel;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    ArrayList<UrlModel> mList;
    Context context;


    public MyAdapter(Context context, ArrayList<UrlModel> mList){

        this.context = context;
        this.mList = mList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        GlideApp.with(context).load(mList.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);

        holder.itemView.setOnClickListener (new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int ps = holder.getLayoutPosition();
                onItemClickListener.onItemClick(ps, v);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null? 0: mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public MyViewHolder(@NonNull View itemView){
            super (itemView);
            imageView = itemView.findViewById(R.id.m_image);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void deleteItem(int index){
        mList.remove(index);
        notifyItemRemoved(index);
    }

    public void addItem(UrlModel dataObj, int index){
        mList.add(dataObj);
        notifyItemInserted(index);
    }


    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }
}
