import java.util.LinkedList;

public class AnotherThreadPool {
    private final PoolExecute[] threadPool;
    private final LinkedList<Runnable> taskQueue;
    private final Object monitor;
    private final int threadNum;
    private boolean isShutdown;

    public AnotherThreadPool(int threadNum) {
        this.isShutdown = false;
        this.threadNum = threadNum;
        this.monitor = new Object();
        this.threadPool = new PoolExecute[this.threadNum];
        this.taskQueue = new LinkedList<>();
        for (int i = 0; i < threadNum; i++) {
            threadPool[i] = new PoolExecute();
        }
    }

    public void execute(Runnable r) {
        if (isShutdown) throw new IllegalStateException("Pool is shutdown");
        synchronized (monitor) {
            taskQueue.add(r);
            monitor.notify();
        }
    }

    public void shutdown() {
        isShutdown = true;
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }

    public class PoolExecute extends Thread {

        public PoolExecute() {
            this.start();
        }

        @Override
        public void run() {
            Runnable task;
            while (true) {
                synchronized (monitor) {
                    while (taskQueue.isEmpty()) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        if (isShutdown) {
                            return;
                        }
                    }
                }
                System.out.println(this.getName());
                task = taskQueue.poll();
                if (task != null) task.run();
                if (isShutdown) break;
            }
        }
    }
}
