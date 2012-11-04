package wifilocator.thread;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;
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
    private BlockingQueue<Signature> memoryQueue;
    
	/**
	 * constructor function of class WifiStateReceiver
	 * @author Eric Wang
	 * @param context
	 * @param wifiService
	 * @param eventQueue
	 */
	public WifiStateReceiver(Context context, WifiService wifiService,BlockingQueue<Signature> eventQueue,BlockingQueue<Signature> memoryQueue)
	{
		this.setContext(context);
		this.wifiService=wifiService;
		this.eventQueue=eventQueue;
		this.memoryQueue=memoryQueue;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action=intent.getAction();
		if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
		{
			wifiService.startScan();
			List<ScanResult> wifiList=wifiService.getWifiList();
			if(wifiList==null) return;
			try {
				Signature s=memoryQueue.take();
				s.setSigList_s(wifiList);
				s.setTimeStamp(System.currentTimeMillis());
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
