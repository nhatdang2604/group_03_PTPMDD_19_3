package com.example.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    String[] filters;
    Context context;
    Bitmap orginalBmp, editBmp;

    FilterAdapter(String[] filters, Context context, Bitmap originalBmp, Bitmap editBmp) {
        this.filters = filters;
        this.context = context;
        this.orginalBmp = originalBmp;
        this.editBmp = editBmp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FilterUtility filterUtility = new FilterUtility();
        holder.filterName.setText(String.valueOf(filters[position]));
        Bitmap newBitmap = filterUtility.setFilter(editBmp, holder.filterName.getText().toString());
        Glide.with(context).asBitmap().load(filterUtility.setFilter(orginalBmp, holder.filterName.getText().toString())).into(holder.filterThumbnail);

        holder.filterThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditImageActivity) context).onMsgFromFragToEdit("FILTER-FLAG", newBitmap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView filterName;
        ImageView filterThumbnail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filterName = (TextView) itemView.findViewById(R.id.filterName);
            filterThumbnail = (ImageView) itemView.findViewById(R.id.filterThumbnail);
        }
    }
}
