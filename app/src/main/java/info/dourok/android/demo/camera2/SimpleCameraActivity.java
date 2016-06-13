package info.dourok.android.demo.camera2;

import android.content.Intent;
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
import info.dourok.camera.RotationEventListener;

public class SimpleCameraActivity extends BaseCameraActivity {
    public static final int REQUEST_CODE_VIEW_IMAGE = 1;
    TextView flash;
    ImageView thumb;
    Gallery mGallery;
    List<Picture> mPictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate");
        super.onCreate(savedInstanceState);
        mGallery = new Gallery(this, "demo");
        mPictures = mGallery.getPictures();
        thumb = (ImageView) findViewById(R.id.thumb);
        if (!mPictures.isEmpty()) {
            thumb.setImageBitmap(BitmapFactory.decodeFile(mPictures.get(mPictures.size() - 1).thumbUri));
        }
        thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPictures.size() > 0) {
                    Intent i = ImagePreviewActivity.createIntent(SimpleCameraActivity.this, mPictures, mPictures.size() - 1);
                    startActivityForResult(i, REQUEST_CODE_VIEW_IMAGE);
                }
            }
        });
    }

    @Override
    protected void onRotationChanged(int newRotation, int oldRotation) {
        int degrees = RotationEventListener.getDegrees(newRotation);
        flash.animate().rotation(degrees).start();
        thumb.animate().rotation(degrees).start();
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
            thumb.setImageBitmap(BitmapFactory.decodeFile(mPictures.get(mPictures.size() - 1).thumbUri));
        }
    }

    @Override
    protected ImageWorker getImageWorker() {
        return mGallery.getImageWorker();
    }

    @Override
    protected int getViewResourceId() {
        return R.layout.activity_simple_camera;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_VIEW_IMAGE) {
            if (resultCode == ImagePreviewActivity.RESULT_MODIFIED) {
                mGallery.updatePicture(Gallery.fromJson(data.getStringExtra(ImagePreviewActivity.KEY_PICTURES)));
                if (!mPictures.isEmpty()) {
                    thumb.setImageBitmap(BitmapFactory.decodeFile(mPictures.get(mPictures.size() - 1).thumbUri));
                }
            }
        }
    }
}
