package com.mccullaghdave.fileexplorer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ComicAdapter extends BaseAdapter {

    private static final String TAG = ComicAdapter.class.getSimpleName();

    private Context context;
    private File[] cbFiles;

    public ComicAdapter(final Context context, final File[] cbFiles) {
        this.context = context;
        this.cbFiles = cbFiles;
    }

    @Override
    public int getCount() {
        return cbFiles.length;
    }

    @Override
    public File getItem(final int position) {
        return cbFiles[position];
    }

    @Override
    public long getItemId(final int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View layout = getView(convertView);
        final File file = getItem(position);
        final TextView title = (TextView) layout.findViewById(R.id.comic_list_item_title);

        if (isViewDisplayingThisFile(file, title)) {
            return layout;
        }

        final ZipMeta meta = getZipMeta(file);
        final TextView pages = (TextView) layout.findViewById(R.id.comic_list_item_pages);
        final ImageView image = (ImageView) layout.findViewById(R.id.comic_list_item_image);

        title.setText(file.getName());
        image.setImageBitmap(meta.mThumb);
        pages.setText(String.valueOf(meta.mPages));

        return layout;
    }

    private View getView(final View convertView) {
        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.comic_list_item, null);
        } else {
            return convertView;
        }
    }

    private boolean isViewDisplayingThisFile(final File file, final TextView title) {
        return file.getName().equals(title.getText());
    }

    private ZipMeta getZipMeta(final File file) {
        try {
            final ZipFile zipFile = new ZipFile(file);
            final ZipEntry zipEntry = zipFile.entries().nextElement();
            final InputStream is = zipFile.getInputStream(zipEntry);

            final BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.outWidth = R.dimen.comic_thumb_width;
            opts.outHeight = R.dimen.comic_thumb_height;
            return new ZipMeta(zipFile.size(), BitmapFactory.decodeStream(is, null, opts));
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return new ZipMeta(-1, null);
    }

    private class ZipMeta {
        final int mPages;
        final Bitmap mThumb;

        ZipMeta(final int pages, final Bitmap image) {
            mPages = pages;
            mThumb = image;
        }
    }
}