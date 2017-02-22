package com.mccullaghdave.fileexplorer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.io.FilenameFilter;

public class FileActivity extends AppCompatActivity {

    private static final String TAG = FileActivity.class.getSimpleName();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final String COMICS_DIRECTORY = "Comics";

    private TextView logView;
    private GridView comicsGridView;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate() Restoring previous state");
        } else {
            Log.d(TAG, "onCreate() No saved state available");
            /* initialize app */
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        // text logView to display log messages
        logView = (TextView) this.findViewById(R.id.logs);
        comicsGridView = (GridView) this.findViewById(R.id.comic_files);

        // check if we have write permission for storage
        if (verifyStoragePermissions(this)) {
            doTheStorageStuffHere();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public boolean verifyStoragePermissions(final Activity activity) {
        // Check if we have write permission
        final int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        return permission == PackageManager.PERMISSION_GRANTED;
    }

    // called when the user responds to the permission dialog
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                handleStoragePermissionResponse(grantResults);
                break;
        }
    }

    private void handleStoragePermissionResponse(final int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doTheStorageStuffHere();
        } else {
            error("User is a big fat meanie!");
        }
    }

    private void doTheStorageStuffHere() {
        if (isExternalStorageWritable()) {
            info("Storage state is writable");

            final File comicDir = getComicStorageDir();
            info("Comic directory is: " + comicDir);

            displayComicBookFiles(comicDir);
        } else {
            error("Storage cannot be written to, please ensure your SD card is properly mounted and is not mounted via USB");
        }
    }

    private void displayComicBookFiles(final File comicDir) {
        final FilenameFilter filter = new RegexFileFilter(".*\\.cb.$");
        final File[] files = comicDir.listFiles(filter);

        final ListAdapter adapter = new ComicAdapter(this, files);
        comicsGridView.setAdapter(adapter);
    }

    public File getComicStorageDir() {
        final File file = Environment.getExternalStoragePublicDirectory(COMICS_DIRECTORY);
        if (file.exists()) {
            info("Comic directory already existed");
        } else {
            if (file.mkdirs()) {
                info("Comic directory created");
            } else {
                error("Comic directory could not be created");
            }
        }
        if (!file.canRead()) {
            error("Comic directory is not readable");
        }
        if (!file.canWrite()) {
            error("Comic directory is not writable");
        }
        return file;
    }

    // write log messages to screen and logcat
    private void info(final String msg) {
        Log.i(TAG, msg);
        logView.append(System.lineSeparator());
        logView.append(msg);
    }

    // write log messages to screen and logcat
    private void error(final String msg) {
        Log.e(TAG, msg);
        logView.append(System.lineSeparator());
        logView.append(msg);
    }

    public boolean isExternalStorageWritable() {
        final String storageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(storageState);
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
