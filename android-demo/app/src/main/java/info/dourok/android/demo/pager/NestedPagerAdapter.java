package info.dourok.android.demo.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * Created by larry on 12/24/16.
 */
class NestedPagerAdapter extends FragmentPagerAdapter {

  private NestedPagerFragment mParent;
  private boolean mRootAdapter;

  public NestedPagerAdapter(NestedPagerFragment nestedPagerFragment, FragmentManager fm) {
    super(fm);
    this.mParent = nestedPagerFragment;
    mRootAdapter = mParent == null;
  }

  @Override public Fragment getItem(int position) {
    Fragment f;
    if (mRootAdapter) {
      f = NestedPagerFragment.newInstance("" + (char) ('A' + position), 0);
    } else {
      f = NestedPagerFragment.newInstance(mParent.mName + (char) ('A' + position),
          mParent.mLevel + 1);
    }
    return f;
  }

  @Override public void setPrimaryItem(ViewGroup container, int position, Object object) {
    super.setPrimaryItem(container, position, object);
    //if (mParent != null) ((Fragment) object).setMenuVisibility(mParent.isMenuVisible());
  }

  @Override public int getCount() {
    return 4;
  }
}
