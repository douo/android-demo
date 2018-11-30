package info.dourok.android.demo.camera2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import info.dourok.camera.BaseCameraActivity;
import info.dourok.camera.ImageWorker;

/**
 * Created by larry on 4/28/16.
 */
public class FileImageWorker extends ImageWorker {

    private static final SimpleDateFormat sFormat = new SimpleDateFormat(
            "yyyyMMddHHmmss", Locale.US);
    public FileImageWorker(BaseCameraActivity context){
        super(context);
    }
    @Override
    public boolean processing(byte[] data) {
        System.out.println("processing:"+Thread.currentThread().toString());
        File dir= context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String dateStr = sFormat.format(new Date())+".jpg";
        File dst = new File(dir,dateStr);
        try {
            saveBitmap(dst.getAbsolutePath(), BitmapFactory.decodeByteArray(data, 0, data.length, null),80);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


//    @Override
//    protected ImageData processingPicture(ImageData image) {
//
//        // 当前月份的文件夹不存在，则创建
//        String basePicsPath = FileTools.getPicsPath()
//                + dateStr.substring(0, 6) + File.separator
//                + srepcase_no;
//        File path = new File(basePicsPath);
//        if (!path.exists()) {
//            path.mkdirs();
//        }
//        PicStatus psatus = getPicStatus();
//        spic_filename = psatus.createNewName(dateStr);
//        spic_url = basePicsPath + File.separator + spic_filename;
//        PicsTools.resizeImageAndWaterMark(
//                imgPath,
//                spic_url,
//                spic_remark,
//                CacheMgr.getInstance().getUser()
//                        .getString("logintimestr").substring(0, 11)
//                        + DateTools.getDate(
//                        System.currentTimeMillis(),
//                        "kk:mm:ss"), THUMBNAIL_SIZE);
//        return image;
//    }

    public static void saveBitmap(String dstFileName, Bitmap mBitmap,
                                  int quality) throws IOException {
        System.out.println("saveBitmap:"+dstFileName);
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
        mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);
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
        }
    }

}
