package info.dourok.android.demo.sdcard;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;

import java.io.File;

import info.dourok.android.demo.R;

public class SDCardDemoActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdcard_demo);

        StorageOptions.determineStorageOptions();
        Log.w("DS","count:"+StorageOptions.count);
        for(String label:StorageOptions.labels){
            Log.w("DS","label:"+label);
        }
        for(String path:StorageOptions.paths){
            Log.w("DS","path:"+path);
        }

        File[] fs = getExternalFilesDirs(null);
        Log.w("DS","fs:"+fs.length);
        for(File f:fs){
            Log.w("DS","path:"+f);
        }

    }
}
