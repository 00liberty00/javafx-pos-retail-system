/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ml.model.Arrival;
import ml.model.Orders;
import ml.model.UserSwing;
import ml.query.orders.AddOrders;
import ml.query.user.IdUserByName;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class DebtPayController implements Initializable {

    @FXML
    private DatePicker datePicker;
    @FXML
    private Button ok;
    @FXML
    private TextField note;
    @FXML
    private Label debt;

    private LocalDate date;
    private Stage dialogStage;
    private boolean okClicked = false;
    private Arrival arrival = new Arrival();

    @FXML
    private void ok(ActionEvent event) {
        UserSwing userSwing = new UserSwing();
        IdUserByName idUserByName = new IdUserByName();
        Authentication authentication = SecurityContextHolder.getContext().
                getAuthentication();
        String login = authentication.getName();
        idUserByName.setLoginUser(login);   //метод для idUser по логину
        userSwing = idUserByName.displayResult(); //Возвращает пользователя
        date = datePicker.getValue();

        Orders o = new Orders();
        o.setUserSwing(userSwing);
        o.setPayment(arrival.getSumInvoice());
        o.setNote(note.getText());
        o.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        AddOrders addOrders = new AddOrders();
        addOrders.add(o);

        okClicked = true;
        arrival = new Arrival();
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setArrival(Arrival arrival) {
        this.arrival = arrival;
    }

    public boolean displayOkClicked() {

        return okClicked;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                datePicker.setValue(LocalDate.now());
                
                note.setText(arrival.getNote() + " / " + arrival.getNumberWaybill() + " от " + sdf.format(arrival.getDate()));
                debt.setText(arrival.getSumInvoice().toString());

            }
        });
    }

}
