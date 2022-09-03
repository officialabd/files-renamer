package com.tools.utilities;

import java.util.Optional;

import javafx.scene.control.TextInputDialog;

public class Dialogs {
    public static final String NO_INPUT = "EMPTY";

    public static String askForInput(String oldname, String headerText) {
        TextInputDialog in = new TextInputDialog(oldname);
        in.setHeaderText(headerText);
        in.setTitle(headerText);
        try {
            Optional<String> rs = in.showAndWait();
            if (rs.isEmpty())
                return NO_INPUT;
            return rs.get();
        } catch (Exception e) {
            return NO_INPUT;
        }
    }
}
