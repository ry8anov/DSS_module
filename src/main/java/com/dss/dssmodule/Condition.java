package com.dss.dssmodule;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Condition {

    @FXML
    Label labelCount;

    @FXML
    Button quitButton;

    @FXML
    Button addButton;

    @FXML
    TextField condition;

    @FXML
    AnchorPane conditionAnchor;

    static List<String> conditionList = new ArrayList<>();
    static double[] arrayCondition;

    public double addCondition(List<BlockHierarchy> blocksHierarchy, List<Double> vectorWeight) throws IOException {
        Stage stageCondition = new Stage();
        FXMLLoader loadercondition = new FXMLLoader(getClass().getResource("condition-page.fxml"));
        AnchorPane rootCondition = loadercondition.load();
        stageCondition.setTitle("АПМАИ:Добавление переменных состояния");
        stageCondition.setResizable(false);
        stageCondition.getIcons().add(new Image(getClass().getResource("LOGO.png").toExternalForm()));
        Scene sceneCondition = new Scene(rootCondition, 600, 400);
        sceneCondition.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stageCondition.setScene(sceneCondition);
        stageCondition.showAndWait();
        AssessmentConditionCalculate conditionCalculateFinal = new AssessmentConditionCalculate();
        conditionList.removeAll(conditionList);
        return 0;
    }

    EventHandler<MouseEvent> quitClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            VectorConditionCalculate conditionCalculate = new VectorConditionCalculate();
            Stage closeStage = (Stage) quitButton.getScene().getWindow();
            try {
                arrayCondition = conditionCalculate.calculateMethod(conditionList);
                closeStage.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };

    EventHandler<MouseEvent> addClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            conditionList.add(condition.getText().toString());
            condition.setText("");
            labelCount.setText(String.valueOf(conditionList.size()));
        }
    };

    public void initialize() {
        addButton.setOnMouseClicked(addClick);
        quitButton.setOnMouseClicked(quitClick);

    }
}
