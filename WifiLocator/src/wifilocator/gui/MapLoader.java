/**
 * 
 */
package wifilocator.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * UI class to load the map
 * @author Eric
 * @version 0
 */
public class MapLoader {

	private Context context;
	private ImageView mapView;
	private ImageView pointView;
	private Bitmap map;
	private MapTouchListenerSecond mapTouchListener;
	
	/**
	 * Contructor fuction
	 * @author Eric Wang
	 * @param context 
	 * @param mapView
	 */
	public MapLoader(Context context,ImageView mapView,ImageView pointView)
	{
		this.setContext(context);
		this.setMapView(mapView);
		this.setPointView(pointView);
	}
	
	/**
	 * 
	 * @param id
	 */
	public void loadMap(int id)
	{
		//map=BitmapFactory.decodeResource(context.getResources(),id).copy(Bitmap.Config.ARGB_8888, true);
		Bitmap tempmap=BitmapFactory.decodeResource(context.getResources(),id).copy(Bitmap.Config.ARGB_8888, true);
		map=tempmap.createScaledBitmap(tempmap, 1000, 2045, false);
		mapView.setImageBitmap(map);
		mapTouchListener=new MapTouchListenerSecond(mapView,pointView);
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
	
//	public void setMapTouchListener(MapTouchListener mapTouchListener)
//	{
//		this.mapTouchListener=mapTouchListener;
//	}
	
	public Bitmap getBitmap()
	{
		return map;
	}
	
	public void setBitmap(int id)
	{
		//map=BitmapFactory.decodeResource(context.getResources(),id).copy(Bitmap.Config.ARGB_8888, true);
		Bitmap tempmap=BitmapFactory.decodeResource(context.getResources(),id).copy(Bitmap.Config.ARGB_8888, true);
		map=tempmap.createScaledBitmap(tempmap, 1000, 2045, false);
	}

	/**
	 * @return the pointView
	 */
	public ImageView getPointView() {
		return pointView;
	}

	/**
	 * @param pointView the pointView to set
	 */
	public void setPointView(ImageView pointView) {
		this.pointView = pointView;
	}
	
}
