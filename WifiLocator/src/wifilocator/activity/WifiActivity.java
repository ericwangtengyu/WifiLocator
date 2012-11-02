package wifilocator.activity;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.net.wifi.WifiManager;
import wifilocator.service.*;
import wifilocator.signature.*;
import wifilocator.thread.DataStorage;
import wifilocator.thread.WifiStateReceiver;

public class WifiActivity extends Activity {
    //------widget-----//
	private Button btn_open;
    private Button btn_close;
    private Button btn_scan;
    private Button btn_stop_scan;
    private TextView wifilist_text;
    private ProgressBar scanning_bar;
    
    private WifiService wifiService;
    private FileService fileService;
    private DataStorage dataStorage;
    
    private Thread consumer;
    private Context context;
    
    private WifiStateReceiver wifiStateReceiver;
    private IntentFilter filter;
    
    private MyBtnListener btnListener;
    private BlockingQueue<Signature> eventQueue;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        
        initVariable();
        initWidget();
        initListener();
        
        try {
			fileService.createFileOnSD("wifiData.csv");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		wifiService.closeWifi();
		try {
			this.unregisterReceiver(wifiStateReceiver);
			fileService.closeFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block;
			e.printStackTrace();
		}
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
        eventQueue=new ArrayBlockingQueue<Signature>(100);
        dataStorage=new DataStorage(fileService,eventQueue);
        wifiStateReceiver=new WifiStateReceiver(this,wifiService,eventQueue);     
        consumer=new Thread(dataStorage);
        consumer.setName("dataStorage");
        btnListener=new MyBtnListener();
        filter=new IntentFilter();
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
    	scanning_bar=(ProgressBar)findViewById(R.id.scanning);
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
    	/*if you need to filter out more actions, 
    	  you can add that action to the filter*/
    }
    
    /**
   	 * display the results of all the latest wifi access points scan
   	 * @author Eric Wang
   	 */
    private void displayAllWifiList()
    {
    	wifiService.startScan();
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
	        	   context.registerReceiver(wifiStateReceiver, filter);
	        	   consumer.start();
	        	   displayAllWifiList();
	               break;
	           case R.id.stopScan:
	        	   context.unregisterReceiver(wifiStateReceiver);
	        	   if(consumer.isAlive())
	        	   consumer.interrupt();
	        	   scanning_bar.setEnabled(false);
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
