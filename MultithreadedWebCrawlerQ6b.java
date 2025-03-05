import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class MultithreadedWebCrawlerQ6b {

    // Thread-safe data structures
    private final BlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
    private final ConcurrentHashMap<String, String> crawledData = new ConcurrentHashMap<>();

    // Thread pool
    private final ExecutorService executorService;

    // Constructor to initialize the thread pool
    public MultithreadedWebCrawlerQ6b(int numThreads) {
        this.executorService = Executors.newFixedThreadPool(numThreads);
    }

    // Method to start crawling
    public void startCrawling(String seedUrl) {
        // Add the seed URL to the queue and mark it as visited
        urlQueue.add(seedUrl);
        visitedUrls.add(seedUrl);

        // Submit initial tasks to the thread pool
        for (int i = 0; i < 10; i++) {
            executorService.submit(new CrawlTask());
        }

        // Shutdown the thread pool after all tasks are completed
        executorService.shutdown();
        try {
            // Wait for all tasks to finish
            if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Crawling is complete.");
            } else {
                System.err.println("Crawling did not complete in the expected time.");
            }
        } catch (InterruptedException e) {
            System.err.println("Crawling interrupted: " + e.getMessage());
        }

        // Print the crawled data
        System.out.println("Data collected:");
        crawledData.forEach((url, content) -> System.out.println(url + " -> " + content));
    }

    // Task to crawl a single URL
    private class CrawlTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                String currentUrl = urlQueue.poll();
                if (currentUrl == null) {
                    break; // No more URLs to process
                }

                try {
                    // Fetch the page content
                    String pageContent = fetchPageContent(currentUrl);
                    crawledData.put(currentUrl, pageContent);

                    // Extract new URLs from the page content
                    Set<String> newUrls = extractUrls(pageContent, currentUrl);
                    for (String newUrl : newUrls) {
                        synchronized (visitedUrls) {
                            if (!visitedUrls.contains(newUrl)) {
                                visitedUrls.add(newUrl);
                                urlQueue.add(newUrl);
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Failed to fetch URL: " + currentUrl + " - " + e.getMessage());
                }
            }
        }

        // Method to fetch the content of a web page
        private String fetchPageContent(String url) throws IOException {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }

            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                scanner.useDelimiter("\\Z");
                return scanner.next();
            }
        }

        // Method to extract URLs from the page content
        private Set<String> extractUrls(String content, String baseUrl) {
            Set<String> urls = new HashSet<>();
            String regex = "<a\\s+(?:[^>]*?\\s+)?href=\"([^\"]*)\"";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                String newUrl = matcher.group(1);
                if (newUrl.startsWith("http")) {
                    urls.add(newUrl);
                } else if (newUrl.startsWith("/")) {
                    // Resolve relative URLs
                    try {
                        URL url = new URL(new URL(baseUrl), newUrl);
                        urls.add(url.toString());
                    } catch (Exception e) {
                        System.err.println("Failed to resolve URL: " + newUrl);
                    }
                }
            }
            return urls;
        }
    }

    // Main method to start the application
    public static void main(String[] args) {
        // Create a crawler with 10 threads
        MultithreadedWebCrawlerQ6b crawler = new MultithreadedWebCrawlerQ6b(10);

        // Start crawling with a seed URL
        System.out.println("Starting the crawling process...");
        crawler.startCrawling("https://www.notesdada.com/?m=1");
        System.out.println("The crawling process is completed....");
    }
}
