package wifilocator.service;

import java.io.File;
import java.io.FileOutputStream;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

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
			File sdFile=new File(Environment.getExternalStorageDirectory(), fileName);
			fileoutputstream=new FileOutputStream(sdFile);
		}
		else
		{
			Toast.makeText(context, "No SD card found on your cellphone", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean deleteFile(String fileName)
	{
		return context.deleteFile(fileName);
	}
	
	/**
	 * 
	 */
	public void appendData()
	{
		
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

