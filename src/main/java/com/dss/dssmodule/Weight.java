package com.dss.dssmodule;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.awt.*;

public class Weight extends Circle {

    Text text = new Text("w:");
    final private static int DEFAULT_RADIUS = 25;
    Weight(double centerX, double centerY){
        super(centerX, centerY, DEFAULT_RADIUS, Color.rgb(194, 194, 194, 0.3));
        viewOrderProperty().set(-2);
        text.setLayoutX(centerX - 17);
        text.setLayoutY(centerY + 3);
        text.getStyleClass().add("text-weight");
        text.viewOrderProperty().set(-2);
        text.setFont(javafx.scene.text.Font.font(Font.SANS_SERIF));
    }
}
