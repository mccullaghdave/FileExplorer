package com.mccullaghdave.fileexplorer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import static com.mccullaghdave.fileexplorer.FileActivity.*;

/**
 * Created by sean on 24/02/17.
 */
class OpenDirectoryClickListener implements AdapterView.OnItemClickListener {

    private static final String TAG = OpenDirectoryClickListener.class.getSimpleName();

    private final Context context;

    OpenDirectoryClickListener(final Context context) {
        this.context = context;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        final String directory = String.valueOf(parent.getAdapter().getItem(position));
        Log.i(TAG, "Opening directory, name=" + directory);

        final Intent intent = new Intent(context, ComicListActivity.class);
        intent.putExtra(EXTRAS_DIRECTORY, directory);
        context.startActivity(intent);
    }

}
