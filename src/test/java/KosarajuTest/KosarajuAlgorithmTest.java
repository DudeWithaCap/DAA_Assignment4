package KosarajuTest;

import KosarajuAlgorithm.KosarajuSCC;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class KosarajuAlgorithmTest {

    @Test
    public void testDataset() {
        String resourceName = "large2.json";  //load dataset

        int[] nHolder = new int[1];
        Map<Integer, List<Integer>> graph = KosarajuSCC.readGraphFromJSON(resourceName, nHolder);
        assertNotNull(graph, "Graph loading failed"); //problem proofing
        assertTrue(nHolder[0] > 0, "Invalid node count");

        KosarajuSCC scc = new KosarajuSCC(graph, nHolder[0]);
        scc.findSCCs();  //run algorithm
        KosarajuSCC.Metrics m = scc.getMetrics();

        System.out.printf("\nPerformance Metrics for %s %nDFS Visits: %d | Edge Traversals: %d | Time: %.3f ns%n",
                resourceName, m.dfsVisits, m.edgeTraversals, m.timeNano / 1000.0
        );

        assertTrue(m.dfsVisits > 0);
        assertTrue(m.edgeTraversals >= 0);
        assertTrue(m.timeNano > 0);
    }
}
