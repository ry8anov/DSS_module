package com.dss.dssmodule;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class BlockHierarchy extends Button {

    public int ID;
    public int ID_PANE;
    public ArrayList<BlockHierarchy> relatedBLocks;

    public BlockHierarchy(String name){
        super(name);
        setCursor(Cursor.HAND);
        setMinWidth(130);
        setMinHeight(30);
        setMaxWidth(130);
        setMaxHeight(60);
        setWrapText(true);
        setTextAlignment(TextAlignment.CENTER);
        relatedBLocks = new ArrayList<>();
        getStyleClass().add("block-hierarchy");
        viewOrderProperty().set(-1);
    }
}
