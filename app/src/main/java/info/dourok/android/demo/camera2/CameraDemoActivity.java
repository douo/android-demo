package info.dourok.android.demo.camera2;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import info.dourok.android.demo.R;
import info.dourok.camera.BaseCameraActivity;
import info.dourok.camera.ImageWorker;

public class CameraDemoActivity extends BaseCameraActivity {
    TextView flash;
    ImageView thumb;
    Gallery mGallery;
    List<Picture> mPictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGallery = new Gallery(this, "demo");
        mPictures = mGallery.getPictures();
        thumb = (ImageView) findViewById(R.id.thumb);
        if (!mPictures.isEmpty()) {
            thumb.setImageBitmap(BitmapFactory.decodeFile(mPictures.get(mPictures.size() - 1).thumbPath));
        }
    }


    @Override
    protected void onCameraReady() {
        flash = (TextView) findViewById(R.id.flashMode);
        if (isFlashSupported()) {
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
        } else {
            flash.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPreProcessingPicture(ImageWorker worker) {
        System.out.println("onPreProcessingPicture:" + Thread.currentThread().toString());
    }

    @Override
    public void onDoneProcessing(ImageWorker worker) {
        System.out.println("onDoneProcessing:" + Thread.currentThread().toString());
        if (!mPictures.isEmpty()) {
            thumb.setImageBitmap(BitmapFactory.decodeFile(mPictures.get(mPictures.size() - 1).thumbPath));
        }
    }

    @Override
    protected ImageWorker getImageWorker() {
        return mGallery.getImageWorker();
    }

    @Override
    protected int getViewResourceId() {
        return R.layout.activity_camera_demo;
    }
}
