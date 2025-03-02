import java.util.*;

public class PackageDelivery4b {
    public static int minRoads(int[] packages, int[][] roads) {
        int n = packages.length;
        List<List<Integer>> graph = new ArrayList<>();

        // Create graph from road connections
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        // Add edges
        for (int[] road : roads) {
            graph.get(road[0]).add(road[1]);
            graph.get(road[1]).add(road[0]);
        }

        int minRoadsCount = Integer.MAX_VALUE;

        // Start BFS from each node and find the minimum roads to collect all packages
        for (int start = 0; start < n; start++) {
            int roadsCount = bfs(start, graph, packages);
            if (roadsCount != -1) {
                minRoadsCount = Math.min(minRoadsCount, roadsCount);
            }
        }

        return minRoadsCount == Integer.MAX_VALUE ? -1 : minRoadsCount; // No solution found
    }

    private static int bfs(int start, List<List<Integer>> graph, int[] packages) {
        int n = graph.size();
        boolean[] visited = new boolean[n];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{start, 0}); // {node, roadsCount}
        visited[start] = true;
        int collected = packages[start] == 1 ? 1 : 0; // Start with the package if present
        int roadsCount = 0;

        // Perform BFS
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int node = current[0];
            int currentRoadsCount = current[1];

            // Check if this node contains a package and collect it
            if (packages[node] == 1) {
                collected++;
            }

            // If all packages are collected, return the current number of roads traveled
            if (collected == countPackages(packages)) {
                return currentRoadsCount;
            }

            // Visit neighbors
            for (int neighbor : graph.get(node)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(new int[]{neighbor, currentRoadsCount + 1});
                }
            }
        }

        return -1; // No valid path to collect all packages
    }

    private static int countPackages(int[] packages) {
        int count = 0;
        for (int pkg : packages) {
            if (pkg == 1) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int[] packages = {0, 0, 0, 1, 1, 0, 0, 1};
        int[][] roads = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}};
        int result = minRoads(packages, roads);
        System.out.println("The minimum number of roads to traverse: " + result);  // Expected: 2
    }
}
