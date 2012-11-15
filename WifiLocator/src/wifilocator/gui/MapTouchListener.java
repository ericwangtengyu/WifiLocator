/**
 * 
 */
package wifilocator.gui;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * @author Eric
 *
 */
public class MapTouchListener implements OnTouchListener{

	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	private static int NONE=0;
	private static int DRAG=1;
	private static int ZOOM=2;
	private static final float MAX_SCALE = 4f;
	private int mode;
	private DisplayMetrics dm;
	private Matrix currentMatrix;
	private Matrix savedMatrix;
	private PointF prevP;
	private PointF midP;
	private float dist;
	private ImageView map_image;
	private Bitmap map;
	
	
	public MapTouchListener(ImageView map_image,Bitmap map)
	{
		currentMatrix=new Matrix();
		savedMatrix=new Matrix();
		prevP=new PointF();
		midP=new PointF();
		dm=new DisplayMetrics();
		mode=NONE;
		this.map_image=map_image;
		this.map=map;
		map_image.setImageMatrix(currentMatrix);
	}
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()&MotionEvent.ACTION_MASK)
		{
		case MotionEvent.ACTION_DOWN:
			prevP.set(event.getX(), event.getY());
			mode=DRAG;
			savedMatrix.set(currentMatrix);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			dist=distance(event);
			if(dist>10f)
			{
				savedMatrix.set(currentMatrix);
				midPoint(midP, event);
				mode=ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode=NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			if(mode==DRAG)
			{
				currentMatrix.set(savedMatrix);
				currentMatrix.postTranslate(event.getX()-prevP.x, event.getY()-prevP.y);
			}
			else if(mode==ZOOM)
			{
				float newDist=distance(event);
				if(newDist>10f)
				{
					currentMatrix.set(savedMatrix);
					float tScale=newDist/dist;
					currentMatrix.postScale(tScale, tScale, midP.x, midP.y);
				}
			}
			break;
		}
		map_image.setImageMatrix(currentMatrix);
		return true;
	}
	public float distance(MotionEvent event)
	{
		float x =event.getX(0) - event.getX(1);
		float y =event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x*x+y*y);
	}
	
	private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x/2, y/2);
    }
	private void minZoom() {
        float minScaleR = Math.min(
                (float) map_image.getWidth() / (float) map.getWidth(),
                (float) map_image.getHeight() / (float) map.getHeight());
        if (minScaleR < 1.0) {
            currentMatrix.postScale(minScaleR, minScaleR);
        }
    }
	
}
