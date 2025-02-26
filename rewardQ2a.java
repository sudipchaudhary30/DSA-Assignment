public class rewardQ2a {

    public int minRewards(int[] ratings) {
        int n = ratings.length;
        int[] rewards = new int[n];
        
        for (int i = 0; i < n; i++) {
            rewards[i] = 1;  
        }
        
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }
        
     
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }
      
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }
        
        return totalRewards;
    }

    public static void main(String[] args) {
        rewardQ2a solution = new rewardQ2a();
        int[] ratings = {4, 3, 2, 5, 1};  
        System.out.println("Minimum rewards needed: " + solution.minRewards(ratings));  // Output: 9
    }
      
}
