public class NumberPrinter {
    private int current = 0;

    // Method to print '0'
    public synchronized void printZero() {
        while (current % 3 != 0) {  // Wait until it's time for '0'
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.print("0");
        current++;  // Increment to indicate the next type of number
        notifyAll();  // Notify other threads to proceed
    }

    // Method to print even numbers
    public synchronized void printEven() {
        while (current % 3 != 1) {  // Wait until it's time for an even number
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.print(current);
        current++;  // Increment to indicate the next number
        notifyAll();  // Notify other threads to proceed
    }

    // Method to print odd numbers
    public synchronized void printOdd() {
        while (current % 3 != 2) {  // Wait until it's time for an odd number
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.print(current);
        current++;  // Increment to indicate the next number
        notifyAll();  // Notify other threads to proceed
    }
}

// ZeroThread to print '0'
class ZeroThread extends Thread {
    private NumberPrinter printer;
    private int n;

    public ZeroThread(NumberPrinter printer, int n) {
        this.printer = printer;
        this.n = n;
    }

    @Override
    public void run() {
        for (int i = 0; i < n; i++) {  // Print '0' n times
            printer.printZero();
        }
    }
}

// EvenThread to print even numbers
class EvenThread extends Thread {
    private NumberPrinter printer;
    private int n;

    public EvenThread(NumberPrinter printer, int n) {
        this.printer = printer;
        this.n = n;
    }

    @Override
    public void run() {
        for (int i = 2; i <= n; i += 2) {  // Print even numbers until n
            printer.printEven();
        }
    }
}

// OddThread to print odd numbers
class OddThread extends Thread {
    private NumberPrinter printer;
    private int n;

    public OddThread(NumberPrinter printer, int n) {
        this.printer = printer;
        this.n = n;
    }

    @Override
    public void run() {
        for (int i = 1; i <= n; i += 2) {  // Print odd numbers until n
            printer.printOdd();
        }
    }
}

 class ThreadController {
    private NumberPrinter printer;
    private int n;

    public ThreadController(NumberPrinter printer, int n) {
        this.printer = printer;
        this.n = n;
    }

    public void startThreads() {
        Thread zeroThread = new ZeroThread(printer, n);
        Thread evenThread = new EvenThread(printer, n);
        Thread oddThread = new OddThread(printer, n);

        zeroThread.start();
        evenThread.start();
        oddThread.start();
    }

    public static void main(String[] args) {
        int n = 6;  // The number limit can be changed here
        NumberPrinter printer = new NumberPrinter();
        ThreadController controller = new ThreadController(printer, n);
        controller.startThreads();
    }
}
