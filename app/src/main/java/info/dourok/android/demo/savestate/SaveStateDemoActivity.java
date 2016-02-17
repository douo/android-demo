package info.dourok.android.demo.savestate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import info.dourok.android.demo.BaseActivity;
import info.dourok.android.demo.R;

public class SaveStateDemoActivity extends BaseActivity {
    Button button;
    TextView textView;
    int rand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_state_demo1);
        button = (Button) findViewById(R.id.toggle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(Integer.toString(rand));
                //设置了 android:freezesText="true" ，让 Activity 重启后 TextView 还能保留原来的文本值
                textView.setFreezesText(true);
            }
        });
        textView = (TextView) findViewById(R.id.textView);
        d("textView：" + textView.getText()); // 此时 savedInstanceState 的数据还未恢复到 View 中
        if (savedInstanceState == null) {
            rand = new Random().nextInt(10000);
        } else {
            rand = savedInstanceState.getInt("rand");
        }
    }


    /**
     * onRestoreInstanceState 与 onCreate 的区别：
     * - onRestoreInstanceState 仅当 saveInstanceState 不为 null 时才会调用，也就是说这个方法可以断言 Activity 是处于恢复状态中；而 onCreate 需要判断 saveInstanceState 是否为 null
     * - 所处的生命周期不同，onRestoreInstanceState 在 onStart 与 onResume 之间调用，也就是此时 UI 已经初始化好了，Activity 也获得当前窗口的控制权，就差绘制在屏幕上了；而 onCreate 中甚至可以在 setContentView 之前恢复状态。
     * - 这个方法也是 Activity 恢复当前存储的 View's SaveState 的地方{@link android.view.Window#restoreHierarchyState}
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        d("textView：" + textView.getText()); // 此时 savedInstanceState 的数据**已经**恢复到 View 中
    }

    /**
     * 什么样的情况下会调用这个方法
     * - onConfigurationChange
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("rand", rand);
    }
}
