import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Bitmaps {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        List<Future<String>> futures = new ArrayList<>(4);

        futures.add(executor.submit(new BitmapGenerator(1000, "Bitmap 1")));
        futures.add(executor.submit(new BitmapGenerator(3000, "Bitmap 2")));
        futures.add(executor.submit(new BitmapGenerator(2000, "Bitmap 3")));
        futures.add(executor.submit(new BitmapGenerator(1500, "Bitmap 4")));
        executor.shutdown();

        for (Future<String> future : futures) {
            try {

                String result = future.get();
                System.out.println(result);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
                executor.shutdownNow();
            }
        }

        System.out.println("Completed!");
    }

    static class BitmapGenerator implements Callable<String> {

        private final long sleepTime;
        private final String bitmapName;

        BitmapGenerator(long sleepTime, String bitmapName) {
            this.sleepTime = sleepTime;
            this.bitmapName = bitmapName;
        }

        @Override
        public String call() throws Exception {
            Thread.sleep(sleepTime);

//            if (bitmapName.contains("2")) {
//                throw new RuntimeException("Bad bitmap");
//            }
            return bitmapName;
        }
    }
}
