package info.dourok.android.demo.camera2;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import static info.dourok.android.demo.camera2.Gallery.fromJson;


public class SimpleCameraActivity extends BaseCameraActivity {
    public static final int REQUEST_CODE_VIEW_IMAGE = 1;
    private static final String KEY_ID = "KEY_ID";
    private static final String KEY_PARENT_DIR = "KEY_PARENT_DIR";
    TextView flash;
    ImageView thumb;
    Gallery mGallery;
    List<Picture> mPictures;

    public static Intent createIntent(Context context, String id) {
        Intent i = new Intent(context, SimpleCameraActivity.class);
        i.putExtra(KEY_ID, id);
        return i;
    }

    public static Intent createIntent(Context context, String id, String parentDir) {
        Intent i = new Intent(context, SimpleCameraActivity.class);
        i.putExtra(KEY_ID, id);
        i.putExtra(KEY_PARENT_DIR, parentDir);
        return i;
    }

    public static void start(Context context, String id) {
        context.startActivity(createIntent(context, id));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate");
        super.onCreate(savedInstanceState);
        String parentDir = getIntent().getStringExtra(KEY_PARENT_DIR);
        if (parentDir != null) {
            mGallery = new Gallery(this, getIntent().getStringExtra(KEY_ID), parentDir);
        } else {
            mGallery = new Gallery(this, getIntent().getStringExtra(KEY_ID));
        }
        mPictures = mGallery.getPictures();
        thumb = (ImageView) findViewById(R.id.thumb);

        updateThumb();
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
        updateThumb();
    }

    private void updateThumb() {
        if (!mPictures.isEmpty()) {
            thumb.setImageBitmap(BitmapFactory.decodeFile(Uri.parse(mPictures.get(mPictures.size() - 1).thumbUri).getPath()));
        }
    }

    @Override
    protected ImageWorker getImageWorker() {
        return new GalleryImageWorker(mGallery, this);
    }

    @Override
    protected int getViewResourceId() {
        return R.layout.activity_simple_camera;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_VIEW_IMAGE) {
            if (resultCode == ImagePreviewActivity.RESULT_MODIFIED) {
                mGallery.updatePicture(fromJson(data.getStringExtra(Gallery.KEY_PICTURES)));
                updateThumb();
            }
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(Gallery.KEY_PICTURES, Gallery.toJson(mPictures));
        setResult(RESULT_OK, data);
        super.finish();
    }
}
