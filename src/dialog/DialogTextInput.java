/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.dialog;

import java.util.Optional;
import javafx.application.Application;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

/**
 *  Создание диалогового окна
 * @author Dave
 */
public class DialogTextInput extends Application {

    private String textTitle;
    private String textHeader;
    private String textContext;
    private String textInput;
    private String result = "";

    /**
     * Ввод текстовых данных в диалоговое окно.
     * @param textTitle
     * @param textHeader
     * @param textContext
     * @param textInput 
     */
    public void dialog(String textTitle, String textHeader, String textContext, String textInput) {
        this.textTitle = textTitle;
        this.textHeader = textHeader;
        this.textContext = textContext;
        this.textInput = textInput;
    }

    /**
     * Старт диалогового окна
     * @param primaryStage 
     */
    @Override
    public void start(Stage primaryStage) {
        TextInputDialog text = new TextInputDialog(textInput);
        text.setTitle(textTitle);
        text.setHeaderText(textHeader);
        text.setContentText(textContext);
        
        Optional<String> result = text.showAndWait();
        if (result.isPresent()) {

            this.result = result.get();
            //Alert alert = new Alert(AlertType.INFORMATION);
            //alert.setContentText("You entered: " + result.get());
            //alert.showAndWait();
        }
        else {
            this.result = "";
        }
    }

    /**
     * Возвращает значение, введенное в дилоговое окно.
     * @return 
     */
    public String display() {
        return this.result;
    }
}
