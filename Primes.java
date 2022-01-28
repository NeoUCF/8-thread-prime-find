import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Primes implements Runnable
{
    public static final int MAX_NUM_THREADS = 8;
    public static final int UP_TO = (int)1E8;
    public static int count = 0;
    public static long sum = 0;
    public static int[] arr = new int[10];

    public static final AtomicIntegerArray sieve = new AtomicIntegerArray(UP_TO + 1);

    public static void main(String[] args)
    {
        findPrimes();
    }

    public void run()
    {
        // System.out.println("Running thread!");
    }

    private static void timeAverage(int n)
    {
        long totalTime = 0;

        for (int i = 0; i < n; i++)
        {
            count = 0;
            sum = 0;
            arr = new int[10];
            final long startTime = System.currentTimeMillis();
            multiThreadPrimeSieve(UP_TO);
            final long endTime = System.currentTimeMillis();
            totalTime += endTime - startTime;
        }

        System.out.println("Average Time: " + (totalTime / n));
    }

    private static void findPrimes()
    {
        // timeAverage(100);
        final long startTime = System.currentTimeMillis();
        multiThreadPrimeSieve(UP_TO);
        final long endTime = System.currentTimeMillis();

        long executionTime = endTime - startTime;

        try {
            FileWriter myWriter = new FileWriter("primes.txt");
            myWriter.write("Prime Count: " + count + "\n");
            myWriter.write("Execution Time: " + executionTime + "ms\n");
            myWriter.write("Sum of Primes: " + sum + "\n");
            myWriter.write("Top Ten:\n");
            for (int i : arr)
                myWriter.write(i + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("Prime Count: " + count);
        System.out.println("Execution Time: " + executionTime + "ms");
        System.out.println("Sum of Primes: " + sum);
        System.out.println("Top Ten:");
        for (Object i : arr)
            System.out.println(i);
    }

    public static void multiSieve(int n)
    {
        //Make array of Thread objects
        Thread[] sieveThreads = new Thread[MAX_NUM_THREADS];

        //Fill array with PrimeSieve objects
        for (int i = 0; i < MAX_NUM_THREADS; i++)
        {
            sieveThreads[i] = new Thread(new PrimeSieve(i + 1));
            sieveThreads[i].start();
        }

        for (int i = 0; i < MAX_NUM_THREADS; i++)
        {
            try
            {
                sieveThreads[i].join();
            }
            catch(Exception ex)
            {
                System.out.println("Exception has been caught" + ex);
            }
        }
    }
    
    // Instead of threading each number to sieve,
    // Why not multithread the counting of the numbers for the sieve
    private static void multiThreadPrimeSieve(int n)
    {
        sieve.set(0, 1);
        sieve.set(1, 1);

        multiSieve(n);

        for (int i = 2; i <= n; i++)
        {
            if (sieve.getAcquire(i) == 0)
            {
                sum += i;
                arr[count%10] = i;
                count++;
            }
        }

        Arrays.sort(arr);
    }
}

class PrimeSieve extends Primes
{
    public int threadNum;

    public PrimeSieve(int th)
    {
        threadNum = th;
    }

    public void run()
    {
        for (int i = 2; i * i <= UP_TO; i++)
            if (sieve.getAcquire(i) == 0)
                for (int p = i * (threadNum + 1); p <= UP_TO; p += i * MAX_NUM_THREADS)
                    sieve.set(p, 1);
    }
}