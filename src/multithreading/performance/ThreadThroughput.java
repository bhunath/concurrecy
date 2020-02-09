package multithreading.performance;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * With the Concurrent Flow of Independent Task
 * ThroughPut Metric is defined to Complete Maximum Task as fast as possible.
 */
public class ThreadThroughput {
    public static String BOOK_RESOURCE = "./resources/war_and_peace.txt";
    public static int NUMBER_OF_THREADS = 4;

    public static void main(String[] args) throws IOException {
        final String war_and_peace = new String(Files.readAllBytes(Paths.get(BOOK_RESOURCE)));
        startServer(war_and_peace);
    }

    private static void startServer(String war_and_peace) throws IOException {
        HttpServer httpServer;
        httpServer = HttpServer.create(new InetSocketAddress(8000),0);
        httpServer.createContext("/search",new WordCountHandler(war_and_peace));
        final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        httpServer.setExecutor(executorService);
        httpServer.start();
    }

    private static class WordCountHandler implements HttpHandler {
        String war_and_peace = "";
        public WordCountHandler(String war_and_peace) {
            this.war_and_peace = war_and_peace;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            final String queryPath = httpExchange.getRequestURI().getQuery();
            final String[] split = queryPath.split("=");
            String action = split[0];
            String wordToFind = split[1];
            if(action != null && "word".equals(action.toLowerCase())){
                long wordCount = countWord(wordToFind);
                final byte[] responseBytes = Long.toString(wordCount).getBytes();
                httpExchange.sendResponseHeaders(200,responseBytes.length);
                final OutputStream responseBody = httpExchange.getResponseBody();
                try {
                    responseBody.write(responseBytes);
                }finally {
                    responseBody.close();
                }

            }else{
                httpExchange.sendResponseHeaders(503,0);
                httpExchange.close();
                return;
            }
        }

        private long countWord(String wordToFind) {
            int startIndex = 0;
            int count = 0;
            while(startIndex >= 0){
                startIndex = war_and_peace.indexOf(wordToFind, startIndex);
                if(startIndex > 0){
                    count++;
                    startIndex++;
                }
            }
            return count;
        }
    }
}
