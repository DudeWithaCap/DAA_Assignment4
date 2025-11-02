package DAGtest;

import DAGalgorithm.DagShortestPath;
import com.google.gson.*;
import org.junit.jupiter.api.Test;
import KahnAlgorithm.KahnTS;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
public class DagShortestPathTest {

    private static class JsonEdge {
        int u, v;
        double w;
    }

    private static Map<Integer, List<DagShortestPath.Edge>> readWeightedGraphFromJSON(String resourceName) {
        Map<Integer, List<DagShortestPath.Edge>> graph = new HashMap<>();

        try (InputStream input = DagShortestPathTest.class
                .getClassLoader()
                .getResourceAsStream(resourceName)) {

            if (input == null) {
                throw new IllegalArgumentException("Dataset not found: " + resourceName);
            }

            JsonObject obj = JsonParser.parseReader(new InputStreamReader(input)).getAsJsonObject();
            JsonArray edgesArray = obj.getAsJsonArray("edges");

            for (JsonElement e : edgesArray) {
                JsonObject edgeObj = e.getAsJsonObject();
                int u = edgeObj.get("u").getAsInt();
                int v = edgeObj.get("v").getAsInt();
                double w = edgeObj.get("w").getAsDouble();

                graph.computeIfAbsent(u, k -> new ArrayList<>()).add(new DagShortestPath.Edge(v, w));
                graph.putIfAbsent(v, new ArrayList<>()); //check if all nodes exist
            }

        } catch (Exception e) {
            System.err.println("Error reading JSON graph: " + e.getMessage());
        }

        return graph;
    }

    @Test
    public void testShortestAndLongestPathsOnDataset() {
        String resourceName = "small2.json";
        int source = 4; //comes from dataset
        Map<Integer, List<DagShortestPath.Edge>> weightedGraph = readWeightedGraphFromJSON(resourceName);

        List<Integer> topoOrder = new ArrayList<>(weightedGraph.keySet());
        Collections.sort(topoOrder); //simple order for small DAGs

        DagShortestPath sp = new DagShortestPath();
        Map<Integer, Double> shortest = sp.shortestPath(weightedGraph, source, topoOrder);
        DagShortestPath.Metrics sm = sp.getMetrics();

        DagShortestPath lp = new DagShortestPath(); //check for longest path
        Map<Integer, Double> longest = lp.longestPath(weightedGraph, source, topoOrder);
        DagShortestPath.Metrics lm = lp.getMetrics();

        assertTrue(sm.relaxations > 0);
        assertTrue(lm.relaxations > 0);
        assertTrue(sm.timeNano > 0);
        assertTrue(lm.timeNano > 0);

        System.out.println("Shortest path distances: " + shortest);
        System.out.printf("Relaxations: %d | Time: %.3f ns%n", sm.relaxations, sm.timeNano / 1000.0);
        System.out.println("Longest path distances: " + longest);
        System.out.printf("Relaxations: %d | Time: %.3f ns%n", lm.relaxations, lm.timeNano / 1000.0);
    }
}
