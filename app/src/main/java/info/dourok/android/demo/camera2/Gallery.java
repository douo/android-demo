package info.dourok.android.demo.camera2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.dourok.camera.BaseCameraActivity;
import info.dourok.camera.ImageWorker;

/**
 * Created by larry on 6/11/16.
 */
public class Gallery {

    public static final String EXT = ".jpg";
    public static final int THUMBNAIL_SIZE = 256;
    public static final String THUMB_EXT = ".thumb";
    String id;
    BaseCameraActivity context;
    String parentDir;
    List<Picture> mPictures;
    private File mFile;

    public Gallery(BaseCameraActivity context, String id) {
        this(context, id, context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
    }

    public Gallery(BaseCameraActivity context, String id, String parentDir) {
        this.id = id;
        this.context = context;
        this.parentDir = parentDir;
        mFile = new File(parentDir, id);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
    }

    static String stripExtension(String str) {
        // Handle null case specially.
        if (str == null) return null;
        // Get position of last '.'.
        int pos = str.lastIndexOf(".");
        // If there wasn't any '.' just return the string as is.
        if (pos == -1) return str;
        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }

    public List<Picture> getPictures() {
        if (mPictures == null) {
            mPictures = buildPictureList();
        }
        return mPictures;
    }

    public GalleryImageWorker getImageWorker() {
        return new GalleryImageWorker(context);
    }

    private List<Picture> buildPictureList() {
        List<Picture> pictures = new ArrayList<>(10);
        for (File f : getPictureDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(EXT);
            }
        })) {
            pictures.add(buildPicture(f));
        }
        return pictures;
    }

    private File getPictureDir() {

        return mFile;
    }

    private Picture buildPicture(File file) {
        long size = file.length();
        long timestamp = Long.parseLong(stripExtension(file.getName()));
        return new Picture(size, timestamp, file.getAbsolutePath(), stripExtension(file.getAbsolutePath()) + THUMB_EXT);
    }

    private String[] newImageName() {
        String basename = Long.toString(System.currentTimeMillis());
        return new String[]{basename + EXT, basename + THUMB_EXT};
    }

    /**
     * 负责将图片写入文件，并生成缩略图
     */
    private class GalleryImageWorker extends ImageWorker {
        public GalleryImageWorker(BaseCameraActivity context) {
            super(context);
        }

        public void saveBitmap(String dstFileName, Bitmap bitmap,
                               int quality, boolean recycle) throws IOException {
            System.out.println("saveBitmap:" + dstFileName);
            File f = new File(dstFileName);
            f.createNewFile();
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                Log.e("error", e.getMessage(), e);
                e.printStackTrace();
            }
            // 压缩bitmap，quality参数取0-100，100质量最高，但保存成文件也最大
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);
            try {
                fOut.flush();
            } catch (IOException e) {
                Log.e("error", e.getMessage(), e);
                e.printStackTrace();
            }
            try {
                fOut.close();
            } catch (IOException e) {
                Log.e("error", e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (recycle)
                    bitmap.recycle();
            }
        }

        @Override
        public boolean processing(byte[] data) {
            String[] names = newImageName();
            File image = new File(getPictureDir(), names[0]);
            File thumb = new File(getPictureDir(), names[1]);
            try {
                Bitmap source = BitmapFactory.decodeByteArray(data, 0, data.length, null);
                saveBitmap(image.getAbsolutePath(), source, 80, false);
                saveBitmap(thumb.getAbsolutePath(), ThumbnailUtils.extractThumbnail(source, THUMBNAIL_SIZE, THUMBNAIL_SIZE), 60, true);
                source.recycle();
                getPictures().add(buildPicture(image));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}