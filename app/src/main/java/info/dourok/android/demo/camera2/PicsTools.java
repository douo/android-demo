package info.dourok.android.demo.camera2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PicsTools {

	private static final int SCALE_HEIGHT = 1200;
	private static final int SCALE_WIDTH = 1600;

	/**
	 * 取得目录底下所有的图片文件
	 * 
	 * @deprecated
	 * @param path
	 * @return
	 */
	public static List<String> getPics(String path) {
		/* 设定目前所在路径 */
		List<String> list = new ArrayList<>();
		File f = new File(path);
		if (f.exists()) {
			File[] files = f.listFiles();
			/* 将所有文件存入ArrayList中 */
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (isImageFile(file.getPath())) {
					list.add(file.getPath());
				}
			}
		}
		return list;
	}

	/**
	 * 判断一个文件是否图片文件
	 * 
	 * @param fName
	 * @return
	 */
	public static boolean isImageFile(String fName) {
		boolean isPics;

		/* 取得扩展名 */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/* 按扩展名的类型决定MimeType */
		isPics = end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp");
		return isPics;
	}

	/**
	 * 将Bitmap保存成JPG文件
	 * 
	 * @param dstFileName
	 * @param mBitmap
	 * @throws IOException
	 */
	public static void saveBitmap(String dstFileName, Bitmap mBitmap,
			int quality) throws IOException {
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

	public static void resizeImageAndWaterMark(byte[] data, String url,
			String firstLine, String secondLine, int thumbnailSize,
			String thumbnailUrl) throws IOException {
		// 强制调用垃圾回收，确保堆内存够用
		System.gc();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		options.inSampleSize = calculateInSampleSize(options, SCALE_WIDTH,
				SCALE_HEIGHT);
		options.inJustDecodeBounds = false;
		Bitmap tmpbm = BitmapFactory.decodeByteArray(data, 0, data.length,
				options);
		// thumb nail
		Bitmap thumbm = Bitmap.createScaledBitmap(tmpbm, thumbnailSize,
				thumbnailSize, false);
		PicsTools.saveBitmap(thumbnailUrl, thumbm, 60);
		thumbm.recycle();
		// tmpbm = bm;
		// 画上水印
		tmpbm = dualLineWaterMark(tmpbm, firstLine, secondLine);
		// 图片质量 100
		PicsTools.saveBitmap(url, tmpbm, 60);
		tmpbm.recycle();
	}

	public static void resizeImageAndWaterMark(String imgPath,String url,
			String firstLine, String secondLine, int thumbnailSize,
			String thumbnailUrl) throws IOException {
		// 强制调用垃圾回收，确保堆内存够用
		System.gc();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgPath, options);
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		options.inSampleSize = calculateInSampleSize(options, SCALE_WIDTH,
				SCALE_HEIGHT);
		options.inJustDecodeBounds = false;
		Bitmap tmpbm = BitmapFactory.decodeFile(imgPath, options);
		// thumb nail
		Bitmap thumbm = Bitmap.createScaledBitmap(tmpbm, thumbnailSize,
				thumbnailSize, false);
		PicsTools.saveBitmap(thumbnailUrl, thumbm, 60);
		thumbm.recycle();
		// tmpbm = bm;
		// 画上水印
		tmpbm = dualLineWaterMark(tmpbm, firstLine, secondLine);
		// 图片质量 100
		PicsTools.saveBitmap(url, tmpbm, 60);
		tmpbm.recycle();
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static void resizeImageAndWaterMark(File f, String firstLine,
			String secondLine, int thumbnailSize, String thumbnailUrl)
			throws IOException {
		byte[] data = new byte[(int) f.length()];
		FileInputStream fis = new FileInputStream(f);
		fis.read(data);
		fis.close();
		resizeImageAndWaterMark(data, f.getAbsolutePath(), firstLine,
				secondLine, thumbnailSize, thumbnailUrl);
	}

	public static Bitmap dualLineWaterMark(Bitmap src, String firstLine,
			String secondLine) {
		final int padding = 5;
		int w = src.getWidth();
		int h = src.getHeight();
		// TML_Library.Debug("Image Width = "+w+" Image Height = "+h);
		Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(src, 0, 0, null);
		Paint paint = new Paint();
		String familyName = "宋体";
		Typeface font = Typeface.create(familyName, Typeface.BOLD);
		paint.setTypeface(font);
		paint.setTextSize(25);
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		Rect bounds = new Rect();
		paint.getTextBounds(secondLine, 0, secondLine.length(), bounds);
		canvas.drawText(secondLine, w - bounds.width() - padding,
				h - bounds.height() - padding, paint);
		paint.getTextBounds(firstLine, 0, firstLine.length(), bounds);
		canvas.drawText(firstLine, w - bounds.width() - padding,
				h - (bounds.height() + padding) * 2, paint);
		return result;
	}

	public static Bitmap creatBackground(Bitmap src) {
		int w = src.getWidth();
		int h = src.getHeight();
		Bitmap bg = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(bg);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		ColorFilter filter = new PorterDuffColorFilter(0x88ffffff,
				PorterDuff.Mode.SRC_ATOP);
		paint.setColorFilter(filter);
		canvas.drawBitmap(src, 0, 0, paint);
		return bg;
	}

	private static void debug(Context ctx, String text) {

		Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
	}

	private static void debug(Context ctx, int t) {
		debug(ctx, t + "");
	}
}
