/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.dialog;

import java.util.Optional;
import javafx.application.Application;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ml.util.MoneyRegexp;
import ml.util.ValidTextField;

/**
 * Создание диалогового окна
 *
 * @author Dave
 */
public class DialogTextInputForCash extends Application {

    private String textTitle;
    private String textHeader;
    private String textContext;
    private String textInput;
    private String result = "";
    private MoneyRegexp moneyRegexp = new MoneyRegexp();
    private DialogAlert dialogAlert = new DialogAlert();

    private ValidTextField validTextField = new ValidTextField();

    /**
     * Ввод текстовых данных в диалоговое окно.
     *
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
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        TextInputDialog text = new TextInputDialog(textInput);
        //Ввод в поле только цифры и точку
        text.getEditor().addEventFilter(KeyEvent.KEY_TYPED, validTextField.numeric_Validation(10));
        text.setTitle(textTitle);
        text.setHeaderText(textHeader);
        text.setContentText(textContext);
        //удаление кнопок по умолчанию
        text.getDialogPane().getButtonTypes().removeAll(ButtonType.CANCEL);

        Optional<String> result = text.showAndWait();

        if ((result.isPresent()) && (moneyRegexp.check(result.get()) == true)) {
            String ss = result.get();
            //Замена , на .
            String cash = ss.replace(',', '.');
            this.result = cash;
            //Alert alert = new Alert(AlertType.INFORMATION);
            //alert.setContentText("You entered: " + result.get());
            //alert.showAndWait();
        } 
    }

    /**
     * Возвращает значение, введенное в дилоговое окно.
     *
     * @return
     */
    public String display() {
        return this.result;
    }
}
