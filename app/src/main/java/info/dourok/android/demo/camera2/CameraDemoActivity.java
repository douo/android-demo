package info.dourok.android.demo.camera2;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import info.dourok.android.demo.R;
import info.dourok.camera.BaseCameraActivity;

public class CameraDemoActivity extends BaseCameraActivity {
    TextView flash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onCameraReady() {
        flash = (TextView) findViewById(R.id.flashMode);
        flash.setText(getFlashModeName(getFlashMode()));
        final PopupMenu popupMenu = new PopupMenu(this, flash);
        for (final String val : getSupportedFlashModes()) {
            MenuItem item = popupMenu.getMenu().add(getFlashModeName(val));
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    updateFlashMode(val);
                    flash.setText(getFlashModeName(getFlashMode()));
                    return true;
                }
            });
        }
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }


    @Override
    protected int getViewResourceId() {
        return R.layout.activity_camera_demo;
    }
}
