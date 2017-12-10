package ritchirp.miamioh.edu;

import java.util.Arrays;

public class DumbSolver implements Solver {
	
	private int[][] adjTable;
	private double bestRouteLength;
	private int[] bestRoute;
	
	public DumbSolver(int[][] Problem){
		this.adjTable = Problem;
	}

	public void solve() {
		bestRouteLength = Double.MAX_VALUE;
		int n = adjTable.length;
		PermIterator perm = new PermIterator(n);
		int[] current;
		int iterations = 0;
		//Go through every permutation
		while(perm.hasNext()){
			double length = 0;
			current = perm.next();
			
			//Calculate path length for current permutation
			for(int i = 0; i < n-1; i++){
				length += adjTable[current[i]][current[i+1]]; 
			}
			
			// The distance to return to the beginning
			length += adjTable[current[0]][current[current.length - 1]];
			
			//update bestRouteLength and bestRoute
			if(length<this.bestRouteLength){
				this.bestRouteLength = length;
				this.bestRoute = Arrays.copyOf(current, current.length);

			}
			iterations++;
		}


	}

	@Override
	public double getBestRouteLength() {
		return this.bestRouteLength;
	}

	public int[] getBestRoute() {
		return this.bestRoute;
	}
	
	public String toLatexString(){
		
		String out = "\\begin{tabular} { "  + getCPart() + " } \n"
				+ "\\hline \n"
				+ getTableString()
				+ "\\end{tabular}";
		
		
		
		return out;
	}
	
	// Iterates through the adjacency matrix and makes the lines of the table
	private String getTableString() {
		String out = "";
		
		for(int i=-1; i<adjTable.length; i++){
			for(int j=-1; j<adjTable.length; j++){
				
				if(i==-1 && j==-1) {
					out = out + "Vertex";
					out = out + " & ";
				}
				else if(i==-1) {
					out = out + (j+1);
					if(j+1<adjTable.length)
						out = out + " & ";
					else
						out = out + " ";
				}
				else if(j == -1) {
					out = out + (i+1);
					out = out + " & ";
				}
				else {
					out = out + adjTable[i][j];
					if (j != adjTable[i].length - 1)
						out = out + " & ";
					else
						out = out + " ";
				}
			}
			out = out + " \\\\  \\hline \n";
		}
		
		return out;
	}
	
	// The part of the table with this form: |c|c|c|....|c|
	private String getCPart(){
		String out = "|";
		for(int i = 0; i<adjTable.length+1; i++){
			out = out + "c|";
		}
		
		return out;
	}
	
}
