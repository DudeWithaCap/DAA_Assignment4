package KosarajuAlgorithm;

import java.io.InputStreamReader;
import java.util.*;
import com.google.gson.*;

public class KosarajuSCC {
    public static class Metrics {
        public int dfsVisits = 0;
        public int edgeTraversals = 0;
        public long timeNano = 0;
    }

    private final Map<Integer, List<Integer>> graph;
    private final Map<Integer, List<Integer>> reversed;
    private final int n;
    private final Metrics metrics = new Metrics();

    public KosarajuSCC(Map<Integer, List<Integer>> graph, int n) {
        this.graph = graph;
        this.n = n;
        this.reversed = reverseGraph(graph);
    }

    // starting Kosaraju
    public List<List<Integer>> findSCCs() {
        long start = System.nanoTime();
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) dfsOriginal(i, visited, stack);
        }

        Arrays.fill(visited, false);
        List<List<Integer>> components = new ArrayList<>();

        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                List<Integer> component = new ArrayList<>();
                dfsReversed(v, visited, component);
                components.add(component);
            }
        }

        metrics.timeNano = System.nanoTime() - start;
        return components;
    }

    private void dfsOriginal(int v, boolean[] visited, Deque<Integer> stack) {
        visited[v] = true;
        metrics.dfsVisits++;
        for (int nei : graph.getOrDefault(v, Collections.emptyList())) {
            metrics.edgeTraversals++;
            if (!visited[nei]) dfsOriginal(nei, visited, stack);
        }
        stack.push(v);
    }

    // DFS for reversed graph
    private void dfsReversed(int v, boolean[] visited, List<Integer> component) {
        visited[v] = true;
        component.add(v);
        metrics.dfsVisits++;
        for (int nei : reversed.getOrDefault(v, Collections.emptyList())) {
            metrics.edgeTraversals++;
            if (!visited[nei]) dfsReversed(nei, visited, component);
        }
    }

    // reverse
    private Map<Integer, List<Integer>> reverseGraph(Map<Integer, List<Integer>> g) {
        Map<Integer, List<Integer>> rev = new HashMap<>();
        for (int u : g.keySet()) {
            for (int v : g.get(u)) {
                rev.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
            }
        }
        return rev;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    // to read json files
    public static Map<Integer, List<Integer>> readGraphFromJSON(String resourceName, int[] nHolder) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        try {
            ClassLoader loader = KosarajuSCC.class.getClassLoader();
            try (var reader = new InputStreamReader(Objects.requireNonNull(loader.getResourceAsStream(resourceName)))) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                int n = root.get("n").getAsInt();
                nHolder[0] = n;

                JsonArray edges = root.getAsJsonArray("edges");
                for (JsonElement e : edges) {
                    JsonObject edge = e.getAsJsonObject();
                    int u = edge.get("u").getAsInt();
                    int v = edge.get("v").getAsInt();
                    graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
                }
            }
        } catch (Exception e) { //problem proofing
            System.err.println("Error reading JSON resource: " + e.getMessage());
        }
        return graph;
    }

    public static void main(String[] args) {
        String resourceName = "large2.json"; // input dataset name
        int[] nHolder = new int[1];

        Map<Integer, List<Integer>> graph = readGraphFromJSON(resourceName, nHolder);
        KosarajuSCC scc = new KosarajuSCC(graph, nHolder[0]);
        List<List<Integer>> result = scc.findSCCs();

        for (List<Integer> comp : result) {
            System.out.println(comp + " (size: " + comp.size() + ")");
        }

        Metrics m = scc.getMetrics();
        System.out.printf("DFS Visits: %d | Edge Traversals: %d | Time (ns): %.3f%n",
                m.dfsVisits, m.edgeTraversals, m.timeNano / 1000.0);
    }
}
