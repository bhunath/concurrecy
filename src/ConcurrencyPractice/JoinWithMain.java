package ConcurrencyPractice;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JoinWithMain {

    final static Consumer<Thread> joinAllThread = thread1 -> {
        try {
            thread1.join(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };

    public static void main(String[] args)  throws  InterruptedException{

        List<Long> numbers = Arrays.asList(500000L, 500000L);

        final Function<Long, Thread> longThreadFunction = number -> new Thread(new FactorialTask(number));

        final List<Thread> factorizationThreads = numbers.stream()
                .map(longThreadFunction)
                .collect(Collectors.toList());

        final Consumer<Thread> startDaemonThread = thread -> {
            thread.setDaemon(true);
            thread.start();
        };

        factorizationThreads.stream().forEach(startDaemonThread);
        factorizationThreads.stream().forEach(joinAllThread);

        final Consumer<Thread> interruptThreads = thread -> thread.interrupt();
        factorizationThreads.stream().filter(Thread::isAlive).forEach(interruptThreads);

        System.out.println("ConcurrencyPractice.Main is exiting !!!");
    }

    public static class FactorialTask implements Runnable{

        private Long number;

        public FactorialTask(Long number) {
            this.number = number;
        }

        @Override
        public void run() {
            System.out.println("Factorization of " + this.number + " is : "+ factorial(this.number));
        }

        // Even join with Timeout ; when time out Expire main that exits but the Current daemon thread is still running.
        // Hence application is still in running state. To solve this we need to identify the hot code and check interrupted.
        private BigInteger factorial(Long number){
            BigInteger result = BigInteger.ONE;

            for (long i = number; i > 0 ; i--) {
                /*if(Thread.currentThread().isInterrupted()){
                    return BigInteger.ZERO;
                }*/
                result = result.multiply(new BigInteger(Long.toString(number)));
            }
            return result;
        }
    }
}
