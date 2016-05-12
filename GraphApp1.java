import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class GraphApp1 {
	private final static String fileName = "data.txt";
	private static boolean foundEdge = false;
	private static Graph initialGraph;
	private static String lastVisitedEdge = null;
	private static Map<String, Set<String>> usedEdges = new HashMap<>();
	private static int totaldistance = 0;
	private static Set<String> visitedCitys = new HashSet<>();
	private static boolean end;

	
	public static void main(String[] args) {
		try {
			initialGraph = new Graph(fileName);
			System.out.println(initialGraph);
			long startTime = System.currentTimeMillis();

			HashMap<String, HashMap<String, Integer>> shortestPathsEdges = new HashMap<>();
			shortestPathsEdges.put("Colombo", ShortestPath(initialGraph, "Colombo"));
			shortestPathsEdges.put("NuwaraEliya", ShortestPath(initialGraph, "NuwaraEliya"));
			shortestPathsEdges.put("Galle", ShortestPath(initialGraph, "Galle"));
			shortestPathsEdges.put("Jaffna", ShortestPath(initialGraph, "Jaffna"));
			shortestPathsEdges.put("Trincomalee", ShortestPath(initialGraph, "Trincomalee"));
			shortestPathsEdges.put("Anuradhapura", ShortestPath(initialGraph, "Anuradhapura"));
			shortestPathsEdges.put("Arugambay", ShortestPath(initialGraph, "Arugambay"));
			shortestPathsEdges.put("Yala", ShortestPath(initialGraph, "Yala"));
			shortestPathsEdges.put("Pinnawala", ShortestPath(initialGraph, "Pinnawala"));
			shortestPathsEdges.put("Kandy", ShortestPath(initialGraph, "Kandy"));

			System.out.println(shortestPathsEdges.get("Colombo"));
			System.out.println(shortestPathsEdges.get("Jaffna"));
			// Add your graph search/traversal/application here!

			HashMap<String, String> path = MST(shortestPathsEdges, "Colombo");
			System.out.println(path);

			printPath(shortestPathsEdges, path, "Colombo");
			System.out.println("total cost: " + totaldistance);

			long endTime = System.currentTimeMillis();
			//System.out.println(endTime-startTime);
			
			// Calculate & Print the time taken by the algorithm/application
			long duration = (endTime - startTime); 
			System.out.print("The execution of this program took: ");
			System.out.println(String.format("%d min, %d sec", 
				    TimeUnit.MILLISECONDS.toMinutes(duration),
				    TimeUnit.MILLISECONDS.toSeconds(duration) - 
				    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
				));
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static HashMap<String, Integer> ShortestPath(Graph g, String start){
		HashMap<String, Integer> shortestPaths= new HashMap<>();
		Set<String> visited = new HashSet<> ();

		for (String s: g.adjacencyList.keySet()
			 ) {
			shortestPaths.put(s, 10000);
		}
		shortestPaths.replace(start, 0);


		while (g.size()>visited.size()){
			String u = extractMin(shortestPaths, visited);
			visited.add(u);
			for (Graph.neighbor n: g.getEdges(u)
				 ) {
				if(shortestPaths.get(n.vertex)>(shortestPaths.get(u)+n.weight))
					shortestPaths.replace(n.vertex, shortestPaths.get(u)+n.weight);
			}
		}
		return shortestPaths;

	}

	private static String extractMin(HashMap<String, Integer> Q, Set<String> visited){
		int min = 10000;
		String minEdge = null;

		for (String s:Q.keySet()
			 ) {
			if(min>Q.get(s) && !visited.contains(s)){
				min=Q.get(s);
				minEdge = s;
			}
		}
		return minEdge;
	}

	private static HashMap<String, String> MST(HashMap<String, HashMap<String, Integer>> g, String start){
		HashMap<String, Integer> weights= new HashMap<>();
		Set<String> visited = new HashSet<> ();
		HashMap<String, String> Parents = new HashMap<>();

		for (String s: g.keySet()
				) {
			weights.put(s, 10000);
			Parents.put(s, "");
		}

		weights.replace(start, 0);

		while(g.size()>visited.size()){
			String u = extractMin(weights, visited);
			visited.add(u);
			for (String s: g.get(u).keySet()
				 ) {
				if(weights.containsKey(s) && (g.get(u).get(s)<weights.get(s)) && !visited.contains(s)){
					Parents.replace(s, u);
					weights.replace(s, g.get(u).get(s));
				}
			}
		}

		return Parents;
	}

	private static int printPath(HashMap<String, HashMap<String, Integer>> shortestPathsEdges, HashMap<String, String> MST, String start){
		int numberOfLeaves = 0;

		for (String s: MST.keySet()
			 ) {
			if(!end && MST.get(s).equals(start)) {
				numberOfLeaves++;
				if(!foundEdge) {
					printShortestPath(start, s);
					printPath(shortestPathsEdges, MST, s);
				}else{
					if(!visitedCitys.contains(s))
						printPath(shortestPathsEdges, MST, s);
				}
			}
		}
		if(numberOfLeaves== 0) {
			foundEdge = true;
			if(lastVisitedEdge!=null){
				printShortestPath(lastVisitedEdge, start);
				String lastnode = start;
				while(!visitedCitys.contains(MST.get(lastnode)) && MST.get(lastnode)!=""){
					printShortestPath(lastnode, MST.get(lastnode));
					lastnode = MST.get(lastnode);
				}
				lastVisitedEdge = start;
				start = lastnode;
			}else {
				lastVisitedEdge = start;
			}
		}




		return 0;
	}

	private static void printShortestPath(String start, String end){
		HashMap<String, Integer> shortestPaths= new HashMap<>();
		Set<String> visited = new HashSet<> ();
		HashMap<String, String> previous = new HashMap<>();

		for (String s: initialGraph.adjacencyList.keySet()
				) {
			shortestPaths.put(s, 10000);
			previous.put(s, null);
		}
		shortestPaths.replace(start, 0);


		while (initialGraph.adjacencyList.size()>visited.size()){
			String u = extractMin(shortestPaths, visited);
			visited.add(u);
			for (Graph.neighbor n: initialGraph.getEdges(u)
					) {
				int weight = n.weight;
				if(usedEdges.containsKey(n.vertex) && usedEdges.get(n.vertex).contains(u)){
					weight = n.weight*2;
				}
				if(shortestPaths.get(n.vertex)>(shortestPaths.get(u)+weight)) {
					shortestPaths.replace(n.vertex, shortestPaths.get(u) + weight);
					previous.replace(n.vertex, u);
				}
			}

		}

		Stack<String> temp = new Stack<>();
		String lastVisited = end;
		while(!lastVisited.equals(start)){
			temp.add(lastVisited);
			lastVisited = previous.get(lastVisited);
		}
		temp.add(lastVisited);

		while(temp.size()>1){
			String startEdge = temp.pop();
			String endEdge = temp.peek();
			int dist = 0;
			for (Graph.neighbor n:initialGraph.getEdges(startEdge)
				 ) {
				if(n.vertex.equals(endEdge)){
					dist = n.weight;
				}
			}
			if(usedEdges.containsKey(startEdge) && usedEdges.get(startEdge).contains(endEdge)){
				dist = dist*2;
			}else {
				Set<String> s1;
				Set<String> s2;
				if(usedEdges.containsKey(startEdge)) s1 = usedEdges.get(startEdge);
				else s1 = new HashSet<>();
				if(usedEdges.containsKey(endEdge)) s2 = usedEdges.get(endEdge);
				else s2 = new HashSet<>();
				s1.add(endEdge);
				s2.add(startEdge);
				usedEdges.put(startEdge, s1);
				usedEdges.put(endEdge, s2);
			}
			System.out.println(startEdge+" -> "+endEdge+"  " + dist);

			totaldistance+= dist;
		}
		visitedCitys.add(end);
	}


}
