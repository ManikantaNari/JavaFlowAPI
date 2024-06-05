import java.util.concurrent.Flow.*;
import java.util.concurrent.CountDownLatch;

public class AccountSubscriber<T> implements Subscriber<T> {
    private final String subscriberName;
    private final CountDownLatch latch;
    private Subscription subscription;

    // Constructor that initializes the subscriber name and latch
    public AccountSubscriber(String subscriberName, CountDownLatch latch) {
        this.subscriberName = subscriberName;
        this.latch = latch;
    }

    // Called when the subscriber is subscribed to the publisher
    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1); // Request one item at a time to avoid overwhelming the system
    }

    // Called when the next item is available
    @Override
    public void onNext(T item) {
        try {
            System.out.println(Thread.currentThread().getName() + " >> " + subscriberName + " received item: " + item);
            // Simulate report generation for the account number
            generateReportForAccount(item);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            subscription.request(1); // Request the next item
        }
    }

    // Simulates the report generation process for the account number
    private void generateReportForAccount(T accountNumber) {
        System.out.println(Thread.currentThread().getName() + " >> Generating report for account: " + accountNumber);
        try {
            Thread.sleep(500); // Simulate time taken to generate the report
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " >> Report generated for account: " + accountNumber);
    }

    // Called when an error occurs
    @Override
    public void onError(Throwable throwable) {
        System.out.println("Error while processing the item");
        throwable.printStackTrace();
        latch.countDown(); // Decrement the latch to signal completion
    }

    // Called when all items have been processed
    @Override
    public void onComplete() {
        System.out.println(Thread.currentThread().getName() + " >> " + subscriberName + " has completed");
        latch.countDown(); // Decrement the latch to signal completion
    }
}
