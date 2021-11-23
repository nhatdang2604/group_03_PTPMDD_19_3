package com.example.gallery;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class AlbumUtility {
    private SharedPreferences sharedPreferences;
    private static AlbumUtility instance;
    private static final String ALL_ALBUM_KEY = "album_list";
    private static final String ALL_ALBUM_DATA_KEY = "album_data";

    private AlbumUtility(Context context) {
        sharedPreferences = context.getSharedPreferences("albums_database", Context.MODE_PRIVATE);
        if (getAllAlbums() == null || getAllAlbumsData() == null) {
            initData();
        }
    }

    public static AlbumUtility getInstance(Context context) {
        if (null == instance)
            instance = new AlbumUtility(context);
        return instance;
    }

    public ArrayList<String> getAllAlbums() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(ALL_ALBUM_KEY, null), type);
    }

    public ArrayList<AlbumData> getAllAlbumsData() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<AlbumData>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(ALL_ALBUM_DATA_KEY, null), type);
    }


    public void setAllAlbums(ArrayList<String> albums) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ALL_ALBUM_KEY);
        editor.putString(ALL_ALBUM_KEY, gson.toJson(albums));
        editor.apply();
    }

    private void initData() {
        ArrayList<String> albums = new ArrayList<String>();
        albums.add("Cats");
        albums.add("Dogs");
        albums.add("Food");
        albums.add("Holiday");
        albums.add("Parties");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(ALL_ALBUM_KEY, gson.toJson(albums));
        editor.putString(ALL_ALBUM_DATA_KEY, gson.toJson(new ArrayList<AlbumData>()));
        editor.apply();
    }

    public boolean addNewAlbum(String albumName) {
        ArrayList<String> albums = getAllAlbums();
        if (albums != null)
            if (albums.add(albumName)) {
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ALL_ALBUM_KEY);
                editor.putString(ALL_ALBUM_KEY, gson.toJson(albums));
                editor.apply();
                return true;
            }
        return false;
    }

    public boolean addNewAlbumData(AlbumData albumData) {
        ArrayList<AlbumData> albumDatas = getAllAlbumsData();
        if (albumDatas != null)
            if (albumDatas.add(albumData)) {
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ALL_ALBUM_DATA_KEY);
                editor.putString(ALL_ALBUM_DATA_KEY, gson.toJson(albumDatas));
                editor.apply();
                return true;
            }
        return false;
    }

    public boolean deleteAlbum(String albumName) {
        ArrayList<String> albums = getAllAlbums();
        if (albums != null)
            for (String album: albums)
                if (album.equals(albumName))
                    if (albums.remove(album)) {
                        Gson gson = new Gson();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(ALL_ALBUM_KEY);
                        editor.putString(ALL_ALBUM_KEY, gson.toJson(albums));
                        editor.apply();
                        return true;
                    }
        return false;
    }
}
