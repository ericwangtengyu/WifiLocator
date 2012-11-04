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
		sigList=new ArrayList<SignatureForm>();
		setSigList_s(wifiList);
		this.timeStamp=timeStamp;
	}
	
	public Signature()
	{
		sigList=new ArrayList<SignatureForm>();
		this.timeStamp=0;
	}
	
	/**
	 * Clone a Signature
	 * @param s ,the signature need to be cloned
	 * @author Eric Wang
	 */
	public void clone(Signature s)
	{
		this.setSigList(s.sigList);
		this.setTimeStamp(s.timeStamp);
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
	 * Set List<'SignatureForm'> by List<'ScanResult'> 
	 * @param  a list of scan results of the latest wifi access point scan
	 * @author Eric Wang
	 */
	public void setSigList_s(List<ScanResult> wifiList)
	{
		for(int i=0;i<wifiList.size();i++)
		{
			SignatureForm tuple=new SignatureForm(wifiList.get(i).SSID,wifiList.get(i).BSSID,wifiList.get(i).level,wifiList.get(i).frequency);
			sigList.add(tuple);
		}
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
	
	/**
	 * Convert a Signature to String.
	 * @author Eric Wang
	 */
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
