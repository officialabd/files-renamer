package com.tools.layouts;

import com.tools.controls.FileProcessor;
import com.tools.controls.Message;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HomePage extends StackPane {

    private StringProperty dirPathProperty = new SimpleStringProperty();
    private StringProperty prefixProperty = new SimpleStringProperty();
    private StringProperty afterNumberProperty = new SimpleStringProperty("");
    private StringProperty postfixProperty = new SimpleStringProperty();
    private String renameOf = "All";
    private BooleanProperty keepOldName = new SimpleBooleanProperty(false);
    private BooleanProperty askForEach = new SimpleBooleanProperty(false);
    private BooleanProperty allowNumbering = new SimpleBooleanProperty(true);
    private Label statusLabel;

    public HomePage(double width, double height) {
        setPrefSize(width, height);
        build();
    }

    private void build() {
        VBox comps = new VBox(createForm(), createButtons());
        comps.setAlignment(Pos.CENTER);
        comps.setSpacing(20);
        getChildren().addAll(createTitle(), comps);
    }

    private Label createTitle() {
        Label title = new Label("Rename Multiple Files EASLY");
        title.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 20));
        title.setTranslateY(-150);
        return title;
    }

    private VBox createForm() {
        VBox form = new VBox(
                statusLabel(),
                directoryPathTextField(),
                fixFields(),
                renameOfRadioButtons(),
                optionsCheckboxs());
        form.setSpacing(10);
        form.setMaxSize(300, 100);
        return form;
    }

    private Label statusLabel() {
        statusLabel = new Label("Fill directory path, prefix, postfix, etc...");
        statusLabel.setWrapText(true);
        statusLabel.setPrefHeight(50);
        statusLabel.setMinHeight(50);
        return statusLabel;
    }

    private TextField directoryPathTextField() {
        TextField dirTextField = new TextField();
        dirTextField.setPromptText("Directory path");
        dirPathProperty.bind(dirTextField.textProperty());
        return dirTextField;
    }

    private HBox fixFields() {
        TextField prefixTextField = new TextField();
        prefixTextField.setPromptText("Prefix");
        prefixProperty.bind(prefixTextField.textProperty());
        TextField postfixTextField = new TextField();
        postfixTextField.setPromptText("Postfix");
        postfixProperty.bind(postfixTextField.textProperty());
        HBox fixs = new HBox(prefixTextField, postfixTextField);
        fixs.setSpacing(10);
        return fixs;
    }

    private VBox optionsCheckboxs() {
        CheckBox keepOldTextCB = new CheckBox("Keep old name");
        keepOldName.bind(keepOldTextCB.selectedProperty());
        CheckBox askForEachCB = new CheckBox("Ask a name for each");
        askForEach.bind(askForEachCB.selectedProperty());
        keepOldTextCB.disableProperty().bind(askForEachCB.selectedProperty());
        CheckBox numberingCB = new CheckBox("Allow numbering");
        allowNumbering.bind(numberingCB.selectedProperty());
        numberingCB.setSelected(true);
        TextField afterNumber = new TextField();
        afterNumber.setPromptText("After number");
        afterNumber.disableProperty().bind(numberingCB.selectedProperty().not());
        afterNumberProperty.bind(afterNumber.textProperty());

        HBox options2 = new HBox(numberingCB, afterNumber);
        options2.setSpacing(10);

        HBox options = new HBox(keepOldTextCB, askForEachCB);
        options.setSpacing(10);

        VBox allOptions = new VBox(options, options2);
        allOptions.setSpacing(10);

        return allOptions;
    }

    private HBox renameOfRadioButtons() {
        ToggleGroup group = new ToggleGroup();
        group.selectedToggleProperty().addListener(e -> {
            if (e instanceof ReadOnlyObjectProperty) {
                @SuppressWarnings("unchecked")
                ReadOnlyObjectProperty<RadioButton> temp = (ReadOnlyObjectProperty<RadioButton>) e;
                renameOf = temp.getValue().getText();
            }
        });
        RadioButton allRB = new RadioButton("All");
        allRB.setToggleGroup(group);
        allRB.setSelected(true);

        RadioButton filesRB = new RadioButton("Only files");
        filesRB.setToggleGroup(group);

        RadioButton foldersRB = new RadioButton("Only folders");
        foldersRB.setToggleGroup(group);

        HBox fileType = new HBox(allRB, filesRB, foldersRB);
        fileType.setSpacing(10);

        return fileType;
    }

    private Button createButtons() {
        Button run = new Button("Run");
        run.setOnMouseClicked((e) -> {
            process(dirPathProperty.getValue(),
                    renameOf,
                    keepOldName.getValue(),
                    askForEach.getValue(),
                    allowNumbering.getValue(),
                    prefixProperty.getValue(), afterNumberProperty.getValue(), postfixProperty.getValue());
        });
        return run;
    }

    private void process(String dirPath, String range, boolean keep, boolean askName, boolean allowNumbers,
            String prefix, String afterNumber, String postfix) {
        if (FileProcessor.checkIfExist(dirPath)) {
            tell(new Message(Message.SUCCESS_TYPE, "Found! Processing...", Message.SUCCESS_COLOR));
            tell(FileProcessor.renameFiles(dirPath, range, keep, allowNumbers, askName, prefix, afterNumber, postfix));
        } else {
            tell(new Message(Message.ERROR_TYPE, "Couldn't find this directory!", Message.ERROR_COLOR));
        }
    }

    private void tell(Message message) {
        statusLabel.setTextFill(Color.valueOf(message.getColor()));
        statusLabel.setText(message.getMessage());
    }

}
