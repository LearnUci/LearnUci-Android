package org.learnuci.ar;

import java.util.Locale;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.cyanogenmod.focal.picsphere.SensorFusion;
import org.learnuci.location.LocationProvider;
import org.learnuci.model.LocationPoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.location.Location;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.util.TypedValue;

class GlRenderer implements Renderer {
  private Square square;
  private Context context;
  private SensorFusion sensors;
  private Bitmap bitmap;
  private Canvas canvas;
  private Object bmpLock = new Object();
  private LocationProvider locationProvider;
  
  private String prevDist = "";
  private Bitmap img;
  private boolean changed = false;
  
  private LocationPoint location;
  
  private Paint titlePaint = new Paint();
  private Paint distPaint = new Paint();
  
  /** Constructor to set the handed over context */
  public GlRenderer(Context context) {
    this.context = context;
    this.square = new Square();
    this.sensors = new SensorFusion(context);
    this.bitmap = Bitmap.createBitmap(100, 100, Config.ARGB_8888);
    this.canvas = new Canvas(bitmap);
    canvas.drawColor(0xFFFFFFFF);

    this.locationProvider = LocationProvider.instance(context);
    
    this.titlePaint.setTextSize(sp(12));
    this.titlePaint.setColor(0xFFFFFFFF);
    this.distPaint.setTextSize(sp(8));
    this.distPaint.setColor(0xFFFFFFFF);
  }
  
  private float sp(float size) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size,
        this.context.getResources().getDisplayMetrics());
  }

  public void setLocation(LocationPoint point) {
    synchronized (bmpLock) {
      this.location = point;
      float height = sp(12) + sp(8) + 25;
      float width = Math.max(this.titlePaint.measureText(this.location.getName()),
          this.distPaint.measureText("99.99 kilometers")) + 30;
      if (this.location.hasImage()) {
        byte[] data = this.location.getImage();
        img = BitmapFactory.decodeByteArray(data, 0, data.length);
        height = img.getHeight();
        width += img.getWidth();
      }
      
      bitmap = Bitmap.createBitmap((int) width, (int) height, Config.ARGB_8888);
      square.setVertexBuffer(width, height);
      canvas = new Canvas(bitmap);
      changed = true;
    }
  }
  
  @Override
  public void onDrawFrame(GL10 gl) {
    // clear Screen and Depth Buffer
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

    // Reset the Modelview Matrix
    gl.glLoadIdentity();

    // Drawing
    Location myLocation = locationProvider.getLocation();
    Location loc = new Location("custom");
    loc.setLatitude(this.location.getLatitude());
    loc.setLongitude(this.location.getLongitude());
    if (myLocation != null) {
      gl.glRotatef(
          (float) (Math.toDegrees(sensors.getFusedOrientation()[0]) - myLocation.bearingTo(loc)),
          0.0f, 1.0f, 0.0f);
    }
    
    gl.glTranslatef(0.0f, 0.0f, -10.0f);
    
    synchronized (bmpLock) {
      String name = this.location.getName();
      String dist = "Unknown Distance";

      if (myLocation != null) {
        
        float d = loc.distanceTo(locationProvider.getLocation());
        if (d > 1000) {
          dist = d == 1000
              ? "1 kilometer" : String.format(Locale.ENGLISH, "%.2f kilometers", d / 1000);
        } else {
          dist = d == 1 ? "1 meter" : String.format(Locale.ENGLISH, "%.2f meters", d);
        }
      }

      //System.out.println(bitmap.getWidth() + " " + bitmap.getHeight());
      if (!prevDist.equals(dist) || changed) {
        canvas.drawColor(0x00FFFFFF, Mode.CLEAR);
        canvas.drawColor(0xDD222222);
        if (img != null) {
          canvas.drawBitmap(img, 0, 0, null);
          canvas.drawText(name, img.getWidth() + 15, sp(12) + 5, titlePaint);
          canvas.drawText(dist, img.getWidth() + 15, sp(12) + sp(8) + 10, distPaint);
        } else {
          int xTitle = (int) ((bitmap.getWidth() - titlePaint.measureText(name)) / 2);
          int xDist = (int) ((bitmap.getWidth() - distPaint.measureText(dist)) / 2);
          canvas.drawText(name, xTitle, sp(12) + 5, titlePaint);
          canvas.drawText(dist, xDist, sp(8) + sp(12) + 10, distPaint);
        }
        square.loadGLTexture(gl, bitmap, this.context);
        changed = false;
      }
      prevDist = dist;
    }
    square.draw(gl);
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    if(height == 0) {             //Prevent A Divide By Zero By
      height = 1;             //Making Height Equal One
    }

    gl.glViewport(0, 0, width, height);   //Reset The Current Viewport
    gl.glMatrixMode(GL10.GL_PROJECTION);  //Select The Projection Matrix
    gl.glLoadIdentity();          //Reset The Projection Matrix

    //Calculate The Aspect Ratio Of The Window
    GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

    gl.glMatrixMode(GL10.GL_MODELVIEW);   //Select The Modelview Matrix
    gl.glLoadIdentity();          //Reset The Modelview Matrix
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    // Load the texture for the square
    square.loadGLTexture(gl, bitmap, this.context);
    
    gl.glEnable(GL10.GL_TEXTURE_2D);      //Enable Texture Mapping ( NEW )
    gl.glShadeModel(GL10.GL_SMOOTH);      //Enable Smooth Shading
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0);
    gl.glClearDepthf(1.0f);           //Depth Buffer Setup
    gl.glEnable(GL10.GL_DEPTH_TEST);      //Enables Depth Testing
    gl.glDepthFunc(GL10.GL_LEQUAL);       //The Type Of Depth Testing To Do
    
    //Really Nice Perspective Calculations
    gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    
  }
}
