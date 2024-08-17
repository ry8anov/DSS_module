package com.dss.dssmodule;

import javafx.scene.Cursor;
import javafx.scene.shape.Line;

public class Arrow extends Line {

    BlockHierarchy start;
    BlockHierarchy end;

    Arrow(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
        setCursor(Cursor.CLOSED_HAND);
        getStyleClass().add("arrow");
    }
}
