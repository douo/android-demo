package info.dourok.android.demo.camera2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;
import java.util.List;

import info.dourok.android.demo.R;

public class ImagePreviewActivity extends AppCompatActivity {
    public static final String KEY_PICTURES = "KEY_PICTURES";
    public static final String KEY_POSITION = "KEY_POSITION";
    public static final int RESULT_MODIFIED = RESULT_FIRST_USER + 1;
    private static final String KEY_MODIFIED = "KEY_MODIFIED";
    ViewPager mPager;
    View mDecorView;
    ImagePagerAdapter mAdapter;
    private boolean modified;
    private boolean mDecorVisible;
    private List<Picture> mPictures;

    public static Intent createIntent(Context context, List<Picture> pictures, int position) {
        Intent i = new Intent(context, ImagePreviewActivity.class);
        i.putExtra(KEY_PICTURES, Gallery.toJson(pictures));
        i.putExtra(KEY_POSITION, position);
        return i;
    }

    public static Intent start(Context context, List<Picture> pictures, int position) {
        Intent i = new Intent(context, ImagePreviewActivity.class);
        i.putExtra(KEY_PICTURES, Gallery.toJson(pictures));
        i.putExtra(KEY_POSITION, position);
        context.startActivity(i);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDecorVisible = true;
        setContentView(R.layout.activity_image_preview);
        mDecorView = getWindow().getDecorView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle((position + 1) + "/" + mPictures.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (savedInstanceState != null) {
            String data = savedInstanceState.getString(KEY_PICTURES);
            if (data != null) {
                mPictures = Gallery.fromJson(data);
            } else {
                mPictures = new ArrayList<>();
            }
        } else {
            String data = getIntent().getStringExtra(KEY_PICTURES);
            if (data != null) {
                mPictures = Gallery.fromJson(data);
            } else {
                mPictures = new ArrayList<>();
            }
        }
        int position = getIntent().getIntExtra(KEY_POSITION, 0);
        mAdapter = new ImagePagerAdapter(mPictures);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(position);
        setTitle((position + 1) + "/" + mPictures.size());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_MODIFIED, modified);
        outState.putString(KEY_PICTURES, Gallery.toJson(mPictures));
        super.onSaveInstanceState(outState);
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
//        mDecorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getSupportActionBar().hide();
        mDecorVisible = false;
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
//        mDecorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getSupportActionBar().show();
        mDecorVisible = true;
    }

    private void toggleSystemUI() {
        if (mDecorVisible) {
            hideSystemUI();
        } else {
            showSystemUI();
        }
    }

    @Override
    public void finish() {
        if (modified) {
            Intent data = new Intent();
            data.putExtra(KEY_PICTURES, Gallery.toJson(mPictures));
            setResult(RESULT_MODIFIED, data);
        }
        super.finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getTitle().equals("删除")) {
            deleteCurrentPicture();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void deleteCurrentPicture() {
        mPictures.remove(mPager.getCurrentItem()).delete();
        modified = true;
        if (mPictures.size() == 0) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("删除");
        return super.onCreateOptionsMenu(menu);
    }

    private void d(String s) {
        Log.d("ImagePreviewActivity", s);
    }

    private class ImagePagerAdapter extends PagerAdapter {
        List<Picture> pictures;

        public ImagePagerAdapter(List<Picture> picture) {
            this.pictures = picture;
        }

        @Override
        public int getCount() {
            return pictures.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            d("instantiateItem:" + position);
            SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) getLayoutInflater().
                    inflate(R.layout.item_pager_image, container, false);
            Picture picture = pictures.get(position);
            imageView.setImage(ImageSource.uri(picture.imageUri));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSystemUI();
                }
            });
            container.addView(imageView);
            return imageView;
        }

        public long getId(int position) {
            return position;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            d("isViewFromObject:" + object);
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            d("destroyItem:" + position + " " + object);
        }
    }
}
