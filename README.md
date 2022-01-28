# How to Run the Program:
On the command prompt, go to the directory which contains Primes.java, then run:
`java Primes.java`


# Design Summary:
The program utilizes multiple threads by using the Threads class.
## Informal Statement:
This program utilizes a sieve to keep track of previously known values that are composite. The program distributes the flitering of each number to 8 processors (threads). First we assume all numbers in the range (except 0 and 1) are prime. Then the processors will start with the lowest prime (2), then mark all multiples of it as not prime. When all are marked in the range, we iterate to the next lowest prime (3) and repeat the same process until the sieve is complete.
After, finding the primes are as simple as going through the sieve and collecting which number is marked as prime. This design is correct as it ensures that each thread fills the sieve before continuing with the finding of primes. This design is efficient as it utilizes all 8 threads and schedules unique (unrepeated) tasks for each to complete. Work is distributed evenly by having each thread represent a multiple of a prime and it marks all multiples as not prime.
## Summary and Experiementational Evaulation:
By using the `timeAverage()` in the program, I am able to quickly determine the average time of n runs.
These tests were done on a Lenovo IdeaPad Flex 5 which has a series 4000 Ryzen 7.
### Single-Thread:
This is tested by altering the `MAX_NUM_THREADS` to be 1.
After 100 runs, the average execution time was: `2370`
### Multi-Thread:
This is tested by altering the `MAX_NUM_THREADS` to be 8.
After 100 runs, the average execution time was: `2086`
### Correctness:
As described in the the informal statement. The sieve gets sequentially filled. In order to keep this correct in a multi-threaded approach, the sieve still gets filled sequentially, but the marking of the multiples gets distributed based on the thread's number. So if we are filling the sieve starting with 2, on the first iteration thread-1 will mark 4 as prime, thread-2 will mark 6 as prime, thread-3 will mark 8 as prime, and so on up to thread-8 marking 16 as prime. The second iteration will have thread-1 mark 18 as prime, thread-2 mark 20 as prime, and so on. The iterations will be done when we get past 10<sup>8</sup>. Next we find the next lowest prime (which would be 3) and follow the same process up to the square root of 10<sup>8</sup>.
### Efficiency:
The reason why we can check up to the square root is because the square root will be the largest number necessary to check if it is prime. For example, for the number 36, the factors are 2, 3, 4, 6, 9, 12, and 18; and 6 his the largest necessary number to check up to. This is particularly useful for a number that is the square of a prime. Ex. 25, we check and find that the factor 5 proves it to be composite.
### Evaluation:
Based on the results, we can find by Amdahl's Law that the multi-thread approach is 13.7% concurrent with a 1.134 speed up.



# Design Summary (Version 1):
This was my first method to finding primes. The methods can be found in the `individual-checking` branch. Below, was my analysis for this method.
The program utilizes multiple threads by using the `import java.util.concurrent` library. In particular, the ExecutorService Class.
## Informal Statement:
This program checks every odd number up to 10<sup>8</sup> to see if it is prime. The program distributes the checking of each number to 8 processors (threads). When finding a prime, each thread will update shared counters to reveal the number of primes and the sum of those primes. This design is correct as it ensures that each thread gets a unique task and it updates the counters properly. This design is efficient as it utilizes all 8 threads and schedules the tasks to an available thread immediately.
## Method for Finding Primes:
The main approach for finding primes can be seen in the single-thread function `oneThreadPrime()`. Every iteration of the `for`-loop will increment the next number to check by 2 since every even number except 2 is composite. By starting on 3 and incrementing by 2, this reduces the numbers to check by half. For each iteration, `isPrime()` will be called and checks possible factors upto what is necessary. This algorithm has O($\sqrt{n}$) since it checks up to $\sqrt{n}$ possible factors before determining if it is prime.
## Method for Multi-Threading:
The threads use the same underlying method for finding primes, but we attempt to evenly distribute the amount of work among the threads. This is done by using the `ExecutorService` library. What it does is create a pool of threads assigned to a task when it is first spawned. This library is perfect for this usecase since we have many tasks (each number that needs to be checked) and only 8 available threads. `ExecutorService` handles the assignment of the tasks to threads by using an internal queue. When a thread is finished with their task, it receives a new task from the queue. `ExecutorService` ensures that each thread uniquely gets the next task so that no two thread is working on the same task.
One issue that must be managed is the `OutOfMemoryError` caused by the large allocation of tasks. The way this is solved is by creating a max number of tasks. Then after that pool is complete, we reinitiate `ExecutorService` and continue with the next batch of tasks.
While each thread is working their task, we keep track of how many primes that is countered and the sum of the primes by using atomic variables.
Using Amdahl's and finding the average time of a single-thread vs. multi-threads, it can be determined that this program is 76\~88% concurrent. This is generally good as it means that there was around 3\~4.5 times speedup.


# 8-thread-prime-find
Your non-technical manager assigns you the task to find all primes between 1 and 10<sup>8</sup>.  The assumption is that your company is going to use a parallel machine that supports eight concurrent threads. Thus, in your design you should plan to spawn 8 threads that will perform the necessary computation. Your boss does not have a strong technical background but she is a reasonable person. Therefore, she expects to see that the work is distributed such that the computational execution time is approximately equivalent among the threads.

Finally, you need to provide a brief summary of your approach and an informal statement reasoning about the correctness and efficiency of your design. Provide a summary of the experimental evaluation of your approach. Remember, that your company cannot afford a supercomputer and rents a machine by the minute, so the longer your program takes, the more it costs. Feel free to use any programming language of your choice that supports multi-threading as long as you provide a ReadMe file with instructions for your manager explaining how to compile and run your program from the command prompt.  

 

# Required Output:

Please print the following output to a file named primes.txt:

\<execution time>  \<total number of primes found>  \<sum of all primes found>

\<top ten maximum primes, listed in order from lowest to highest>

 

# Notes on Output:

1. Zero and one are neither prime nor composite, so please don't include them in the total number of primes found and the sum of all primes found.
2. The execution time should start prior to spawning the threads and end after all threads complete.



# Resources Used:
* <https://primes.utm.edu/howmany.html>
* <http://compoasso.free.fr/primelistweb/page/prime/liste_online_en.php>
* <https://stackoverflow.com/questions/17416949/how-to-get-an-integer-value-that-is-greater-than-the-maximum-integer-value>
* <https://stackoverflow.com/questions/21034955/when-to-use-long-vs-long-in-java>
* <https://stackoverflow.com/questions/2572868/how-to-time-java-program-execution-speed>
* <https://stackoverflow.com/questions/1590831/safely-casting-long-to-int-in-java>
* <https://stackoverflow.com/questions/7093186/the-literal-xyz-of-type-int-is-out-of-range/7093207>
* <https://www.w3schools.com/java/java_threads.asp>
* <https://stackoverflow.com/questions/541487/implements-runnable-vs-extends-thread-in-java>
* <https://www.baeldung.com/java-thread-lifecycle>
* <https://www.javatpoint.com/how-to-create-a-thread-in-java>
* <https://stackoverflow.com/questions/9916264/create-a-simple-queue-with-java-threads>
* <https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/Executors.html#newFixedThreadPool(int)>
* <https://www.baeldung.com/java-executor-service-tutorial>
* <https://www.youtube.com/watch?v=6Oo-9Can3H8&list=PLhfHPmPYPPRk6yMrcbfafFGSbE2EPK_A6&index=5>
* <https://www.baeldung.com/java-atomic-variables>
* <https://stackoverflow.com/questions/53964366/whats-the-difference-between-getvolatile-and-getacquire>
* <https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/atomic/AtomicLong.html>
* <https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Executors.html#newFixedThreadPool(int)>
* <https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html>
* <https://www.baeldung.com/java-executor-service-tutorial>
* <https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html#submit(java.lang.Runnable)>
* <http://tutorials.jenkov.com/java-util-concurrent/executorservice.html>
* <https://stackoverflow.com/questions/877096/how-can-i-pass-a-parameter-to-a-java-thread>
* <https://stackoverflow.com/questions/1250643/how-to-wait-for-all-threads-to-finish-using-executorservice>
* <https://stackoverflow.com/questions/3269445/executorservice-how-to-wait-for-all-tasks-to-finish>
* <https://stackoverflow.com/questions/8183205/what-could-be-the-cause-of-rejectedexecutionexception/8183463>
* <https://www.baeldung.com/java-queue-linkedblocking-concurrentlinked#concurrentlinkedqueue>
* <https://stackoverflow.com/questions/28235693/java-concurrentlinkeddeque-vs-concurrentlinkedqueue-the-difference>
* <https://www.geeksforgeeks.org/queue-interface-java/>
* <https://stackoverflow.com/questions/4021855/preventing-concurrentlinkedqueuet-from-outofmemory-exception>
* <https://stackoverflow.com/questions/1426754/linkedblockingqueue-vs-concurrentlinkedqueue>
* <https://stackoverflow.com/questions/13398212/can-i-use-priorityblockingqueue-with-multiple-threads>
* <https://stackoverflow.com/questions/5695017/priorityqueue-not-sorting-on-add>

Also Checkout the `commentedOut` and `sieve` branch to see other ideas and process tried.
