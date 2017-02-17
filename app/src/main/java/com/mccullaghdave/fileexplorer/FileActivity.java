package com.mccullaghdave.fileexplorer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileActivity extends AppCompatActivity {

    private static final String TAG = FileActivity.class.getSimpleName();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final String COMICS_DIRECTORY = "Comics";
    private static final String META_FILE = "meta.txt";

    private TextView logView;

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

        // check if we have write permission for storage
        if (verifyStoragePermissions(this)) {
            doTheStorageStuffHere();
        }
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

            writeTestFile(comicDir);
            readTestFile(comicDir);
        } else {
            error("Storage cannot be written to, please ensure your SD card is properly mounted and is not mounted via USB");
        }
    }

    private void writeTestFile(final File directory) {
        final File metaFile = new File(directory, META_FILE);

        try {
            final String content = "Test context " + DateFormat.getDateTimeInstance().format(new Date());
            FileUtils.write(metaFile, content, UTF_8);
        } catch (final IOException e) {
            e.printStackTrace();
            error(e.getMessage());
        }
    }

    private void readTestFile(final File directory) {
        final File metaFile = new File(directory, META_FILE);
        try {
            final String content = FileUtils.readFileToString(metaFile, UTF_8);
            info("File contents are: " + content);
        } catch (final IOException e) {
            e.printStackTrace();
            error(e.getMessage());
        }
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

}
