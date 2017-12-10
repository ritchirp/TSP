package ritchirp.miamioh.edu;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class TSPDemo {

	public static void main(String[] args) throws Exception {

		
		TSPParser parser = new TSPParser();
		try {
//			double[][] adj = parser.parseFile("berlin52.tsp");
//			
//			SimulatedAnnealer solver = new SimulatedAnnealer(adj);
////			DumbSolver solver = new DumbSolver(parser.parseFile(adj));
//			long start = System.currentTimeMillis();
//			solver.setITERATIONS(adj.length * 12000);
//			solver.setMAX_BLOCK_SIZE(adj.length);
//			solver.solve();
//			long end = System.currentTimeMillis();
//			
//			
//			System.out.println("" + (end - start)/1000.0 + " seconds");
//			System.out.println((solver.getBestRouteLength() - 6110)/6110);
////			System.out.println(Arrays.toString(solver.getBestRoute()));
////			System.out.println(solver.toLatexString());
			
			
			HashMap<String, Double> optimal = new HashMap<String,Double>();
			LinkedList<String> graphs = new LinkedList<>();
			
//			optimal.put("ulysses16", new Double(6859));
//			graphs.add("ulysses16");
//			optimal.put("berlin52", new Double(7542));
//			graphs.add("berlin52");
//			optimal.put("ch130", new Double(6110));
//			graphs.add("ch130");
			optimal.put("lin318", new Double(42029));
			graphs.add("lin318");
//			optimal.put("ali535", new Double(202310));
//			graphs.add("ali535");
//			
			for(String gr : graphs) {
				test(gr,optimal.get(gr));
			}
			
		} catch (TSPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}
	
	static void test(String graphName, double realValue) throws Exception {
		FileWriter fw = new FileWriter(graphName + ".txt");
		BufferedWriter out = new BufferedWriter(fw);
		
		TSPParser parser = new TSPParser();

		double[][] adj = parser.parseFile(graphName + ".tsp");
		
		String latex = "|";
		for(int i = 0; i<adj.length+1; i++){
			latex = latex + "c|";
		}
		
		
		out.append(graphName);
		out.newLine();
		out.append("\\begin{tabular} {" + latex + "}");
		out.newLine();
		out.append("\\hline iterations & max block size & error (\\%) & time (seconds) \\\\");
		out.newLine();
		out.append("\\hline \\hline");
		out.newLine();
		

		
		int n = adj.length;
		int[] iterations = {(int)Math.sqrt(n) * 10000, (int)Math.sqrt(n) * 4 * 10000};//, (int)Math.sqrt(n) * 16 * 10000};
		int[] blockSizes = {n};//, n/2, n/3};
		
		for(int blockSize: blockSizes) {
			for(int i : iterations) {
				double bestRouteLength = Double.MAX_VALUE;
				double time = System.currentTimeMillis();
				
				
				// setup solver
				int x = 10;
				for(int j=0; j<x; j++) {
					SimulatedAnnealer solver = new SimulatedAnnealer(adj);
					solver.setITERATIONS(i);
					solver.setMAX_BLOCK_SIZE(blockSize);

					// time solve
					solver.solve();
					if(solver.getBestRouteLength() < bestRouteLength)
						bestRouteLength = solver.getBestRouteLength();
				}
				time = System.currentTimeMillis() - time;
				time /= x;
				
				int ratio = i/((int)Math.sqrt(n)*10000);
				
				double error = Math.abs(realValue - bestRouteLength)/realValue;
				//write data
				String toAppend = String.format("$ %d \\cdot %d \\cdot  10^5$" + " & %d & %.2f & %.2f \\\\ \\hline", ratio, n, blockSize, error*100, time/1000);
				out.append(toAppend);
				out.newLine();

			}
		}
		
		out.append("\\end{tabular}");

		
		out.close();
	}

}
