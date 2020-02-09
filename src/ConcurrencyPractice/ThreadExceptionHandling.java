package ConcurrencyPractice;

public class ThreadExceptionHandling {

    public static void main(String[] args) throws InterruptedException {
        Thread exceptionThrowingThread = new Thread(new ExceptionTask());
        exceptionThrowingThread.setName("ExceptionThread");

        exceptionThrowingThread.start();
        exceptionThrowingThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(t.getName() + " : Exception Caught !!");
            }
        });
        Thread.sleep(10000);
        System.out.println("Exiting ConcurrencyPractice.Main Thread!!");
    }

    public static class ExceptionTask implements Runnable{

        @Override
        public void run() {
            throw new RuntimeException("Intentional Exception");
        }
    }
}
