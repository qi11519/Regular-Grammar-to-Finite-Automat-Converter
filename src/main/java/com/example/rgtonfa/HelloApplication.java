package com.example.rgtonfa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        mainScreenController controller = new mainScreenController();
        controller.setPrimaryStage(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainScreen.fxml"));


        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("TOC ASSIGNMENT");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();

    }
}