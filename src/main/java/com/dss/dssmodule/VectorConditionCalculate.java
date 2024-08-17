package com.dss.dssmodule;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import java.util.concurrent.CompletableFuture;

public class VectorConditionCalculate {
    private static List<String> dimensionMatrix = new ArrayList<String>();
    private static List<Label> labelName=new ArrayList<Label>();
    private static List<Label> labelNameForWeight=new ArrayList<Label>();
    private static List<Label> labelNameClone=new ArrayList<Label>();
    public TextField textFieldCalculateArray[][];
    public static List<String> blocksHierarchy = new ArrayList<String>();
    public boolean flag=true;
    public static double IO=0.0;
    public static double vectorWeight[];
    Stage stage2 = new Stage();
    int size;
    @FXML
    GridPane matrixGridPane = new GridPane();
    @FXML
    Button importanseButtonCalculate;
    @FXML
    GridPane gridResult;
    @FXML
    AnchorPane secondAnchorPane;
    @FXML
    Button buttonExit;

    private CompletableFuture<Double> resultFuture;
    public double[] calculateMethod(List<String> blocksHierarchy) throws IOException {
        this.blocksHierarchy = blocksHierarchy;
        FXMLLoader loaderCalculate = new FXMLLoader(getClass().getResource("condition-calculate-page.fxml"));
        AnchorPane rootCalculate = loaderCalculate.load();
        stage2.setTitle("АПМАИ:Расчет важности");
        stage2.setResizable(false);
        stage2.getIcons().add(new Image(getClass().getResource("LOGO.png").toExternalForm()));
        Scene sceneCalculate = new Scene(rootCalculate, 1030, 635);
        sceneCalculate.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage2.setScene(sceneCalculate);
        stage2.showAndWait();
        return vectorWeight;
    }

    public void paintMethod(){
        if (labelName.size() != 0){
            labelName.removeAll(labelName);
            labelNameClone.removeAll(labelNameClone);
            dimensionMatrix.removeAll(dimensionMatrix);
            labelNameForWeight.removeAll(labelNameForWeight);
        }
        for (String str : this.blocksHierarchy) {
            size++;
            dimensionMatrix.add(str);
            labelName.add(new Label(str));
            labelNameClone.add(new Label(str));
            labelNameForWeight.add(new Label(str));
        }

        for (int i = 0; i < labelName.size(); i++){
            labelName.get(i).getStyleClass().add("text-gridpane");
            labelNameClone.get(i).getStyleClass().add("text-gridpane");
            labelNameForWeight.get(i).getStyleClass().add("text-gridpane");
        }

        textFieldCalculateArray = new TextField[dimensionMatrix.size()][dimensionMatrix.size()];
        for (int i = 0; i < dimensionMatrix.size() + 1; i++) {
            for (int j = 0; j < dimensionMatrix.size() + 1; j++) {

                if (i == 0 & j == 0) continue;
                if (i == 0) {
                    labelName.get(j - 1).setStyle("-fx-padding: 0 5 0 5;");
                    matrixGridPane.add(labelName.get(j - 1), i, j);
                } else if (j == 0) {
                    labelName.get(i - 1).setStyle("-fx-padding: 0 5 0 5;");
                    matrixGridPane.add(labelNameClone.get(i - 1), i, j);
                } else {
                    textFieldCalculateArray[i - 1][j - 1] = new TextField();
                    matrixGridPane.add(textFieldCalculateArray[i - 1][j - 1], i, j);
                }
            }
        }
    }

    EventHandler<MouseEvent> calculateClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            gridResult.getChildren().clear();
            double lyamdaMax = 0.0;
            double sumLaymda = 0.0;
            IO=0.0;
            final double MIOCONST[] = {0.00, 0.00, 0.58, 0.90, 1.12};
            double OO = 0.0;
            double ProizVectorLine = 1;
            double sumVectorPriority = 0;
            double GeometryMean[] = new double[dimensionMatrix.size()];
            vectorWeight = new double[dimensionMatrix.size()];
            double MatrixDouble[][] = new double[dimensionMatrix.size()][dimensionMatrix.size()];
            boolean flagTry=true;
            for (int i = 0; i < dimensionMatrix.size(); i++) {
                for (int j = 0; j < dimensionMatrix.size(); j++) {
                    try {
                        MatrixDouble[i][j] = Double.parseDouble(textFieldCalculateArray[i][j].getText());
                    } catch (NumberFormatException e) {
                        int number = textFieldCalculateArray[i][j].getText().indexOf("/");
                        if (number == -1){
                            number = textFieldCalculateArray[i][j].getText().indexOf(",");
                            flagTry=false;
                        }
                        try {
                            if(flagTry) {
                                MatrixDouble[i][j] = Double.parseDouble(textFieldCalculateArray[i][j].getText()
                                        .substring(0, number)) / Double.parseDouble(textFieldCalculateArray[i][j].getText()
                                        .substring(number + 1, ((String) textFieldCalculateArray[i][j].getText()).length()));
                            } else{
                                MatrixDouble[i][j]=Double.parseDouble((String) (textFieldCalculateArray[i][j].getText()
                                        .substring(0, number) +"."+ textFieldCalculateArray[i][j].getText()
                                        .substring(number + 1, ((String) textFieldCalculateArray[i][j].getText()).length())));
                            }

                        } catch (StringIndexOutOfBoundsException exc) {
                            System.out.println("Неправильный тип данных");

                        }
                    }
                }
            }

            for (int i = 0; i < GeometryMean.length; i++) {
                for (int j = 0; j < GeometryMean.length; j++) {
                    ProizVectorLine *= MatrixDouble[j][i];
                }

                GeometryMean[i] =Math.pow(ProizVectorLine, ((double)1/GeometryMean.length));
                sumVectorPriority+=GeometryMean[i];
                ProizVectorLine = 1;

            }

            for (int i=0; i<GeometryMean.length; i++){
                vectorWeight[i]=GeometryMean[i]/sumVectorPriority;
            }

            for(int i=0; i<labelName.size();i++){
                for (int j=0;j<labelName.size();j++){
                    sumLaymda +=MatrixDouble[j][i]*vectorWeight[j];
                }
                lyamdaMax+=sumLaymda;
                sumLaymda=0;
            }
            for (int i = 0; i < labelName.size(); i++) {
                gridResult.add(labelNameForWeight.get(i), 0, i);
                Label label = new Label(Double.toString(Math.round(vectorWeight[i]*1000)/1000.0));
                label.getStyleClass().add("text-gridpane");
                gridResult.add(label, 1, i);
            }
            IO=(lyamdaMax-labelName.size())/(labelName.size()-1);
            if(labelName.size()<3 | labelName.size()>=6) {
                OO = 0;
            }
            else{
                OO=IO/MIOCONST[labelName.size()-1];
            }
            lyamdaMax=Math.round(lyamdaMax*1000)/1000.0;
            IO=Math.round(IO*1000)/1000.0;
            OO=Math.round(OO*1000)/1000.0;
            Label label = new Label("λmax");
            label.getStyleClass().add("text-gridpane");
            gridResult.add(label, 0, labelName.size());
            label = new Label(Double.toString(lyamdaMax));
            label.getStyleClass().add("text-gridpane");
            gridResult.add(label, 1,labelName.size());
            label =new Label("ИО");
            label.getStyleClass().add("text-gridpane");
            gridResult.add(label, 0, labelName.size()+1);
            label = new Label(Double.toString(IO));
            label.getStyleClass().add("text-gridpane");
            gridResult.add(label, 1, labelName.size()+1);
            label=new Label("OO");
            label.getStyleClass().add("text-gridpane");
            gridResult.add(label, 0, labelName.size()+2);
            label=new Label(Double.toString(OO));
            label.getStyleClass().add("text-gridpane");
            gridResult.add(label, 1, labelName.size()+2);

            if (IO>0.10){
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Error");
                alertError.setHeaderText("Значение ОО превышает 0.1");
                alertError.setContentText("Сделайте перерасчет");
                alertError.showAndWait();
            }
        }
    };

    EventHandler<WindowEvent> exit = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent windowEvent) {
            matrixGridPane.getChildren().clear();
        }
    };

    EventHandler<MouseEvent> exitClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            matrixGridPane.getChildren().clear();
            Stage stage = (Stage) buttonExit.getScene().getWindow();
            stage.close();
        }
    };

    @FXML
    public void initialize() throws IOException {
        size = 0;
        stage2.setOnCloseRequest(exit);
        secondAnchorPane.viewOrderProperty().set(1);
        buttonExit.setOnMouseClicked(exitClick);
        paintMethod();
        importanseButtonCalculate.setOnMouseClicked(calculateClick);
        size = 0;
        gridResult.getChildren().remove(labelName);
        gridResult.getChildren().remove(labelNameClone);
    }

}
