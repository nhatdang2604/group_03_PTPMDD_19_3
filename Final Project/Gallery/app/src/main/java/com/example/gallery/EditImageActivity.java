package com.example.gallery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.effect.EffectFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.io.FileOutputStream;

public class EditImageActivity extends AppCompatActivity implements EditCallbacks{

    ImageView imgEdit;
    BottomNavigationView bottomNavEdit;
    Fragment currentFragment;
    RotateFragment rotateFragment;
    FilterFragment filterFragment;
    Bitmap currentBitmap;
    String pathPictureFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        changeTheme(checkTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_image_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        imgEdit = (ImageView) findViewById(R.id.imgEdit);
        pathPictureFile = intent.getStringExtra("pathToPictureFolder");
        Glide.with(this).asBitmap().load(pathPictureFile).into(imgEdit);
        currentBitmap = BitmapFactory.decodeFile(pathPictureFile);
        filterFragment = new FilterFragment(this, currentBitmap);
        rotateFragment = new RotateFragment(this, currentBitmap);
        currentFragment = rotateFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.editFragment, currentFragment)
                .commit();

        bottomNavEdit = (BottomNavigationView) findViewById(R.id.bottomNavEdit);
        bottomNavEdit.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (R.id.nav_rotate == id) {
                    currentFragment = rotateFragment;
                }
                if (R.id.nav_filter == id) {
                    currentFragment = filterFragment;
                }
                if (currentFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.editFragment, currentFragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });

    }

    private void changeTheme(boolean isChecked) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.Theme_Gallery_);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.Theme_Gallery);
        }
    }

    private boolean checkTheme() {
        SharedPreferences preferencesContainer = getSharedPreferences("app theme", Activity.MODE_PRIVATE);
        boolean theme = false;
        if (preferencesContainer != null && preferencesContainer.contains("dark mode"))
            theme = preferencesContainer.getBoolean("dark mode", false);
        return theme;
    }

    @Override
    public void onMsgFromFragToEdit(String sender, Bitmap request) {
        switch (sender) {
            case "FILTER-FLAG":
                Glide.with(this).asBitmap().load(request).into(imgEdit);
                currentBitmap = request;
                rotateFragment.onMsgFromMainToFrag(request);
                break;
            case "ROTATE-FLAG":
                Glide.with(this).asBitmap().load(request).into(imgEdit);
                currentBitmap = request;
                filterFragment.onMsgFromMainToFrag(request);
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (R.id.menuReset == id) {
            Glide.with(this).asBitmap().load(pathPictureFile).into(imgEdit);
            rotateFragment.onMsgFromMainToFrag(null);
            filterFragment.onMsgFromMainToFrag(null);
            return true;
        }
        if (R.id.menuSave == id) {
            saveImage(currentBitmap);
        }
        if (android.R.id.home == id) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void saveImage(Bitmap bitmap) {
        String pathFile = pathPictureFile.substring(0, pathPictureFile.lastIndexOf("/"));
        File pictureFile = new File(pathFile, bitmap.toString() + ".jpg");
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            Toast.makeText(this, "Saved to " + pathFile, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Error to save image in edit ", e.getMessage());
        }
    }
}
