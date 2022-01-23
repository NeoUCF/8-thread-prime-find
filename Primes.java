import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Primes implements Runnable
{
    public static final long UP_TO = (long)1E8;
    public static final int MAX_NUM_THREADS = 8;
    public static final long MAX_POOL = (long)1E7;


    public static final AtomicLong counter = new AtomicLong(1);
    public static final AtomicLong totalSum = new AtomicLong(2);
    public static final AtomicLong currentNum = new AtomicLong(3);

    public static void main(String[] args)
    {
        // int coreCount = Runtime.getRuntime().availableProcessors();
        // System.out.println(coreCount);

        findPrimes();
        // Primes p = new Primes();
        // Thread th = new Thread(p);
        // th.start();
    }

    public void run()
    {
        // System.out.println("Running thread!");
    }

    private static void findPrimes()
    {
        final long startTime = System.currentTimeMillis();
        // oneThreadPrime(UP_TO);
        multiThreadPrime(UP_TO);
        final long endTime = System.currentTimeMillis();

        System.out.println("Total execution time: " + (endTime - startTime));
    }

    public static boolean isPrime(long p)
    {
        for (long i = 3; i <= Math.pow(p, 0.5); i += 2)
            if ((p % i) == 0)
                return false;

        return true;
    }

    private static long oneThreadPrime(long n)
    {
        // System.out.println(n);
        
        if (n < 2) return 0;
        long sum = 2;
        long count = 1;

        for (long i = 3; i <= n; i += 2)
            if (isPrime(i))
            {
                // System.out.println(i + ", ");
                sum += i;
                count++;
            }

        System.out.println("sum: " + sum);
        System.out.println("count: " + count);
        return sum;
    }

    private static long multiThreadPrime(long n)
    {
        // System.out.println(n);

        if (n < 2) return 0;

        // ExecutorService service = Executors.newFixedThreadPool(MAX_NUM_THREADS);

        // for (long i = 3; i <= n; i += 2)
        // while (currentNum.getAcquire() <= (n / 4))
        // {
        //     service.submit(new TestPrimes(currentNum.getAcquire()));
        //     currentNum.addAndGet(2);
        // }
        // service.shutdown();

        // try {
        //     service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        // } catch (InterruptedException e) {
        //     System.err.println("Error: " + e);
        //     return -1L;
        // }
        for (long i = MAX_POOL; i <= n; i += MAX_POOL)
            loop(i);

        System.out.println("totalSum: " + totalSum);
        System.out.println("counter: " + counter);
        return totalSum.getAcquire();
    }

    public static void loop(long n)
    {
        ExecutorService service = Executors.newFixedThreadPool(MAX_NUM_THREADS);

        while (currentNum.getAcquire() <= n)
        {
            service.submit(new TestPrimes(currentNum.getAcquire()));
            currentNum.addAndGet(2);
        }
        service.shutdown();

        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.err.println("Error: " + e);
        }
    }
}

class TestPrimes extends Primes
{
    public long cur;

    public TestPrimes(long i)
    {
        cur = i;
        // System.out.println("log: " + cur);
    }

    public void run()
    {
        // System.out.println("run: " + cur);

        // System.out.println("Running thread!");
        if (isPrime(cur))
        {
            // System.out.print(cur + ", ");
            totalSum.addAndGet(cur);
            counter.incrementAndGet();
        }
    }
}