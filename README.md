Design and Analysis of Algorithms

Assignment 4

Adilet Kabiyev

SE-2433

Implementation of algorithms for Strongly Connected Components, Topological Ordering, Shortest Path in DAG.

Strongly Connected Components

SCC graph is build using Kosaraju algorithm. Kosaraju algorithm key points are: performing DFS, reverse the graph, perform DFS in reversed order and then extract SCC.

<img width="600" height="328" alt="image" src="https://github.com/user-attachments/assets/bdde0ad6-bded-4a2d-9bdc-de4cb5109913" />

 

Topological Sort

Topological Sort was done using Kahn’s algorithm. After compressing the SCCs into single nodes, the condensation graph becomes a DAG.
Key points for Kahn’s algorithm: finding indegree of each node, start queue, iteratively remove nodes, dequeue zero indegree nodes.
 
<img width="433" height="342" alt="image" src="https://github.com/user-attachments/assets/c2b4aa9c-5200-4598-9dba-6bfeae8ccef8" />


Shortest and Longest Path

Finding shortest and longest path was done using DAG (Directed Acyclic Graph). It finds both paths using edge weights.
Key points: get topological order from Kahn’s algorithm, initialize distances, relax outgoing edges.
 
<img width="394" height="338" alt="image" src="https://github.com/user-attachments/assets/b555cb03-70ba-48e9-9ca9-83113808a49f" />


Graph dataset description.

All datasets were structured based on given tasks.json provided. 

Small1.json – simple DAG styled, no cycles. 6 nodes.

Small2.json – SCC with one cycle. 7 nodes.

Small3.json – 2 disconnected components. 8 nodes.

Medium1.json – mix of cyclic graphs and acyclic structures. 12 nodes.

Medium2.json – Multiple SCCs, medium density graph. 15 nodes.

Medium3.json – dense DAG styled, no cycles. 18 nodes.

Large1.json – multiple SCCs. 25 nodes.

Large2.json – DAG styles, small cycles. 30 nodes.

Large3.json – dense, DAG styled. 40 nodes.



Analysis

Strongly Connected Components

Kosaraju algorithm demonstrated linear performance across all datasets. For smaller graphs, the total number of DFS visits and edge traversals closely matched the number of vertices and edges. As the dataset size increased, execution time grew proportionally, showing predictable behavior. The number of strongly connected components also decreased with larger acyclic regions.

Topological Sort

Kahn topological sort algorithm showed excellent consistency and scalability. The number of push and pop operations was equal to the number of vertices in each dataset, as expected. This confirmed the stability and low overhead of queue-based processing.

Shortest Path in DAG

DAG shortest and longest path algorithms performed efficiently across all acyclic datasets. For small and medium graphs, computation times remained in the good timing order. In larger datasets, the difference between shortest and longest path timings wasn’t bad, confirming balanced computational load.



Conclusion
In conclusion, Kosaraju efficiently decomposed cyclic graphs into SCCs, Kahn maintained consistent queue operations for acyclic ordering, and the DAG path algorithms provided reliable shortest and longest distance calculations. Overall, the outcomes showed that each algorithm performs good for its graph type. Kosaraju is most effective for general directed graphs, especially those containing cycles. Kahn’s algorithm and the DAG path computations achieve the best performance on acyclic graphs.

