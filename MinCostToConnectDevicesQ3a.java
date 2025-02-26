import java.util.*;

public class MinCostToConnectDevicesQ3a {

    static class Edge {
        int device;
        int cost;
        
        Edge(int device, int cost) {
            this.device = device;
            this.cost = cost;
        }
    }

    public static int minCostToConnectDevices(int n, int[] modules, int[][] connections) {
        // Create adjacency list to represent the graph
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            adj.add(new ArrayList<>());
        }

        for (int i = 0; i < n; i++) {
            adj.get(0).add(new Edge(i + 1, modules[i]));  // (cost, device)
        }

        for (int[] conn : connections) {
            int device1 = conn[0];
            int device2 = conn[1];
            int cost = conn[2];
            adj.get(device1).add(new Edge(device2, cost));
            adj.get(device2).add(new Edge(device1, cost));
        }

        // Prim's algorithm setup
        int totalCost = 0;
        boolean[] visited = new boolean[n + 1];  // To track visited devices
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a.cost));

        for (Edge edge : adj.get(0)) {
            minHeap.add(edge);
        }

        int edgesUsed = 0;

        while (!minHeap.isEmpty() && edgesUsed < n) {
            Edge currentEdge = minHeap.poll();
            int device = currentEdge.device;
            int cost = currentEdge.cost;

            if (visited[device]) {
                continue;
            }

            visited[device] = true;
            totalCost += cost;
            edgesUsed++;

            for (Edge nextEdge : adj.get(device)) {
                if (!visited[nextEdge.device]) {
                    minHeap.add(nextEdge);
                }
            }
        }

        return totalCost;
    }

    public static void main(String[] args) {
        int n = 3;
        int[] modules = {1, 2, 2};
        int[][] connections = {{1, 2, 1}, {2, 3, 1}};
        
        int totalCost = minCostToConnectDevices(n, modules, connections);
        System.out.println("The minimum total cost to connect all devices in the network is: " + totalCost);
    }
}
