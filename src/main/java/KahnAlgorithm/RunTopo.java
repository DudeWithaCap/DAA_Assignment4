package KahnAlgorithm;

import KosarajuAlgorithm.KosarajuSCC;
import java.util.*;
public class RunTopo {
    public static void main(String[] args) {
        String resourceName = "small2.json";  //change dataset here
        int[] nHolder = new int[1];
        Map<Integer, List<Integer>> graph = KosarajuSCC.readGraphFromJSON(resourceName, nHolder);

        KosarajuSCC sccAlgo = new KosarajuSCC(graph, nHolder[0]); //first start Kosaraju and then proceed with Kahn
        List<List<Integer>> sccList = sccAlgo.findSCCs();
        Map<Integer, List<Integer>> dag = KahnTS.buildCondensationGraph(sccList, graph); //build DAG from SCCs

        KahnTS topo = new KahnTS(); //starting Kahn algorithm
        List<Integer> topoOrder = topo.kahnTopologicalSort(dag);
        KahnTS.Metrics m = topo.getMetrics();

        List<Integer> derivedOrder = KahnTS.deriveOriginalOrder(topoOrder, sccList);

        System.out.println("SCC Count: " + sccList.size());
        System.out.println("Topological order of SCCs: " + topoOrder);
        System.out.println("Derived order of original nodes: " + derivedOrder);
        System.out.printf("Pushes: %d | Pops: %d | Time: %.3f ns%n",
                m.pushes, m.pops, m.timeNano / 1000.0);
    }
}
