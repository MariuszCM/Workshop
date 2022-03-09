package application;

import javafx.animation.ScaleTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;

public class Receiver extends Thread {

    Random rand = new Random();
    int id;
    int count;

    volatile int numberOfOrders;
    volatile Shelf shelf;
    GUIController sc;

    volatile int initialReceiverDelayMin;
    volatile int initialReceiverDelayMax;

    public Receiver(int id, int numberOfOrders, Shelf shelf, ImageView receiverBox, int initialReceiverDelayMin, int initialReceiverDelayMax) {

        this.id = id;
        this.numberOfOrders = numberOfOrders;
        this.shelf = shelf;
        this.count = 1;
        this.sc = sc;
        this.initialReceiverDelayMax = initialReceiverDelayMax;
        this.initialReceiverDelayMin = initialReceiverDelayMin;

    }

    public void addOrderButton() {
        numberOfOrders++;
    }

    public void substractOrderButton() {
        if (count + 1 <= numberOfOrders) {
            numberOfOrders--;
        }
    }

    void waitingForOrder() {
        delay(initialReceiverDelayMax, initialReceiverDelayMin);
    }

    public void delay(int a, int b) {
        try {
            Thread.sleep(rand.nextInt(a - b) + b);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void recieve() {

        count = 1;
        while (count <= numberOfOrders) {
            waitingForOrder();
            shelf.addOrder(count);
            count++;
        }
    }

    public void run() {
        recieve();
    }
}
