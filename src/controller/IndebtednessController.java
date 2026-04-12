/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import ml.kkt.KKT;
import ml.model.Orders;
import ml.query.indebtedness.IndebtByCode;
import ml.query.indebtedness.IndebtList;
import ml.query.indebtedness.UpdateIndebt;
import ml.table.IndebtednessReportsTable;
import ml.telegram.Bot;
import ml.window.CashOutWindow;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class IndebtednessController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private DatePicker dateReport;
    @FXML
    private Button okReportsButton;
    @FXML
    private TableView<IndebtednessReportsTable> tableReports;
    @FXML
    private TableColumn<IndebtednessReportsTable, String> noteReportsColumn;
    @FXML
    private TableColumn<IndebtednessReportsTable, BigDecimal> sumReportsColumn;
    @FXML
    private TableColumn<IndebtednessReportsTable, Date> dateReportsColumn;
    @FXML
    private TableColumn<IndebtednessReportsTable, String> recordReportsColumn;
    @FXML
    private MenuItem payContext;

    private List<Orders> crList;
    private boolean checkIndebt = false;
    private ObservableList<IndebtednessReportsTable> indebtednessReportsData = FXCollections.observableArrayList();
    private KKT kkt;
    private Bot bot;

    @FXML
    private void getDate(ActionEvent event) {
        //удаление строк в таблице
        for (int i = -1; i < tableReports.getItems().size(); i++) {
            tableReports.getItems().clear();

        }

        getIndebtDate(dateReport.getValue());
    }

    //Погашение/Оплата задолжности/заказа
    @FXML
    private void okReports(ActionEvent event) {
        IndebtednessReportsTable i = tableReports.getSelectionModel().getSelectedItem();

        IndebtByCode indebtByCode = new IndebtByCode();
        indebtByCode.setId(i.getId());

        Orders o = indebtByCode.displayResult();

        //o.setNote(i.getNote() + " / Погашена(Оплачена)");
        new CashOutWindow(i.getSum().toString(), i.getNote(), o, kkt, bot);
        if (checkIndebt == true) {
            //удаление строк в таблице
            for (int j = -1; j < tableReports.getItems().size(); j++) {
                tableReports.getItems().clear();

            }
            getIndebtDate(dateReport.getValue());
        }

        //new CashOutWindow();
    }

    @FXML
    private void setPay(ActionEvent event) {
        Orders orders = new Orders();
        UpdateIndebt ui = new UpdateIndebt();
        IndebtByCode indebtByCode = new IndebtByCode();

        IndebtednessReportsTable table = tableReports.getSelectionModel().getSelectedItem();
        String note = table.getNote();
        indebtByCode.setId(table.getId());
        orders = indebtByCode.displayResult();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Оплата накладной!");
        alert.setHeaderText("Накладная оплачена?");
        alert.setContentText(note);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            orders.setNote(note + " / Погашена(Оплачена)");
            ui.update(orders);

            for (int j = -1; j < tableReports.getItems().size(); j++) {
                tableReports.getItems().clear();

            }
            getIndebtDate(dateReport.getValue());
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    public void setBot(Bot bot){
        this.bot = bot;
    }
    
    public void setKKT(KKT kkt) {
        this.kkt = kkt;
    }
    
    public void getCheckIndebt(boolean checkIndebt) {
        this.checkIndebt = checkIndebt;
    }

    private void getIndebtDate(LocalDate date) {
        IndebtList indebtList = new IndebtList();
        //indebtList.setDate(date.toString());
        crList = indebtList.listIndebtCategory(date);

        Orders cr = null;

        for (Orders gg1 : crList) {
            cr = gg1;
            if (cr != null) {
                displayResult(cr);
            }
        }
        crList.clear();
    }

    private void displayResult(Orders orders) {
        noteReportsColumn.setCellValueFactory(new PropertyValueFactory<IndebtednessReportsTable, String>("note"));
        sumReportsColumn.setCellValueFactory(new PropertyValueFactory<IndebtednessReportsTable, BigDecimal>("sum"));
        dateReportsColumn.setCellValueFactory(new PropertyValueFactory<IndebtednessReportsTable, Date>("date"));
        recordReportsColumn.setCellValueFactory(new PropertyValueFactory<IndebtednessReportsTable, String>("user"));

        IndebtednessReportsTable indebtednessReportsTable = new IndebtednessReportsTable();
        for (int i = 0; i < 1; i++) {
            indebtednessReportsTable.setId(orders.getIdOrder());
            indebtednessReportsTable.setNote(orders.getNote());
            indebtednessReportsTable.setSum(orders.getPayment());
            indebtednessReportsTable.setDate(orders.getDate());
            indebtednessReportsTable.setUser(orders.getUserSwing().getName());

            // заполняем таблицу данными
            indebtednessReportsData.add(indebtednessReportsTable);
        }

        tableReports.setItems(indebtednessReportsData);

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Date nowDate = new Date();
                dateReport.setValue(LocalDate.now());

                //getCRDate(LocalDate.now());
            }
        });
    }

}
