package info.dourok.android.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

import static info.dourok.android.demo.LogUtils.LOGD;
import static info.dourok.android.demo.LogUtils.getMethodName;
import static info.dourok.android.demo.LogUtils.makeLogTag;

public class BaseFragment extends Fragment {
    protected final String TAG = "V:" + makeLogTag(((Object) this).getClass());
    protected int hash = System.identityHashCode(this);

    public void onAttach(Context context) {
        if (!(context instanceof BaseActivity)) {
            throw new ClassCastException(this + " must attach to BaseActivity");
        }
        super.onAttach(context);
        d(getMethodName());
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        d(getMethodName());
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        d(getMethodName());
    }

    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        d(getMethodName());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        d(getMethodName());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onStart() {
        super.onStart();
        d(getMethodName());
    }

    /**
     * 仅当 {@link android.app.Activity#onResume} 调用时，Fragment 的 onResume 才会调用
     * 实际的调度代码是在 {@link FragmentActivity#onResumeFragments()} 中调用的
     * 覆盖掉 onResumeFragments 那么该 Activity 下的所有 Fragment 都不会调用 onResume，这时 onPause 也不会调用
     * 但是居然不会有什么影响，我之前就是不小心这样子做的 -_-
     */
    public void onResume() {
        super.onResume();
        d(getMethodName());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        d("setUserVisibleHint:" + isVisibleToUser);
    }

    public void onPause() {
        super.onPause();
        d(getMethodName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        d(getMethodName());
        super.onSaveInstanceState(outState);
    }


    public void onStop() {
        super.onStop();
        d(getMethodName());
    }

    public void onDestroyView() {
        super.onDestroyView();
        d(getMethodName());
    }

    public void onDestroy() {
        super.onDestroy();
        d(getMethodName());
    }


    @Override
    public void onDetach() {
        super.onDetach();
        d(getMethodName());

    }

    public void showToast(String text) {
        if (isAttached()) {
            getBaseActivity().showToast(text);
        }
    }

    public void showToast(int resId) {
        if (isAttached()) {
            getBaseActivity().showToast(resId);
        }
    }

    /**
     * 是否 Attached 到 BaseActivity
     *
     * @return
     */
    public boolean isAttached() {
        return getActivity() != null;
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected void d(String msg) {
        LOGD(String.format("%s:0x%X", TAG, hash), msg);
    }

    protected void d(String msg, Throwable ex) {
        LOGD(TAG, msg, ex);
    }

    protected SparseArray<?> getSavedViewState() {
        try {
            Field f = Fragment.class.getDeclaredField("mSavedViewState");
            f.setAccessible(true);
            return (SparseArray<?>) f.get(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
