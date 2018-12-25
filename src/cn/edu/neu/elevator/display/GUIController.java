package cn.edu.neu.elevator.display;

import cn.edu.neu.elevator.external.ElevatorButton;
import cn.edu.neu.elevator.external.Environment;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * GUI controller for elevator simulation
 * No logic of elevator included in this class.
 * Only public view operations provided.
 */
public class GUIController {

    public static GUIController instance;

    public GUIController() {
        instance = this;
    }

    public static GUIController getInstance() {
        return instance;
    }

    /**
     * FXML injection fields
     */
    @FXML
    private TextArea txtAreaLogger;

    @FXML
    private Button btnOpenDoor;

    @FXML
    private Button btnCloseDoor;

    @FXML
    private Button btnBlockDoor;

    @FXML
    private Button btnFloorSubmit;

    @FXML
    private TextField txtFloor;

    @FXML
    private ProgressIndicator progDoorIndicator;

    @FXML
    private ProgressIndicator progRunning;

    @FXML
    private Label txtStatus;

    @FXML
    private Slider sliderFloor;

    @FXML
    private Label txtFloorIndicator;

    @FXML
    private Label txtDoorStatus;

    /**
     * Submit the text to the log area
     * @param text text to show
     */
    public void submitLogText(String text) {
        Platform.runLater(()->txtAreaLogger.appendText(text+"\n"));
    }

    /**
     * set the indicator's floor
     * @param floor
     */
    public void setTxtFloorIndicator(int floor) {
        Platform.runLater(()->txtFloorIndicator.setText(floor+"F"));
    }

    /**
     * Set the elevator status indicator
     * @param isIdle if the elevator is Idle
     * @param status the text status of elevator
     */
    public void setElevatorStatus(boolean isIdle, String status) {
        Platform.runLater(()-> {
            txtStatus.setText(status);
            progRunning.setVisible(!isIdle);
        });
    }

    /**
     * Elevator running delay simulation animation method
     * this method provided a way to increase or decrease the height of the elevator cart
     * @param diff the height to increase or decrease (minus)
     */
    public void adjustSliderHeight(double diff) {
        Platform.runLater(()-> {
            double newValue = sliderFloor.getValue() + diff;
            if (newValue < sliderFloor.getMin()) {
                // too small value, set to minimum
                sliderFloor.setValue(sliderFloor.getMin());
            } else if (newValue > sliderFloor.getMax()) {
                // too large value, set to maximum
                sliderFloor.setValue(sliderFloor.getMax());
            } else {
                // valid value
                sliderFloor.setValue(newValue);
                System.out.println("V=" + newValue);
            }
        });
    }

    public void setSliderHeight(double value){
        Platform.runLater(()->sliderFloor.setValue(value));
    }

    /**
     * Elevator door delay simulation animation
     * @param diff the value to change (<=0: Closed; >=100:Open; otherwise: in the moving progress or blockDoor)
     */
    public void adjustDoorWidth(double diff) {
        Platform.runLater(()-> {
            double newValue = progDoorIndicator.getProgress() + diff;
            if (newValue < 0) {
                progDoorIndicator.setProgress(0);
            } else if (newValue > 100) {
                progDoorIndicator.setProgress(100);
            } else {
                progDoorIndicator.setProgress(newValue);
            }
        });
    }

    /**
     * Set the status of door with a text
     * @param status status text
     */
    public void setDoorStatus(String status) {
        Platform.runLater(()->txtDoorStatus.setText(status));
    }

    // Self action methods

    /**
     * Submit floor method
     * Simulation of pressing on a floor button
     * This method will invoke the press button method in the environment interface
     */
    @FXML
    public void submitFloor(ActionEvent event) {
        // press the button on the environment
        // and environment will forward the request to elevator panel
        Environment.getInstance().pressButton(
                new ElevatorButton(ElevatorButton.ButtonType.FLOOR_BUTTON, Integer.valueOf(txtFloor.getText()))
        );
    }

    /**
     * Forward press openDoor button action to environment interface
     * @param event JavaFX Action Event Injection
     */
    @FXML
    public void onOpenDoorPressed(ActionEvent event) {
        Environment.getInstance().pressButton(new ElevatorButton(ElevatorButton.ButtonType.OPEN_BUTTON));
    }

    /**
     * Forward press closeDoor button action to environment interface
     * @param event JavaFX Action Event Injection
     */
    @FXML
    public void onCloseDoorPressed(ActionEvent event) {
        Environment.getInstance().pressButton(new ElevatorButton(ElevatorButton.ButtonType.CLOSED_BUTTON));
    }

    /**
     * Trigger the sensor in environment to simulation the block of door
     * @param event JavaFX Action Event Injection
     */
    @FXML
    public void onBlockDoorPressed(ActionEvent event) {
        Environment.getInstance().onDoorBlocked();
    }
}
