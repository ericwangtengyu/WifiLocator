/**
 * 
 */
package wifilocator.gui;

import wifilocator.activity.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	
	public void loadMap(String path)
	{
		map=BitmapFactory.decodeResource(context.getResources(),R.drawable.smallmap);
		mapView.setImageBitmap(map);
		mapView.setOnTouchListener(mapTouchListener);
		
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
	
	
}
