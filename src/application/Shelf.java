package application;

import javafx.application.Platform;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Shelf extends Thread {

    final Lock accessToMonitor;
    final Condition shelfIsFull;
    final Condition shelfIsEmpty;
    volatile Order[] orders;
    int enter;
    int exit;
    int counterOfOrdersOnShelf;
    int size;

    Random rand = new Random();
    int counterOfFixedOrders;
    int numberOfAllOrders;
    int currentCount;
    GUIController sceneController;

    Shelf(int size, int numOfOrders, GUIController sceneController) throws IOException {

        this.size = size;
        this.orders = new Order[size];

        //Indexes of shelf
        this.enter = 0;
        this.exit = 0;

        //Counters
        this.counterOfOrdersOnShelf = 0;
        this.counterOfFixedOrders = 0;
        this.numberOfAllOrders = numOfOrders;
        this.currentCount = 0;

        //Condition and Locks
        this.accessToMonitor = new ReentrantLock();
        this.shelfIsFull = accessToMonitor.newCondition();
        this.shelfIsEmpty = accessToMonitor.newCondition();

        this.sceneController = sceneController;
    }


    public void addOrder(int count) {

        Order element = new Order(count);
        currentCount = count;

        accessToMonitor.lock();

        try {


            if (counterOfOrdersOnShelf == size) {
                try {
                    shelfIsFull.await();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            orders[enter] = element;
            enter = (enter + 1) % size;

            System.out.println("Reciever: I recived order number : " + element.id);

            counterOfOrdersOnShelf = counterOfOrdersOnShelf + 1;

            Platform.runLater(() -> {
                sceneController.ordersOnShelf.setText(Integer.toString(counterOfOrdersOnShelf));
            });

            Platform.runLater(() -> {
                sceneController.orderNumber.setText(Integer.toString(element.id));
                sceneController.clientData.setText(element.returnNameData());
                sceneController.clientData2.setText(element.returnAddressData());
            });

            shelfIsEmpty.signal();

        } finally {
            accessToMonitor.unlock();
        }
    }

    public Order getOrder(int fixerID) {

        accessToMonitor.lock();
        Order element;

        try {
            if (counterOfOrdersOnShelf == 0) {
                try {
                    shelfIsEmpty.await();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            element = orders[exit];
            exit = (exit + 1) % size;

            counterOfOrdersOnShelf = counterOfOrdersOnShelf - 1;

            Platform.runLater(() -> {
                sceneController.ordersOnShelf.setText(Integer.toString(counterOfOrdersOnShelf));
            });

            shelfIsFull.signal();

        } finally {
            accessToMonitor.unlock();
        }
        return element;
    }

    public void delay(int a, int b) {
        try {
            Thread.sleep(rand.nextInt(a) + b);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void fixCounterIncrement() {
        counterOfFixedOrders++;
        Platform.runLater(() -> {
            sceneController.ordersFixed.setText(Integer.toString(counterOfFixedOrders));
        });
    }
}


