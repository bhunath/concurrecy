package JavaConcurrencyConstructs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CompletableFutureExample {

    public static void main(String[] args) throws InterruptedException {
        final List<BigInteger> bigIntegers = Arrays.asList(BigInteger.TEN, new BigInteger("100000"), new BigInteger("10000000000"));
        final List<CompletableFuture<List<BigInteger>>> completableFutures = new ArrayList<>();
        bigIntegers.stream().forEach(bigInteger -> {
            CompletableFuture.supplyAsync(()->calculateFactors(bigInteger)).thenApplyAsync(x->{
                System.out.println(x);
                return x;
            }).thenRun(()-> System.out.println("Complete"));
        });
        // Need to stop the main thread to get the output will look into it.
        //Thread.sleep(10000);

    }

    public static List<BigInteger> calculateFactors(BigInteger number)  {
        List<BigInteger> collectFactors = new ArrayList<>();
        final BigInteger divideBy = number.divide(new BigInteger("2"));
        for (BigInteger start = BigInteger.ONE; start.compareTo(divideBy) != 0  ; start = start.add(BigInteger.ONE)){
            //System.out.println("Running Task" + start);
            if(number.mod(start) == BigInteger.ZERO){
                collectFactors.add(start);
            }
        }
        return  collectFactors;
    }
}
