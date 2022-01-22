# 8-thread-prime-find
Your non-technical manager assigns you the task to find all primes between 1 and 108.  The assumption is that your company is going to use a parallel machine that supports eight concurrent threads. Thus, in your design you should plan to spawn 8 threads that will perform the necessary computation. Your boss does not have a strong technical background but she is a reasonable person. Therefore, she expects to see that the work is distributed such that the computational execution time is approximately equivalent among the threads. Finally, you need to provide a brief summary of your approach and an informal statement reasoning about the correctness and efficiency of your design. Provide a summary of the experimental evaluation of your approach. Remember, that your company cannot afford a supercomputer and rents a machine by the minute, so the longer your program takes, the more it costs. Feel free to use any programming language of your choice that supports multi-threading as long as you provide a ReadMe file with instructions for your manager explaining how to compile and run your program from the command prompt.  

 

# Required Output:

Please print the following output to a file named primes.txt:

<execution time>  <total number of primes found>  <sum of all primes found>

<top ten maximum primes, listed in order from lowest to highest>

 

# Notes on Output:

    Zero and one are neither prime nor composite, so please don't include them in the total number of primes found and the sum of all primes found.
    The execution time should start prior to spawning the threads and end after all threads complete.
