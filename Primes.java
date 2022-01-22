class Primes
{
    public static void main(String[] args)
    {
        System.out.println("Hello");
        findPrimes();
    }

    private static void findPrimes()
    {
        final long startTime = System.currentTimeMillis();
        oneThreadPrime((long)1E8);
        final long endTime = System.currentTimeMillis();

        System.out.println("Total execution time: " + (endTime - startTime));
    }

    private static boolean isPrime(long p)
    {
        for (long i = 3; i <= Math.pow(p, 0.5); i += 2)
            if ((p % i) == 0)
                return false;

        return true;
    }

    private static long oneThreadPrime(long n)
    {
        System.out.println(n);
        
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
}