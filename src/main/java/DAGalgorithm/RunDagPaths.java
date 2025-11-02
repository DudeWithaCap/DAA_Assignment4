package DAGalgorithm;
import KosarajuAlgorithm.KosarajuSCC;
import KahnAlgorithm.KahnTS;
import java.util.*;

public class RunDagPaths {
    public static void main(String[] args) {
        String resourceName = "large3.json"; //change dataset here
        int source = 0; // from dataset

        int[] nHolder = new int[1];
        Map<Integer, List<Integer>> unweightedGraph = KosarajuSCC.readGraphFromJSON(resourceName, nHolder);

        Map<Integer, List<DagShortestPath.Edge>> weightedGraph = new HashMap<>();
        for (int u : unweightedGraph.keySet()) {
            weightedGraph.put(u, new ArrayList<>());
            for (int v : unweightedGraph.get(u)) {
                weightedGraph.get(u).add(new DagShortestPath.Edge(v, 1));
            }
        }

        List<Integer> topoOrder = new ArrayList<>(unweightedGraph.keySet());
        Collections.sort(topoOrder);

        DagShortestPath sp = new DagShortestPath();
        Map<Integer, Double> dist = sp.shortestPath(weightedGraph, source, topoOrder);
        DagShortestPath.Metrics m1 = sp.getMetrics();

        DagShortestPath lp = new DagShortestPath(); //check the longest path
        Map<Integer, Double> longDist = lp.longestPath(weightedGraph, source, topoOrder);
        DagShortestPath.Metrics m2 = lp.getMetrics();

        System.out.println("Source: " + source);
        System.out.println("Distances: " + dist);
        System.out.printf("Relaxations: %d | Time: %.3f ns%n", m1.relaxations, m1.timeNano / 1000.0);

        System.out.println("Longest distances: " + longDist);
        System.out.printf("Relaxations: %d | Time: %.3f ns%n", m2.relaxations, m2.timeNano / 1000.0);
    }
}

