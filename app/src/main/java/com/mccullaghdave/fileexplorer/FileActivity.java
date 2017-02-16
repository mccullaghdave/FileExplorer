package com.mccullaghdave.fileexplorer;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class FileActivity extends AppCompatActivity {
 private static final String TAG = FileActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate() Restoring previous state xxxxxxxxxxxxxxxxxxxxxxxx");
        } else {
            Log.d(TAG, "onCreate() No saved state availablexxxxxxxxxxxxxxxxxxxx");
            /* initialize app */
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
