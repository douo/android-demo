package info.dourok.android.demo.camera2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.Surface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import info.dourok.camera.BaseCameraActivity;
import info.dourok.camera.ImageWorker;

import static android.util.Log.d;

/**
 * 负责将图片写入文件，并生成缩略图
 */
class GalleryImageWorker extends ImageWorker {
    private Gallery gallery;

    public GalleryImageWorker(Gallery gallery, BaseCameraActivity context) {
        super(context);
        this.gallery = gallery;
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
        String[] names = gallery.newImageName();
        File image = new File(gallery.getPictureDir(), names[0]);
        File thumb = new File(gallery.getPictureDir(), names[1]);
        try {

            Bitmap source = BitmapFactory.decodeByteArray(data, 0, data.length, null);
            int rotation = context.getRotation();
            if (rotation != Surface.ROTATION_90) {
                Matrix matrix = new Matrix();
                int degres = (Surface.ROTATION_90 - rotation) * 90;
                d("Gallery", "d:" + degres);
                matrix.postRotate(degres);
                Bitmap b = Bitmap.createBitmap(source, 0, 0,
                        source.getWidth(), source.getHeight(), matrix, true);
                source.recycle();
                source = b;
            }

            saveBitmap(image.getAbsolutePath(), source, 80, false);
            saveBitmap(thumb.getAbsolutePath(),
                    ThumbnailUtils.extractThumbnail(source, Gallery.THUMBNAIL_SIZE, Gallery.THUMBNAIL_SIZE), 60, true);
            source.recycle();
            gallery.getPictures().add(gallery.buildPicture(image));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
