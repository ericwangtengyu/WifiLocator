/**
 * 
 */
package wifilocator.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

/**
 * @author Eric
 *
 */
public class MapLoader {

	private Context context;
	private ImageView mapView;
	private Bitmap map;
	private MapTouchListener mapTouchListener;
	
	public MapLoader(Context context,ImageView mapView)
	{
		this.setContext(context);
		this.setMapView(mapView);
	}
	
	public void loadMap(int id)
	{
		map=BitmapFactory.decodeResource(context.getResources(),id).copy(Bitmap.Config.ARGB_8888, true);
		mapView.setImageBitmap(map);
		mapTouchListener=new MapTouchListener(mapView,map);
		mapView.setOnTouchListener(mapTouchListener);
//		Canvas canvas=new Canvas(map);
//		Paint paint = new Paint();  
//        paint.setColor(Color.RED);
//        canvas.drawCircle(100, 100, 10, paint);
	}

	/**
	 * @return the mapView
	 */
	public ImageView getMapView() {
		return mapView;
	}

	/**
	 * @param mapView the mapView to set
	 */
	public void setMapView(ImageView mapView) {
		this.mapView = mapView;
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}
	
	public void setMapTouchListener(MapTouchListener mapTouchListener)
	{
		this.mapTouchListener=mapTouchListener;
	}
	
}
