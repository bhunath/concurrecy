package ConcurrencyPractice;

import java.math.BigInteger;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class ThreadInterrupt {

    public static void main(String[] args) throws InterruptedException {

        Thread longRunningThread1 = new Thread(new LongRunningThread());
        longRunningThread1.setName("Long Running Thread1");

        Thread longRunningThread2 = new LongRunningThread2();
        longRunningThread2.setName("Long Running Thread2");

        Thread computationThread = new Thread(new ComputationTask(new BigInteger("2"),new BigInteger("10000000000000000000")));
        computationThread.setName("Computation Thread");

        computationThread.setDaemon(true);
        longRunningThread1.start();
        longRunningThread2.start();
        computationThread.start();

        /*longRunningThread1.interrupt();
        Thread.sleep(1000);
        longRunningThread2.interrupt();
        Thread.sleep(10000);
        computationThread.interrupt();*/
        System.out.println("ConcurrencyPractice.Main Thread !!");

    }


    public static class LongRunningThread implements Runnable{

        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(500000);
                } catch (InterruptedException e) {
                    System.out.println("Thread "+ Thread.currentThread().getName()+ " is Interrupted");
                    return;
                }
            }
        }
    }

    // Not a good way yo interrupt as this will make other thread slow
    public static class LongRunningThread2 extends Thread {
        @Override
        public void run() {
            super.run();
            while(!this.isInterrupted()){
               // System.out.println("Thread " + this.getName() + "is not yet Interrupted");
            }
            System.out.println("Thread " + this.getName() + " is Interrupted");
        }
    }


    // Computation Task which can be exhaustive if large numbers are passed.
    public static class ComputationTask implements Runnable {

        private BigInteger base;
        private BigInteger pow;

        public ComputationTask(BigInteger base, BigInteger pow) {
            this.base = base;
            this.pow = pow;
        }

        @Override
        public void run() {
            System.out.println(this.base + " pow " + this.pow + " = " + pow(base,pow));
        }

        // We need to find the hot spot in our code to check if thread is interrupted.
        // in this case pow method is the causing the High CPU so we will check if thread is interrupted
        // on each thread execution.
        private static BigInteger pow(BigInteger base, BigInteger pow){
            BigInteger result = BigInteger.ONE;

            for (BigInteger i = BigInteger.ONE ; i.compareTo(pow) != 0 ; i = i.add(BigInteger.ONE) ) {
                if(Thread.currentThread().isInterrupted()){
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }

            return result;
        }
    }
}
