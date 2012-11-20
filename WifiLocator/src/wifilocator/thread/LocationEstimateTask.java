/**
 * 
 */
package wifilocator.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.graphics.PointF;
import android.net.wifi.ScanResult;

import wifilocator.service.FileService;
import wifilocator.service.WifiService;
import wifilocator.signature.Signature;
import wifilocator.signature.UserLocation;

/**
 * @author Eric
 *
 */
public class LocationEstimateTask extends TimerTask{

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	private UserLocation userLocation;
	private WifiService wifiService;
	private BlockingQueue<PointF> eventQueue;
	private BlockingQueue<PointF> memoryQueue;
	
	public LocationEstimateTask(WifiService wifiService,FileService fileService,BlockingQueue<PointF> eventQueue,BlockingQueue<PointF> memoryQueue)
	{
		this.wifiService=wifiService;
		this.userLocation=new UserLocation();
		List<Signature> refSigList=new ArrayList<Signature>();
		String fileName="wifiData.csv";
		try {
			refSigList=fileService.readFile(fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userLocation.setRefSigList(refSigList);
		this.eventQueue=eventQueue;
		this.memoryQueue=memoryQueue;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		wifiService.startScan();
		List<ScanResult> wifiList=wifiService.getWifiList();
		Signature userSignature=new Signature(wifiList,System.currentTimeMillis());
		userLocation.setUserSignature(userSignature);
		PointF userPoint;
		try {
			userPoint = memoryQueue.take();
			userPoint.set(userLocation.getLocation());
			eventQueue.put(userPoint);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the userLocation
	 */
	public UserLocation getUserLocation() {
		return userLocation;
	}
	/**
	 * @param userLocation the userLocation to set
	 */
	public void setUserLocation(UserLocation userLocation) {
		this.userLocation = userLocation;
	}

	public BlockingQueue<PointF> getMemoryQueue()
	{
		return memoryQueue;
	}
	
	public BlockingQueue<PointF> getEventQueue()
	{
		return eventQueue;
	}
}
