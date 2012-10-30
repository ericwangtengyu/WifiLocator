package wifilocator.signature;

public class Signature {
	private String ssId;
	private String bssId;
	private int level;
	private int frequency;
	
	public Signature(String ssId,String bssId,int level, int frequency)
	{
		this.ssId=ssId;
		this.bssId=bssId;
		this.level=level;
		this.frequency=frequency;
	}
	
	public Signature()
	{
		ssId="N/A";
		bssId="N/A";
		level=0;
		frequency=0;
	}
	
	public String toString()
	{
		StringBuilder str=new StringBuilder();
		str.append(ssId).append(bssId).append(level).append(frequency);
		return str.toString();
	}
	
	public String getSSID()
	{
		return ssId;
	}
	
	public String getBSSID()
	{
		return bssId;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public int getFrequency()
	{
		return frequency;
	}
}
