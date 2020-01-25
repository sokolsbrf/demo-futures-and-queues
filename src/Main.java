import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        Waiter waiter = new Waiter();
        Sender sender = new Sender(waiter.getQueue());

        waiter.start();
        sender.start();
    }

    static class Waiter extends Thread {

        private BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);

        public Waiter() {
            setDaemon(true);
        }

        public BlockingQueue<Runnable> getQueue() {
            return queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Runnable task = queue.poll(500, TimeUnit.MILLISECONDS);

                    if (task != null) {
                        System.out.println("Some work!");
                        task.run();
                    } else {
                        System.out.println("Z-z-z-z");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    static class Sender extends Thread {

        private final Queue<Runnable> queue;

        Sender(Queue<Runnable> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            queue.add(() -> {
                System.out.println("First");
            });
            someSleep();

            queue.add(() -> System.out.println("Second"));
            someSleep();

            queue.add(() -> System.out.println("Third"));
        }

        private void someSleep() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
