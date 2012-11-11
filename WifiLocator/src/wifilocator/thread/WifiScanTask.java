/**
 * 
 */
package wifilocator.thread;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import android.net.wifi.ScanResult;

import wifilocator.service.WifiService;
import wifilocator.signature.Signature;

/**
 * Scheduled task of scanning wifi access points.
 * A child class of TimerTask
 * @author Eric Wang
 * @version 0
 */
public class WifiScanTask extends TimerTask{
	private WifiService wifiService;
    private BlockingQueue<Signature> eventQueue;
    private BlockingQueue<Signature> memoryQueue;
    
    /**
     * Constructor function of wifi scan task
     * @author Eric Wang
     */
    public WifiScanTask(WifiService wifiService,BlockingQueue<Signature> eventQueue,BlockingQueue<Signature> memoryQueue)
    {
    	this.setWifiService(wifiService);
    	this.setEventQueue(eventQueue);
    	this.setMemoryQueue(memoryQueue);
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		wifiService.startScan();
		List<ScanResult> wifiList=wifiService.getWifiList();
		if(wifiList.size()==0) return;
		try {
			Signature s=memoryQueue.take();
			s.setSigList_s(wifiList);
			s.setTimeStamp(System.currentTimeMillis());
			s.setHashMap();
			eventQueue.put(s);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the wifiService
	 */
	public WifiService getWifiService() {
		return wifiService;
	}
	/**
	 * @param wifiService the wifiService to set
	 */
	public void setWifiService(WifiService wifiService) {
		this.wifiService = wifiService;
	}
	/**
	 * @return the eventQueue
	 */
	public BlockingQueue<Signature> getEventQueue() {
		return eventQueue;
	}
	/**
	 * @param eventQueue the eventQueue to set
	 */
	public void setEventQueue(BlockingQueue<Signature> eventQueue) {
		this.eventQueue = eventQueue;
	}
	/**
	 * @return the memoryQueue
	 */
	public BlockingQueue<Signature> getMemoryQueue() {
		return memoryQueue;
	}
	/**
	 * @param memoryQueue the memoryQueue to set
	 */
	public void setMemoryQueue(BlockingQueue<Signature> memoryQueue) {
		this.memoryQueue = memoryQueue;
	}

}
