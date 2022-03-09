package application;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Application {

    Shelf shelf;
    Receiver receiver;
    Fixer[] fixers = new Fixer[3];

    int addButton;
    GUIController sceneController;

    int initialSizeOfShelf = 0;
    int initialNumberOfOrders = 0;
    volatile int initialFixerDelayMin = 0;
    volatile int initialFixerDelayMax = 0;
    volatile int initialReceiverDelayMin = 0;
    volatile int initialReceiverDelayMax = 0;

    Application(GUIController sceneController) {
        this.sceneController = sceneController;
        this.addButton = 0;
    }

    public void addOrderButtonPressed() throws IOException {

        receiver.addOrderButton();
        Platform.runLater(() -> {
            sceneController.allOrders.setText(Integer.toString(receiver.numberOfOrders));
        });
        fixers[addButton].addOrderButton();
        if (addButton < 2) {
            addButton++;
        } else {
            addButton = 0;
        }
    }

    public void substractOrderButtonPressed() throws IOException {

        receiver.substractOrderButton();
        Platform.runLater(() -> {
            sceneController.allOrders.setText(Integer.toString(receiver.numberOfOrders));
        });

        fixers[addButton].subtractOrderButton();
        if (addButton < 2) {
            addButton++;
        } else {
            addButton = 0;
        }
    }

    public boolean stringIsNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void initializeProcesses() throws IOException {

        int[] fixersRepetitions = new int[3];

        for (int i = 0; i < initialNumberOfOrders; i++) {
            fixersRepetitions[i % 3]++;
        }

        shelf = new Shelf(initialSizeOfShelf, initialNumberOfOrders, sceneController);
        sceneController.translate = new TranslateTransition();

        receiver = new Receiver(1, initialNumberOfOrders, shelf, sceneController.receiverBox, initialReceiverDelayMin, initialReceiverDelayMax);

        Platform.runLater(() -> {
            sceneController.allOrders.setText(Integer.toString(receiver.numberOfOrders));
        });

        fixers = new Fixer[3];

        sceneController.circle1.setFill(Color.GREEN);
        sceneController.circle2.setFill(Color.GREEN);
        sceneController.circle3.setFill(Color.GREEN);

        fixers[0] = new Fixer(0, fixersRepetitions[0], shelf, sceneController.circle1, sceneController.fixer1, initialFixerDelayMin, initialFixerDelayMax);
        fixers[1] = new Fixer(1, fixersRepetitions[1], shelf, sceneController.circle2, sceneController.fixer2, initialFixerDelayMin, initialFixerDelayMax);
        fixers[2] = new Fixer(2, fixersRepetitions[2], shelf, sceneController.circle3, sceneController.fixer3, initialFixerDelayMin, initialFixerDelayMax);

        receiver.start();

        for (int i = 1; i < 4; i++) {
            fixers[i - 1].start();
        }

        try {
            receiver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < 4; i++) {
            try {
                fixers[i - 1].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void initializeProcessesViaNewTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                initializeProcesses();
                return null;
            }
        };
        new Thread(task).start();
    }

    public boolean textFieldDataIsCorrect(String data) {
        if (data.length() != 0 && stringIsNumeric(data)) {
            return Integer.parseInt(data) > 0;
        }
        return false;
    }

    public void startSimulationButtonPressed() throws IOException {

        int size = 0;
        int numberOfOrders = 0;
        int fixerDelayMin = 0;
        int fixerDelayMax = 0;
        int receiverDelayMin = 0;
        int receiverDelayMax = 0;

        //initializing variables from properties file
        try (InputStream input = new FileInputStream("./src/resources/data.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            size = Integer.parseInt(prop.getProperty("shelfSize"));
            numberOfOrders = Integer.parseInt(prop.getProperty("numberOfOrders"));
            fixerDelayMin = Integer.parseInt(prop.getProperty("fixerDelayMin"));
            fixerDelayMax = Integer.parseInt(prop.getProperty("fixerDelayMax"));
            receiverDelayMin = Integer.parseInt(prop.getProperty("receiverDelayMin"));
            receiverDelayMax = Integer.parseInt(prop.getProperty("receiverDelayMax"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        //reading input from GUI textFields
        String textInputShelfSize = sceneController.shelfSize.getText();
        String textInputOrdersNumber = sceneController.ordersNum.getText();

        String textInputFixerDelayMin = sceneController.fixerDelayMin.getText();
        String textInputFixerDelayMax = sceneController.fixerDelayMax.getText();

        String textInputReceiverDelayMin = sceneController.receiverDelayMin.getText();
        String textInputReceiverDelayMax = sceneController.receiverDelayMax.getText();



        //verifying correctness from textFields input
        if (textFieldDataIsCorrect(textInputShelfSize)) {
            size = Integer.parseInt(textInputShelfSize);
        }
        if (textFieldDataIsCorrect(textInputOrdersNumber)) {
            numberOfOrders = Integer.parseInt(textInputOrdersNumber);
        }

        if (textFieldDataIsCorrect(textInputFixerDelayMin)) {
            fixerDelayMin = Integer.parseInt(textInputFixerDelayMin);
        }
        if (textFieldDataIsCorrect(textInputFixerDelayMax)) {
            fixerDelayMax = Integer.parseInt(textInputFixerDelayMax);
        }

        if (textFieldDataIsCorrect(textInputReceiverDelayMin)) {
            receiverDelayMin = Integer.parseInt(textInputReceiverDelayMin);
        }
        if (textFieldDataIsCorrect(textInputReceiverDelayMax)) {
            receiverDelayMax = Integer.parseInt(textInputReceiverDelayMax);
        }

        this.initialSizeOfShelf = size;
        this.initialNumberOfOrders = numberOfOrders;
        this.initialFixerDelayMax = fixerDelayMax;
        this.initialFixerDelayMin = fixerDelayMin;
        this.initialReceiverDelayMin = receiverDelayMin;
        this.initialReceiverDelayMax = receiverDelayMax;

        //initializing fixers and reciever threads
        initializeProcessesViaNewTask();
    }

    public void reactToSliderChange() {

        double temporary = sceneController.slider.valueProperty().getValue();

        if (receiver != null) {
            for (int i = 0; i < 3; i++) {
                fixers[i].initialFixerDelayMax = this.initialFixerDelayMax - ((int) temporary * 500);
                fixers[i].initialFixerDelayMin = fixers[i].initialFixerDelayMax - 500;
            }
            receiver.initialReceiverDelayMax = this.initialReceiverDelayMax - ((int) temporary * 200);
            receiver.initialReceiverDelayMin = receiver.initialReceiverDelayMax - 500;
        }
    }
}
