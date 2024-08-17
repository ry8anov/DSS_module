package com.dss.dssmodule;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainPageController {

    static int countBlocks = 0;
    static int currentSelectedBlocks = 0;

    public static List<BlockHierarchy> selectedButtons = new ArrayList<>();
    public static List<BlockHierarchy> blocksHierarchy = new ArrayList<>();
    public static List<Text> textElements = new ArrayList<>();
    public static List<Arrow> arrows = new ArrayList<>();
    public static List<Weight> weights = new ArrayList<>();
    public static BlockHierarchy currentBlockHierarchy;
    public double vectorWeight[];
    public List <Double> ListWeight = new ArrayList<>();
    public Double Ob1;
    public Double Ob3;

    @FXML
    private ImageView iconApp;

    @FXML
    private Button buttonAddElement;

    @FXML
    private Button buttonAddText;

    @FXML
    private AnchorPane hierarchyPanel;

    @FXML
    private AnchorPane mainScene;

    @FXML
    private Pane toolBar;

    @FXML
    private ScrollPane infoPane;

    @FXML
    private Accordion infoElements;

    @FXML
    private TabPane processPane;

    @FXML
    private Button weightButtonCalculate;

    @FXML
    private Button buttonCalculate;


    final EventHandler<ActionEvent> addBlockHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            BlockHierarchy currentBlockHierarchy = createAndInputBlock();
            Pane currentPane = (Pane) processPane.getSelectionModel().getSelectedItem().getContent();
            blocksHierarchy.add(currentBlockHierarchy);
            currentBlockHierarchy.setLayoutX(currentPane.getWidth() / 2);
            currentBlockHierarchy.setLayoutY(currentPane.getHeight() / 2);
            currentBlockHierarchy.setOnMouseDragged(mouseMoveBlockHandler);
            currentBlockHierarchy.setOnMouseClicked(mouseSelectBlockHandler);
            Weight weight = new Weight(currentBlockHierarchy.getLayoutX(),
                                       currentBlockHierarchy.getLayoutY());
            weights.add(weight);
            currentPane.getChildren().addAll(currentBlockHierarchy, weight, weight.text);
            addInfoBlocks(currentBlockHierarchy);
        }
    };

    final EventHandler<ActionEvent> addTextHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            Text currentText = createAndInputText();
            Pane currentPane = (Pane) processPane.getSelectionModel().getSelectedItem().getContent();
            textElements.add(currentText);
            currentText.setLayoutX(currentPane.getWidth() / 2);
            currentText.setLayoutY(currentPane.getHeight() / 2);
            currentText.setOnMouseDragged(mouseMoveTextHandler);
            currentPane.getChildren().add(currentText);
        }
    };

    final EventHandler<MouseEvent> mouseMoveBlockHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            BlockHierarchy currentBlockHierarchy = (BlockHierarchy) mouseEvent.getSource();
            if(mouseEvent.getButton() == MouseButton.SECONDARY){
                currentBlockHierarchy.setLayoutX(mouseEvent.getSceneX() - currentBlockHierarchy.getWidth() * 3.5);
                currentBlockHierarchy.setLayoutY(mouseEvent.getSceneY() - currentBlockHierarchy.getHeight() * 1.5);
                updateWeight(currentBlockHierarchy);
                for (BlockHierarchy blockHierarchy : currentBlockHierarchy.relatedBLocks) {
                    updateArrow(currentBlockHierarchy, blockHierarchy);
                }
            }
        }
    };

    final EventHandler<MouseEvent> mouseSelectBlockHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                currentBlockHierarchy = (BlockHierarchy) mouseEvent.getSource();
                Pane currentPane = (Pane) processPane.getSelectionModel().getSelectedItem().getContent();
                currentBlockHierarchy.setOnKeyPressed(selectOnCloseKeyPressed);
                selectedButtons.add(currentBlockHierarchy);
                currentSelectedBlocks++;
                if (currentSelectedBlocks % 2 == 0 && currentSelectedBlocks != 0) {
                    BlockHierarchy preBlockHierarchy = selectedButtons.get(currentSelectedBlocks - 2);
                    if(currentBlockHierarchy.equals(preBlockHierarchy)) {
                        return;
                    }
                    preBlockHierarchy.relatedBLocks.add(currentBlockHierarchy);
                    currentBlockHierarchy.relatedBLocks.add(preBlockHierarchy);
                    double startX, startY, endX, endY;
                    boolean isStart = true;
                    if(compareHeight(preBlockHierarchy, currentBlockHierarchy)){
                        startX = preBlockHierarchy.getBoundsInParent().getCenterX();
                        startY = preBlockHierarchy.getBoundsInParent().getCenterY() + preBlockHierarchy.getHeight() / 2;
                        endX = currentBlockHierarchy.getBoundsInParent().getCenterX();
                        endY = currentBlockHierarchy.getBoundsInParent().getCenterY() - currentBlockHierarchy.getHeight() / 2;
                    } else {
                        isStart = false;
                        startX = currentBlockHierarchy.getBoundsInParent().getCenterX();
                        startY = currentBlockHierarchy.getBoundsInParent().getCenterY() + currentBlockHierarchy.getHeight() / 2;
                        endX = preBlockHierarchy.getBoundsInParent().getCenterX();
                        endY = preBlockHierarchy.getBoundsInParent().getCenterY() - preBlockHierarchy.getHeight() / 2;
                    }
                    if (disableOrEnablePaintArrow()) {
                        Arrow arrow = new Arrow(startX, startY, endX, endY);
                        arrow.setOnMouseClicked(mouseCancelArrowHandler);
                        if (isStart) {
                            arrow.start = preBlockHierarchy;
                            arrow.end = currentBlockHierarchy;
                        } else {
                            arrow.start = currentBlockHierarchy;
                            arrow.end = preBlockHierarchy;
                        }
                        currentPane.getChildren().add(arrow);
                        arrows.add(arrow);
                    }
                }
            }
        }
    };

    final EventHandler<MouseEvent> mouseCancelArrowHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Pane currentPane = (Pane) processPane.getSelectionModel().getSelectedItem().getContent();
            Arrow currentArrow = (Arrow) mouseEvent.getSource();
            BlockHierarchy startElement = currentArrow.start;
            BlockHierarchy endElement = currentArrow.end;
            startElement.relatedBLocks.remove(endElement);
            endElement.relatedBLocks.remove(startElement);
            arrows.remove(currentArrow);
            currentPane.getChildren().remove(currentArrow);
        }
    };

    final EventHandler<MouseEvent> mouseMoveTextHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Text currentText = (Text) mouseEvent.getSource();
            if(mouseEvent.getButton() == MouseButton.SECONDARY){
                currentText.setLayoutX(mouseEvent.getSceneX() - 360);
                currentText.setLayoutY(mouseEvent.getSceneY() - 70);
            }
        }
    };

    final EventHandler<MouseEvent> changeProcessPaneHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            int indexCurrentPane = processPane.getSelectionModel().getSelectedIndex();
            infoElements.getPanes().removeAll(infoElements.getPanes());
            for(BlockHierarchy blockHierarchy : blocksHierarchy) {
                if(blockHierarchy.ID_PANE == indexCurrentPane) addInfoBlocks(blockHierarchy);
            }
        }
    };

    final EventHandler<KeyEvent> selectOnCloseKeyPressed = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode() == KeyCode.L) {
                for (Button button : blocksHierarchy) {
                    button.setStyle("");
                }
            }
            else if (keyEvent.getCode() == KeyCode.ALT){
                currentBlockHierarchy.setStyle("-fx-background-color: red;");
            }
        }
    };

    final EventHandler<MouseEvent> weightClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            DirectCalculate directCalculate = new DirectCalculate();
            try {
                vectorWeight = directCalculate.calculateMethod(blocksHierarchy, processPane.getSelectionModel().getSelectedIndex());
                setCalculateWeight(vectorWeight);
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }
    };

    final EventHandler<MouseEvent> conditionClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Condition condition = new Condition();
            for (BlockHierarchy block: blocksHierarchy){
                if (block.getStyle() == "-fx-background-color: red;"
                        && block.ID_PANE == processPane.getSelectionModel().getSelectedIndex()){
                    ListWeight.add(formatNumberMethod(block));
                    System.out.println(ListWeight.get(ListWeight.indexOf(formatNumberMethod(block))));
                }
            }
            try {
                if (processPane.getSelectionModel().getSelectedIndex()==0)
                    Ob1=condition.addCondition(blocksHierarchy, ListWeight);
                else Ob3=condition.addCondition(blocksHierarchy, ListWeight);
                negativeOrPositiveOb();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ListWeight.removeAll(ListWeight);
            System.out.println(Ob1);
        }
    };

    boolean compareHeight(BlockHierarchy block1, BlockHierarchy block2){
        return block1.getBoundsInParent().getCenterY() < block2.getBoundsInParent().getCenterY();
    }

    BlockHierarchy createAndInputBlock() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Введите название блока");
        textInputDialog.getDialogPane().setContentText("Название:");
        Optional<String> result = textInputDialog.showAndWait();
        BlockHierarchy blockHierarchy = new BlockHierarchy(result.get());
        blockHierarchy.ID = countBlocks++;
        blockHierarchy.ID_PANE = processPane.getSelectionModel().getSelectedIndex();
        return blockHierarchy;
    }

    Text createAndInputText() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Введите текст");
        textInputDialog.getDialogPane().setContentText("Введите текст:");
        Optional<String> result = textInputDialog.showAndWait();
        Text text = new Text(result.get());
        text.setCursor(Cursor.MOVE);
        text.getStyleClass().add("text-on-panel");
        return text;
    }

    void updateArrow(BlockHierarchy v1, BlockHierarchy v2) {
        Arrow currentArrow = new Arrow(1, 1 , 1, 1);
        Pane currentPane = (Pane) processPane.getSelectionModel().getSelectedItem().getContent();
        double startX, startY, endX, endY;
        for(Arrow arrow : arrows){
            if(arrow.start == v1 && arrow.end == v2 || arrow.start == v2 && arrow.end == v1){
                currentArrow = arrow;
                break;
            }
        }
        arrows.remove(currentArrow);
        currentPane.getChildren().remove(currentArrow);
        if(compareHeight(v1, v2)) {
            startX = v1.getBoundsInParent().getCenterX();
            startY = v1.getBoundsInParent().getCenterY() + v1.getHeight() / 2;
            endX = v2.getBoundsInParent().getCenterX();
            endY = v2.getBoundsInParent().getCenterY() - v2.getHeight() / 2;
            Arrow newArrow = new Arrow(startX, startY, endX, endY);
            newArrow.setOnMouseClicked(mouseCancelArrowHandler);
            newArrow.start = v1;
            newArrow.end = v2;
            currentPane.getChildren().add(newArrow);
            arrows.add(newArrow);
        } else {
            startX = v1.getBoundsInParent().getCenterX();
            startY = v1.getBoundsInParent().getCenterY() - v1.getHeight() / 2;
            endX = v2.getBoundsInParent().getCenterX();
            endY = v2.getBoundsInParent().getCenterY() + v2.getHeight() / 2;
            Arrow newArrow = new Arrow(startX, startY, endX, endY);
            newArrow.setOnMouseClicked(mouseCancelArrowHandler);
            newArrow.start = v1;
            newArrow.end = v2;
            currentPane.getChildren().add(newArrow);
            arrows.add(newArrow);
        }
    }

    void updateWeight(BlockHierarchy blockHierarchy) {
        Weight currentWeight = weights.get(blocksHierarchy.indexOf(blockHierarchy));
        Pane currentPane = (Pane) processPane.getSelectionModel().getSelectedItem().getContent();
        currentPane.getChildren().remove(currentWeight);
        currentPane.getChildren().remove(currentWeight.text);
        weights.remove(currentWeight);
        Weight newWeight = new Weight(blockHierarchy.getLayoutX(), blockHierarchy.getLayoutY());
        newWeight.text.setText(currentWeight.text.getText());
        currentPane.getChildren().addAll(newWeight, newWeight.text);
        weights.add(blocksHierarchy.indexOf(blockHierarchy), newWeight);
    }

    void addInfoBlocks(BlockHierarchy blockHierarchy){
        TitledPane titledPane = new TitledPane();
        titledPane.setText(blockHierarchy.ID_PANE  + " : " + blockHierarchy.getText());
        Pane pane = new Pane();
        pane.setMinHeight(180);
        pane.setMinWidth(100);
        // Редактор веса
        Label weightLabel = new Label("w: ");
        weightLabel.setLayoutX(10);
        weightLabel.setLayoutY(15);
        weightLabel.setStyle("-fx-font-weight: bold");
        TextField weightTextField = new TextField("");
        weightTextField.setLayoutX(30);
        weightTextField.setLayoutY(10);
        pane.getChildren().addAll(weightLabel, weightTextField);
        // Редактор названия
        Label nameLabel = new Label("n: ");
        nameLabel.setLayoutX(10);
        nameLabel.setLayoutY(62);
        nameLabel.setStyle("-fx-font-weight: bold");
        TextField nameTextField = new TextField("");
        nameTextField.setLayoutX(30);
        nameTextField.setLayoutY(60);
        pane.getChildren().addAll(nameLabel, nameTextField);
        // Обработка информации
        Button saveInfoButton = new Button("Сохранить");
        saveInfoButton.setLayoutX(30);
        saveInfoButton.setLayoutY(100);
        saveInfoButton.getStyleClass().add("save-button");
        // Удаление элемента
        Button deleteButton = new Button("Удалить");
        deleteButton.setLayoutX(30);
        deleteButton.setLayoutY(135);
        deleteButton.getStyleClass().add("save-button");
        saveInfoButton.setOnAction(actionEvent -> {
            // Изменение веса блока                                                     
            if(!weightTextField.getText().isEmpty()){                                   
                weights.get(blockHierarchy.ID).text.setText("w: " + weightTextField.getText());
                weightTextField.setText("");
            }                                                                           
            // Изменение имени блока
            if(!nameTextField.getText().isEmpty()) {
                blockHierarchy.setText(nameTextField.getText());
                titledPane.setText(blockHierarchy.ID_PANE + " : " + nameTextField.getText());
                nameTextField.setText("");
            }
        });
        deleteButton.setOnAction(actionEvent -> {
            Pane currentPane = (Pane) processPane.getSelectionModel().getSelectedItem().getContent();
            currentPane.getChildren().remove(blockHierarchy);
            Weight currentWeight = weights.get(blocksHierarchy.indexOf(blockHierarchy));
            currentPane.getChildren().remove(currentWeight);
            currentPane.getChildren().remove(currentWeight.text);
            weights.remove(currentWeight);
            blocksHierarchy.remove(blockHierarchy);
            for(TitledPane currentTitledPane: infoElements.getPanes()) {
                if(currentTitledPane.getText().equals(blockHierarchy.ID_PANE + " : " + blockHierarchy.getText())) {
                    infoElements.getPanes().remove(currentTitledPane);
                    break;
                }
            }
        });
        pane.getChildren().addAll(saveInfoButton, deleteButton);
        titledPane.setContent(pane);
        infoElements.getPanes().add(titledPane);
    }

    public void setCalculateWeight(double[] vectorWeight){
        int count=0;
        int indexActiveTab=processPane.getSelectionModel().getSelectedIndex();
        for (BlockHierarchy blockHierarchy: blocksHierarchy){
            if (blockHierarchy.getStyle()=="-fx-background-color: red;" && blockHierarchy.ID_PANE==indexActiveTab){
                int index = blocksHierarchy.indexOf(blockHierarchy);
                weights.get(index).text.setText("w: " + Double.toString(Math.round(vectorWeight[count]*100)/100.0));
                count++;
            }
        }
    }

    private boolean disableOrEnablePaintArrow(){
        boolean flagDisableOrEnablePaint = true;
        for (Button button: blocksHierarchy){
            if (button.getStyle()=="-fx-background-color: red;") {
                flagDisableOrEnablePaint = false;
                break;
            }
        }
        return flagDisableOrEnablePaint;
    }

    public void negativeOrPositiveOb(){
        if (Ob1!=null && Ob3!=null){
            if(Ob3<Ob1){
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Error");
                alertError.setHeaderText("Обобщение ухудшилось.");
                alertError.setContentText("Сделайте перерасчет.");
                alertError.showAndWait();
            }
        }
    }

    public double formatNumberMethod(BlockHierarchy block){
        int index = String.valueOf(weights.get(blocksHierarchy.
                indexOf(block)).text.getText()).indexOf(",");
        if (index == -1){
            index = String.valueOf(weights.get(blocksHierarchy.
                    indexOf(block)).text.getText()).indexOf("/");
            if (index == -1){
                return Double.parseDouble(String.valueOf(weights.get(blocksHierarchy.
                        indexOf(block)).text.getText()).substring(3));
            } else{
                return Double.parseDouble(String.valueOf(weights.get(blocksHierarchy.
                        indexOf(block)).text.getText()).substring(3, index))/
                        Double.parseDouble(String.valueOf(weights.get(blocksHierarchy.indexOf(block))
                                .text.getText()).substring(index+1, (weights
                                .get(blocksHierarchy.indexOf(block)).text.getText()).length()));
            }
        }else{
            return Double.parseDouble(String.valueOf(weights.get(blocksHierarchy.
                    indexOf(block)).text.getText()).substring(3, index) +"."+ weights.get(blocksHierarchy.
                            indexOf(block)).text.getText().substring(index + 1, ((String) weights.get(blocksHierarchy.
                            indexOf(block)).text.getText()).length()));
        }
    }

    @FXML
    void initialize() {
        buttonAddElement.setOnAction(addBlockHandler);
        buttonAddText.setOnAction(addTextHandler);
        processPane.setOnMouseClicked(changeProcessPaneHandler);
        infoPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        iconApp.setImage(new Image(getClass().getResource("LOGO.png").toExternalForm()));
        weightButtonCalculate.setOnMouseClicked(weightClick);
        buttonCalculate.setOnMouseClicked(conditionClick);
    }
}