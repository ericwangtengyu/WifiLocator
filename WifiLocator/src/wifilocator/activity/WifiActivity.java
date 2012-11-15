package wifilocator.activity;

import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.wifi.WifiManager;
import wifilocator.gui.MapLoader;
import wifilocator.gui.MapTouchListener;
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
    private EditText roomnum_text;
    private ImageView map_image;
    
    
    private WifiService wifiService;
    private FileService fileService;
    private DataStorage dataStorage;
    
    private Thread consumer;
    private Context context;
    private Handler handler;
    
    
    private WifiStateReceiver wifiStateReceiver;
    private IntentFilter filter;
    
    private Timer timer;
    private WifiScanTask wifiScanTask;
    private UIUpdateTask uiUpdateTask;
    
    private MyBtnListener btnListener;
    private BlockingQueue<Signature> eventQueue;
    private BlockingQueue<Signature> memoryQueue;
    
    private PowerManager powerManager;
	private WakeLock wakeLock;
	//GUI part
	private MapLoader mapLoader;
	
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
		if(wifiScanTask!=null)
     	   wifiScanTask.cancel();
		if(uiUpdateTask!=null)
		   uiUpdateTask.cancel();
		timer.cancel();
		wifiService.closeWifi();
		try {
			//this.unregisterReceiver(wifiStateReceiver);
			fileService.closeFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block;
			e.printStackTrace();
		}
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
        eventQueue=new ArrayBlockingQueue<Signature>(10);
        memoryQueue=new ArrayBlockingQueue<Signature>(10);
        for(int i=0;i<10;i++)
        {
        	Signature s=new Signature();
        	try {
				memoryQueue.put(s);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        dataStorage=new DataStorage(fileService,eventQueue,memoryQueue);
        wifiStateReceiver=new WifiStateReceiver(this,wifiService,eventQueue,memoryQueue);     
        consumer=new Thread(dataStorage);
        consumer.setName("dataStorage");
        handler=new Handler(){
        	public void handleMessage(Message msg) {
        		displayAllWifiList();
            }
        };
        btnListener=new MyBtnListener();
        filter=new IntentFilter();
        timer=new Timer();
        powerManager=(PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Wifi");
        mapLoader=new MapLoader(context,map_image);
        mapLoader.loadMap(R.drawable.smallmap);

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
	        	   //context.registerReceiver(wifiStateReceiver, filter);
	             try {
	   			   fileService.createFileOnSD(roomnum_text.getText().toString());
	   		        } catch (Exception e) {
	   			   // TODO Auto-generated catch block
	   			   e.printStackTrace();
	   		       }
	        	   //wifiService.startScan();
        		   wifiScanTask=new WifiScanTask(wifiService,eventQueue,memoryQueue);
        		   timer.scheduleAtFixedRate(wifiScanTask, 0, 1000);
        		   uiUpdateTask=new UIUpdateTask(handler);
        		   timer.scheduleAtFixedRate(uiUpdateTask, 0, 1000);
	        	   if(!consumer.isAlive())
	        	   consumer.start();
	        	   displayAllWifiList();
	        	   btn_scan.setEnabled(false);
	               break;
	           case R.id.stopScan:
	        	   //context.unregisterReceiver(wifiStateReceiver);
	        	   if(wifiScanTask!=null)
	        	   wifiScanTask.cancel();
	        	   if(uiUpdateTask!=null)
	        		   uiUpdateTask.cancel();
	        	   btn_scan.setEnabled(true);
	        	   wifilist_text.setText("");
	        	   try {
	       			fileService.closeFile();
	       		    } catch (Exception e) {
	       			  e.printStackTrace();
	       		    }
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
