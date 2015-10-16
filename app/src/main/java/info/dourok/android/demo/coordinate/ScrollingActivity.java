package info.dourok.android.demo.coordinate;

import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import info.dourok.android.demo.R;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "test", Snackbar.LENGTH_SHORT).show();
            }
        });
        ((AppBarLayout) findViewById(R.id.appbar)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                findViewById(R.id.image).setScaleX(1 + i * 1.f / appBarLayout.getHeight());
                findViewById(R.id.image).setScaleY(1 + i * 1.f / appBarLayout.getHeight());
                System.out.println("onOffsetChanged:" + i + ",height:" + appBarLayout.getHeight());
            }
        });
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scroll);
        ((CoordinatorLayout) scrollView.getParent()).setStatusBarBackgroundColor(0xFF00FF00);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
