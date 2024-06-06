# JavaFlowAPI

The Java 9 Flow API is a part of the java.util.concurrent package, introduced to provide a standard set of interfaces for building reactive streams. It is designed to handle asynchronous data streams and includes built-in support for backpressure, which allows consumers to regulate the rate of data production.

Core Interfaces
The Flow API defines four main interfaces:

Publisher<T>
Subscriber<T>
Subscription
Processor<T, R>
Here's a closer look at each of these interfaces:

1. Publisher<T>
A Publisher is responsible for producing items and sending them to Subscribers. It defines a single method:
public interface Publisher<T> {
    void subscribe(Subscriber<? super T> subscriber);
}

2. Subscriber<T>
A Subscriber consumes items produced by a Publisher. It has four methods to handle different stages of data consumption:
public interface Subscriber<T> {
    void onSubscribe(Subscription subscription);
    void onNext(T item);
    void onError(Throwable throwable);
    void onComplete();
}

3. Subscription
A Subscription represents a link between a Publisher and a Subscriber. It allows the Subscriber to request items and cancel the subscription:
public interface Subscription {
    void request(long n);
    void cancel();
}

4. Processor<T, R>
A Processor combines the roles of both Publisher and Subscriber. It processes incoming items and produces new items:
public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {}
