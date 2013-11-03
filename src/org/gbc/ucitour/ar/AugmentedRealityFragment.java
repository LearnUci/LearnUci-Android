package org.gbc.ucitour.ar;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

/**
 * Augmented Reality activity that displays a window ontop of a window feed
 */
public class AugmentedRealityFragment extends Fragment {
  private CameraPreview mPreview;
  private Camera mCamera;
  private GLSurfaceView glSurfaceView;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // Initiate the Open GL view and
    // create an instance with this activity
    glSurfaceView = new GLSurfaceView(getActivity());
    glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    glSurfaceView.setRenderer(new GlRenderer(getActivity()));

    // Add the overlay feed
    //getActivity().addContentView(glSurfaceView,
    //    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    
    // Create an instance of Camera
    mPreview = new CameraPreview(getActivity());
    
    // Add the camera feed
    //getActivity().addContentView(mPreview, new LayoutParams(LayoutParams.MATCH_PARENT,
    //    LayoutParams.MATCH_PARENT));
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return glSurfaceView;
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
}
