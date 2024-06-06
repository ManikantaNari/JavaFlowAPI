import java.util.concurrent.*;
import java.util.List;

public class BasicFlowAPI {
    public static void main(String[] args) {
        List<String> accountNumberList = List.of("Wells123", "Wells234", "Wells345", "Wells456", "Wells567",
                "Wells678", "Wells789", "Wells890", "Wells901", "Wells012");

        CompletableFuture<Void> future = new CompletableFuture<>();

        AccountSubscriber<String> accountSubscriber = new AccountSubscriber<>("Subscriber1",
                accountNumberList.size(), future);

        try (SubmissionPublisher<String> accountPublisher = new SubmissionPublisher<>()) {
            accountPublisher.subscribe(accountSubscriber);

            for (String accountNumber : accountNumberList) {
                System.out.println(Thread.currentThread().getName() + " Publishing item: " + accountNumber);
                accountPublisher.submit(accountNumber);
            }

            // Wait for the future to complete
            future.join();
        }
    }
}


