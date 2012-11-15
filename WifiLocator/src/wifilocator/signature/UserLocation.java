/**
 * 
 */
package wifilocator.signature;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;

/**
 * @author Eric
 *
 */
public class UserLocation {

	private PointF pLocation;
	//Reference Database
	private List<Signature> refSigList;
	private Signature userSig;
	private DistanceMetric distanceMetric;
	
	public UserLocation(List<Signature> refSigList,Signature userSig)
	{
		this.setRefSigList(refSigList);
		this.setUserSigSignature(userSig);
		pLocation=new PointF();
		//missing wifi hotspots is replaced with -100
		distanceMetric=new DistanceMetric(-100);
	}
	
	public PointF getLocation()
	{
		int k=4;
		List<Signature> k_refSigList=K_nearest(k);
		Centroid(k_refSigList);
		return pLocation;
	}
	
	public List<Signature> K_nearest(int k)
	{
		List<Signature> k_refSigList=new ArrayList<Signature>(k);
		int n=refSigList.size();
		int []index=new int[n];
		int []dist=new int[n];
		int temp,tempIndex;
		for(int i=0;i<n;i++)
		{
			index[i]=i;
			dist[i]=distanceMetric.getUniDistance(refSigList.get(i), userSig);
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
	//DO I need to transform it to int?
	public void Centroid(List<Signature> refSigList)
	{
		float sumX=0;
		float sumY=0;
		int size=refSigList.size();
		for(int i=0;i<size;i++)
		{
			sumX+=refSigList.get(i).getCoordinate().x;
			sumY+=refSigList.get(i).getCoordinate().y;
		}
		pLocation.set(sumX/size, sumY/size);
		
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
	public void setUserSigSignature(Signature userSig) {
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
