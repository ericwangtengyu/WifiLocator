/**
 * 
 */
package wifilocator.signature;

import java.util.ArrayList;
import java.util.List;
import android.net.wifi.ScanResult;

/**
 * @author Eric
 *
 */
public class Signature {

	private List<SignatureForm> sigList;
	private long timeStamp;
	
	public Signature(List<ScanResult> wifiList, long timeStamp)
	{
		//sigList=new List<SignatureForm>();
		sigList=new ArrayList<SignatureForm>();
		for(int i=0;i<wifiList.size();i++)
		{
			SignatureForm tuple=new SignatureForm(wifiList.get(i).SSID,wifiList.get(i).BSSID,wifiList.get(i).level,wifiList.get(i).frequency);
			sigList.add(tuple);
		}
		this.timeStamp=timeStamp;
	}
	
	/**
	 * @return the sigList
	 */
	public List<SignatureForm> getSigList() {
		return sigList;
	}
	/**
	 * @param sigList the sigList to set
	 */
	public void setSigList(List<SignatureForm> sigList) {
		this.sigList = sigList;
	}
	/**
	 * @return the timeStamp
	 */
	public long getTimeStamp() {
		return timeStamp;
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String toString()
	{
		StringBuilder str=new StringBuilder();
		str.append(timeStamp).append("\r\n");
		for(int i=0;i<sigList.size();i++)
		{
			str.append(sigList.get(i).toString());
		}
		str.append("\r\n");
		return str.toString();
	}
	
}
