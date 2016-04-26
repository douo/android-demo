package info.dourok.android.demo.camera2;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import info.dourok.android.demo.R;
import info.dourok.camera.BaseCameraActivity;
import info.dourok.camera.ImageData;

public class CameraDemoActivity extends BaseCameraActivity<ImageData> {
    TextView flash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onCameraReady() {

        flash = (TextView) findViewById(R.id.flashMode);
        if(isFlashSupported()) {
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
        }else{
            flash.setVisibility(View.GONE);
        }
    }

    @Override
    protected ImageData processingPicture(ImageData image) {
        System.out.println("processing:"+Thread.currentThread().toString());
        return image;
    }

    @Override
    protected void onPreProcessingPicture() {
        System.out.println("pre:"+Thread.currentThread().toString());
    }

    @Override
    protected void onFinishProcessingPicture(ImageData image) {
        System.out.println("finish:"+Thread.currentThread().toString());
    }

    @Override
    protected ImageData buildImageData(byte[] data) {
        return new ImageData(data);
    }


    @Override
    protected int getViewResourceId() {
        return R.layout.activity_camera_demo;
    }
}
