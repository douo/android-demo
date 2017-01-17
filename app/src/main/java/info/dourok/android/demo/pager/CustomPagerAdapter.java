package info.dourok.android.demo.pager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Locale;

/**
 * Created by larry on 12/16/16.
 */

public class CustomPagerAdapter extends PagerAdapter {
  Context mContext;

  CustomPagerAdapter(Context context) {
    mContext = context;
  }

  @Override public int getCount() {
    return 5;
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    TextView tv = new TextView(mContext);
    tv.setText(String.format(Locale.CHINA, "Position: %d", position));
    tv.setTextColor(0xFFFF0000);
    tv.setBackgroundColor((0xF << position) | 0xFF000000);
    tv.setId(position);
    container.addView(tv);
    return tv;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  @Override public float getPageWidth(int position) {
    if (position == getCount() - 1) return 1;
    return 0.75f;
  }
}
