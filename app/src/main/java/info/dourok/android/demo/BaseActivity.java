package info.dourok.android.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import static info.dourok.android.demo.LogUtils.LOGD;
import static info.dourok.android.demo.LogUtils.LOGE;
import static info.dourok.android.demo.LogUtils.makeLogTag;

/**
 * @author Tiou Lims
 */
public abstract class BaseActivity extends
        AppCompatActivity {
    private final static int serialVersionUID = 1325347200;
    private final static String JUDGE_KEY = "update_code";

    protected final String TAG = "V:" + makeLogTag(((Object) this).getClass());
    int hash = System.identityHashCode(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        d("onCreate:" + hash);
        if (savedInstanceState == null) {
            d("savedInstanceState: null");
        } else {
            d(debugBundle(savedInstanceState));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        d("onStart:" + hash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        d("onResume:" + hash);
    }

    @Override
    protected void onPause() {
        super.onPause();
        d("onPause:" + hash);
    }

    @Override
    protected void onStop() {
        super.onStop();
        d("onStop:" + hash);
    }

    @Override
    protected void onDestroy() {
        d("onDestroy:" + hash);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        d("onSaveInstanceState:" + hash);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        d("onRestoreInstanceState:" + hash);
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }


    protected void onResumeFragments() {
        d("onResumeFragments");
    }


    protected void d(String msg) {
        LOGD(TAG, msg);
    }

    protected void d(String msg, Throwable ex) {
        LOGE(TAG, msg, ex);
    }

    protected String debugBundle(Bundle bundle) {
        StringBuilder builder = new StringBuilder();
        for (String key : bundle.keySet()) {
            Object obj = bundle.get(key);
            builder.append("Key:").append(key).append("\n");
            builder.append("Type:").append(obj.getClass().getName()).append('\n');
            builder.append("Value:").append(obj).append('\n');
        }
        return builder.toString();
    }
}
