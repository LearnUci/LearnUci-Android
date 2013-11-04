package org.gbc.ucitour.ar;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple wrapper around a Camera and a SurfaceView that renders a centered
 * preview of the Camera to the surface. We need to center the SurfaceView
 * because not all devices have cameras that support preview sizes at the same
 * aspect ratio as the device's display.
 */
class CameraPreview extends ViewGroup implements SurfaceHolder.Callback {
  private final String TAG = "Preview";

  SurfaceView mSurfaceView;
  SurfaceHolder mHolder;
  Size mPreviewSize;
  List<Size> mSupportedPreviewSizes;
  Camera mCamera;
  boolean mSurfaceCreated = false;

  @SuppressWarnings("deprecation")
  CameraPreview(Context context) {
    super(context);

    mSurfaceView = new SurfaceView(context);
    addView(mSurfaceView);

    // Install a SurfaceHolder.Callback so we get notified when the
    // underlying surface is created and destroyed.
    mHolder = mSurfaceView.getHolder();
    mHolder.addCallback(this);
    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
  }

  public void setCamera(Camera camera) {
    mCamera = camera;
    setCameraDisplayOrientation(CameraInfo.CAMERA_FACING_BACK, camera);
  }

  public void switchCamera(Camera camera) {
    setCamera(camera);
    try {
      camera.setPreviewDisplay(mHolder);
    } catch (IOException exception) {
      Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // We purposely disregard child measurements because act as a
    // wrapper to a SurfaceView that centers the camera preview instead
    // of stretching it.
    final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
    final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
    setMeasuredDimension(width, height);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    if (getChildCount() > 0) {
      final View child = getChildAt(0);

      final int width = r - l;
      final int height = b - t;

      int previewWidth = width;
      int previewHeight = height;
      if (mPreviewSize != null) {
        previewWidth = mPreviewSize.width;
        previewHeight = mPreviewSize.height;
      }

      // Center the child SurfaceView within the parent.
      if (width * previewHeight > height * previewWidth) {
        final int scaledChildWidth = previewWidth * height / previewHeight;
        child.layout((width - scaledChildWidth) / 2, 0, (width + scaledChildWidth) / 2, height);
      } else {
        final int scaledChildHeight = previewHeight * width / previewWidth;
        child.layout(0, (height - scaledChildHeight) / 2, width, (height + scaledChildHeight) / 2);
      }
    }
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    // The Surface has been created, acquire the camera and tell it where
    // to draw.
    try {
      if (mCamera != null) {
        mCamera.setPreviewDisplay(holder);
      }
    } catch (IOException exception) {
      Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
    }
    if (mPreviewSize == null) {
      requestLayout();
    }
    mSurfaceCreated = true;
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    // Surface will be destroyed when we return, so stop the preview.
    if (mCamera != null) {
      mCamera.stopPreview();
    }
  }

  private static void setCameraDisplayOrientation(int cameraId, android.hardware.Camera camera) {
    android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
    android.hardware.Camera.getCameraInfo(cameraId, info);
    int degrees = 0;
    int result;
    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (info.orientation + degrees) % 360;
      result = (360 - result) % 360; // compensate the mirror
    } else { // back-facing
      result = (info.orientation - degrees + 360) % 360;
    }
    if (camera != null) {
      camera.setDisplayOrientation(result);
    }
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    mCamera.startPreview();
  }

}