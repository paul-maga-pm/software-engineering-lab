package com.salesagents.business.utils;

import com.salesagents.domain.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ProductObservable {
    private List<ProductObserver> observers = new ArrayList<>();

    public synchronized void addObserver(ProductObserver observer) {
        observers.add(observer);
    }

    public synchronized void removeObserver(ProductObserver observer) {
        observers.remove(observer);
    }

    public synchronized void notifyThatProductWasAdded(Product addedProduct) {
        var threadPool = Executors.newFixedThreadPool(5);
        observers.forEach((observer -> {
            Runnable worker = () -> {
                observer.productWasAdded(addedProduct);
            };
            threadPool.execute(worker);
        }));
    }

    public synchronized void notifyThatProductWasRemoved(String idOfRemovedProduct) {
        var threadPool = Executors.newFixedThreadPool(5);
        observers.forEach((observer -> {
            Runnable worker = () -> {
                observer.productWasRemoved(idOfRemovedProduct);
            };
            threadPool.execute(worker);
        }));
    }

    public synchronized void notifyThatProductWasUpdated(Product newValue) {
        var threadPool = Executors.newFixedThreadPool(5);
        observers.forEach((observer -> {
            Runnable worker = () -> {
                observer.productWasUpdated(newValue);
            };
            threadPool.execute(worker);
        }));
    }
}
