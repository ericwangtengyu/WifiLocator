/**
 * 
 */
package wifilocator.signature;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;

/**
 * Provides basic methods to calculate user's Location
 * @author Eric
 * @version 0
 */
public class UserLocation {

	private PointF pLocation;
	//Reference Database
	private List<Signature> refSigList;
	private Signature userSig;
	private DistanceMetric distanceMetric;
	private float []dist;
	
	
	/**
	 * Constructor of UserLocation
	 * @author Eric Wang
	 * @param refSigList reference signature database
	 * @param userSig user signature
	 */
	public UserLocation(List<Signature> refSigList,Signature userSig)
	{
		this.setRefSigList(refSigList);
		this.setUserSignature(userSig);
		pLocation=new PointF();
		//missing wifi hotspots is replaced with -100
		distanceMetric=new DistanceMetric(-100);
	}
	
	public UserLocation()
	{
		refSigList=new ArrayList<Signature>();
		userSig=new Signature();
		pLocation=new PointF();
		distanceMetric=new DistanceMetric(-120);
	}
	/**
	 * Calculate the location of user
	 * @author Eric Wang
	 * @return User Location
	 */
	public PointF getLocation()
	{
		int k=3;
		List<Signature> k_refSigList=K_nearest(k);
		WeightedCentroid(k_refSigList);
		return pLocation;
	}
	
	/**
	 * Choose K-Nearest reference signature from the Reference Database
	 * @author Eric Wang
	 * @param k 
	 * @return
	 */
	public List<Signature> K_nearest(int k)
	{
		List<Signature> k_refSigList=new ArrayList<Signature>(k);
		int n=refSigList.size();
		int []index=new int[n];
		dist=new float[n];
		float temp;
		int tempIndex;
		for(int i=0;i<n;i++)
		{
			index[i]=i;
			dist[i]=distanceMetric.getBiDistance(refSigList.get(i), userSig);
		}
		//Bubble K Sort, No need to sort All.
		for(int i=0;i<k;i++)
		{
			for(int j=n-1;j>i;j--)
			{
				if(dist[j]<dist[j-1])
				{
					temp=dist[j];
					tempIndex=index[j];
					dist[j]=dist[j-1];
					index[j]=index[j-1];
					dist[j-1]=temp;
					index[j-1]=tempIndex;
				}
			}
		}
		
		for(int i=0;i<k;i++)
		{
			k_refSigList.add(refSigList.get(index[i]));
		}
		
		return k_refSigList;
	}
	
	/**
	 * Calculate the Weighted centroid of a List of reference Signatures
	 * @author Eric Wang
	 * @param refSigList a list of reference Signatures
	 */
	public void WeightedCentroid(List<Signature> refSigList)
	{
		float sumX=0;
		float sumY=0;
		float denorm=0;
		//DO I need to transform it to int?
		int size=refSigList.size();
		for(int i=0;i<size;i++)
		{   
			if(dist[i]!=0)
			{
				sumX+=refSigList.get(i).getCoordinate().x*(1/dist[i]);
				sumY+=refSigList.get(i).getCoordinate().y*(1/dist[i]);
				denorm+=1/dist[i];
			}
			else
			{
				pLocation.set(refSigList.get(i).getCoordinate());
				return;
			}
		}
		pLocation.set(sumX/denorm, sumY/denorm);
		
	}

	/**
	 * @return the userSig
	 */
	public Signature getUserSignature() {
		return userSig;
	}

	/**
	 * @param userSig the userSig to set
	 */
	public void setUserSignature(Signature userSig) {
		this.userSig = userSig;
	}

	/**
	 * @return the refSigList
	 */
	public List<Signature> getRefSigList() {
		return refSigList;
	}

	/**
	 * @param refSigList the refSigList to set
	 */
	public void setRefSigList(List<Signature> refSigList) {
		this.refSigList = refSigList;
	}
}
