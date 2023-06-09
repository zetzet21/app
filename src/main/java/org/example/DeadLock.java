package org.example;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeadLock {
    public static void main(String[] args) {
        ArrayList<Transaction> Trans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Trans.add(new Transaction(i));
        }

        Comparator<Transaction> comparator = new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return Trans.indexOf(o1) - Trans.indexOf(o2);
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(() -> handle(Trans.get(0), Trans.get(1), comparator));
        executorService.execute(() -> handle(Trans.get(1), Trans.get(0), comparator));
    }

    public static void handle(Transaction res1, Transaction res2, Comparator<Transaction> comparator) {
        Object lock1 = res1;
        Object lock2 = res2;
        if (comparator.compare(res1, res2) < 0) {
            lock2 = res1;
            lock1 = res2;
        }

        System.out.println(Thread.currentThread().getName() + " Trans 1");
        synchronized (lock1) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (lock2) {
                res1.setValue(res2.getValue());
            }
        }
        System.out.println(Thread.currentThread().getName() + " Trans 2");
    }

    static class Transaction {
        int value;

        public Transaction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
