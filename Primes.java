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

    public static final AtomicLong totalSum = new AtomicLong(2);
    public static final AtomicInteger counter = new AtomicInteger(1);
    public static final AtomicInteger currentNum = new AtomicInteger(2);
    public static final AtomicIntegerArray sieve = new AtomicIntegerArray(UP_TO + 1);
    public static volatile boolean[] boolSieve = new boolean[UP_TO + 1];

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
/*
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
*/
    public static void multiSieve(int n)
    {
        // for (int i = 2 * threadNum; i * i <= n; i += 2 * MAX_NUM_THREADS)
        //     if (sieve.getAcquire(i) == 0)
        //         for (int j = i * i; j <= n; j += i)
        //             sieve.set(j, 1);
        // for (int i = 2; i * i <= n; i++)
        // {
        //     for (int j = i * threadNum; j <= n; j += i * MAX_NUM_THREADS)
        //     {
        //         if (sieve.getAcquire(j) == 0)
        //         {
        //             sieve.set(j, 1);
        //         }
        //     }
        // }

        ExecutorService service = Executors.newFixedThreadPool(MAX_NUM_THREADS);
        for (int i = 2; i * i <= n; i++)
            if (sieve.getAcquire(i) == 0)
            {

                for (int j = 1; j <= 8; j++)
                    service.submit(new PrimeSieve(i, j));
                
            }
        
            service.shutdown();

        // try {
        //     service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        // } catch (InterruptedException e) {
        //     System.err.println("Error: " + e);
        // }
    }

    public static void multiSieve2(int n)
    {
        ExecutorService service = Executors.newFixedThreadPool(MAX_NUM_THREADS);

        for (int j = 1; j <= 8; j++)
            service.submit(new PrimeSieve2(j));

        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.err.println("Error: " + e);
        }
    }

    public static void multiSieve3(int n)
    {
        for (int i = 1; i <= MAX_NUM_THREADS; i++)
        {
            PrimeSieve3 p = new PrimeSieve3(i);
            Thread th = new Thread(p);
            th.start();
            try
            {
                th.join(1);
                System.out.println("Current Thread: " + Thread.currentThread().getName());
            }
            catch(Exception ex)
            {
                System.out.println("Exception has" + " been caught" + ex);
            }
        }
        System.out.println(Thread.activeCount() + "Out the loop" +  Thread.currentThread());

        try
        {
            Thread.sleep(2000);
            System.out.println("Current Thread: " + Thread.currentThread().getName());
        }
        catch(Exception ex)
        {
            System.out.println("Exception has" + " been caught" + ex);
        }
    }
    
    // if element in sieve is 0, then it's prime
    public static void setSieve2(int n)
    {
        for (int i = 2; i * i <= n; i++)
            if (!boolSieve[i])
                for (int j = i * i; j <= n; j += i)
                    boolSieve[j] = true;
    }

    public static void multiSieve4(int n)
    {
        // for (int i = 1; i <= MAX_NUM_THREADS; i++)
        // {
        //     PrimeSieve3 p = new PrimeSieve3(i);
        //     Thread th = new Thread(p);
        //     th.start();
        //     try
        //     {
        //         th.join(1);
        //         System.out.println("Current Thread: " + Thread.currentThread().getName());
        //     }
        //     catch(Exception ex)
        //     {
        //         System.out.println("Exception has" + " been caught" + ex);
        //     }
        // }

        PrimeSieve5 p1 = new PrimeSieve5(2);
        Thread th1 = new Thread(p1);
        PrimeSieve5 p2 = new PrimeSieve5(3);
        Thread th2 = new Thread(p2);
        PrimeSieve5 p3 = new PrimeSieve5(5);
        Thread th3 = new Thread(p3);
        PrimeSieve5 p4 = new PrimeSieve5(7);
        Thread th4 = new Thread(p4);
        PrimeSieve5 p5 = new PrimeSieve5(11);
        Thread th5 = new Thread(p5);
        PrimeSieve5 p6 = new PrimeSieve5(13);
        Thread th6 = new Thread(p6);
        PrimeSieve5 p7 = new PrimeSieve5(17);
        Thread th7 = new Thread(p7);
        PrimeSieve5 p8 = new PrimeSieve5(19);
        Thread th8 = new Thread(p8);
        try
        {
            th1.start();
        //     th1.join(10);
            th2.start();
        //     th2.join(10);
            th3.start();
        //     th3.join(10);
            th4.start();
        //     th4.join(10);
            th5.start();
        //     th5.join(10);
            th6.start();
        //     th6.join(10);
            th7.start();
        //     th7.join(10);
            th8.start();
            th8.join();

        //     System.out.println("Current Thread: " + Thread.currentThread().getName());
        }
        catch(Exception ex)
        {
        //     System.out.println("Exception has" + " been caught" + ex);
        }
        // System.out.println("OUTS");
    }

    // Instead of threading each number to sieve,
    // Why not multithread the counting of the numbers for the sieve
    private static void multiThreadPrimeSieve(int n)
    {
        sieve.set(0, 1);
        sieve.set(1, 1);
        // boolSieve[0] = boolSieve[1] = true;
        long sum = 0;
        int count = 0;

        // ExecutorService service = Executors.newFixedThreadPool(MAX_NUM_THREADS);
        int[] firstEightPrimes = {2, 3, 5, 7, 11, 13, 17, 19};

        // service.submit(new PrimeSieve(2));
        // setSieve(n);
        // multiSieve(n);
        // multiSieve2(n);
        // multiSieve3(n);
        multiSieve4(n);



        // for (int i = 3; i <= n; i += 2)
        // {
        //     service.submit(new PrimeSieve(i));
        // }

        // service.shutdown();

        // try {
        //     service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        // } catch (InterruptedException e) {
        //     System.err.println("Error: " + e);
        // }

        for (int i = 2; i <= n; i++)
        {
            if (sieve.getAcquire(i) == 0)
            {
                sum += i;
                count++;
                // System.out.print(i +", ");
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

class PrimeSieve6 extends Primes
{

}

class PrimeSieve5 extends Primes
{
    public int cur;

    public PrimeSieve5(int th)
    {
        cur = th;
    }

    public void run()
    {
        for (int i = cur; i * i <= UP_TO; i++)
            if (sieve.getAcquire(i) == 0)
                for (int j = i * i; j <= UP_TO; j += i)
                    sieve.getAndSet(j, 1);
    }
}

class PrimeSieve4 extends Primes
{
    public int threadNum;

    public PrimeSieve4(int th)
    {
        threadNum = th;
    }

    public void run()
    {
        for (int i = 2; i * i <= UP_TO; i++)
            if (!boolSieve[i])
                for (int p = i * (threadNum + 1); p <= UP_TO; p += i * MAX_NUM_THREADS)
                    boolSieve[p] = true;
    }
}

class PrimeSieve3 extends Primes
{
    public int threadNum;

    public PrimeSieve3(int th)
    {
        threadNum = th;
    }

    public void run()
    {
        // System.out.println("running" + cur);
        // for (int i = cur; i * i <= UP_TO; i++)
        // if ((sieve.getAcquire(cur) == 0) && (cur * cur <= UP_TO))
        //     for (int j = cur * cur; j <= UP_TO; j += cur)
        //         sieve.set(j, 1);
        // System.out.println("finished " + cur);

        for (int i = 2; i * i <= UP_TO; i++)
            if (sieve.getAcquire(i) == 0)
            {
                totalSum.addAndGet(i);
                counter.incrementAndGet();
                for (int p = i * (threadNum + 1); p <= UP_TO; p += i * MAX_NUM_THREADS)
                    sieve.set(p, 1);
            }
    }
}

class PrimeSieve2 extends Primes
{
    public int threadNum;

    public PrimeSieve2(int th)
    {
        threadNum = th;
    }

    public void run()
    {
        // System.out.println("running" + cur);
        // for (int i = cur; i * i <= UP_TO; i++)
        // if ((sieve.getAcquire(cur) == 0) && (cur * cur <= UP_TO))
        //     for (int j = cur * cur; j <= UP_TO; j += cur)
        //         sieve.set(j, 1);
        // System.out.println("finished " + cur);

        for (int i = 2; i * i <= UP_TO; i++)
            if (sieve.getAcquire(i) == 0)
            {

                for (int p = i * (threadNum + 1); p <= UP_TO; p += i * MAX_NUM_THREADS)
                    sieve.set(p, 1);
            }
    }
}

class PrimeSieve extends Primes
{
    public int cur;
    public int threadNum;

    public PrimeSieve(int i, int j)
    {
        cur = i;
        threadNum = j;
    }

    public void run()
    {
        // System.out.println("running" + cur);
        // for (int i = cur; i * i <= UP_TO; i++)
        // if ((sieve.getAcquire(cur) == 0) && (cur * cur <= UP_TO))
        //     for (int j = cur * cur; j <= UP_TO; j += cur)
        //         sieve.set(j, 1);
        // System.out.println("finished " + cur);

        for (int p = cur * (threadNum + 1); p <= UP_TO; p += cur * MAX_NUM_THREADS)
            sieve.set(p, 1);
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