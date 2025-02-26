import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class kthlowestcombinedQ1b {
    
     static class Pair {
        int i, j;
        int sum;

        Pair(int i, int j, int sum) {
            this.i = i;
            this.j = j;
            this.sum = sum;
        }
    }
    public static int kthLowestCombinedReturn(int[] returns1, int[] returns2, int k) {
        int m = returns1.length, n = returns2.length;
      
        PriorityQueue<Pair> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a.sum));
        Set<String> visited = new HashSet<>();
    
        minHeap.offer(new Pair(0, 0, returns1[0] + returns2[0]));
        visited.add("0,0");
        
        for (int count = 0; count < k; count++) {
            Pair current = minHeap.poll();
            int i = current.i, j = current.j;
          
            if (i + 1 < m && !visited.contains((i + 1) + "," + j)) {
                minHeap.offer(new Pair(i + 1, j, returns1[i + 1] + returns2[j]));
                visited.add((i + 1) + "," + j);
            }

            if (j + 1 < n && !visited.contains(i + "," + (j + 1))) {
                minHeap.offer(new Pair(i, j + 1, returns1[i] + returns2[j + 1]));
                visited.add(i + "," + (j + 1));
            }
        }

        return minHeap.peek().sum;
    }

    public static void main(String[] args) {
        int[] returns1 = {1, 3, 5};
        int[] returns2 = {2, 4, 6};
        int k = 4;

        System.out.println("The " + k + "th lowest combined return is: " + kthLowestCombinedReturn(returns1, returns2, k));
    }
}
