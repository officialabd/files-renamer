package com.tools;

import com.tools.layouts.HomePage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class View extends Application {

    private static final String TITLE = "File Editor";
    private static final String ICON = "file:src/assets/icons/mainIcon.png";

    @Override
    public void start(Stage stage) {
        var scene = new Scene(new HomePage(640, 480), 640, 480);
        stage.setResizable(false);
        stage.setTitle(TITLE);
        stage.getIcons().add(new Image(ICON));
        stage.setScene(scene);
        stage.show();
    }

    public static void run() {
        launch();
    }

}