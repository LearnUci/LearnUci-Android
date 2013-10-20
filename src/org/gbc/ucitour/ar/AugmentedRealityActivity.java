package org.gbc.ucitour.ar;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.ViewGroup.LayoutParams;

/**
 * Augmented Reality activity that displays a window ontop of a window feed
 */
public class AugmentedRealityActivity extends Activity {
  private Camera camera;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // Initiate the Open GL view and
    // create an instance with this activity
    GLSurfaceView glSurfaceView = new GLSurfaceView(this);
    glSurfaceView.setLayoutParams(
        new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
    glSurfaceView.setZOrderOnTop(true);
    glSurfaceView.setRenderer(new GlRenderer(this));

    // Add the overlay feed
    addContentView(glSurfaceView,
        new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    
    // Create an instance of Camera
    camera = Camera.open();
    CameraPreview preview = new CameraPreview(this, camera);
    
    // Add the camera feed
    addContentView(preview, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
  }
  
  @Override
  public void onStop() {
    super.onStop();
    if (camera != null) {
      camera.stopPreview();
      camera.release();
    }
  }
}
