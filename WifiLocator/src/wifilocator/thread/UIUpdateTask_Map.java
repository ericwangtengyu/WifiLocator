/**
 * 
 */
package wifilocator.thread;

import java.util.concurrent.BlockingQueue;

import android.graphics.PointF;
import android.os.Handler;


/**
 * @author Eric
 *
 */
public class UIUpdateTask_Map implements Runnable {


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	private BlockingQueue<PointF> memoryQueue;
	private BlockingQueue<PointF> eventQueue;
	private Handler handler;
	
	public UIUpdateTask_Map(BlockingQueue<PointF> eventQueue,BlockingQueue<PointF> memoryQueue,Handler handler)
	{
		this.eventQueue=eventQueue;
		this.memoryQueue=memoryQueue;
		this.handler=handler;
	}
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true)
			{
				PointF p=eventQueue.take();
				handler.obtainMessage(1,p).sendToTarget();
				memoryQueue.put(p);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
