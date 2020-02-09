<h1>Notes</h1>

<h3>Terminating Thread [Thread.interrupt and Daemon Thread]</h3>
1) In a program if any non Daemon Thread is alive ; Daemon thread will  not terminate until complete.
2) Instead of constantly checking Thread.isInterrupted use Thread.sleep to more optimized Thread Scheduling.
3) Either we need to use a Method that respond to Thread.interrupt signal e.g. Thread.sleep or We need to monitor the Thread.isInterrupted explicitly.
4) If all non Daemon Thread Complete then all Daemon Thread will also Terminate.

<h3>ConcurrencyPractice.ThreadExceptionHandling </h3>
1) Use UncaughtException Handler for clearing Thread Resource.
2) Exception in any Thread will terminate that Thread but will not terminate the main Thread.


<H3> ConcurrencyPractice.JoinWithMain Example </H3>
1) Never rely on Thread's Order of Execution.
2) Always use thread co-ordination.
3) Design code for worst case scenario.
4) Thread may take unreasonably long time so always use Thread.join with Time Limit.





