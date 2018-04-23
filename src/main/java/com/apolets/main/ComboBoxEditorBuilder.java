package com.apolets.main;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.cells.editors.base.EditorNodeBuilder;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

public class ComboBoxEditorBuilder implements EditorNodeBuilder<String> {

    private JFXComboBox<String> comboBox;
    private ObservableList<String> items;

    public ComboBoxEditorBuilder(ObservableList<String> items) {
        this.items = items;
    }

    @Override
    public void startEdit() {

    }

    @Override
    public void cancelEdit() {

    }

    @Override
    public void updateItem(String s, boolean b) {
        Platform.runLater(() -> {
            comboBox.requestFocus();
        });
    }

    @Override
    public Region createNode(String s, EventHandler<KeyEvent> eventHandler, ChangeListener<Boolean> changeListener) {
        comboBox = new JFXComboBox<>();
        comboBox.getItems().addAll(items);
        comboBox.getSelectionModel().select(s);
        comboBox.focusedProperty().addListener(changeListener);
        return comboBox;
    }

    @Override
    public String getValue() {
        return comboBox.getValue();
    }

    @Override
    public void setValue(String s) {
        comboBox.getSelectionModel().select(s);
    }

    @Override
    public void validateValue() {

    }
}
