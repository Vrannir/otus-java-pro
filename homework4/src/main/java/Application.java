
public class Application {
    public static void main(String[] args) throws Exception {
        AnotherThreadPool pool = new AnotherThreadPool(4);
        for (int i = 0; i < 20; i++) {
            int ctr = i;
            try {
                pool.execute(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(ctr);
                });
            } catch (IllegalStateException e) {
                System.out.println("Pool is shutdown");
            }
        }
        Thread.sleep(3000);
        System.out.println("Shutdown!!!");
        pool.shutdown();

        try {
            pool.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(21);
            });
        } catch (IllegalStateException e) {
            System.out.println("Pool is shutdown");
        }
    }
}
