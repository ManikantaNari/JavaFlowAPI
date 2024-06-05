import java.util.concurrent.SubmissionPublisher;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BasicFlowAPI {
    public static void main(String[] args) {
        // List of alphanumeric account numbers
        List<String> accountNumberList = List.of("Wells123", "Wells234", "Wells345", "Wells456", "Wells567", "Wells678", "Wells789", "Wells890", "Wells901", "Wells012");

        // CountDownLatch to wait for all tasks to complete
        CountDownLatch latch = new CountDownLatch(accountNumberList.size());

        // Create a subscriber with the latch
        AccountSubscriber<String> accountSubscriber = new AccountSubscriber<>("Subscriber1", latch);

        // Create a publisher
        SubmissionPublisher<String> accountPublisher = new SubmissionPublisher<>();

        // Subscribe the subscriber to the publisher
        accountPublisher.subscribe(accountSubscriber);

        // Publish each account number to the subscriber
        accountNumberList.forEach(accountNumber -> {
            try {
                Thread.sleep(900); // Adjusted sleep time for faster processing
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " Publishing item: " + accountNumber);
            accountPublisher.submit(accountNumber);
        });

        // Close the publisher after publishing all items
        accountPublisher.close();

        // Wait for all reports to be generated
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // All reports have been generated
        System.out.println("All account reports have been generated");
    }
}
