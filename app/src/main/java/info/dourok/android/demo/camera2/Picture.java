package info.dourok.android.demo.camera2;

/**
 * 文件名为时间戳
 * Created by larry on 6/11/16.
 */
public class Picture {
    long size;
    long date;
    String imagePath;
    String thumbPath;

    Picture(long size, long date, String imagePath, String thumbPath) {
        this.size = size;
        this.date = date;
        this.imagePath = imagePath;
        this.thumbPath = thumbPath;
    }

}
