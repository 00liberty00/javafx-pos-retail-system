/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author Dave
 */
public class DialogAlert {

    public void alert(String textTitle, String textHeader, String textContext) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(textTitle);
        alert.setHeaderText(textHeader);
        alert.setContentText(textContext);
        alert.showAndWait();
    }
}
