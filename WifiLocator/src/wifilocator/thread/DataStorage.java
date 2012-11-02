/**
 * 
 */
package wifilocator.thread;

import java.util.concurrent.BlockingQueue;

import wifilocator.service.FileService;
import wifilocator.signature.*;

/**
 * @author Eric Wang
 *
 */
public class DataStorage implements Runnable {
	private FileService fileService;
	private BlockingQueue<Signature> eventQueue;
	public DataStorage(FileService fileService,BlockingQueue<Signature> eventQueue)
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

}
