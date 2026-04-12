/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.dialog;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 * Создание диалогового окна (ДА, НЕТ)
 *
 * @author Dave
 */
public class DialogChoose {

    private String textTitle;
    private String textHeader;
    private String textContext;
    private String textInput;
    private boolean result = false;

    public void alert(String textTitle, String textHeader, String textContext) {

        ButtonType yes = new ButtonType("Да", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(AlertType.CONFIRMATION, "", yes, no);
        alert.setTitle(textTitle);
        alert.setHeaderText(textHeader);
        alert.setContentText(textContext);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yes) {
            this.result = true;
        } else {
            this.result = false;
        }
    }

    /**
     * Возвращает значение, выбранное в диалоговом окне.
     *
     * @return
     */
    public boolean display() {
        return this.result;
    }
}
