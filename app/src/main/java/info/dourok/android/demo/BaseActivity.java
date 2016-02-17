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
            d(LogUtils.debugBundle(savedInstanceState));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        d("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        d("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        d("onStop");
    }

    @Override
    protected void onDestroy() {
        d("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        d("onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        d("onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }


    /**
     * 必须调用父类的方法，给 Fragment 分派 onResume 回调
     */
    protected void onResumeFragments() {
        super.onResumeFragments();
        d("onResumeFragments");
    }

    protected void d(String msg) {
        LOGD(String.format("%s:0x%X", TAG, hash), msg);
    }

    protected void d(String msg, Throwable ex) {
        LOGE(TAG, msg, ex);
    }

}