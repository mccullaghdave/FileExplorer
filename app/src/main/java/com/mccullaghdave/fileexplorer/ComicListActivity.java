package com.mccullaghdave.fileexplorer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.io.FilenameFilter;

import static com.mccullaghdave.fileexplorer.FileActivity.EXTRAS_DIRECTORY;

public class ComicListActivity extends AppCompatActivity {

    private static final String TAG = ComicListActivity.class.getSimpleName();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private GridView comicsGridView;
    private String directory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_list);

        comicsGridView = (GridView) this.findViewById(R.id.comic_list);
        directory = this.getIntent().getExtras().getString(EXTRAS_DIRECTORY);

        if (verifyStoragePermissions()) {
            openDirectoryAndDisplayFiles();
        }
    }

    private boolean verifyStoragePermissions() {
        // Check if we have write permission
        final int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
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
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openDirectoryAndDisplayFiles();
        } else {
            Toast.makeText(this, "Storage permission was denied", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void openDirectoryAndDisplayFiles() {
        if (isExternalStorageWritable()) {
            final File comicDir = getComicStorageDir(directory);
            Log.i(TAG, "Comic directory is: " + comicDir);

            displayComicBookFiles(comicDir);
        } else {
            Log.e(TAG, "Storage cannot be written to, please ensure your SD card is properly mounted and is not mounted via USB");
        }
    }

    private void displayComicBookFiles(final File comicDir) {
        final FilenameFilter filter = new RegexFileFilter(".*\\.cb.$");
        final File[] files = comicDir.listFiles(filter);

        final ListAdapter adapter = new ComicAdapter(this, files);
        comicsGridView.setAdapter(adapter);
    }

    public File getComicStorageDir(final String directory) {
        final File file = Environment.getExternalStoragePublicDirectory(directory);
        if (file.exists()) {
            Log.i(TAG, "Comic directory already existed");
        } else {
            if (file.mkdirs()) {
                Log.i(TAG, "Comic directory created");
            } else {
                Log.e(TAG, "Comic directory could not be created");
            }
        }
        if (!file.canRead()) {
            Log.e(TAG, "Comic directory is not readable");
        }
        if (!file.canWrite()) {
            Log.e(TAG, "Comic directory is not writable");
        }
        return file;
    }

    public boolean isExternalStorageWritable() {
        final String storageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(storageState);
    }
}
