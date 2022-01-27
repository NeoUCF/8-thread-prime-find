import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Primes implements Runnable
{
    public static final long UP_TO = (long)1E8;
    public static final long MAX_TASKS = (long)1E7;
    public static final int MAX_NUM_THREADS = 8;
    public static final int MAX_QUEUE_SIZE = 10;

    public static final AtomicLong counter = new AtomicLong(1);
    public static final AtomicLong totalSum = new AtomicLong(2);
    public static final AtomicLong currentNum = new AtomicLong(3);

    public static final PriorityBlockingQueue<Long> q = new PriorityBlockingQueue<Long>(MAX_QUEUE_SIZE);

    public static void main(String[] args)
    {
        findPrimes();
    }

    public void run()
    {
        // System.out.println("Running thread!");
    }

    private static void findPrimes()
    {
        final long startTime = System.currentTimeMillis();
        multiThreadPrime(UP_TO);
        final long endTime = System.currentTimeMillis();

        long executionTime = endTime - startTime;
        Object[] a = q.toArray();
        Arrays.sort(a);

        try {
            FileWriter myWriter = new FileWriter("primes.txt");
            myWriter.write("Prime Count: " + counter + "\n");
            myWriter.write("Execution Time: " + executionTime + "ms");
            myWriter.write("Sum of Primes: " + totalSum + "\n");
            myWriter.write("Top Ten:\n");
            for (Object i : a)
                myWriter.write(i.toString() + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.println("Prime Count: " + counter);
        System.out.println("Execution Time: " + executionTime + "ms");
        System.out.println("Sum of Primes: " + totalSum);
        System.out.println("Top Ten:");
        for (Object i : a)
            System.out.println(i);
    }

    public static boolean isPrime(long p)
    {
        for (long i = 3; i * i <= p; i += 2)
            if ((p % i) == 0)
                return false;

        return true;
    }

    // if element in sieve is false, then it's prime
    public static void setSieve(boolean[] sieve, int n)
    {
        for (int i = 2; i * i <= n; i++)
            if (!sieve[i])
                for (int j = i * i; j <= n; j += i)
                    sieve[j] = true;
    }

    private static long oneThreadPrimeSieve(long n)
    {
        boolean[] sieve = new boolean[(int)n + 1];
        sieve[0] = sieve[1] = true;
        long sum = 0;
        int count = 0;
        int[] arr = new int[10];

        setSieve(sieve, (int)n);

        for (int i = 2; i <= n; i++)
        {
            if (!sieve[i])
            {
                sum += i;
                arr[count%10] = i;
                count++;
            }
        }

        System.out.println("sum: " + sum);
        System.out.println("count: " + count);
        Arrays.sort(arr);
        System.out.println("Top 10 primes: " + Arrays.toString(arr));

        // for (int i = (int)n - 10; i < ; i++)
        return n;
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
        if (n < 2) return 0;

        for (long i = MAX_TASKS; i <= n; i += MAX_TASKS)
            loop(i);

        while (q.size() != MAX_QUEUE_SIZE)
            cleanQueue();

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

    public static void cleanQueue()
    {
        if (q.size() > MAX_QUEUE_SIZE)
        {
            try {
                q.take();
            } catch (InterruptedException e) {
                System.err.println("Error: " + e);
            }
        }
    }
}

class TestPrimes extends Primes
{
    public long cur;

    public TestPrimes(long i)
    {
        cur = i;
    }

    public void run()
    {
        if (isPrime(cur))
        {
            totalSum.addAndGet(cur);
            counter.incrementAndGet();

            cleanQueue();
            q.add(cur);
        }
    }
}