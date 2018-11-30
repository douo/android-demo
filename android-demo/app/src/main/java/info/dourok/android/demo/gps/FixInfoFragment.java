package info.dourok.android.demo.gps;


import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.dourok.android.demo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FixInfoFragment extends Fragment {


    @BindView(R.id.fix)
    TextView mFix;
    @BindView(R.id.lat)
    TextView mLat;
    @BindView(R.id.lon)
    TextView mLon;
    @BindView(R.id.alt)
    TextView mAlt;
    @BindView(R.id.bearing)
    TextView mBearing;
    @BindView(R.id.speed)
    TextView mSpeed;
    @BindView(R.id.accuracy)
    TextView mAccuracy;

    Location mLocation;
    String mFixStatus;

    public static FixInfoFragment newInstance(Location location, String fixStatus) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("location", location);
        bundle.putString("fixStatus", fixStatus);
        FixInfoFragment f = new FixInfoFragment();
        f.setArguments(bundle);
        return f;
    }

    public FixInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mLocation = getArguments().getParcelable("location");
            mFixStatus = getArguments().getString("fixStatus");
        } else {
            mLocation = savedInstanceState.getParcelable("location");
            mFixStatus = savedInstanceState.getString("fixStatus");
        }

    }

    public void setLocation(Location location) {
        mLocation = location;
        refreshLocation();
    }

    public void setFixStatus(String fixStatus) {
        mFixStatus = fixStatus;
        refreshFixStatus();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("location", mLocation);
        outState.putString("fixStatus", mFixStatus);
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fix_info, container, false);
        ButterKnife.bind(this, view);
        refreshFixStatus();
        refreshLocation();
        return view;
    }


    private void refreshFixStatus() {
        mFix.setText(mFixStatus);
    }

    private String getLatitude(Location location) {
        return String.format(Locale.getDefault(), "%.6f", location.getLatitude());
    }


    private String getLongitude(Location location) {
        return String.format(Locale.getDefault(), "%.6f", location.getLongitude());
    }

    private String getAltitude(Location location) {
        return String.format(Locale.getDefault(), "%.2f 米", location.getAltitude());
    }

    private String getAccuracy(Location location) {
        return String.format(Locale.getDefault(), "%.2f 米", location.getAccuracy());
    }

    private String getBearing(Location location) {
        return String.format(Locale.getDefault(), "%.2f°", location.getBearing());
    }

    private String getSpeed(Location location) {
        return String.format(Locale.getDefault(), "%.2f 米", location.getSpeed());
    }

    private void refreshLocation() {
        mLat.setText(getLatitude(mLocation));
        mLon.setText(getLongitude(mLocation));
        mAccuracy.setText(getAccuracy(mLocation));
        mAlt.setText(getAltitude(mLocation));
        mBearing.setText(getBearing(mLocation));
        mSpeed.setText(getSpeed(mLocation));
    }

}
