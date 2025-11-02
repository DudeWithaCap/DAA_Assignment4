package DAGalgorithm;

import java.util.*;

public class DagShortestPath {

    public static class Metrics {
        public long relaxations = 0;
        public long timeNano = 0;
    }

    private final Metrics metrics = new Metrics();
    public Metrics getMetrics() {
        return metrics;
    }

    public static class Edge {
        public int to;
        public double weight;
        public Edge(int to, double weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public Map<Integer, Double> shortestPath(
            Map<Integer, List<Edge>> graph, int source, List<Integer> topoOrder) {

        long start = System.nanoTime();
        Map<Integer, Double> dist = new HashMap<>();

        for (int u : graph.keySet()) {
            dist.putIfAbsent(u, Double.POSITIVE_INFINITY);
            for (Edge e : graph.get(u)) {
                dist.putIfAbsent(e.to, Double.POSITIVE_INFINITY);
            }
        }
        dist.put(source, 0.0);

        for (int u : topoOrder) {
            if (dist.get(u) != Double.POSITIVE_INFINITY) {
                for (Edge e : graph.getOrDefault(u, Collections.emptyList())) {
                    double newDist = dist.get(u) + e.weight;
                    if (newDist < dist.get(e.to)) {
                        dist.put(e.to, newDist);
                        metrics.relaxations++;
                    }
                }
            }
        }

        metrics.timeNano = System.nanoTime() - start;
        return dist;
    }

    public Map<Integer, Double> longestPath(
            Map<Integer, List<Edge>> graph, int source, List<Integer> topoOrder) { //check if longest path

        long start = System.nanoTime();
        Map<Integer, Double> dist = new HashMap<>();

        for (int u : graph.keySet()) {
            dist.putIfAbsent(u, Double.NEGATIVE_INFINITY);
            for (Edge e : graph.get(u)) {
                dist.putIfAbsent(e.to, Double.NEGATIVE_INFINITY);
            }
        }
        dist.put(source, 0.0);

        for (int u : topoOrder) {
            if (dist.get(u) != Double.NEGATIVE_INFINITY) {
                for (Edge e : graph.getOrDefault(u, Collections.emptyList())) {
                    double newDist = dist.get(u) + e.weight;
                    if (newDist > dist.get(e.to)) {
                        dist.put(e.to, newDist);
                        metrics.relaxations++;
                    }
                }
            }
        }

        metrics.timeNano = System.nanoTime() - start;
        return dist;
    }

    public List<Integer> reconstructPath(
            Map<Integer, List<Edge>> graph,
            int source,
            int target,
            Map<Integer, Double> dist,
            List<Integer> topoOrder) {

        Map<Integer, Integer> prev = new HashMap<>();

        for (int u : topoOrder) {
            for (Edge e : graph.getOrDefault(u, Collections.emptyList())) {
                double expectedDist = dist.get(u) + e.weight;
                if (Math.abs(expectedDist - dist.get(e.to)) < 1e-9) {
                    prev.put(e.to, u);
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        for (Integer at = target; at != null; at = prev.get(at)) {
            path.add(at);
            if (at == source) break;
        }
        Collections.reverse(path);
        return path;
    }
}
