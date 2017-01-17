package info.dourok.android.demo.pager;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by larry on 1/11/17.
 */

public class HierarchyFragmentPagerAdapter extends PagerAdapter {

  // 两种情况下为真

  // 1. 宿主是根 Pager，或不是 AdapterHolder
  // 2. 父 Adapter 是 Visible 且宿主是 Primary Item
  private boolean mVisible;

  private Fragment mCurrentPrimaryItem;
  private PagerAdapter mAdapter;

  /**
   *
   * @param adapter 代理的实际 PagerAdapter，只支持 Fragment
   * @param holder 所有子 Fragment 必须自己实现这个接口，并传递自身进来。
   * 如果根 Adapter 的宿主是 Fragment，便不能继承 AdapterHolder，建议用匿名类
   */
  public HierarchyFragmentPagerAdapter(PagerAdapter adapter, AdapterHolder holder) {
    mAdapter = adapter;
    mVisible = true;
    if (holder != null) {
      if (holder instanceof Fragment) {
        // 一个 hack，初始化 的 visible 状态
        // holder 不是 Fragment 表示 Adapter 为根 Adapter
        // menu visible 为 true，便断言宿主 Fragment 是 primary item.
        mVisible = ((Fragment) holder).isMenuVisible();
      }
    }
  }

  /**
   * 通知子 Adapter（宿主是 holder） ，父 Adapter(当前的 Adapter) visible 发生了变化。
   * 或者通知子 Adapter，父 Adapter 希望它的 visible 发生变化
   */
  private void notifyChildVisibleChanged(boolean visible, Fragment holder) {
    if (holder instanceof AdapterHolder) {
      HierarchyFragmentPagerAdapter adapter = ((AdapterHolder) holder).getAdapter();
      if (adapter != null) {
        adapter.setVisible(visible);
      }
    }
  }

  public boolean isVisible() {
    return mVisible;
  }

  /**
   * 设置当前 Adapter 是否是 visible
   */
  protected void setVisible(boolean visible) {
    mVisible = visible;
    // visible 为 true, mCurrentPrimaryItem 才能显示菜单
    if (mCurrentPrimaryItem != null) {
      mCurrentPrimaryItem.setMenuVisibility(visible);
      mCurrentPrimaryItem.setUserVisibleHint(visible);
      //通知子 Adapter 父 Adapter visible 发生了变化
      notifyChildVisibleChanged(visible, mCurrentPrimaryItem);
    }
  }

  @Override public void setPrimaryItem(ViewGroup container, int position, Object object) {
    mAdapter.setPrimaryItem(container, position, object);
    // 这个方法能改变当前
    Fragment fragment = (Fragment) object;
    if (fragment != mCurrentPrimaryItem) {
      if (mCurrentPrimaryItem != null) {
        //前任 primary item 发生变化，通知其后代更新状态
        notifyChildVisibleChanged(false, mCurrentPrimaryItem);
      }
      mCurrentPrimaryItem = fragment;
      if (fragment != null) {
        Fragment parent = fragment.getParentFragment();
        if (parent == null || !(parent instanceof AdapterHolder)) {
          // 拿不到 parent 有两种情况
          // Adapter 在根 Pager 里
          // 也有可能是第一次初始化，当前 Fragment 还未和其父 Fragment 建立链接
          setVisible(isVisible());
        } else {
          // 否则，只有父 Adapter 是 visible primary，当前 primary item 才可能是 visible primary.
          setVisible(((AdapterHolder) parent).getAdapter().isVisible());
        }
      }
    }
  }

  @Override public void finishUpdate(ViewGroup container) {
    mAdapter.finishUpdate(container);
  }

  public interface AdapterHolder {
    HierarchyFragmentPagerAdapter getAdapter();
  }

  //代理

  @Override public Object instantiateItem(ViewGroup container, int position) {
    return mAdapter.instantiateItem(container, position);
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    mAdapter.destroyItem(container, position, object);
  }

  @Override public int getCount() {
    return mAdapter.getCount();
  }

  @Override public void startUpdate(ViewGroup container) {
    mAdapter.startUpdate(container);
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return mAdapter.isViewFromObject(view,object);
  }

  @Override public Parcelable saveState() {
    return mAdapter.saveState();
  }

  @Override public void restoreState(Parcelable state, ClassLoader loader) {
    mAdapter.restoreState(state, loader);
  }

  @Override public int getItemPosition(Object object) {
    return mAdapter.getItemPosition(object);
  }

  @Override public void notifyDataSetChanged() {
    mAdapter.notifyDataSetChanged();
  }

  @Override public void registerDataSetObserver(DataSetObserver observer) {
    mAdapter.registerDataSetObserver(observer);
  }

  @Override public void unregisterDataSetObserver(DataSetObserver observer) {
    mAdapter.unregisterDataSetObserver(observer);
  }

  @Override public CharSequence getPageTitle(int position) {
    return mAdapter.getPageTitle(position);
  }

  @Override public float getPageWidth(int position) {
    return mAdapter.getPageWidth(position);
  }
}
