package org.gbc.ucitour.ar;

import org.gbc.ucitour.location.LocationProvider;
import org.gbc.ucitour.model.LocationPoint;
import org.gbc.ucitour.net.Query;

import com.google.gson.JsonObject;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.location.Location;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.ViewGroup.LayoutParams;

public class AugmentedRealityActivity extends Activity {
  
  private Camera camera;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    LocationProvider provider = LocationProvider.instance(this);
    
    // Get the relevant point
    String pointId = getIntent().getStringExtra("point");
    LocationPoint point = Query.single("id", "4504699138998272");
    
    Location location = provider.getLocation();
    Location target = new Location("Luci Server");
    target.setLatitude(point.getLatitude());
    target.setLongitude(point.getLongitude());
    //System.out.println(location);
    //float bearing = location.bearingTo(target);
    
    // Initiate the Open GL view and
    // create an instance with this activity
    GLSurfaceView glSurfaceView = new GLSurfaceView(this);
    glSurfaceView.setLayoutParams(
        new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
    glSurfaceView.setZOrderOnTop(true);
    glSurfaceView.setRenderer(new GlRenderer(this));
    addContentView(glSurfaceView,
        new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    
    // Create an instance of Camera
    camera = Camera.open();
    CameraPreview preview = new CameraPreview(this, camera);
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
