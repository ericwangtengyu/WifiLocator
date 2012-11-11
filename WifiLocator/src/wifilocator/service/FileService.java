package wifilocator.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
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
	 * Constructor function of FileService
	 * @author Eric Wang
	 * @param context
	 */
	public FileService(Context context)
	{
		this.context=context;
	}
	
	/**
	 * create a file specified by the fileName on internal storage of cell phone
	 * @author Eric Wang
	 * @param fileName
	 * @throws Exception
	 */
	public void createFile(String fileName) throws Exception
	{
		fileoutputstream=context.openFileOutput(fileName, Context.MODE_PRIVATE);
	}
	
	/**
	 * create a file specified by the fileName on SD storage
	 * @author Eric Wang
	 * @param fileName
	 * @throws Exception
	 */
	public void createFileOnSD(String fileName) throws Exception
	{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			//File sdfile=new File(Environment.getExternalStorageDirectory(),getDateFormat()+".csv");
			File sdfile=new File(Environment.getExternalStorageDirectory(),fileName+".csv");
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
	public void appendData(Signature sig)
	{
		String str;
		str=sig.toString();
		try {
			fileoutputstream.write(str.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author David Thole
	 * @return the date we create the csv file
	 */
	public String getDateFormat() {
    	Date dateNow = new Date ();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
		StringBuilder myDate = new StringBuilder( dateFormat.format( dateNow ) );
		return myDate.toString();
    }
	
	/**
	 * 
	 */
	public void readFile()
	{
	}
	
	/**
	 * Close the File
	 * @author Eric Wang
	 * @throws Exception
	 */
	public void closeFile() throws Exception
	{
		fileoutputstream.close();
	}
	

}

