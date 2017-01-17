package info.dourok.android.demo.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import info.dourok.android.demo.R;

/**
 * Created by larry on 12/24/16.
 */

public class NestedPagerFragment extends Fragment
    implements HierarchyFragmentPagerAdapter.AdapterHolder {
  private static final String TAG = "NestedPagerFragment";
  String mName;
  int mLevel;
  ViewPager mPager;
  TextView mNameView;
  private PagerAdapter mAdapter;

  public NestedPagerFragment() {

  }

  public static NestedPagerFragment newInstance(String name, int level) {
    Bundle bundle = new Bundle();
    NestedPagerFragment f = new NestedPagerFragment();
    bundle.putString("mName", name);
    bundle.putInt("mLevel", level);
    f.setArguments(bundle);
    return f;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mName = getArguments().getString("mName");
    mLevel = getArguments().getInt("mLevel");
    setHasOptionsMenu(true);
    Log.d(TAG, mName + " onCreate");
  }
  //
  //@Override public void onStart() {
  //  Log.d(TAG, mName + " onStart");
  //  super.onStart();
  //}
  //
  //@Override public void onResume() {
  //  Log.d(TAG, mName + " onResume");
  //  super.onResume();
  //}
  //
  //@Override public void onAttach(Context context) {
  //  mName = getArguments().getString("mName");
  //  Log.d(TAG, mName + " onAttach");
  //  super.onAttach(context);
  //}
  //
  //@Override public void onDestroy() {
  //  Log.d(TAG, mName + " onDestroy");
  //  super.onDestroy();
  //}
  //
  //@Override public void onStop() {
  //  Log.d(TAG, mName + " onStop");
  //  super.onStop();
  //}
  //
  //@Override public void onPause() {
  //  Log.d(TAG, mName + " onPause");
  //  super.onPause();
  //}
  //
  //@Override public void onDetach() {
  //  Log.d(TAG, mName + " onDetach");
  //  super.onDetach();
  //}

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.add(mName);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
    return true;
  }

  @Override public void setMenuVisibility(boolean menuVisible) {
    Log.d(TAG, mName + " setMenuVisibility:" + menuVisible + " " + isAdded());
    if (isAdded() && getChildFragmentManager().getFragments() != null) {
      // 找不到 FragmentStatePagerAdapter 的当前 fragment
      Fragment f = getChildFragmentManager().findFragmentByTag(
          "android:switcher:" + mPager.getId() + ":" + mPager.getCurrentItem());
      if (f != null) {
        f.setMenuVisibility(menuVisible);
      }
    }
    super.setMenuVisibility(menuVisible);
  }

  public int getCount() {
    return mName.charAt(mName.length() - 1) - 'A';
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    Log.d(TAG, mName + " onCreateView");
    // 因为 view 是由 pageradpater 添加进 viewpager 所以这里不应该 attach
    View view = inflater.inflate(R.layout.fragment_nested_pager, container, false);
    mPager = (ViewPager) view.findViewById(R.id.pager);

    //mAdapter = new NestedPagerAdapter(this, getChildFragmentManager());
    mAdapter =
        new HierarchyFragmentPagerAdapter(new NestedPagerAdapter(this, getChildFragmentManager()),
            this);
    if (mLevel < 3) mPager.setAdapter(mAdapter);
    mNameView = (TextView) view.findViewById(R.id.name);
    mNameView.setText(mName);
    view.setBackgroundColor(((0xFF / (getCount() + 1)) << (mLevel * 8)) | 0x88000000);
    return view;
  }

  @Override public HierarchyFragmentPagerAdapter getAdapter() {
    return (HierarchyFragmentPagerAdapter) mAdapter;
  }
}
