package wifilocator.thread;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;
import android.widget.Toast;
import wifilocator.service.WifiService;
import wifilocator.signature.*;

/**
 * Receiver of Wifi Actions
 * @author Eric Wang
 * @version beta
 */
public class WifiStateReceiver extends BroadcastReceiver{
	private Context context;
	private WifiService wifiService;
    private BlockingQueue<Signature> eventQueue;
    //private BlockingQueue<List<ScanResult>> memoryQueue;
    
	/**
	 * constructor function of class WifiStateReceiver
	 * @author Eric Wang
	 * @param context
	 * @param wifiService
	 * @param eventQueue
	 */
	public WifiStateReceiver(Context context, WifiService wifiService,BlockingQueue<Signature> eventQueue)
	{
		this.setContext(context);
		this.wifiService=wifiService;
		this.eventQueue=eventQueue;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action=intent.getAction();
		if(action.equals(WifiManager.RSSI_CHANGED_ACTION))
		{
			wifiService.startScan();
			Toast.makeText(context, "RSSI value changed", Toast.LENGTH_SHORT).show();
			List<ScanResult> wifiList=wifiService.getWifiList();
			Signature s=new Signature(wifiList,System.currentTimeMillis());
			try {
				eventQueue.put(s);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
