class NumberPrinterQ6a {
    private int current = 0;
    private final int n; // Limit for numbers

    public NumberPrinterQ6a(int n) {
        this.n = n;
    }

    // Method to print '0'
    public synchronized void printZero() {
        for (int i = 0; i < n; i++) {
            while (current % 2 != 0) {  // Wait until it's time for '0'
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.print("0");
            current++;  
            notifyAll();  // Notify other threads
        }
    }

    // Method to print odd numbers
    public synchronized void printOdd() {
        for (int i = 1; i <= n; i += 2) {
            while (current % 4 != 1) {  // Wait until it's time for an odd number
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.print(i);
            current++;  
            notifyAll();  // Notify other threads
        }
    }

    // Method to print even numbers
    public synchronized void printEven() {
        for (int i = 2; i <= n; i += 2) {
            while (current % 4 != 3) {  // Wait until it's time for an even number
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.print(i);
            current++;  
            notifyAll();  // Notify other threads
        }
    }
}

// Thread to print '0'
class ZeroThread extends Thread {
    private final NumberPrinterQ6a printer;

    public ZeroThread(NumberPrinterQ6a printer) {
        this.printer = printer;
    }

    @Override
    public void run() {
        printer.printZero();
    }
}

// Thread to print odd numbers
class OddThread extends Thread {
    private final NumberPrinterQ6a printer;

    public OddThread(NumberPrinterQ6a printer) {
        this.printer = printer;
    }

    @Override
    public void run() {
        printer.printOdd();
    }
}

// Thread to print even numbers
class EvenThread extends Thread {
    private final NumberPrinterQ6a printer;

    public EvenThread(NumberPrinterQ6a printer) {
        this.printer = printer;
    }

    @Override
    public void run() {
        printer.printEven();
    }
}

public class ThreadController {
    public static void main(String[] args) {
        int n = 6;  // Set limit for numbers
        NumberPrinterQ6a printer = new NumberPrinterQ6a(n);

        Thread zeroThread = new ZeroThread(printer);
        Thread oddThread = new OddThread(printer);
        Thread evenThread = new EvenThread(printer);

        zeroThread.start();
        oddThread.start();
        evenThread.start();
    }
}
