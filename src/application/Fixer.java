package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

public class Fixer extends Thread {

    int fixerID;
    int repetitionCounter;
    volatile int numOfRepetitions;
    volatile Shelf shelf;

    volatile int initialFixerDelayMin;
    volatile int initialFixerDelayMax;
    Random rand = new Random();

    @FXML
    Circle circle;
    @FXML
    Label fixing;

    public Fixer(int id, int numOfRepetitions, Shelf shelf, Circle circle, Label fixing, int initialFixerDelayMin, int initialFixerDelayMax) {

        this.fixerID = id;
        this.numOfRepetitions = numOfRepetitions;
        this.shelf = shelf;
        this.circle = circle;
        this.fixing = fixing;
        this.repetitionCounter = 0;
        this.initialFixerDelayMax = initialFixerDelayMax;
        this.initialFixerDelayMin = initialFixerDelayMin;
    }

    public void run() {
        fix();
        Platform.runLater(() -> {
            this.fixing.setText("End of Work");
        });
    }

    void takingAnotherOrder() {
        delay(initialFixerDelayMax, initialFixerDelayMin);
    }

    public void delay(int a, int b) {
        try {
            Thread.sleep(rand.nextInt(a - b) + b);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOrderButton() {
        this.numOfRepetitions++;
    }

    public void subtractOrderButton() {
        if (repetitionCounter + 1 <= this.numOfRepetitions) {
            this.numOfRepetitions--;
        }
    }

    public void fixing(int orderID) {

        this.circle.setFill(Color.RED);
        Platform.runLater(() -> {
            this.fixing.setText("Fixing Order: " + orderID);
        });
        delay(initialFixerDelayMax, initialFixerDelayMin);

        this.circle.setFill(Color.GREEN);
        Platform.runLater(() -> {
            this.fixing.setText("Taking Another Order");
        });
    }

    public void fix() {

        while (repetitionCounter < numOfRepetitions) {
            takingAnotherOrder();
            Order order = shelf.getOrder(fixerID);
            fixing(order.id);
            System.out.println("Fixer " + fixerID + ":  I fixed order number :" + order.id);
            shelf.fixCounterIncrement();
            repetitionCounter++;
        }
    }
}
