package com.dss.dssmodule;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssessmentConditionCalculate {
    private static List<Button> dimensionMatrix = new ArrayList<Button>();
    private static List<Label> labelName = new ArrayList<Label>();
    private static List<Label> labelNameForWeight = new ArrayList<Label>();
    private static List<Label> labelNameClone = new ArrayList<Label>();
    public TextField textFieldCalculateArray[][];
    public static List<BlockHierarchy> blocksHierarchy = new ArrayList<BlockHierarchy>();
    public boolean flag = true;
    public static double IO = 0.0;
    public static List<Double> vectorWeight;
    public static int indexActiveTab;
    public static double[] intermediateConditionArray;
    static double Ob=0;
    Stage stage2 = new Stage();
    int size;
    static double [] arrayCondition;
    static List<String> conditionList;

    @FXML
    Button importanseButtonCalculate;

    @FXML
    GridPane gridResult;

    @FXML
    AnchorPane secondAnchorPane;

    @FXML
    GridPane matrixGridPane = new GridPane();

    @FXML
    Button buttonExit;

}
