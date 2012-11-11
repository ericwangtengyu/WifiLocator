package wifilocator.signature;



/**
 * This is a class defined to store the wifi tupes 
 * of the form{TimeStamp,SSID,BSSID,LEVEL,FREQUENCY}
 * @author Eric
 * @version 0
 */
public class SignatureForm {
	private String ssId;
	private String bssId;
	private int level;
	private int frequency;
	
	/**
	 * Constructor function of Signature
	 * @author Eric Wang
	 * @param ssId
	 * @param bssId
	 * @param level
	 * @param frequency
	 */
	public SignatureForm(String ssId,String bssId,int level, int frequency)
	{
		this.ssId=ssId;
		this.bssId=bssId;
		this.level=level;
		this.frequency=frequency;
	}
	
	/**
	 * Constructor function without parameters
	 * @author Eric Wang
	 */
	public SignatureForm()
	{
		ssId="N/A";
		bssId="N/A";
		level=0;
		frequency=0;
	}
	
	/**
	 * Convert Signature to string
	 * @author Eric Wang
	 * @return signature of String form
	 */
	public String toString()
	{
		StringBuilder str=new StringBuilder();
		//str.append(ssId).append(",").append(bssId).append(",").append(level).append(",").append(frequency).append("\r\n");
		str.append(bssId).append(",").append(level).append(",");
		return str.toString();
	}
	
	/**
	 * @author Eric Wang
	 * @return ssId
	 */
	public String getSSID()
	{
		return ssId;
	}
	
	/**
	 * @author Eric Wang
	 * @return bssId
	 */
	public String getBSSID()
	{
		return bssId;
	}
	
	/**
	 * @author Eric Wang
	 * @return level
	 */
	public int getLevel()
	{
		return level;
	}
	
	/**
	 * @author Eric Wang
	 * @return frequency
	 */
	public int getFrequency()
	{
		return frequency;
	}
}
