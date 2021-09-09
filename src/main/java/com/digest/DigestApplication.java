package com.digest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DigestApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DigestApplication.class.getResource("digest-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Формирование digest для запроса createSessionRequest в подсистему Информационное Взаимодействие РСА");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}