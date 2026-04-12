/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ml.kkt.KKT;
import ml.model.CaseRecord;
import ml.model.CashOut;
import ml.query.cashout.DeleteCashOut;
import ml.telegram.Bot;
import ml.util.ValidTextField;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class ChooseSumInvoiceController implements Initializable {

    @FXML
    private Button ok;
    @FXML
    private ComboBox<BigDecimal> sumInvoiceCombo;
    @FXML
    private Label noteSumInvoice;
    @FXML
    private TextField newSumInvoice;

    private Stage dialogStage;
    private boolean okClicked = false;
    private ValidTextField validTextField = new ValidTextField();
    private CaseRecord crr = new CaseRecord();
    private List<String> choices;
    private List<CaseRecord> crList;
    private BigDecimal sumInvoice;
    private List<CaseRecord> crNewList = new ArrayList<CaseRecord>();
    private BigDecimal sum;
    private Integer numCashOutCaseRecord;
    private DeleteCashOut deleteCashOut = new DeleteCashOut();
    private CashOut cashOut = new CashOut();
    private KKT kkt;
    private Bot bot;

    @FXML
    private void getOk(ActionEvent event) {

        boolean isMyComboBoxEmpty = sumInvoiceCombo.getSelectionModel().isEmpty();

        if (isMyComboBoxEmpty == true) {
            sum = new BigDecimal(newSumInvoice.getText());
            //Выводит из ККТ
            kkt.cashOut(newSumInvoice.getText());
        } else {
            sum = sumInvoiceCombo.getValue();

                bot.checkCashOutBot("Вывод в приходе: " + sumInvoice.toString(), cashOut.getNote());
            deleteCashOut.delete(cashOut);
        }
        ok.setDisable(true);
        okClicked = true;
        dialogStage.close();
    }

    /**
     * Вывод на экран описание выбранной суммы
     */
    @FXML
    private void getSumInvoice() {

        for (CaseRecord gg1 : crNewList) {
            //crr = gg1;
            if ((gg1.getCashOut().getSumCash() == sumInvoiceCombo.getValue())) {
                noteSumInvoice.setText(gg1.getCashOut().getNote());
                cashOut = gg1.getCashOut();
                numCashOutCaseRecord = gg1.getCashOut().getIdCashOut();
            }
        }

    }

    public BigDecimal displaySum() {

        return sum;
    }

    public boolean displayOkClicked() {

        return okClicked;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setChoose(List<CaseRecord> crList, BigDecimal sumInvoice, KKT kkt) {
        this.kkt = kkt;
        this.crList = crList;
        this.sumInvoice = sumInvoice;
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
        //Ввод в поле только цифры и точку
        newSumInvoice.addEventFilter(KeyEvent.KEY_TYPED, validTextField.numeric_Validation(10));
        newSumInvoice.setText("0.00");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                for (CaseRecord gg1 : crList) {
                    crr = gg1;
                    if ((crr.getCashOut() != null) && (crr.getArrival() == null)) {

                        sumInvoiceCombo.getItems().add(crr.getCashOut().getSumCash());
                        crNewList.add(crr);
                    }

                }
                sumInvoiceCombo.getItems().add(new BigDecimal("0.00"));
            }
        });
    }

}
