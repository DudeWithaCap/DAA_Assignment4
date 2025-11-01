package KahnTest;

import KahnAlgorithm.KahnTS;
import org.junit.jupiter.api.Test;
import KosarajuAlgorithm.KosarajuSCC;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class KahnTSTest {
    @Test
    public void testKahnMetrics() {
        String resourceName = "small2.json";  //change dataset

        int[] nHolder = new int[1];
        Map<Integer, List<Integer>> graph = KosarajuSCC.readGraphFromJSON(resourceName, nHolder);

        KosarajuSCC sccAlgo = new KosarajuSCC(graph, nHolder[0]);
        List<List<Integer>> sccList = sccAlgo.findSCCs();

        KahnTS topo = new KahnTS();
        Map<Integer, List<Integer>> dag = KahnTS.buildCondensationGraph(sccList, graph);
        List<Integer> topoOrder = topo.kahnTopologicalSort(dag);

        KahnTS.Metrics m = topo.getMetrics();

        System.out.printf(
                "\n=== Kahn Metrics for %s ===%nPushes: %d | Pops: %d | Time: %.3f ns%n",
                resourceName, m.pushes, m.pops, m.timeNano / 1000.0
        );

        assertTrue(m.pushes > 0);
        assertTrue(m.pops > 0);
        assertTrue(m.timeNano > 0);
        assertEquals(sccList.size(), topoOrder.size(), "All SCCs should appear in topo order");
    }
}
