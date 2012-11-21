/**
 * 
 */
package wifilocator.signature;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * DistanceMetric we will use.
 * @author Eric
 * @version 0
 */
public class DistanceMetric {
	
	private int lowerBound;
	
	/**
	 * Constructor fucniton
	 * @author Eric Wang
	 * @param lowerBound here we treat a missing tuple as a tuple with signal strength specified by the lower bound.
	 */
	public DistanceMetric(int lowerBound)
	{
		this.setLowerBound(lowerBound);
	}
	
	/**
	 * Calculate the Unidirectional Distance between 2 signatures.
	 * @author Eric Wang
	 * @param ref reference signature
	 * @param user user signature
	 * @return unidirectional distance
	 */
	public int getUniDistance(Signature ref, Signature user)
	{
		int distance=0;
		ref.setHashMap();
		user.setHashMap();
		Map<String,Integer> m1=ref.getHashMap();
		Map<String,Integer> m2=user.getHashMap();
		Set<String> s1=new HashSet<String>(m1.keySet());
		Set<String> s2=new HashSet<String>(m2.keySet());
		s1.retainAll(s2);
		Iterator<String> it=s1.iterator();
		String tmp;
		//Intersection of ref signature and user signature
		while(it.hasNext())
		{
			tmp=it.next();
			distance+=Math.abs(m1.get(tmp)-m2.get(tmp));
		}
		//Set of User signature- Ref signature
		s2.removeAll(s1);
		it=s2.iterator();
		while(it.hasNext())
		{
			tmp=it.next();
			distance+=Math.abs(m2.get(tmp)-lowerBound);
		}
		return distance;
	}
	
	/**
	 * Calculate the Bidirectional Distance between 2 signatures.
	 * @author Eric Wang
	 * @param ref reference signature
	 * @param user user signature
	 * @return bidirectional distance
	 */
	public int getBiDistance(Signature ref, Signature user)
	{
		int distance=0;
		Iterator<String> it;
		String tmp;
		Map<String,Integer> m1=ref.getHashMap();
		Map<String,Integer> m2=user.getHashMap();
		Set<String> s1=m1.keySet();
		Set<String> s2=m2.keySet();
		Set<String> s3=m1.keySet();
		s1.retainAll(s2);

		//Intersection of ref signature and user signature
		it=s1.iterator();
		while(it.hasNext())
		{
			tmp=it.next();
			distance+=Math.abs(m1.get(tmp)-m2.get(tmp));
		}
		//Set of User signature- Ref signature
		s2.removeAll(s1);
		it=s2.iterator();
		while(it.hasNext())
		{
			tmp=it.next();
			distance+=Math.abs(m2.get(tmp)-lowerBound);
		}
		//Set of Ref signature- user signature
		s3.removeAll(s1);
		it=s3.iterator();
		while(it.hasNext())
		{
			tmp=it.next();
			distance+=Math.abs(m1.get(tmp)-lowerBound);
		}
		return distance;
	}
	
	/**
	 * @return the lowerBound
	 */
	public int getLowerBound() {
		return lowerBound;
	}

	/**
	 * @param lowerBound the lowerBound to set
	 */
	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}
}
