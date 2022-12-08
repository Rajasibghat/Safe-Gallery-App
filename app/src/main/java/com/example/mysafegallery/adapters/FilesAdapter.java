package com.example.mysafegallery.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mysafegallery.R;
import com.example.mysafegallery.interfaces.ClickListenerInterface;

import java.io.File;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FilesViewHolder> {
    private ClickListenerInterface clickListener;
    private List<File> filesList;

    public FilesAdapter(ClickListenerInterface clickListener){
        this.clickListener=clickListener;
    }
    public void setFiles(List<File> filesList){
        this.filesList=filesList;
    }
    @NonNull
    @Override
    public FilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.files_adapter_view,parent,false);
        return new FilesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesViewHolder holder, int position) {
        File file=filesList.get(position);
        Glide
                .with(holder.itemView)
                .load(file)
                .centerCrop()
                .placeholder(R.drawable.ic_loading)
                .into(holder.fileImage);
        holder.fileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(file);
            }
        });
        holder.fileImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clickListener.onItemLongClick(file);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    class FilesViewHolder extends RecyclerView.ViewHolder {
        ImageView fileImage;
        public FilesViewHolder(@NonNull View itemView) {
            super(itemView);
            fileImage=itemView.findViewById(R.id.fileThumbNail);
        }
    }
}
