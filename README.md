# How to Run the Program:
On the command prompt, go to the directory which contains Primes.java, then run:
java Primes.java


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
* <https://stackoverflow.com/questions/877096/how-can-i-pass-a-parameter-to-a-java-thread>
* <https://stackoverflow.com/questions/1250643/how-to-wait-for-all-threads-to-finish-using-executorservice>