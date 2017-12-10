package ritchirp.miamioh.edu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SimulatedAnnealer implements Solver {
	
	private double[][] adjTable;
	private double bestRouteLength;
	private int[] bestRoute;
	
	// These variables can be changed to optimize the algorithm
	private int ITERATIONS = 900000;
	private int MAX_BLOCK_SIZE = 100;
	private double INITIAL_TEMP = 0.9;
	private double EVAPORATION = 0.95;
	
	public SimulatedAnnealer(double[][] ds) {
		this.adjTable = ds;
	}
	
	public void solve() {
		
		//Create a new possible route and shuffle it
		int[] currentRoute = new int[adjTable.length];
		for(int i=0; i<currentRoute.length; i++){
			currentRoute[i] = i;
		}
		currentRoute = shuffle(currentRoute);
		bestRoute = currentRoute;
		
		
		
		for(int i=1; i<ITERATIONS; i++) {
			int[] candidate = makeChange(currentRoute); // candidate route
			double difference = compareRoutes(candidate, currentRoute); // candidateRouteLength - currentRouteLength
			if(difference<0) { // if the route is better
				currentRoute = candidate;
				if(compareRoutes(candidate, bestRoute) <0)
					bestRoute = candidate.clone();
			}
			else {
				double R = Math.random();
				if(1/(1+(difference/(INITIAL_TEMP * Math.pow(EVAPORATION, i)))) > R){
					currentRoute = candidate;
				}
			}
		}
		
		bestRouteLength = routeLength(bestRoute);
		
		
		
	}
	
	private double compareRoutes(int[] route1, int[] route2) {
		double length1 = routeLength(route1);
		double length2 = routeLength(route2);
		
		return (length1 - length2)/(length1 + length2);
	}
	
	private int[] makeChange(int[] route) {
		int[] out = route.clone();
		
		
		int i =  (int)(Math.random()*(route.length));
		int diff =  ((int)(Math.random()*(MAX_BLOCK_SIZE + 1)));
		int j = (diff + i)%route.length;

		diff /=2;
		
		while(diff > 0){
			if(j<0)
				j+=route.length;
			if(i<0)
				i+=route.length;
			
			
			int temp = out[i];
			out[i] = out[j];
			out[j] = temp;	
			i = (i+1)%route.length;
			j = (j-1)%route.length;
			
			diff--;
		}
		
		
		return out;
	}
	
	private double routeLength(int[] route) {
		double length = 0;
		for(int i = 0; i < adjTable.length-1; i++){
			length += adjTable[route[i]][route[i+1]]; 
		}
		

		length += adjTable[route[0]][route[route.length - 1]];
		
		return length;
	}
	
	@Override
	public double getBestRouteLength() {
		return this.bestRouteLength;
	}

	public int[] getBestRoute() {
		return bestRoute;
	}
	
	public int[] shuffle(int[] route){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int n: route){
			temp.add(new Integer(n));
		}
		Collections.shuffle(temp);
		for(int i=0; i<route.length; i++){
			route[i] = temp.get(i);
		}
		
		return route;
	}

	public void setINITIAL_TEMP(double iNITIAL_TEMP) {
		INITIAL_TEMP = iNITIAL_TEMP;
	}

	public void setEVAPORATION(double eVAPORATION) {
		EVAPORATION = eVAPORATION;
	}

	public void setITERATIONS(int iTERATIONS) {
		ITERATIONS = iTERATIONS;
	}

	public void setMAX_BLOCK_SIZE(int mAX_BLOCK_SIZE) {
		MAX_BLOCK_SIZE = mAX_BLOCK_SIZE;
	}
	
	
	
	
}
