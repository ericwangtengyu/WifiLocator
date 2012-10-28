package wifilocator.service;

import java.util.List;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

/**
 * Wifi service provider; manage Wifi operations.
 * @author Eric Wang
 * @version beta
 */
public class WifiService {
	private WifiManager m_wifiManager;
	private WifiInfo m_wifiInfo;
	private WifiLock m_wifiLock;
	private List<WifiConfiguration> m_wifiConfigList;
	private List<ScanResult> m_wifiList;
	
	/**
	 * constructor function of WifiHelper
	 * @author Eric Wang
	 * @param interface to the application environment
	 */
	public WifiService(Context context)
	{
		m_wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		//m_wifiInfo=m_wifiManager.getConnectionInfo();	
	}
	
	/**
	 * open the WIFI
	 * @author Eric Wang
	 */
	public void openWifi()
	{
		if(!m_wifiManager.isWifiEnabled())
		{
			m_wifiManager.setWifiEnabled(true);
		}
	}
	
	/**
	 * close the WIFI
	 * @author Eric Wang
	 */
	public void closeWifi()
	{
		if(m_wifiManager.isWifiEnabled())
		{
			m_wifiManager.setWifiEnabled(false);
		}
	}
	
	/**
	 * get the WIFI state
	 * @author Eric Wang
	 * @return the state of Wifi
	 */
	public int getState()
	{
		return m_wifiManager.getWifiState();
	}
	
	/**
	 * create the WIFI Lock
	 * @author Eric Wang
	 */
	public void createWifiLock()
	{
		m_wifiLock=m_wifiManager.createWifiLock("Wifi Lock");
	}
	
	/**
	 * acquire the WIFI Lock
	 * @author Eric Wang
	 */
	public void acquireWifiLock()
	{
		m_wifiLock.acquire();
	}
	
	/**
	 * release the WIFI Lock
	 * @author Eric Wang
	 */
	public void releaseWifiLock()
	{
		if(m_wifiLock.isHeld())
			m_wifiLock.release();
	}
	
	/**
	 * get the WIFI configurations
	 * @author Eric Wang
	 * @return Configuration List of Wifi Networks
	 */
	public List<WifiConfiguration> getWifiConfiguration()
	{
		return m_wifiConfigList;
	}
	
	/**
	 * start WIFI scan
	 * @author Eric Wang
	 * @return True if the operation succeeded, false otherwise
	 */
	public boolean startScan()
	{
		if(m_wifiManager.startScan())
		{
			m_wifiList=m_wifiManager.getScanResults();
			m_wifiConfigList=m_wifiManager.getConfiguredNetworks();
			return true;
		}
		else return false;
	}
	
	/**
	 * get the results of the latest access point scan
	 * @author Eric Wang
	 * @return List of access points.
	 */
	public List<ScanResult> getWifiList()
	{
		return m_wifiList;
	}
	
	/**
	 * get WIFI List in String
	 * @author Eric Wang
	 * @return String Form: the list of access points.
	 */
	public StringBuilder getWifiListInString()
	{
		StringBuilder result=new StringBuilder();
		if(m_wifiList!=null)
		{
			for(int i=0;i<m_wifiList.size();i++)
			{
				if(m_wifiList.get(i).SSID.equals("eduroam"))
				result.append(m_wifiList.get(i).toString()).append("\n");
			}
			return result;
		}
		return result;
	}
	
	/**
	 * get the state of any WIFI connection that is active or in the process of being set up
	 * @author Eric Wang
	 * @return dynamic information about the current Wi-Fi connection
	 */
	public WifiInfo getWifiInfo()
	{
		m_wifiInfo=m_wifiManager.getConnectionInfo();
		return m_wifiInfo;
	}
	
	/**
	 * get the WIFI INFO in String
	 * @author Eric Wang
	 * @return String Form: active WIFI connection information
	 */
	public StringBuilder getWifiInfoInString()
	{
		m_wifiInfo=m_wifiManager.getConnectionInfo();
		StringBuilder wifiInfo=new StringBuilder();
		if(m_wifiInfo!=null)
			wifiInfo.append(m_wifiInfo.toString());
		else wifiInfo.append("NULL");
		return wifiInfo;
	}
	
	/**
	 * add and connect to a new network described by the configuration
	 * @author Eric Wang
	 * @param configuration of a Wifi access point
	 */
	public void connectNewNetWork(WifiConfiguration config){  
        int netId=m_wifiManager.addNetwork(config);  
        m_wifiManager.enableNetwork(netId, true);  
    }
	
	/**
	 * connect to an already configured network
	 * @author Eric Wang
	 * @param index of this configured network in the configuration list
	 */
	public void connectConfiguration(int index){  
        if(index>m_wifiConfigList.size()){  
            return ;  
        }  
        m_wifiManager.enableNetwork(m_wifiConfigList.get(index).networkId, true);  
    }  
	
	/**
	 * disconnect a network with a specified ID
	 * @author Eric Wang
	 * @param Network ID
	 */
	public boolean disconnectNetWork(int netId)
	{
		 boolean r1=m_wifiManager.disableNetwork(netId);  
	     boolean r2=m_wifiManager.disconnect(); 
	     return r1&r2;
	}
	

}

