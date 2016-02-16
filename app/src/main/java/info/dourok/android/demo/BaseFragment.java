package info.dourok.android.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static info.dourok.android.demo.LogUtils.LOGD;
import static info.dourok.android.demo.LogUtils.getMethodName;
import static info.dourok.android.demo.LogUtils.makeLogTag;

public class BaseFragment extends Fragment {
    protected final String TAG = "V:" + makeLogTag(((Object) this).getClass());
    int hash = System.identityHashCode(this);

    public void onAttach(Context context) {
        if (!(context instanceof BaseActivity)) {
            throw new ClassCastException(this + " must attach to BaseActivity");
        }
        super.onAttach(context);
        d(getMethodName() + hash);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        d(getMethodName() + hash);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        d(getMethodName() + hash);
    }

    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        d(getMethodName() + hash);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        d(getMethodName() + hash);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onStart() {
        super.onStart();
        d(getMethodName() + hash);
    }

    public void onResume() {
        super.onResume();
        d(getMethodName() + hash);
    }

    public void onPause() {
        super.onPause();
        d(getMethodName() + hash);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        d(getMethodName() + hash);
        super.onSaveInstanceState(outState);
    }


    public void onStop() {
        super.onStop();
        d(getMethodName() + hash);
    }

    public void onDestroyView() {
        super.onDestroyView();
        d(getMethodName() + hash);
    }

    public void onDestroy() {
        super.onDestroy();
        d(getMethodName() + hash);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        d(getMethodName() + hash);

    }

    public View findViewById(int id) {
        if (getView() != null) {
            return getView().findViewById(id);
        } else if (isAttached()) {
            return getActivity().findViewById(id);
        } else {
            return null;
        }
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
        LOGD(TAG, msg);
    }

    protected void d(String msg, Throwable ex) {
        LOGD(TAG, msg, ex);
    }

}
