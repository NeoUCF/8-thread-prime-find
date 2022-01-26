import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;

public class Primes implements Runnable
{
    public static final int UP_TO = (int)1E8;
    public static final int MAX_TASKS = (int)1E7;
    public static final int MAX_NUM_THREADS = 8;
    public static final int MAX_QUEUE_SIZE = 10;

    public static final AtomicLong totalSum = new AtomicLong(0);
    public static final AtomicInteger counter = new AtomicInteger(0);
    public static final AtomicInteger currentNum = new AtomicInteger(2);
    public static final AtomicIntegerArray sieve = new AtomicIntegerArray(UP_TO + 1);

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
        // multiThreadPrime(UP_TO);
        // oneThreadPrime(UP_TO);
        // oneThreadPrimeSieve(UP_TO);
        multiThreadPrimeSieve(UP_TO);
        final long endTime = System.currentTimeMillis();

        long executionTime = endTime - startTime;
        Object[] a = q.toArray();
        Arrays.sort(a);
        System.out.println(executionTime + "ms\t" + counter + "\t" + totalSum);
        System.out.println(Arrays.toString(a));
    }

    public static boolean isPrime(long p)
    {
        // System.out.println(p);
        for (long i = 3; i * i <= p; i += 2)
            if ((p % i) == 0)
                return false;

        return true;
    }

    public static void debugPrint()
    {
        System.out.println("in sieve");
        for (int i = 0; i < 10; i++)
        {
            // System.out.println(sieve.get(i));
        }
    }

    public static boolean isPrime2(long[] sieve, long p)
    {
        // System.out.println(p);
        for (int i = 0; sieve[i] * sieve[i] <= p; i++)
            if ((p % sieve[i]) == 0)
                return false;

        return true;
    }

    public static boolean isPrime3(boolean[] sieve, int p)
    {
        for (int i = 2; i * i <= p; i++)
            if (sieve[i])
                for (int j = i * i; j <= p; j += i)
                    sieve[j] = false;

        return !sieve[p];
    }

    // if element in sieve is false, then it's prime
    public static void setSieve1(boolean[] sieve, int n)
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

        setSieve1(sieve, (int)n);

        for (int i = 2; i < n; i++)
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
        long[] sieve = new long[20000000];
        long sum = sieve[0] = 2;
        int count = 1;

        for (int i = 3; i <= n; i += 2)
        {
            if (isPrime2(sieve, i))
            // if(isPrime(i))
            {
                sum += i;
                sieve[count] = i;
                count++;
            }
        }

        System.out.println("sum: " + sum);
        System.out.println("count: " + count);
        return sum;
    }

    // if element in sieve is 0, then it's prime
    public static void setSieve(int n)
    {
        for (int i = 2; i * i <= n; i++)
            if (sieve.getAcquire(i) == 0)
                for (int j = i * i; j <= n; j += i)
                    sieve.set(j, 1);
    }

    public static void loop(int n)
    {
        ExecutorService service = Executors.newFixedThreadPool(MAX_NUM_THREADS);

        while (currentNum.getAcquire() <= n)
        {
            service.submit(new PrimeSieve(currentNum.getAcquire()));
            currentNum.incrementAndGet();
        }
        System.out.println("done" + sieve.get(18));

        service.shutdown();

        // try {
        //     service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        // } catch (InterruptedException e) {
        //     System.err.println("Error: " + e);
        // }
    }

    private static void multiThreadPrimeSieve(int n)
    {
        sieve.set(0, 1);
        sieve.set(1, 1);
        long sum = 0;
        int count = 0;

        ExecutorService service = Executors.newFixedThreadPool(MAX_NUM_THREADS);
        int[] firstEightPrimes = {2, 3, 5, 7, 11, 13, 17, 19};

        service.submit(new PrimeSieve(2));

        for (int p = 3; p < 16; p += 2)
        {
            service.submit(new PrimeSieve(p));
        }
        System.out.println("done" + sieve.get(18));
        
        // for (int i = 23; i <= n; i += 2)
        // {
        //     service.submit(new PrimeSieve(i));
        // }

        service.shutdown();
        System.out.println("done" + sieve.get(18));

        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.err.println("Error: " + e);
        }
        System.out.println("done" + sieve.get(18));

        // currentNum.set(23); // Next known prime to count off with

        // for (int i = MAX_TASKS; i <= n; i += MAX_TASKS)
        // {
        //     loop(i);
        //     System.out.println(i + " batches " + currentNum.getAcquire());
        // }

        // setSieve(n);
        System.out.println("done" + sieve.get(18));

        for (int i = 2; i < n; i++)
        {
            if (sieve.getAcquire(i) == 0)
            {
                totalSum.getAndAdd(i);
                // arr[count%10] = i;
                counter.incrementAndGet();
                sum += i;
                // arr[count%10] = i;
                count++;
            }
        }

        System.out.println("sum: " + sum);
        System.out.println("count: " + count);
    }

    private static long multiThreadPrime(long n)
    {
        if (n < 2) return 0;

        for (long i = MAX_TASKS; i <= n; i += MAX_TASKS)
            loop1(i);

        while (q.size() != MAX_QUEUE_SIZE)
            cleanQueue();

        return totalSum.getAcquire();
    }

    public static void loop1(long n)
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

class PrimeSieve extends Primes
{
    public int cur;

    public PrimeSieve(int i)
    {
        cur = i;
    }

    public void run()
    {
        System.out.println("running" + cur);
        for (int i = cur; i * i <= UP_TO; i += 8)
        {
            // System.out.println(i);

            if (sieve.getAcquire(i) == 0)
                for (int j = i * i; j <= UP_TO; j += i)
                {
                    // System.out.println(j);

                    sieve.getAndIncrement(j);
                    // System.out.println("process" + i);
                    
                }
        }
        System.out.println("finished " + cur);

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