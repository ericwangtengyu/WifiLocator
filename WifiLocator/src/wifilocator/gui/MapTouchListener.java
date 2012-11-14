/**
 * 
 */
package wifilocator.gui;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * @author Eric
 *
 */
public class MapTouchListener implements OnTouchListener{

	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	private static int INITIAL=0;
	private static int DRAG=1;
	private static int ZOOM=2;
	
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
