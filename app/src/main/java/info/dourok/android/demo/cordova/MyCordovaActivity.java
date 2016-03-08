package info.dourok.android.demo.cordova;

import android.os.Bundle;

import org.apache.cordova.CordovaActivity;

public class MyCordovaActivity extends CordovaActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init();
        launchUrl = "file:///android_asset/www/index.html";
        loadUrl(launchUrl);
    }
}
