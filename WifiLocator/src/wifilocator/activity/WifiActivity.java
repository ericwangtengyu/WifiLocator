package wifilocator.activity;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
//import android.graphics.Color;
import android.graphics.PointF;
//import android.graphics.PorterDuff;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.net.wifi.WifiManager;
import wifilocator.gui.LocationDraw;
import wifilocator.gui.MapLoader;

import wifilocator.service.*;
import wifilocator.signature.*;
import wifilocator.thread.*;

public class WifiActivity extends Activity {
    //------widget-----//
	private Button btn_open;
    private Button btn_close;
    private Button btn_scan;
    private Button btn_stop_scan;
    private TextView wifilist_text;
    private ProgressBar scanning_bar;
    private ImageView map_image;
    
    
    private WifiService wifiService;
    private FileService fileService;
    
    private Thread consumer;
    private Context context;
    private Handler handler;
	private BlockingQueue<PointF> eventQueue;
	private BlockingQueue<PointF> memoryQueue;
    
    private IntentFilter filter;
    
    private Timer timer;
    private LocationEstimateTask locatEstiTask;
    private UIUpdateTask_Map uiUpdate;
    
    private MyBtnListener btnListener;
    
    private PowerManager powerManager;
	private WakeLock wakeLock;
	//GUI part
	private MapLoader mapLoader;
	private LocationDraw penDraw;
	float x_value=0;
	float y_value=0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        
        initWidget();
        initVariable();
        initListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_wifi, menu);
        return true;
    }
    
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(locatEstiTask!=null)
			locatEstiTask.cancel();
		timer.cancel();
		wifiService.closeWifi();
		Toast.makeText(this, "OnDestroy", Toast.LENGTH_SHORT).show();
	}
    
    /**
     * initiate private variables defined in this class
     * @author Eric Wang
     */
    private void initVariable()
    {
    	context=this;
        wifiService=new WifiService(this);
        fileService=new FileService(this);
        eventQueue=new ArrayBlockingQueue<PointF>(10);
		memoryQueue=new ArrayBlockingQueue<PointF>(10);
        try {
    		for(int i=0;i<10;i++)
    		{
    			memoryQueue.put(new PointF());
    		}
		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
        handler=new Handler(){
        	public void handleMessage(Message msg) {
        		//displayAllWifiList();
        		userLocationUpdate(msg);
            }
        };
        uiUpdate=new UIUpdateTask_Map(eventQueue,memoryQueue,handler);
        consumer=new Thread(uiUpdate);
        consumer.setName("UI Update");
        btnListener=new MyBtnListener();
        filter=new IntentFilter();
        timer=new Timer();
        powerManager=(PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Wifi");
        mapLoader=new MapLoader(context,map_image);
        mapLoader.loadMap(R.drawable.jessup);
        penDraw=new LocationDraw(mapLoader.getBitmap());

    }
    

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		wakeLock.acquire();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		wakeLock.release();
	}

	/**
	 * Initiate the widgets
	 * @author Eric Wang
	 */
	private void initWidget()
    {
    	btn_open=(Button)findViewById(R.id.openWifi);
    	btn_close=(Button)findViewById(R.id.closeWifi);
    	btn_scan=(Button)findViewById(R.id.scanWifi);
    	btn_stop_scan=(Button)findViewById(R.id.stopScan);
    	wifilist_text=(TextView)findViewById(R.id.wifiList);
    	wifilist_text.setMovementMethod(ScrollingMovementMethod.getInstance());
    	scanning_bar=(ProgressBar)findViewById(R.id.scanning);
    	map_image=(ImageView)findViewById(R.id.mapView);
    }
    
    /**
   	 * Initiate the Listeners
   	 * @author Eric Wang
   	 */
    private void initListener()
    {
    	btn_open.setOnClickListener(btnListener);
    	btn_close.setOnClickListener(btnListener);
    	btn_scan.setOnClickListener(btnListener);
    	btn_stop_scan.setOnClickListener(btnListener);
    	
    	filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
    	filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
    	/*if you need to filter out more actions, 
    	  you can add that action to the filter*/
    }
    
    /**
   	 * display the results of all the latest wifi access points scan
   	 * @author Eric Wang
   	 */
    private void displayAllWifiList()
    {
    	StringBuilder scanResult=wifiService.getWifiListInString();
    	wifilist_text.setText(scanResult.toString());
    }
    
    /**
     * Update the location of user on the map
     * @author Eric Wang
     */
    @SuppressLint("NewApi")
	private void userLocationUpdate(Message msg)
    {
    	mapLoader.setBitmap(R.drawable.jessup);
    	penDraw.changeMap((mapLoader.getBitmap()));
    	float x=((PointF)msg.obj).x;
    	float y=((PointF)msg.obj).y;
//    	penDraw.draw(x_value,y_value);
//    	penDraw.getCanvas().drawColor(Color.WHITE,PorterDuff.Mode.CLEAR);
    	penDraw.draw(x, y);
    	map_image.setImageBitmap(mapLoader.getBitmap());
//    	x_value=x_value+20.5f;
//    	y_value=y_value+20.5f;
    }
    
    /**
     * User defined class which implements the OnClickListerer interface.
     * Listener for button click event.
     * @author Eric Wang
     * @version beta
     */
    private class MyBtnListener implements OnClickListener{  
    	  
        public void onClick(View v) {  
            // TODO Auto-generated method stub  
	        switch (v.getId()) {  
	           case R.id.scanWifi:
	        	   locatEstiTask =new LocationEstimateTask(wifiService,fileService,eventQueue,memoryQueue);
        		   timer.scheduleAtFixedRate(locatEstiTask, 0, 1000);
	        	   if(!consumer.isAlive())
	        	   consumer.start();
	        	   btn_scan.setEnabled(false);
	               break;
	           case R.id.stopScan:
	        	   if(locatEstiTask!=null)
	        		   locatEstiTask.cancel();
	        	   btn_scan.setEnabled(true);
	        	   break;
	           case R.id.openWifi: 
	        	   wifiService.openWifi();
	               Toast.makeText(WifiActivity.this,"Current Wifi state is"+wifiService.getState(), Toast.LENGTH_SHORT).show();
	               break;  
	           case R.id.closeWifi:  
	        	   wifiService.closeWifi();
	        	   Toast.makeText(WifiActivity.this,"Current Wifi state is"+wifiService.getState(), Toast.LENGTH_SHORT).show(); 
	               break;  
	           default:  
	               break;
	        }
        }
    }   
}
