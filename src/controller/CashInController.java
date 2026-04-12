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
import ml.model.CashIn;
import ml.model.UserSwing;
import ml.query.caserecord.AddCaseRecord;
import ml.query.cashin.AddCashIn;
import ml.query.user.IdUserByName;
import ml.telegram.Bot;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class CashInController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField sum;
    @FXML
    private TextField note;
    @FXML
    private Button ok;

    private DialogAlert dialogAlert = new DialogAlert();
    private KKT kkt;
    private Bot bot;

    @FXML
    private void ok(ActionEvent event) {

        if (!"".equals(note.getText())) {
            AddCashIn cashIn = new AddCashIn();
            CashIn сashIn = new CashIn();
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

            //Запись в CashIn
            String s = sum.getText();
            //Замена , на .
            String cash = s.replace(',', '.');
            сashIn.setSumCash(new BigDecimal(cash));
            сashIn.setNote(note.getText());
            cashIn.add(сashIn);

            //Ввод денег в ККТ
            kkt.cashIn(cash);
            bot.checkCashInBot(sum.getText(), note.getText());

            //Запись в CaseRecord
            caseRecord.setDate(date);
            сashIn.getCaseRecord().add(caseRecord);
            caseRecord.setCashIn(сashIn);
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

    public void setKKT(KKT kkt) {
        this.kkt = kkt;
    }

    public void setBot(Bot bot) {
        this.bot = bot;

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
