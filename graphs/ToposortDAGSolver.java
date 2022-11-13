package graphs;

import java.util.*;

public class ToposortDAGSolver<V> implements ShortestPathSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;
    private final V start;

    public ToposortDAGSolver(Graph<V> graph, V start) {
        this.edgeTo = new HashMap<>();
        this.distTo = new HashMap<>();
        this.start = start;

        Set<V> visited = new HashSet<>();
        List<V> result = new ArrayList<>();
        topoSort(graph, visited, result, start);
        Collections.reverse(result);
        edgeTo.put(start, null);
        distTo.put(start, 0.0);
        for (V from : result) {
            for (Edge<V> edge : graph.neighbors(from)) {
                V to = edge.to();
                double oldDist = distTo.getOrDefault(to, Double.POSITIVE_INFINITY);
                double newDist = distTo.get(from) + edge.weight();
                if (newDist < oldDist) {
                    distTo.put(to, newDist);
                    edgeTo.put(to, edge);
                }
            }
        }
    }

    private void topoSort(Graph<V> graph, Set<V> visited, List<V> result, V start) {        
        visited.add(start);
        for (Edge<V> edge : graph.neighbors(start)) {
            V to = edge.to();
            if (!visited.contains(to)) {
                topoSort(graph, visited, result, to);
            }
        }
        result.add(start);
    }

    public List<V> solution(V goal) {
        List<V> path = new ArrayList<>();
        V curr = goal;
        path.add(curr);
        while (edgeTo.get(curr) != null) {
            curr = edgeTo.get(curr).from();
            path.add(curr);
        }
        Collections.reverse(path);
        return path;
    }
}
