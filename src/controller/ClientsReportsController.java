/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import ml.model.CheckDiscount;
import ml.model.Discount;
import ml.query.clientsDiscount.FullClientByIdDiscount;
import ml.query.clientsDiscount.ListChecksClientDiscount;
import ml.query.clientsDiscount.ListClientsDiscount;
import ml.query.discount.NumberDiscount;
import ml.query.discount.UpdateDiscount;
import ml.table.FullClientTable;
import ml.table.ClientsTable;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class ClientsReportsController implements Initializable {

    @FXML
    private TableView<ClientsTable> clientsTable;
    @FXML
    private TableColumn<ClientsTable, String> numcardClientTableColumn;
    @FXML
    private TableColumn<ClientsTable, String> clientTableColumn;
    @FXML
    private TableColumn<ClientsTable, BigDecimal> sumPurchaseTableColumn;
    @FXML
    private Label totalLabel;
    @FXML
    private TableView<FullClientTable> fullClientTable;
    @FXML
    private TableColumn<FullClientTable, Date> datePurchaseColumn;
    @FXML
    private TableColumn<FullClientTable, BigDecimal> sumPurchaseColumn;
    @FXML
    private TableColumn<FullClientTable, BigDecimal> paymentColumn;
    @FXML
    private TableColumn<FullClientTable, BigDecimal> balanceColumn;
    @FXML
    private TextField percentTextField;
    @FXML
    private TextField phoneTextField;

    private ListClientsDiscount clientsDiscount = new ListClientsDiscount();
    private ListChecksClientDiscount checksClientDiscount = new ListChecksClientDiscount();

    private ObservableList<ClientsTable> allClientData = FXCollections.observableArrayList();
    private ObservableList<FullClientTable> fullClientData = FXCollections.observableArrayList();

    private List<Discount> discountClientList;
    private List<CheckDiscount> discountChecksClientList;
    private Discount client = new Discount();
    private UpdateDiscount updateDiscount = new UpdateDiscount();
    private int selectRow = -1;

    @FXML
    private void updatePercentDiscount(KeyEvent event) {
        //Ввод поле ПРОЦЕНТЫ КЛИЕНТА только цифры
        percentTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    percentTextField.setText(oldValue);
                }
            }
        });

        if (!"".equals(percentTextField.getText())) {
            client.setPercent(percentTextField.getText());

            System.out.println("Новые проценты " + client.getPercent());
            updateDiscount.update(client);
        }

    }

    @FXML
    private void updatePhoneClient(KeyEvent event) {
    }

    /**
     * Просмотр клиента
     */
    @FXML
    private void viewClient() {

        fullClientData.clear();      //Очищает таблицу

        //Значение номера выбранной строки
        FullClientByIdDiscount byIdDiscount = new FullClientByIdDiscount();

        NumberDiscount byNumcard = new NumberDiscount();
        ClientsTable clients = clientsTable.getSelectionModel().getSelectedItem();

        if (clients != null) {

            byNumcard.numberDiscount(clients.getNumcard());
            client = byNumcard.displayResult();
            // discountClientList = byIdDiscount.listDiscount(clients.getNumcard());

            percentTextField.setText(client.getPercent());
            phoneTextField.setText(client.getPhone());
        }

        discountChecksClientList = checksClientDiscount.listChecksDiscount(client);
        discountChecksClientList.forEach((c) -> {
            displayChecksResult(c);

        });

//        List<TablePosition> positions = clientsTable.getSelectionModel().getSelectedCells();
//        if (!positions.isEmpty()) {
//
//            int i = clientsTable.getItems().size();   // кол-во строк в таблице
//            if (i > 0) {
//
//                TablePosition pos = clientsTable.getSelectionModel().getSelectedCells().get(0);
//                selectRow = pos.getRow();
//
////                            tableArrival.getSelectionModel().clearSelection();
//            }
//        }
    }

    /**
     * Список клиентов
     */
    private void listClients() {
        clientsDiscount.clearList();
        discountClientList = clientsDiscount.listDiscount();
        discountClientList.forEach((g) -> {
            displayResult(g);
        });
    }

    /**
     * Список чеков клиента
     */
    private void listChecksClients() {
        clientsDiscount.clearList();
        discountClientList = clientsDiscount.listDiscount();
        discountClientList.forEach((d) -> {
            displayResult(d);

        });
    }

    private void displayResult(Discount d) {
        numcardClientTableColumn.setCellValueFactory(new PropertyValueFactory<ClientsTable, String>("numcard"));
        clientTableColumn.setCellValueFactory(new PropertyValueFactory<ClientsTable, String>("fistName"));
        sumPurchaseTableColumn.setCellValueFactory(new PropertyValueFactory<ClientsTable, BigDecimal>("sumPurchase"));
        ClientsTable clTable = new ClientsTable();
        //for (Goods g : goodsList) {
        for (int i = 0; i < 1; i++) {
            clTable.setNumcard(d.getNumcard());
            clTable.setFistName(d.getName() + " " + d.getLastname());
            clTable.setSumPurchase(d.getSumChecks());

            allClientData.add(clTable);
        }

        clientsTable.setItems(allClientData);
    }

    private void displayChecksResult(CheckDiscount cd) {
        datePurchaseColumn.setCellValueFactory(new PropertyValueFactory<FullClientTable, Date>("datePurchase"));
        sumPurchaseColumn.setCellValueFactory(new PropertyValueFactory<FullClientTable, BigDecimal>("sumPurchase"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<FullClientTable, BigDecimal>("payment"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<FullClientTable, BigDecimal>("balance"));

        FullClientTable fcTable = new FullClientTable();

        for (int i = 0; i < 1; i++) {
            fcTable.setDatePurchase(cd.getCheck().getDate());
            fcTable.setSumPurchase(cd.getCheck().getSum());
//            fcTable.setPayment(cd.getCheck());
//            fcTable.setBalance(cd.getCheck());
            fullClientData.add(fcTable);
        }

        fullClientTable.setItems(fullClientData);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Список клиентов
        listClients();
    }

}
