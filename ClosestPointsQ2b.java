public class ClosestPointsQ2b {

    public static int[] closestPoints(int[] x_coords, int[] y_coords) {
        int minDistance = Integer.MAX_VALUE;
        int[] result = new int[2];  

        for (int i = 0; i < x_coords.length; i++) {
            for (int j = i + 1; j < x_coords.length; j++) {  // Start from i+1 to avoid comparing same pairs twice
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                if (distance < minDistance || (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDistance = distance;
                    result[0] = i;
                    result[1] = j;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};

        int[] result = closestPoints(x_coords, y_coords);

        System.out.println("The closest pair of points are: [" + result[0] + ", " + result[1] + "]");
    }
}
