/**
 * 
 */
package wifilocator.thread;

import java.util.concurrent.BlockingQueue;

import wifilocator.service.FileService;
import wifilocator.service.WifiService;
import wifilocator.signature.*;

/**
 * @author Eric Wang
 *
 */
public class DataStorage implements Runnable {
	private FileService fileService;
	private WifiService wifiService;
	private BlockingQueue<Signature> eventQueue;
	public DataStorage(FileService fileService,WifiService wifiService,BlockingQueue<Signature> eventQueue)
	{
		this.setFileService(fileService);
		this.setWifiService(wifiService);
		this.setEventQueue(eventQueue);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true)
			{
				Signature s=eventQueue.take();
				fileService.appendData(s);
			}
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
	public BlockingQueue<Signature> getEventQueue() {
		return eventQueue;
	}

	/**
	 * @param eventQueue the eventQueue to set
	 */
	public void setEventQueue(BlockingQueue<Signature> eventQueue) {
		this.eventQueue = eventQueue;
	}

	/**
	 * @return the wifiService
	 */
	public WifiService getWifiService() {
		return wifiService;
	}

	/**
	 * @param wifiService the wifiService to set
	 */
	public void setWifiService(WifiService wifiService) {
		this.wifiService = wifiService;
	}

}
