/**
 * 
 */
package wifilocator.gui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
/**
 * UI class to draw the user location onto map
 * @author Eric Wang
 * @version 0
 */
public class LocationDraw {

	private Bitmap map;
	private Canvas canvas;
	private int x;
	private int y;
	/**
	 * Constructor fuction
	 * @param map  here map is served as our canvas
	 * @author Eric Wang
	 */
	public LocationDraw(Bitmap map)
	{
		this.setMap(map);
		canvas=new Canvas(map);
	}
	
	public void draw(int x,int y)
	{
		canvas.save();
		Paint paint = new Paint();  
        paint.setColor(Color.RED);
        canvas.drawCircle(x, y, 10, paint);
        canvas.restore();
	}
	/**
	 * @return the map
	 */
	public Bitmap getMap() {
		return map;
	}
	/**
	 * @param map the map to set
	 */
	public void setMap(Bitmap map) {
		this.map = map;
	}
}
