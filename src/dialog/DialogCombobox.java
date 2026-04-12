/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.dialog;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import ml.util.MoneyRegexp;

/**
 * Создание диалогового окна (ComboBox)
 *
 * @author Dave
 */
public class DialogCombobox {

    private String textTitle;
    private String textHeader;
    private String textContext;
    private String textInput;
    private BigDecimal result;
    private MoneyRegexp moneyRegexp = new MoneyRegexp();

    public void combo(String textTitle, String textHeader, String textContext, List<String> choices) {

        ButtonType yes = new ButtonType("Да", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Сумма", choices);

        dialog.setTitle(textTitle);
        dialog.setHeaderText(textHeader);
        dialog.setContentText(textContext);

        //добавление своих кнопок
        dialog.getDialogPane().getButtonTypes().add(yes);
        dialog.getDialogPane().getButtonTypes().add(no);

        //удаление кнопок по умолчанию
        dialog.getDialogPane().getButtonTypes().removeAll(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK);

        Optional<String> result = dialog.showAndWait();

        if ((result.isPresent()) && (moneyRegexp.check(result.get()) == true)) {
            this.result = new BigDecimal(result.get());
        } else {
            this.result = new BigDecimal("0.00");
        }
    }

    /**
     * Возвращает значение, выбранное в диалоговом окне.
     *
     * @return
     */
    public BigDecimal display() {
        return this.result;
    }
}
