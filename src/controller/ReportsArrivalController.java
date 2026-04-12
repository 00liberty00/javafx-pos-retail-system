/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import ml.model.Arrival;
import ml.model.ArrivalDish;
import ml.model.ArrivalDishList;
import ml.model.ArrivalList;
import ml.query.arrival.ArrivalListByIdArrival;
import ml.query.arrival.DateArrival;
import ml.query.arrivaldish.ArrivalDishListByIdArrivalDish;
import ml.query.arrivaldish.DateArrivalDish;
import ml.table.ReportsArrivalListTable;
import ml.table.ReportsArrivalTable;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class ReportsArrivalController implements Initializable {

    @FXML
    private DatePicker date;
    @FXML
    private TextField codeNameGoodTextField;
    @FXML
    private TableView<ReportsArrivalTable> tableReportsArrival;
    @FXML
    private TableColumn<ReportsArrivalTable, Long> numberArrivalColumn;
    @FXML
    private TableColumn<ReportsArrivalTable, String> dateColumn;
    @FXML
    private TableColumn<ReportsArrivalTable, String> categArrivalColumn;
    @FXML
    private Pane tablePane;
    @FXML
    private TableView<ReportsArrivalListTable> tableArrivalList;
    @FXML
    private TableColumn<ReportsArrivalListTable, String> codeCol;
    @FXML
    private TableColumn<ReportsArrivalListTable, String> nameCol;
    @FXML
    private TableColumn<ReportsArrivalListTable, BigDecimal> amountCol;
    @FXML
    private TableColumn<ReportsArrivalListTable, BigDecimal> priceOptCol;
    @FXML
    private TableColumn<ReportsArrivalListTable, BigDecimal> priceCol;
    @FXML
    private Label nameUserLabel;
    @FXML
    private Label nameUser;
    @FXML
    private Label sumInvoiceLabel;
    @FXML
    private Label invoiceText;
    @FXML
    private Label sumArrivalLabel;
    @FXML
    private Label arrivalText;
    @FXML
    private Label numInvoiceLabel;
    @FXML
    private Label numInvoiceText;
    @FXML
    private CheckBox viewArrivalDishCheckBox;

    private ObservableList<ReportsArrivalTable> reportsArrivalData = FXCollections.observableArrayList();
    private ObservableList<ReportsArrivalListTable> reportsArrivalListData = FXCollections.observableArrayList();

    private DateArrival dateArrival = new DateArrival();
    private List<Arrival> arList;
    private List<ArrivalList> arrivalViewList;

    private DateArrivalDish dateArrivalDish = new DateArrivalDish();
    private List<ArrivalDish> arDishList;
    private List<ArrivalDishList> arrivalDishViewList;

    /**
     * Поиск по дате
     *
     * @param event
     */
    @FXML
    private void getDate(ActionEvent event) {
        reportsArrivalData.clear();               //Очищает таблицу
        reportsArrivalListData.clear();           //Очищает таблицу

        if (viewArrivalDishCheckBox.isSelected()) {
            dateArrivalDish.setDate(date.getValue().toString());
            arDishList = dateArrivalDish.displayResult();
            ArrivalDish ard = null;
            for (ArrivalDish gg1 : arDishList) {
                ard = gg1;
                if (ard != null) {
                    displayArrivalDishResult(ard);
                    displayArrivalDishListResult(ard);

                }
            }
        } else {
            dateArrival.setDate(date.getValue().toString());
            arList = dateArrival.displayResult();

            Arrival cr = null;
            for (Arrival gg1 : arList) {
                cr = gg1;
                if (cr != null) {
                    displayArrivalResult(cr);
                    displayArrivalListResult(cr);
                }
            }
        }
        getCodeNameGood();
    }

    /**
     * Поиск коду или назв товара
     *
     */
    private void getCodeNameGood() {

        nameUser.setText("");
        invoiceText.setText("");
        arrivalText.setText("");
        numInvoiceText.setText("");

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ReportsArrivalListTable> filteredData = new FilteredList<>(reportsArrivalListData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        codeNameGoodTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (person.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<ReportsArrivalListTable> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableArrivalList.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableArrivalList.setItems(sortedData);
    }

    /**
     * Просмотр прихода
     *
     * @param event
     */
    @FXML
    private void viewArrival(MouseEvent event) {
        reportsArrivalListData.clear();      //Очищает таблицу
        //Значение номера выбранной строки

        if (viewArrivalDishCheckBox.isSelected()) {

            ArrivalDishListByIdArrivalDish byIdArrivalDish = new ArrivalDishListByIdArrivalDish();
            ReportsArrivalTable arrivalDish = tableReportsArrival.getSelectionModel().getSelectedItem();

            if (arrivalDish != null) {

                arrivalDishViewList = byIdArrivalDish.listArrivalDish(arrivalDish.getNumberArrival());

                ArrivalDishList ar = null;
                for (ArrivalDishList gg1 : arrivalDishViewList) {
                    ar = gg1;
                    if (ar != null) {
                        //System.out.println("Проценты равны : " + cr.getCheck().getCheckDiscount().getDiscount().getPercent());
                        displaySmallArrivalDishListResult(ar);
                        nameUserLabel.setVisible(true);
                        nameUser.setText(ar.getArrivalDish().getUserSwing().getName());
                        invoiceText.setText(ar.getArrivalDish().getSumInvoice().toString());
//                        arrivalText.setText(ar.getArrivalDish().getSumArrival().toString());
                        numInvoiceText.setText(ar.getArrivalDish().getNumberWaybill());

                        sumInvoiceLabel.setVisible(true);
                        sumArrivalLabel.setVisible(true);
                        numInvoiceLabel.setVisible(true);
                    }
                }

            }

        } else {

            ArrivalListByIdArrival byIdArrival = new ArrivalListByIdArrival();
            ReportsArrivalTable arrival = tableReportsArrival.getSelectionModel().getSelectedItem();

            if (arrival != null) {

                arrivalViewList = byIdArrival.listArrival(arrival.getNumberArrival());

                ArrivalList ar = null;
                for (ArrivalList gg1 : arrivalViewList) {
                    ar = gg1;
                    if (ar != null) {
                        //System.out.println("Проценты равны : " + cr.getCheck().getCheckDiscount().getDiscount().getPercent());
                        displaySmallArrivalListResult(ar);
                        nameUserLabel.setVisible(true);
                        nameUser.setText(ar.getArrival().getUserSwing().getName());
                        invoiceText.setText(ar.getArrival().getSumInvoice().toString());
                        arrivalText.setText(ar.getArrival().getSumArrival().toString());
                        numInvoiceText.setText(ar.getArrival().getNumberWaybill());

                        sumInvoiceLabel.setVisible(true);
                        sumArrivalLabel.setVisible(true);
                        numInvoiceLabel.setVisible(true);
                    }
                }

            }
        }
    }

    public void setData(Long number) {
        System.out.println("CATCH");
        /*reportsArrivalListData.clear();      //Очищает таблицу
        tableArrivalList.setItems(reportsArrivalListData);*/
        ArrivalListByIdArrival byIdArrival = new ArrivalListByIdArrival();
        arrivalViewList = new ArrayList<ArrivalList>();
        arrivalViewList = byIdArrival.listArrival(number);

        for (int i = 0; i < arrivalViewList.size(); i++) {

            System.out.println(": " + arrivalViewList.get(i).getGoods().getName());
        }

    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayArrivalResult(Arrival ar) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        numberArrivalColumn.setCellValueFactory(new PropertyValueFactory<ReportsArrivalTable, Long>("numberArrival"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<ReportsArrivalTable, String>("date"));
        categArrivalColumn.setCellValueFactory(new PropertyValueFactory<ReportsArrivalTable, String>("category"));

        ReportsArrivalTable reportsArrivalTable = new ReportsArrivalTable();
        for (int i = 0; i < 1; i++) {

            reportsArrivalTable.setNumberArrival(ar.getIdArrival());
            reportsArrivalTable.setDate(sdf.format(ar.getDate()));
            reportsArrivalTable.setCategory(ar.getNote());

            /*if ("ROLE_ADMIN".equals(auth.toString())) {
            reportsDayTable.setResidue(goods.getResidue());
            }*/
            // заполняем таблицу данными
            reportsArrivalData.add(reportsArrivalTable);
        }
        tableReportsArrival.setItems(reportsArrivalData);
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayArrivalDishResult(ArrivalDish ard) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        numberArrivalColumn.setCellValueFactory(new PropertyValueFactory<ReportsArrivalTable, Long>("numberArrival"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<ReportsArrivalTable, String>("date"));
        categArrivalColumn.setCellValueFactory(new PropertyValueFactory<ReportsArrivalTable, String>("category"));

        ReportsArrivalTable reportsArrivalTable = new ReportsArrivalTable();
        for (int i = 0; i < 1; i++) {

            reportsArrivalTable.setNumberArrival(ard.getIdArrivalDish());
            reportsArrivalTable.setDate(sdf.format(ard.getDate()));
            reportsArrivalTable.setCategory("Продукты");

            /*if ("ROLE_ADMIN".equals(auth.toString())) {
            reportsDayTable.setResidue(goods.getResidue());
            }*/
            // заполняем таблицу данными
            reportsArrivalData.add(reportsArrivalTable);
        }
        tableReportsArrival.setItems(reportsArrivalData);
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayArrivalListResult(Arrival ar) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        codeCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, String>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, String>("name"));
        amountCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, BigDecimal>("amount"));
        priceCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, BigDecimal>("price"));
        priceOptCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, BigDecimal>("priceOpt"));

        ReportsArrivalListTable reportsArrivalListTable;

        //ReportsCheckListTable reportsCheckListTable;
        Set<ArrivalList> s = ar.getArrivalLists();
        Iterator<ArrivalList> it = s.iterator();

        while (it.hasNext()) {
            ArrivalList arrivalList = it.next();

            reportsArrivalListTable = new ReportsArrivalListTable();

            reportsArrivalListTable.setCode(arrivalList.getGoods().getCode());
            reportsArrivalListTable.setName(arrivalList.getGoods().getName());
            reportsArrivalListTable.setAmount(arrivalList.getAmount());
            reportsArrivalListTable.setPrice(arrivalList.getPrice());
            reportsArrivalListTable.setPriceOpt(arrivalList.getPriceOpt());
            // заполняем таблицу данными
            reportsArrivalListData.add(reportsArrivalListTable);
        }

        tableArrivalList.setItems(reportsArrivalListData);
    }

    /**
     * Просмотр прихода продуктов
     */
    private void displayArrivalDishListResult(ArrivalDish ard) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        codeCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, String>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, String>("name"));
        amountCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, BigDecimal>("amount"));
//        priceCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, BigDecimal>("price"));
        priceOptCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, BigDecimal>("priceOpt"));

        ReportsArrivalListTable reportsArrivalListTable;

        //ReportsCheckListTable reportsCheckListTable;
        Set<ArrivalDishList> s = ard.getArrivalDishLists();
        Iterator<ArrivalDishList> it = s.iterator();

        while (it.hasNext()) {
            ArrivalDishList arrivalDishList = it.next();

            reportsArrivalListTable = new ReportsArrivalListTable();

            reportsArrivalListTable.setCode(arrivalDishList.getDish().getCode());
            reportsArrivalListTable.setName(arrivalDishList.getDish().getName());
            reportsArrivalListTable.setAmount(arrivalDishList.getAmount());
//            reportsArrivalListTable.setPrice(arrivalList.getPrice());
            reportsArrivalListTable.setPriceOpt(arrivalDishList.getPriceOpt());
            // заполняем таблицу данными
            reportsArrivalListData.add(reportsArrivalListTable);
        }

        tableArrivalList.setItems(reportsArrivalListData);
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displaySmallArrivalListResult(ArrivalList arrivalList) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        codeCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, String>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, String>("name"));
        amountCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, BigDecimal>("amount"));
        priceCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, BigDecimal>("price"));
        priceOptCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, BigDecimal>("priceOpt"));

        ReportsArrivalListTable reportsArrivalListTable = new ReportsArrivalListTable();

        for (int i = 0; i < 1; i++) {

            reportsArrivalListTable.setCode(arrivalList.getGoods().getCode());
            reportsArrivalListTable.setName(arrivalList.getGoods().getName());
            reportsArrivalListTable.setAmount(arrivalList.getAmount());
            reportsArrivalListTable.setPrice(arrivalList.getPrice());
            reportsArrivalListTable.setPriceOpt(arrivalList.getPriceOpt());
            // заполняем таблицу данными
            reportsArrivalListData.add(reportsArrivalListTable);
        }

        tableArrivalList.setItems(reportsArrivalListData);
        System.out.println("");
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displaySmallArrivalDishListResult(ArrivalDishList arrivalDishList) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        codeCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, String>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, String>("name"));
        amountCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, BigDecimal>("amount"));
        priceCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, BigDecimal>("price"));
        priceOptCol.setCellValueFactory(new PropertyValueFactory<ReportsArrivalListTable, BigDecimal>("priceOpt"));

        ReportsArrivalListTable reportsArrivalListTable = new ReportsArrivalListTable();

        for (int i = 0; i < 1; i++) {

            reportsArrivalListTable.setCode(arrivalDishList.getDish().getCode());
            reportsArrivalListTable.setName(arrivalDishList.getDish().getName());
            reportsArrivalListTable.setAmount(arrivalDishList.getAmount());
//            reportsArrivalListTable.setPrice(arrivalDishList.getPrice());
            reportsArrivalListTable.setPriceOpt(arrivalDishList.getPriceOpt());
            // заполняем таблицу данными
            reportsArrivalListData.add(reportsArrivalListTable);
        }

        tableArrivalList.setItems(reportsArrivalListData);
        System.out.println("");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                date.setValue(LocalDate.now());

                getCodeNameGood();

            }
        });
    }

    @FXML
    private void viewArrivaDishCheckBoxAction(ActionEvent event) {
        reportsArrivalData.clear();               //Очищает таблицу
        reportsArrivalListData.clear();           //Очищает таблицу
        if (viewArrivalDishCheckBox.isSelected()) {

            dateArrivalDish.setDate(date.getValue().toString());
            arDishList = dateArrivalDish.displayResult();
            ArrivalDish ard = null;
            for (ArrivalDish gg1 : arDishList) {
                ard = gg1;
                if (ard != null) {
                    displayArrivalDishResult(ard);
                    displayArrivalDishListResult(ard);
                }
            }

        } else {
            dateArrival.setDate(date.getValue().toString());
            arList = dateArrival.displayResult();
            Arrival cr = null;
            for (Arrival gg1 : arList) {
                cr = gg1;
                if (cr != null) {
                    displayArrivalResult(cr);
                    displayArrivalListResult(cr);
                }
            }
        }
    }
}
