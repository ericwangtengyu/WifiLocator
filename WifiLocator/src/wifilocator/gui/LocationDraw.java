/**
 * 
 */
package wifilocator.gui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
/**
 * @author Eric
 *
 */
import android.graphics.drawable.*;
public class LocationDraw {

	private Bitmap map;
	private Canvas canvas;
	private int x;
	private int y;
	public LocationDraw(Bitmap map)
	{
		this.setMap(map);
		canvas=new Canvas(map);
		Paint paint = new Paint();  
        paint.setColor(Color.RED);
        canvas.drawCircle(x, y, 10, paint);
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
