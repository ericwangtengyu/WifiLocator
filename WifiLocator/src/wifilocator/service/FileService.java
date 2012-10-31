package wifilocator.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import android.net.wifi.ScanResult;
import wifilocator.signature.*;

/**
 * File service provider; helps to do the file operations.
 * @author Eric Wang
 * @version beta
 */
public class FileService{
	private Context context;
	private FileOutputStream fileoutputstream;
	
	/**
	 * 
	 * @param context
	 */
	public FileService(Context context)
	{
		this.context=context;
	}
	
	/**
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public void createFile(String fileName) throws Exception
	{
		fileoutputstream=context.openFileOutput(fileName, Context.MODE_PRIVATE);
	}
	
	/**
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public void createFileOnSD(String fileName) throws Exception
	{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			File sdfile=new File(Environment.getExternalStorageDirectory(), fileName);
			fileoutputstream=new FileOutputStream(sdfile);
		}
		else
		{
			Toast.makeText(context, "No SD card found on your cellphone", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 
	 * @param fileName
	 * @return True if the file was successfully deleted; else false.
	 */
	public boolean deleteFile(String fileName)
	{
		return context.deleteFile(fileName);
	}
	
	/**
	 * Every time getting a wifi signal List, store it into file.
	 * append the list to our csv file.
	 * @author Eric Wang
	 * @param wifiList
	 * @param timeStamp
	 */
	public void appendData(List<ScanResult> wifiList, long timeStamp)
	{
		StringBuilder str=new StringBuilder();
		for(int i=0;i<wifiList.size();i++)
		{
			Signature sig=new Signature(wifiList.get(i).SSID,wifiList.get(i).BSSID,wifiList.get(i).level,wifiList.get(i).frequency,timeStamp);
			str.append(sig.toString());
		}
		try {
			fileoutputstream.write(str.toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void readFile()
	{
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void closeFile() throws Exception
	{
		fileoutputstream.close();
	}
	

}

