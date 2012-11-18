/**
 * 
 */
package wifilocator.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

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
public class LocationUpdateTask extends TimerTask{

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	private UserLocation userLocation;
	private WifiService wifiService;
	private FileService fileService;
	private List<Signature> refSigList;
	
	public LocationUpdateTask(WifiService wifiService,FileService fileService)
	{
		this.wifiService=wifiService;
		this.fileService=fileService;
		this.userLocation=new UserLocation();
		List<Signature> refSigList=new ArrayList<Signature>();
		String fileName="";
		try {
			refSigList=fileService.readFile(fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userLocation.setRefSigList(refSigList);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		wifiService.startScan();
		List<ScanResult> wifiList=wifiService.getWifiList();
		Signature userSignature=new Signature(wifiList,System.currentTimeMillis());
		userLocation.setUserSignature(userSignature);
		PointF userPoint=userLocation.getLocation();
		
		
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


}
