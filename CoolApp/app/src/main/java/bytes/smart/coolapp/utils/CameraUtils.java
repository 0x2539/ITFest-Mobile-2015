package bytes.smart.coolapp.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;

/**
 * Created by alexbuicescu on 14.08.2015.
 */
public class CameraUtils {
    private static final String TAG = "CameraUtils";

    private static boolean isLedFlashing = false;
    private static boolean isLedBlinking = false;
    private static Camera camera;
    private static Camera.Parameters cameraParameters;
    private static boolean isLightOn;

    public static void blinkLed(final Context context, boolean blink, long time)
    {
        if(blink && !isLedBlinking)
        {
            Log.i(TAG, "blink led");
            time = time * 1000 * 60;
            final long startTime = System.currentTimeMillis();
            isLedBlinking = true;
            isLedFlashing = false;
            stopLed(context);
            final long finalTime = time;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    float Hz = 10;
                    long sleepMS= (long) ((1/Hz)*1000/2);
                    flashLed(context);
//                    camera = Camera.open();
//                    try {
//                        camera.setPreviewTexture(surfaceTexture);
////                    camera.setPreviewDisplay(null);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    camera.startPreview();
                    Log.i(TAG, "blink led in thread");
                    while(isLedBlinking && startTime + finalTime > System.currentTimeMillis())
                    {
                        Log.i(TAG, "blink led in loop");
                        if(isLedFlashing)
                        {
                            flipFlash();
                            try {
                                Thread.sleep(sleepMS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            flipFlash();
                            try {
                                Thread.sleep(sleepMS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    stopLed(context);
//                    camera.stopPreview();
//                    camera.release();
                }
            }).start();
        }
        else
        if(blink && isLedBlinking)
        {
            isLedBlinking = false;
            stopLed(context);
            blinkLed(context, blink, time);
        }
        else
        {
            isLedBlinking = false;
            stopLed(context);
        }
    }

    static SurfaceTexture surfaceTexture = new SurfaceTexture(0);

    public static void flashLed(Context context)
    {
        if(isFlashAvailable(context) && isLedBlinking)
        {
            if(!isLedFlashing) {
                Log.i(TAG, "camera flash on");
                try {
                    camera = Camera.open();
                    cameraParameters = camera.getParameters();
                    camera.setPreviewTexture(surfaceTexture);
                    camera.startPreview();
//                    camera.setPreviewDisplay(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Camera.Parameters p = camera.getParameters();
//                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                camera.setParameters(p);
                isLedFlashing = true;
            }
        }
    }

    public static void stopLed(Context context)
    {
        if(isFlashAvailable(context)) {
//            if (isLedFlashing && camera != null) {
                isLedFlashing = false;
                Log.i(TAG, "camera flash off");
//                camera = Camera.open();
//                Camera.Parameters p = camera.getParameters();
//                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                camera.setParameters(p);
            try {
                camera.stopPreview();
                camera.release();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
//            }
        }
    }

    private static boolean isFlashAvailable(Context context)
    {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
    private static void flipFlash(){
        try {
            if (isLightOn) {
                cameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(cameraParameters);
                isLightOn = false;
            } else {
                cameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(cameraParameters);
                isLightOn = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
