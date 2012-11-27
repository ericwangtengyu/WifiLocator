/**
 * 
 */
package wifilocator.thread;

import java.util.concurrent.BlockingQueue;

import wifilocator.service.FileService;
import wifilocator.signature.*;

/**
 * A Thread to store the Wifi data into a CSV file.(used in Data Collection)
 * @author Eric Wang
 * @version 0
 */
public class DataStorage implements Runnable {
	private FileService fileService;
	private BlockingQueue<Signature> eventQueue;
	private BlockingQueue<Signature> memoryQueue;
	public DataStorage(FileService fileService,BlockingQueue<Signature> eventQueue,BlockingQueue<Signature> memoryQueue)
	{
		this.setFileService(fileService);
		this.setEventQueue(eventQueue);
		this.setMemoryQueue(memoryQueue);
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
				memoryQueue.put(s);
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
	 * @return the memoryQueue
	 */
	public BlockingQueue<Signature> getMemoryQueue() {
		return memoryQueue;
	}

	/**
	 * @param memoryQueue the memoryQueue to set
	 */
	public void setMemoryQueue(BlockingQueue<Signature> memoryQueue) {
		this.memoryQueue = memoryQueue;
	}

}
