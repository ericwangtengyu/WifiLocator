/**
 * 
 */
package wifilocator.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import android.graphics.PointF;
import android.net.wifi.ScanResult;

import wifilocator.service.FileService;
import wifilocator.service.WifiService;
import wifilocator.signature.Signature;
import wifilocator.signature.UserLocation;

/**
 * @author Eric
 * The timerTask that esitemate the User Location.
 * @version 0
 */
public class LocationEstimateTask extends TimerTask{

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	private UserLocation userLocation;
	private WifiService wifiService;
	private BlockingQueue<PointF> eventQueue;
	private BlockingQueue<PointF> memoryQueue;
	
	/**
	 * Constructor function of LocationEstimateTask.
	 * @author Eric Wang
	 * @param wifiService
	 * @param fileService
	 * @param eventQueue
	 * @param memoryQueue
	 */
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
		Vector<Signature> userSigVector=new Vector<Signature>();
		for(int i=0;i<5;i++)
		{
			wifiService.startScan();
			userSigVector.add(new Signature(wifiService.getWifiList(),System.currentTimeMillis()));
		}
		Signature userSignature=averageSignature(userSigVector);
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
	
	public Signature averageSignature(Vector<Signature> userSigVector)
	{
		Signature sig=new Signature();
		Map<String,ArrayList<Integer>> sigMap=new HashMap<String,ArrayList<Integer>>();
		String tmp;
		for(int i=0;i<userSigVector.size();i++)
		{
			Map<String,Integer> tempMap=userSigVector.get(i).getHashMap();
			Iterator<String> it=tempMap.keySet().iterator();
			while(it.hasNext())
			{
				tmp=it.next();
				if(!sigMap.containsKey(tmp))
				{
					sigMap.put(tmp, new ArrayList<Integer>());
				}
				sigMap.get(tmp).add(tempMap.get(tmp));
			}
		}
		Iterator<String> itt=sigMap.keySet().iterator();
		while(itt.hasNext())
		{
			tmp=itt.next();
			ArrayList<Integer> valueList=sigMap.get(tmp);
			int sum=0;
			for(int i=0;i<valueList.size();i++)
			{
				sum+=valueList.get(i);
			}
			sum=sum/valueList.size();
			sig.getHashMap().put(tmp, sum);
		}
		return sig;
	}
}
