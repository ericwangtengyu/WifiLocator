/**
 * 
 */
package wifilocator.gui;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
/**
 * UI class to draw the user location onto map
 * @author Eric Wang
 * @version 0
 */
public class LocationDraw {

	private Canvas canvas;
	/**
	 * Constructor fuction
	 * @param map  here map is served as our canvas
	 * @author Eric Wang
	 */
	public LocationDraw(Bitmap map)
	{
		canvas=new Canvas(map);
	}
	
	public void draw(float x,float y)
	{
		//canvas.save();
		canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);
		Paint paint = new Paint();  
        paint.setColor(Color.RED);
        canvas.drawCircle(x, y, 10, paint);
        //canvas.restore();
	}
	
	public void changeMap(Bitmap map)
	{
		canvas.setBitmap(map);
	}
	
	public void setCanvas(Canvas canvas)
	{
		this.canvas=canvas;
	}
	
	public Canvas getCanvas()
	{
		return canvas;
	}
}
