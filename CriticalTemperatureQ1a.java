
public class CriticalTemperatureQ1a {
    public static int findCriticalTemperature(int k, int n) {
      
        if (n == 0) return 0;
        if (k == 1) return n;

        int minMeasurements = Integer.MAX_VALUE;

        // Iterate through each temperature level
        for (int i = 1; i <= n; i++) {
           
            int measurementsIfReacts = findCriticalTemperature(k - 1, i - 1);
            int measurementsIfNotReacts = findCriticalTemperature(k, n - i);
            int worstCase = 1 + Math.max(measurementsIfReacts, measurementsIfNotReacts);
            minMeasurements = Math.min(minMeasurements, worstCase);
        }
        return minMeasurements;
    }
    public static void main(String[] args) {
        int k = 2; 
        int n = 7; 

        int result = findCriticalTemperature(k, n);
        System.out.println("Minimum number of measurements required: " + result);
    }
}
