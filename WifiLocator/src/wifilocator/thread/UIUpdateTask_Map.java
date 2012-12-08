/**
 * 
 */
package wifilocator.thread;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import android.graphics.PointF;
import android.os.Handler;


/**
 * A new Thread to do UI updates used in Map View
 * @author Eric
 * @version 0
 */
public class UIUpdateTask_Map implements Runnable {


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	private BlockingQueue<PointF> memoryQueue;
	private BlockingQueue<PointF> eventQueue;
	private Handler handler;
	private Queue<PointF> pointQueue;
	
	/**
	 * Constructor function of UIUpdateTask_Map
	 * @param eventQueue
	 * @param memoryQueue
	 * @param handler
	 * @author Eric Wang
	 */
	public UIUpdateTask_Map(BlockingQueue<PointF> eventQueue,BlockingQueue<PointF> memoryQueue,Handler handler)
	{
		this.eventQueue=eventQueue;
		this.memoryQueue=memoryQueue;
		this.handler=handler;
		pointQueue=new LinkedList<PointF>();
	}
	
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true)
			{
				PointF p=eventQueue.take();
				if(pointQueue.size()<5)
				{
					pointQueue.offer(p);
				}
				else
				{
					pointQueue.poll();
					pointQueue.offer(p);
				}
				float sumX=0;
				float sumY=0;
				Iterator<PointF> it=pointQueue.iterator();
				while(it.hasNext())
				{
					PointF tP=it.next();
					sumX+=tP.x;
					sumY+=tP.y;
				}
				p.set(sumX/pointQueue.size(), sumY/pointQueue.size());
				handler.obtainMessage(1,p).sendToTarget();
				memoryQueue.put(p);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
