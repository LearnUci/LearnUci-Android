package org.learnuci.ar;

import org.learnuci.model.LocationPoint;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class AugmentedRealityFragment extends Fragment {
  private GLSurfaceView glView;
  
  private CameraPreview mPreview;
  private Camera mCamera;
  private FrameLayout fl;
  private GlRenderer renderer;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    renderer = new GlRenderer(getActivity());
    mPreview = new CameraPreview(getActivity());
    glView = new GLSurfaceView(getActivity());
    glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
    glView.setRenderer(renderer);
    glView.setZOrderOnTop(true);
    fl = new FrameLayout(getActivity());
    fl.addView(mPreview);
    fl.addView(glView);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return fl;
  }
  
  @Override
  public void onPause() {
    super.onPause();

    // Because the Camera object is a shared resource, it's very
    // important to release it when the activity is paused.
    if (mCamera != null) {
        mPreview.setCamera(null);
        mCamera.release();
        mCamera = null;
    }
  }
  
  @Override
  public void onResume() {
    super.onResume();
    mCamera = Camera.open();
    mPreview.setCamera(mCamera);
  }
  
  @Override
  public void onHiddenChanged(boolean hidden) {
    if (hidden) {
      glView.setVisibility(View.GONE);
    } else {
      glView.setVisibility(View.VISIBLE); 
    }
  }
  
  public void setLocation(LocationPoint location) {
    renderer.setLocation(location); 
  }
}
