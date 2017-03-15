package info.dourok.android.demo.intent;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import info.dourok.android.demo.LogUtils;
import info.dourok.android.demo.R;

public class IntentDemoActivity extends Activity {

    private static final String TAG = "IntentDemoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_demo);
        Log.d(TAG, LogUtils.dumpIntent(getIntent()));
        Location location = getIntent().getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        Log.d(TAG, location.toString());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "new Intent");
        Log.d(TAG, LogUtils.dumpIntent(intent));
    }
}
