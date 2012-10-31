/**
 * 
 */
package wifilocator.thread;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import android.net.wifi.ScanResult;
import wifilocator.service.FileService;

/**
 * @author Eric
 *
 */
public class DataStorage implements Runnable {
	private FileService fileService;
	private BlockingQueue<List<ScanResult>> eventQueue;
	public DataStorage(FileService fileService, BlockingQueue<List<ScanResult>> eventQueue)
	{
		this.setFileService(fileService);
		this.setEventQueue(eventQueue);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// TODO Auto-generated method stub
		try {
			List<ScanResult> wifiList=eventQueue.take();// whether it is final, do I need to copy out its value
			//fileService.appendData(wifiList, timeStamp);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the fileService
	 */
	public FileService getFileService() {
		return fileService;
	}

	/**
	 * @param fileService the fileService to set
	 */
	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	/**
	 * @return the eventQueue
	 */
	public BlockingQueue<List<ScanResult>> getEventQueue() {
		return eventQueue;
	}

	/**
	 * @param eventQueue the eventQueue to set
	 */
	public void setEventQueue(BlockingQueue<List<ScanResult>> eventQueue) {
		this.eventQueue = eventQueue;
	}

}
