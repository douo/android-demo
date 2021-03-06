package info.dourok.android.demo.savestate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import info.dourok.android.demo.BaseActivity;
import info.dourok.android.demo.R;

public class FragmentStateDemoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_state_demo);

        SimpleTextFragment f = new SimpleTextFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, f, "frag").commit();
        Button button = (Button) findViewById(R.id.toggle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getSupportFragmentManager().findFragmentByTag("frag");
                if (f.isDetached()) {
                    getSupportFragmentManager().beginTransaction().attach(f).commit();
                } else {
                    /**
                     * detach 会使 fragment 到 destroy view 状态
                     * 其 view 树 会分发 save instance state 事件
                     * 但 fragment 本身不会 save instance state
                     */
                    getSupportFragmentManager().beginTransaction().detach(f).commit();
                }
            }
        });

    }
}
