package info.dourok.android.demo.pager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import info.dourok.android.demo.R;

public class PagerDemoActivity extends AppCompatActivity {
  private static final String TAG = "PagerDemoActivity";
  ViewPager pager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pager_demo);
    setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));
    pager = (ViewPager) findViewById(R.id.pager);
    //pager.setAdapter(new CustomPagerAdapter(this));
    //pager.setAdapter(new NestedPagerAdapter(null, getSupportFragmentManager()));
    pager.setAdapter(
        new HierarchyFragmentPagerAdapter(new NestedPagerAdapter(null, getSupportFragmentManager()),
            new HierarchyFragmentPagerAdapter.AdapterHolder() {
              @Override public HierarchyFragmentPagerAdapter getAdapter() {
                return (HierarchyFragmentPagerAdapter) pager.getAdapter();
              }
            }));
    pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected:" + position);
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });
  }
}
