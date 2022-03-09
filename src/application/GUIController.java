package application;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GUIController implements Initializable {

    @FXML
    public TextField shelfSize;
    @FXML
    public TextField ordersNum;
    @FXML
    public TextField fixerDelayMin;
    @FXML
    public TextField fixerDelayMax;
    @FXML
    public TextField receiverDelayMin;
    @FXML
    public TextField receiverDelayMax;
    @FXML
    public Label orderNumber;
    @FXML
    public Label ordersOnShelf;
    @FXML
    public Label ordersFixed;

    @FXML
    public Circle circle1;
    @FXML
    public Circle circle2;
    @FXML
    public Circle circle3;

    @FXML
    public Label fixer1;
    @FXML
    public Label fixer2;
    @FXML
    public Label fixer3;
    @FXML
    public Label receiverSliderLabel;
    @FXML
    public Label allOrders;
    @FXML
    public Label clientData;
    @FXML
    public Label clientData2;
    @FXML
    public ImageView receiverBox;
    @FXML
    public TranslateTransition translate = new TranslateTransition();

    @FXML
    public Slider slider;

    Application graphicsPanel = new Application(this);

    public void addOrderButtonPressed(ActionEvent event) throws IOException {
        graphicsPanel.addOrderButtonPressed();
    }

    public void subtractOrderButtonPressed(ActionEvent event) throws IOException {
        graphicsPanel.substractOrderButtonPressed();
    }

    public void startSimulationButtonPressed(ActionEvent event) throws IOException {
        graphicsPanel.startSimulationButtonPressed();
    }

    public void initializeSliderListener(Slider slider, Label label) {
        try {
            slider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(
                        ObservableValue<? extends Number> observableValue,
                        Number oldValue,
                        Number newValue) {
                    label.textProperty().setValue(String.valueOf(newValue.intValue()));
                    graphicsPanel.reactToSliderChange();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSliderListener(slider, receiverSliderLabel);
        Image image = new Image("pictures/box.png");
        ImageView imageView = new ImageView(image);
        imageView.setX(100);
        imageView.setY(100);
        imageView.setVisible(true);

    }
}
