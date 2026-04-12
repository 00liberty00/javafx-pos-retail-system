/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ml.dialog.DialogAlert;
import ml.kkt.KKT;
import ml.model.CaseRecord;
import ml.model.CashOut;
import ml.model.Orders;
import ml.model.UserSwing;
import ml.query.caserecord.AddCaseRecord;
import ml.query.cashout.AddCashOut;
import ml.query.indebtedness.UpdateIndebt;
import ml.query.user.IdUserByName;
import ml.telegram.Bot;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class CashOutController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField sum;
    @FXML
    private TextField note;
    @FXML
    private Button ok;

    private DialogAlert dialogAlert = new DialogAlert();
    private Stage dialogStage;
    private Orders orders = new Orders();
    private KKT kkt;
    private Bot bot;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTopText(String sumOut, String noteOut) {
        // set text from another class
        sum.setText(sumOut);
        note.setText(noteOut);
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public void setKKT(KKT kkt) {
        this.kkt = kkt;
    }

    public void setBot(Bot bot) {
        this.bot = bot;

    }

    @FXML
    private void ok(ActionEvent event) {

        if (!"".equals(note.getText())) {
            AddCashOut addCashOut = new AddCashOut();
            CashOut сashOut = new CashOut();
            CaseRecord caseRecord = new CaseRecord();
            AddCaseRecord addCaseRecord = new AddCaseRecord();
            UserSwing userSwing = new UserSwing();
            IdUserByName idUserByName = new IdUserByName();
            Date date = new Date();

            Authentication authentication = SecurityContextHolder.getContext().
                    getAuthentication();
            String login = authentication.getName();
            idUserByName.setLoginUser(login);   //метод для idUser по логину
            userSwing = idUserByName.displayResult(); //Возвращает пользователя

            /**
             * Если вывод денег связан с долгом
             */
            if (orders.getIdOrder() != null) {
                IndebtednessController controller = new IndebtednessController();
                UpdateIndebt ui = new UpdateIndebt();
                orders.setNote(note.getText() + " / Погашена(Оплачена)");
                ui.update(orders);

                controller.setKKT(kkt);
                controller.getCheckIndebt(true);
            }

            //Запись в CashOut
            String s = sum.getText();
            //Замена , на .
            String cash = s.replace(',', '.');
            сashOut.setSumCash(new BigDecimal(cash));
            сashOut.setNote(note.getText());
            addCashOut.add(сashOut);
            //телеграм бот отправляет сообщение о выводе денег

            bot.checkCashOutBot(sum.getText(), note.getText());
            //Ввод денег в ККТ
            kkt.cashOut(cash);

            //Запись в CaseRecord
            caseRecord.setDate(date);
            сashOut.getCaseRecord().add(caseRecord);
            caseRecord.setCashOut(сashOut);
            caseRecord.setUserSwing(userSwing);
            addCaseRecord.add(caseRecord);
            //Очищает все поля
            sum.setText("0.00");
            note.setText("");
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.close();
        } else {
            dialogAlert.alert("Внимание!!!", "Поле 'Описание' пустое", "Введите описание");
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //Ввод поле СУММА только цифр точки или запятой
        sum.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*([.,]\\d*)?")) {
                    sum.setText(oldValue);
                }
            }
        });
    }
}
