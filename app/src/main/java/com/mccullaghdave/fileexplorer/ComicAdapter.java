package com.mccullaghdave.fileexplorer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class ComicAdapter extends BaseAdapter {

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
        View layout;

        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.comic_list_item, null);
        } else {
            layout = convertView;
        }

        final TextView text = (TextView) layout.findViewById(R.id.comic_list_item_text);
        text.setText(cbFiles[position].getName());

//        final ImageView image = (ImageView) layout.findViewById(R.id.comic_list_item_image);
//        image.setImageResource(R.mipmap.ic_launcher);

        return layout;
    }
}