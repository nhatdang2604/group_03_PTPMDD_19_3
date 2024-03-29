package com.example.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FilterFragment extends Fragment implements FragmentCallbacks {
    RecyclerView filterRecView;
    Context context;
    Bitmap originalBmp, editBmp;
    FilterAdapter adapter;
    String[] filters = { "No Effect", "Auto", "Cream", "Forest", "Cozy", "Blossom", "Evergreen", "Grayscale", "Sharpen", "Vintage"};

    FilterFragment(Context context, Bitmap bitmap) {
        this.context = context;
        this.originalBmp = bitmap;
        this.editBmp = bitmap.copy(bitmap.getConfig(), true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View filterFragment = inflater.inflate(R.layout.filter_image_fragment, container, false);
        filterRecView = (RecyclerView) filterFragment.findViewById(R.id.filterRecView);
        adapter = new FilterAdapter(filters, context, originalBmp, editBmp);
        filterRecView.setAdapter(adapter);
        filterRecView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        return filterFragment;
    }

    @Override
    public void onMsgFromMainToFrag(Bitmap result) {
        if (null != result)
            editBmp = result;
        else
            editBmp = originalBmp;
    }
}
