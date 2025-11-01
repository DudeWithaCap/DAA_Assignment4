package KahnAlgorithm;
import java.util.*;

public class KahnTS {

    public static class Metrics {
        public long pushes = 0;
        public long pops = 0;
        public long timeNano = 0;
    }

    private final Metrics metrics = new Metrics();

    public Metrics getMetrics() {
        return metrics;
    }

    public static Map<Integer, List<Integer>> buildCondensationGraph(
            List<List<Integer>> sccList, Map<Integer, List<Integer>> originalGraph) {

        Map<Integer, List<Integer>> dag = new HashMap<>();
        Map<Integer, Integer> nodeToSCC = new HashMap<>();

        for (int i = 0; i < sccList.size(); i++) { //give vertices SCC index
            for (int node : sccList.get(i)) {
                nodeToSCC.put(node, i);
            }
        }

        for (int u : originalGraph.keySet()) { //create DAG edges between SCCs
            for (int v : originalGraph.get(u)) {
                int sccU = nodeToSCC.get(u);
                int sccV = nodeToSCC.get(v);
                if (sccU != sccV) {
                    dag.computeIfAbsent(sccU, k -> new ArrayList<>()).add(sccV);
                }
            }
        }

        for (int i = 0; i < sccList.size(); i++) {
            dag.putIfAbsent(i, new ArrayList<>());
        }

        return dag;
    }

    public List<Integer> kahnTopologicalSort(Map<Integer, List<Integer>> dag) {
        long start = System.nanoTime(); // start timer

        Map<Integer, Integer> indegree = new HashMap<>();
        for (int u : dag.keySet()) {
            indegree.putIfAbsent(u, 0);
            for (int v : dag.get(u)) {
                indegree.put(v, indegree.getOrDefault(v, 0) + 1);
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (var entry : indegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
                metrics.pushes++;  //count enqueue
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            metrics.pops++;   //count dequeue
            topoOrder.add(u);

            for (int v : dag.getOrDefault(u, Collections.emptyList())) {
                indegree.put(v, indegree.get(v) - 1);
                if (indegree.get(v) == 0) {
                    queue.add(v);
                    metrics.pushes++;
                }
            }
        }

        metrics.timeNano = System.nanoTime() - start; // stop timer
        return topoOrder;
    }

    public static List<Integer> deriveOriginalOrder(
            List<Integer> topoOrder, List<List<Integer>> sccList) {

        List<Integer> orderedNodes = new ArrayList<>();
        for (int sccIndex : topoOrder) {
            orderedNodes.addAll(sccList.get(sccIndex));
        }
        return orderedNodes;
    }
}
