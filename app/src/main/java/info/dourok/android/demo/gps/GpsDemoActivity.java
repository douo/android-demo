package info.dourok.android.demo.gps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.transition.Fade;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.dourok.android.demo.LogUtils;
import info.dourok.android.demo.R;

@SuppressWarnings("MissingPermission")
public class GpsDemoActivity extends AppCompatActivity {

    private static final String TAG = "GpsDemoActivity";
    @BindView(R.id.lat)
    TextView mLat;
    @BindView(R.id.lon)
    TextView mLon;
    @BindView(R.id.fix)
    TextView mFix;
    @BindView(R.id.satellites_view)
    SatellitesView mSatellitesView;

    LocationManager mLocationManager;
    private Scene scene2;
//    private Scene scene1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_demo);
        ButterKnife.bind(this);
        scene2 = Scene.getSceneForLayout((ViewGroup) findViewById(R.id.fix_info_container), R.layout.gps_info_view, this);
//        TransitionManager.go(scene2);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        makeUseOfNewLocation(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        startLocation();
    }


    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0x2);
            return false;
        }
        return true;
    }

    private boolean checkProvider() {
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            new AlertDialog.Builder(this).setMessage("Gps 未开启，请前往开启")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).show();
            return false;
        }
    }

    private void resetAgps() {
        mLocationManager.sendExtraCommand(LocationManager.GPS_PROVIDER, "delete_aiding_data", null);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        startLocation();
    }


    public void startLocation() {
        if (checkPermission() && checkProvider()) {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    makeUseOfNewLocation(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    String msg = "";
                    switch (status) {
                        case LocationProvider.AVAILABLE:
                            msg = "AVAILABLE";
                            break;
                        case LocationProvider.OUT_OF_SERVICE:
                            msg = "OUT_OF_SERVICE";
                            break;
                        case LocationProvider.TEMPORARILY_UNAVAILABLE:
                            msg = "TEMPORARILY_UNAVAILABLE";
                            break;
                    }
                    Log.d(TAG, "onStatusChanged:" + provider + ":" + msg);
                    Log.d(TAG, LogUtils.debugBundle(extras));
                }

                public void onProviderEnabled(String provider) {
                    Log.d(TAG, "onProviderEnabled" + provider);
                }

                public void onProviderDisabled(String provider) {
                    Log.d(TAG, "onProviderDisabled" + provider);
                }
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mLocationManager.registerGnssStatusCallback(new GnssStatus.Callback() {
                    @Override
                    public void onStarted() {
                        Log.d(TAG, "onStarted");
                    }

                    @Override
                    public void onStopped() {
                        Log.d(TAG, "onStopped");
                    }

                    @Override
                    public void onFirstFix(int ttffMillis) {
                        Log.d(TAG, "onFirstFix:" + ttffMillis);
                    }

                    @Override
                    public void onSatelliteStatusChanged(GnssStatus status) {
                        mSatellitesView.setSatellites(status);
                    }
                });
            } else {
                mLocationManager.addGpsStatusListener(new GpsStatus.Listener() {
                    @Override
                    public void onGpsStatusChanged(int event) {
                        GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
                        mSatellitesView.setSatellites(gpsStatus.getSatellites());
                    }
                });
            }

//        http://www.gpsinformation.org/dale/nmea.html
//        locationManager.addNmeaListener(new GpsStatus.NmeaListener() {
//            @Override
//            public void onNmeaReceived(long timestamp, String nmea) {
//                Log.d(TAG, timestamp + ":" + nmea);
//            }
//        });


//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0,
//                PendingIntent.getActivity(this, 0x1, new Intent(this, IntentDemoActivity.class), PendingIntent.FLAG_CANCEL_CURRENT));

        }

    }

    private void makeUseOfNewLocation(final Location location) {
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            if (location.hasAltitude()) {
                mFix.setText("3D Fix");
            } else {
                mFix.setText("2D Fix");
            }
        } else {
            mFix.setText("No Fix");
        }

//        Log.d(GpsDemoActivity.class.getSimpleName(), location.getLatitude() + "," + location.getLongitude());
        mLat.setText(String.format(Locale.CHINA, "%s", location.getLatitude()));
        mLon.setText(String.format(Locale.CHINA, "%s", location.getLongitude()));

    }

    @OnClick(R.id.fix_info_container)
    public void onTransition() {
        transition();
    }

    private void transition() {
        Log.d(TAG, "transition");
        TransitionManager.go(scene2);
    }
}
