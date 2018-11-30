package info.dourok.android.demo.savestate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import info.dourok.android.demo.BaseFragment;
import info.dourok.android.demo.LogUtils;
import info.dourok.android.demo.R;

public class SimpleTextFragment extends BaseFragment {

    Button button;
    TextView textView;
    TextView hashView;
    int rand;

    public SimpleTextFragment() {
        // Required empty public constructor
    }

    public static SimpleTextFragment newInstance() {
        SimpleTextFragment fragment = new SimpleTextFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_simple_text, container, false);
        button = (Button) v.findViewById(R.id.toggle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(Integer.toString(rand));
                //Activity 重启后 TextView 还能保留原来的文本值，但是 FragmentPageAdapter destroy 后再恢复却不能保留了
                textView.setFreezesText(true);
            }
        });
        textView = (TextView) v.findViewById(R.id.textView);
        d("textView：" + textView.getText()); // 此时 savedInstanceState 的数据还未恢复到 View 中
        if (savedInstanceState == null) {
            rand = new Random().nextInt(10000);
        } else {
            rand = savedInstanceState.getInt("rand");
        }

        hashView = (TextView) v.findViewById(R.id.hash);
        hashView.setText(String.format("0x%X", hash));
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        d("" + getSavedViewState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        d(LogUtils.debugBundle(savedInstanceState));
        d("textView：" + textView.getText()); // 此时 savedInstanceState 的数据还未恢复到 View 中
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
