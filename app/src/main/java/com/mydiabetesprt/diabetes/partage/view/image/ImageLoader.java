package com.mydiabetesprt.diabetes.partage.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.mydiabetesprt.diabetes.partage.data.file.FileUs;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageLoader {

    private static ImageLoader instance;

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    private Picasso picasso;

    private ImageLoader() {

    }

    public void init(Context context) {
        picasso = new Picasso.Builder(context).build();
    }

    public void clearDiskCache(Context context) {
        File cache = new File(context.getApplicationContext().getCacheDir(), "picasso-cache");
        if (cache.exists() && cache.isDirectory()) {
            FileUs.deleteDirectory(cache);
        }
    }


    public void load(@DrawableRes int imageRes, @DrawableRes int placeholderRes, ImageView imageView, Bitmap.Config config) {
        picasso.load(imageRes).placeholder(placeholderRes).config(config).into(imageView);
    }

    public void load(@DrawableRes int imageRes, ImageView imageView) {
        picasso.load(imageRes).into(imageView);
    }
}
