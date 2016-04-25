package info.dourok.camera;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public abstract class BaseCameraActivity extends AppCompatActivity {

    private static final int FOCUS_AREA_SIZE = 100;
    private Camera mCamera;
    private CameraPreview mPreview;


    private View mCapture;
    private View mTouchView;
    private View mFocusView;

    private boolean focusing;
    private boolean pictureRequest;
    private boolean autoFocusSuccess;

    private String flashMode;
    private List<String> supportedFlashModes;


    private boolean autoFocusSupported;
    private final static String TAG = BaseCameraActivity.class.getCanonicalName();


    protected int getViewResourceId() {
        return R.layout.base_camera;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getViewResourceId());
        mPreview = new CameraPreview(this);
        //setup Touch View
        mTouchView = findViewById(R.id.preview);
        if (autoFocusSupported) {
            mTouchView.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!isBusy() && event.getAction() == MotionEvent.ACTION_DOWN) {
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        int half = FOCUS_AREA_SIZE / 2;

                        if (x + half > mPreview.getWidth()) {
                            x = mPreview.getWidth() - half;
                        }
                        if (x - half < 0) {
                            x = half;
                        }
                        if (y + half > mPreview.getHeight()) {
                            y = mPreview.getHeight() - half;
                        }
                        if (y - half < 0) {
                            y = half;
                        }

                        Rect touchRect = new Rect(x - half, y - half, x + half, y + half);

                        submitFocusAreaRect(touchRect);
                    }
                    return false;
                }
            });
        }
        ((FrameLayout) findViewById(R.id.preview)).addView(mPreview, 0);
        mFocusView = findViewById(R.id.focus);
        // setup capture button
        mCapture = findViewById(R.id.capture);
        mCapture.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                d("onTouch:" + event.getAction());
                if (!isBusy()) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (autoFocusSupported) {
                                autoFocusButtonDown();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (autoFocusSupported) {
                                if (!pictureRequest) {
                                    pictureRequest = true;
                                    takePicture();
                                }
                                autoFocusButtonUp();
                                v.performClick();
                            } else {
                                takePictureWithParas();
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            autoFocusButtonUp();
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        boolean opened = safeCameraOpen();
        if(opened){
            setupCamera();
            mPreview.setCamera(mCamera);
            onCameraReady();
        }else{
            new AlertDialog.Builder(this).setTitle("无法打开摄像头!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
    }

    private boolean safeCameraOpen() {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            mCamera = Camera.open();
            qOpened = (mCamera != null);
        } catch (Exception e) {
            // Logcat 标签 ISecCameraHardware,SecCameraHardware 一般会显示出错信息
            // 标签还有 QualcommCameraHardware 所以 CameraHardware 应该更通用
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }

    private void setupCamera() {
        try {
            mCamera.setDisplayOrientation(90);

            Parameters params = mCamera.getParameters();

            d(prettyParameters(params));
            List<String> focusModes = params.getSupportedFocusModes();
            autoFocusSupported = false;
            if (focusModes != null) {
                if (focusModes.contains(Parameters.FOCUS_MODE_AUTO)) {
                    params.setFocusMode(Parameters.FOCUS_MODE_AUTO);
                    autoFocusSupported = true;
                }
            }

            //初始化闪光灯设置
            supportedFlashModes = params.getSupportedFlashModes();
            if (supportedFlashModes != null && supportedFlashModes.size() > 0) {
                SharedPreferences sp = getPreferences(MODE_PRIVATE);
                flashMode = sp.getString("FLASH", supportedFlashModes.get(0));
                params.setFlashMode(flashMode);
            }

            Size size = getOptimalSize(params.getSupportedPictureSizes(), 1600, 0, 1);
            params.setPictureSize(size.width, size.height);

            mCamera.setParameters(params);
        } catch (Exception e) {
            // Logcat 标签 ISecCameraHardware,SecCameraHardware 一般会显示出错信息
            // 标签还有 QualcommCameraHardware 所以 CameraHardware 应该更通用
            e.printStackTrace();
        }
    }

    protected abstract void onCameraReady();

    public boolean isFlashOn() {
        return flashMode.equals(Parameters.FLASH_MODE_ON);
    }

    public void setFlashOn(boolean b) {
        if (isFlashOn() != b) {
            updateFlashMode(Parameters.FLASH_MODE_ON);
        }
    }

    public List<String> getSupportedFlashModes() {
        return supportedFlashModes;
    }

    public void updateFlashMode(String value) {
        Parameters parameters = mCamera.getParameters();
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes.contains(value)) {
            flashMode = value;
            parameters.setFlashMode(value);
            SharedPreferences sp = getPreferences(MODE_PRIVATE);
            sp.edit().putString("FLASH",value).apply();
            mCamera.setParameters(parameters);
        }else {
            throw new UnsupportedOperationException("flash mode is not supported");
        }
    }

    public String getFlashMode(){
        return flashMode;
    }



    private void releaseCameraAndPreview() {
        mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onResume() {
        d("onResume");
        super.onResume();
        if (mCamera == null) {
            safeCameraOpen();
            setupCamera();
            mPreview.setCamera(mCamera);
        }
    }

    @Override
    protected void onPause() {
        d("onPause");
        super.onPause();
        // releaseMediaRecorder(); // if you are using MediaRecorder, release it
        // first
        releaseCameraAndPreview();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        releaseCameraAndPreview();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isBusy() && event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_CAMERA:
                    if (!pictureRequest) {
                        pictureRequest = true;
                        takePicture();
                    }
                    break;
                case KeyEvent.KEYCODE_FOCUS:
                    if (!focusing) {
                        autoFocusButtonDown();
                    }
                    break;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!isBusy() && event.getAction() == KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_CAMERA:
                    pictureRequest = false;
                    break;
                case KeyEvent.KEYCODE_FOCUS:
                    autoFocusButtonUp();
                    break;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void autoFocusButtonDown() {
        if (!autoFocusSuccess) {
            mCamera.autoFocus(mAutoFocusCallback);
            focusing = true;
        }
    }

    private void autoFocusButtonUp() {
        autoFocusSuccess = false;
        // focusing = false;
    }

    private void takePicture() {
        if (autoFocusSuccess) {
            takePictureWithParas();
        }
    }

    private void submitFocusAreaRect(final Rect touchRect) {
        Parameters cameraParameters = mCamera.getParameters();
        if (cameraParameters.getMaxNumFocusAreas() == 0
                || !Parameters.FOCUS_MODE_AUTO.equals(cameraParameters.getFocusMode())) {
            return;
        }
        try {
            mFocusView.setBackgroundResource(R.drawable.ic_focus_focusing);
            LayoutParams lp = (LayoutParams) mFocusView.getLayoutParams();
            lp.topMargin = touchRect.top;
            lp.leftMargin = touchRect.left;
            lp.width = touchRect.width();
            lp.height = touchRect.height();
            mFocusView.setLayoutParams(lp);
            mTouchView.requestLayout();

            // Convert from View's width and height to +/- 1000

            Rect focusArea = new Rect();

            focusArea.set(touchRect.left * 2000 / mPreview.getWidth() - 1000,
                    touchRect.top * 2000 / mPreview.getHeight() - 1000,
                    touchRect.right * 2000 / mPreview.getWidth() - 1000,
                    touchRect.bottom * 2000 / mPreview.getHeight() - 1000);

            d("touch:" + touchRect);
            d("focus:" + focusArea);
            // Submit focus area to camera

            ArrayList<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
            focusAreas.add(new Camera.Area(focusArea, 1000));

            cameraParameters.setFocusAreas(focusAreas);
            mCamera.setParameters(cameraParameters);

            d("Busy when focus:" + isBusy());
            // Start the autofocus operation
            mCamera.autoFocus(mAutoFocusCallback);
        } catch (Exception e) {
            e.printStackTrace();
            focusing = false;
            mFocusView.setBackgroundDrawable(null);
        }
    }

    private void takePictureWithParas() {
        setBusy(true);
        //mCamera.setParameters(realtimeParameters.update(mCamera.getParameters()));
        pictureRequest = false;
        mCamera.takePicture(shutterCallback, null, mPicture);
    }

    final Handler handler = new Handler();

    private AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            d("onAutoFocus:" + success);
            focusing = false;
            if (success) {
                mFocusView.setBackgroundResource(R.drawable.ic_focus_focused);
            } else {
                mFocusView.setBackgroundResource(R.drawable.ic_focus_failed);
            }

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mFocusView.setBackgroundDrawable(null);
                }
            }, 1000);
            if (pictureRequest) {
                pictureRequest = false;
                takePictureWithParas();
                return;
            }
            pictureRequest = false;
            if (success) {
                autoFocusSuccess = true;
            }
        }
    };

    protected void restartPreview() {
        mCamera.startPreview();
    }

    protected void handlePicture(byte[] data) {
        // setBusy(true);
        d("PictureTaken:" + data.length);
        // setBusy(false);
    }

    private final ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
        }
    };

    protected PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            handlePicture(data);
            mCamera.startPreview();
            setBusy(false);
        }
    };

    /**
     * Toolkit
     */

    /**
     * 找出大于 width height 的最小尺寸(断言 list 是从小到大排序的) 找不到则返回最后一个 size
     *
     * @param list
     * @param width
     * @param height
     * @param factor
     * @return
     */
    private static Size getOptimalSize(List<Size> list, int width, int height, int factor) {
        Size result = null;
        for (final Size size : list) {
            if (size.width >= width * factor && size.height >= height * factor) {
                result = size;
                break;
            }
        }
        if (result == null) {
            result = list.get(list.size() - 1);
        }
        d("getOptimalSize: " + result.width + " x " + result.height);
        return result;
    }

    private static String prettyParameters(Parameters p) {
        String[] s = p.flatten().split(";");
        StringBuilder b = new StringBuilder();
        for (String _s : s) {
            b.append(_s).append("\n");
        }
        return b.toString();
    }

    /**
     *
     * @param flashMode
     * @return 返回闪光灯模式的名词，未知返回 null
     */
    public static String getFlashModeName(String flashMode){
        switch (flashMode){
            case Parameters.FLASH_MODE_AUTO:
                return "自动";
            case Parameters.FLASH_MODE_ON:
                return "打开";
            case Parameters.FLASH_MODE_OFF:
                return "关闭";
            case Parameters.FLASH_MODE_TORCH:
                return "长亮";
            case Parameters.FLASH_MODE_RED_EYE:
                return "红眼";
            default:
                return null;
        }
    }

    private static String getCameraSupportedInfo(Parameters p) {
        StringBuilder b = new StringBuilder();
        b.append("Supported Antibanding List:").append("\n");
        for (Object o : p.getSupportedAntibanding()) {
            b.append(o).append("\n");
        }
        b.append("Supported ColorEffects List:").append("\n");
        for (Object o : p.getSupportedColorEffects()) {
            b.append(o).append("\n");
        }
        b.append("Supported FlashModes List:").append("\n");
        for (Object o : p.getSupportedFlashModes()) {
            b.append(o).append("\n");
        }
        b.append("Supported FocusModes List:").append("\n");
        for (Object o : p.getSupportedFocusModes()) {
            b.append(o).append("\n");
        }
        b.append("Supported JpegThumbnailSizes List:").append("\n");
        for (Object o : p.getSupportedJpegThumbnailSizes()) {
            b.append(o).append("\n");
        }
        b.append("Supported PictureFormats List:").append("\n");
        for (Object o : p.getSupportedPictureFormats()) {
            b.append(o).append("\n");
        }
        b.append("Supported PictureSizes List:").append("\n");
        for (Object o : p.getSupportedPictureSizes()) {
            b.append(o).append("\n");
        }
        b.append("Supported PreviewFormats List:").append("\n");
        for (Object o : p.getSupportedPreviewFormats()) {
            b.append(o).append("\n");
        }
        b.append("Supported PreviewFpsRange List:").append("\n");
        for (Object o : p.getSupportedPreviewFpsRange()) {
            b.append(o).append("\n");
        }
        b.append("Supported PreviewFrameRates List:").append("\n");
        for (Object o : p.getSupportedPreviewFrameRates()) {
            b.append(o).append("\n");
        }
        b.append("Supported PreviewFpsRange List:").append("\n");
        for (Object o : p.getSupportedPreviewFpsRange()) {
            b.append(o).append("\n");
        }
        b.append("Supported PreviewSizes List:").append("\n");
        for (Object o : p.getSupportedPreviewSizes()) {
            b.append(o).append("\n");
        }
        b.append("Supported SceneModes List:").append("\n");
        for (Object o : p.getSupportedSceneModes()) {
            b.append(o).append("\n");
        }
        b.append("Supported VideoSizes List:").append("\n");
        for (Object o : p.getSupportedVideoSizes()) {
            b.append(o).append("\n");
        }
        b.append("Supported WhiteBalance List:").append("\n");
        for (Object o : p.getSupportedWhiteBalance()) {
            b.append(o).append("\n");
        }

        b.append("MaxExposureCompensation: ").append(p.getMaxExposureCompensation() + "\n");
        b.append("MaxNumDetectedFaces: ").append(p.getMaxNumDetectedFaces() + "\n");
        b.append("MaxNumFocusAreas: ").append(p.getMaxNumFocusAreas() + "\n");
        b.append("MaxNumMeteringAreas: ").append(p.getMaxNumMeteringAreas() + "\n");
        b.append("MaxZoom: ").append(p.getMaxZoom() + "\n");

        return b.toString();
    }

    private static void d(Object o) {
        Log.i(TAG, o == null ? "null" : o.toString());
    }

    private boolean busy; // TODO multi thread support

    protected void setBusy(boolean b) {
        this.busy = b;
    }


    public boolean isBusy() {
        return busy;
    }

}
