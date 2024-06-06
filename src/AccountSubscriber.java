import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

class AccountSubscriber<T> implements Flow.Subscriber<T> {
    private final String subscriberName;
    private final CompletableFuture<Void> future;
    private final ExecutorService executorService;
    private Flow.Subscription subscription;

    public AccountSubscriber(String subscriberName, CompletableFuture<Void> future) {
        this.subscriberName = subscriberName;
        this.future = future;
        this.executorService = Executors.newFixedThreadPool(10); // Adjust based on your requirements
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1); // Requesting one item at a time initially
    }

    @Override
    public void onNext(T item) {
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " >> " + subscriberName + " received item: " + item);
                // Simulate report generation for the account number
                generateReportForAccount(item);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, executorService);

        // Request the next item
        subscription.request(1);
    }

    private void generateReportForAccount(T accountNumber) {
        // Simulate a longer processing time for Wells345
        long sleepTime = "Wells345".equals(accountNumber) ? 15000 : 1500;
        System.out.println(Thread.currentThread().getName() + " >> Generating report for account: " + accountNumber);

        try {
            Thread.sleep(sleepTime); // Simulate time taken to generate the report
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " >> Report generated for account: " + accountNumber + " (sleepTime: " + sleepTime + " ms)");
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Error while processing the item");
        throwable.printStackTrace();
        future.completeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        System.out.println(Thread.currentThread().getName() + " >> " + subscriberName + " has completed");
        future.complete(null);
    }
}