package com.mccullaghdave.fileexplorer;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileActivity extends AppCompatActivity {

    private static final String TAG = FileActivity.class.getSimpleName();

    private static final String PREF_KEY_STORAGE_DIRECTORIES = "PREF_KEY_STORAGE_DIRECTORIES";
    private static final String COMICS_DIRECTORY = "Comics";

    private ListView directoryList;
    private SharedPreferences prefs;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate() Restoring previous state");
        } else {
            Log.d(TAG, "onCreate() No saved state available, initializing app");
            addDefaultComicDirectoryToSharedPreferences();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        directoryList = (ListView) this.findViewById(R.id.storage_directory_list);
        listAllComicDirectories();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void addDefaultComicDirectoryToSharedPreferences() {
        final List<String> directories = retrieveStorageDirectories();
        final Editor editor = prefs.edit();
        editor.putStringSet(PREF_KEY_STORAGE_DIRECTORIES, new HashSet<>(directories));
        editor.apply();
    }

    private void listAllComicDirectories() {
        final ListAdapter adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                retrieveStorageDirectories());

        directoryList.setAdapter(adapter);
    }

    private List<String> retrieveStorageDirectories() {
        final Set<String> set = prefs.getStringSet(PREF_KEY_STORAGE_DIRECTORIES, new HashSet<String>());
        if (set.isEmpty()) {
            set.add(COMICS_DIRECTORY);
        }

        return new ArrayList<>(set);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("File Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
