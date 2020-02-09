package JavaConcurrencyConstructs;

import com.sun.javafx.collections.ImmutableObservableList;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class ForkJoinTest {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        List<Future<BigInteger>> futures = new ArrayList<>();
        final List<BigInteger> bigIntegers = Arrays.asList(new BigInteger("10000"), new BigInteger("100000000000"), new BigInteger("10000"), new BigInteger("10000"));
        bigIntegers.stream().forEach(x->{
            final ForkJoinTask<BigInteger> submit = forkJoinPool.submit(new ComputationalTask(x));
            futures.add(submit);
        });

        while(futures != null && futures.size() != 0 ){
            final Iterator<Future<BigInteger>> iterator = futures.iterator();
            while(iterator.hasNext()){
                Future<BigInteger> future = iterator.next();
                if(future.isDone()){
                    BigInteger result = null;
                    try {
                        result = future.get();
                        iterator.remove();
                        System.out.println(result);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
            /*for (Future<BigInteger> future: futures) {
                if(future.isDone()){
                    BigInteger result = null;
                    try {
                        result = future.get();
                        futures.remove(future);
                        System.out.println(result);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }*/
            /*futures.stream().filter(future -> future!= null && future.isDone()).forEach(future -> {
                BigInteger result = null;
                try {
                    result = future.get();
                    futures.remove(future);
                    System.out.println(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });*/
        }

    }

    public static class ComputationalTask extends RecursiveTask<BigInteger> {

        public BigInteger number;

        public ComputationalTask(BigInteger number) {
            this.number = number;
        }

        @Override
        protected BigInteger compute() {
            BigInteger result = BigInteger.ZERO;
            for(BigInteger start = BigInteger.ONE; start.compareTo(this.number) != 0 ; start = start.add(BigInteger.ONE)){
                result = result.add(start);
            }
            return  result;
        }
    }
}
