package com.example.rgtonfa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private Button btnEpsilon;

    @FXML
    private Button btnArrow;

    @FXML
    private TextField rgArea;

    @FXML
    void onEpsilonButtonClick(ActionEvent event) {
            rgArea.appendText("ε");

    }

    @FXML
    void onArrowButtonClick(ActionEvent event) {
            rgArea.appendText("→");
    }
}
