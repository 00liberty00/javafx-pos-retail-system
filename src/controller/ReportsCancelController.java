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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import ml.model.Writeoff;
import ml.model.WriteoffList;
import ml.query.cancellation.CancelListByIdCancel;
import ml.query.cancellation.DateCancel;
import ml.table.ReportsCancelListTable;
import ml.table.ReportsCancelTable;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class ReportsCancelController implements Initializable {

    @FXML
    private DatePicker date;
    @FXML
    private TextField codeNameGoodTextField;
    @FXML
    private TableView<ReportsCancelTable> tableReportsCancel;
    @FXML
    private TableColumn<ReportsCancelTable, Long> numberCancelColumn;
    @FXML
    private TableColumn<ReportsCancelTable, String> dateColumn;
    @FXML
    private TableColumn<ReportsCancelTable, String> categCancelColumn;
    @FXML
    private Pane tablePane;
    @FXML
    private TableView<ReportsCancelListTable> tableCancelList;
    @FXML
    private TableColumn<ReportsCancelListTable, String> codeCol;
    @FXML
    private TableColumn<ReportsCancelListTable, String> nameCol;
    @FXML
    private TableColumn<ReportsCancelListTable, BigDecimal> amountCol;
    @FXML
    private TableColumn<ReportsCancelListTable, BigDecimal> priceCol;
    @FXML
    private Label nameUserLabel;
    @FXML
    private Label nameUser;
    @FXML
    private Label sumCancelLabel;
    @FXML
    private Label cancelText;
    @FXML
    private Label noteText;
    @FXML
    private Label noteLabel;

    private ObservableList<ReportsCancelTable> reportsCancelData = FXCollections.observableArrayList();
    private ObservableList<ReportsCancelListTable> reportsCancelListData = FXCollections.observableArrayList();

    private DateCancel dateCancel = new DateCancel();
    private List<Writeoff> caList;
    private List<WriteoffList> cancelViewList;

    /**
     * Поиск по дате
     *
     * @param event
     */
    @FXML
    private void getDate(ActionEvent event) {
        reportsCancelData.clear();               //Очищает таблицу
        reportsCancelListData.clear();           //Очищает таблицу
        dateCancel.setDate(date.getValue().toString());
        caList = dateCancel.displayResult();

        Writeoff cr = null;
        for (Writeoff gg1 : caList) {
            cr = gg1;
            if (cr != null) {
                displayCancelResult(cr);
                displayCancelListResult(cr);
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
        cancelText.setText("");

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ReportsCancelListTable> filteredData = new FilteredList<>(reportsCancelListData, p -> true);

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
        SortedList<ReportsCancelListTable> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableCancelList.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableCancelList.setItems(sortedData);
    }

    /**
     * Просмотр списания
     *
     * @param event
     */
    @FXML
    private void viewCancel(MouseEvent event) {
        reportsCancelListData.clear();      //Очищает таблицу
        //Значение номера выбранной строки
        CancelListByIdCancel byIdCancel = new CancelListByIdCancel();
        ReportsCancelTable cancel = tableReportsCancel.getSelectionModel().getSelectedItem();
        if (cancel != null) {

            cancelViewList = byIdCancel.listCancel(cancel.getNumberCancel());

            WriteoffList ca = null;
            for (WriteoffList gg1 : cancelViewList) {
                ca = gg1;
                if (ca != null) {
                    //System.out.println("Проценты равны : " + cr.getCheck().getCheckDiscount().getDiscount().getPercent());
                    displaySmallCancelListResult(ca);
                    nameUserLabel.setVisible(true);
                    nameUser.setText(ca.getWriteoff().getUserSwing().getName());
                    cancelText.setText(ca.getWriteoff().getSum().toString());
                    noteText.setText(ca.getWriteoff().getNote());
                    sumCancelLabel.setVisible(true);
                    noteLabel.setVisible(true);
                }
            }

        }
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayCancelResult(Writeoff ar) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        numberCancelColumn.setCellValueFactory(new PropertyValueFactory<ReportsCancelTable, Long>("numberCancel"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<ReportsCancelTable, String>("date"));
        categCancelColumn.setCellValueFactory(new PropertyValueFactory<ReportsCancelTable, String>("category"));

        /* ReportsArrivalTable reportsArrivalTable;
        
        //ReportsCheckListTable reportsCheckListTable;
        Set<ArrivalList> s = ar.getArrivalLists();
        Iterator<ArrivalList> it = s.iterator();
        
        while (it.hasNext()) {
        ArrivalList arrivalList = it.next();
        
        reportsArrivalTable = new ReportsArrivalTable();
        
        reportsArrivalTable.setNumberArrival(ar.getIdArrival());
        reportsArrivalTable.setDate(sdf.format(ar.getDate()));
        reportsArrivalTable.setCategory(arrivalList.getGoods().getCategoryGoods().getName());
        // заполняем таблицу данными
        reportsArrivalData.add(reportsArrivalTable);
        }
        
        tableReportsArrival.setItems(reportsArrivalData);*/
        ///Set<CaseRecord> s = ar.getArrivalLists();
        //Iterator<CaseRecord> it = s.iterator();
        ReportsCancelTable reportsCancelTable = new ReportsCancelTable();
        for (int i = 0; i < 1; i++) {

            reportsCancelTable.setNumberCancel(ar.getIdWriteoff());
            reportsCancelTable.setDate(sdf.format(ar.getDate()));
            reportsCancelTable.setCategory(ar.getNote());

            /*if ("ROLE_ADMIN".equals(auth.toString())) {
            reportsDayTable.setResidue(goods.getResidue());
            }*/
            // заполняем таблицу данными
            reportsCancelData.add(reportsCancelTable);
        }
        tableReportsCancel.setItems(reportsCancelData);
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayCancelListResult(Writeoff ar) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        codeCol.setCellValueFactory(new PropertyValueFactory<ReportsCancelListTable, String>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<ReportsCancelListTable, String>("name"));
        amountCol.setCellValueFactory(new PropertyValueFactory<ReportsCancelListTable, BigDecimal>("amount"));
        priceCol.setCellValueFactory(new PropertyValueFactory<ReportsCancelListTable, BigDecimal>("price"));

        ReportsCancelListTable reportsCancelListTable;

        //ReportsCheckListTable reportsCheckListTable;
        Set<WriteoffList> s = ar.getWriteoffLists();
        Iterator<WriteoffList> it = s.iterator();

        while (it.hasNext()) {
            WriteoffList cancelList = it.next();

            reportsCancelListTable = new ReportsCancelListTable();

            reportsCancelListTable.setCode(cancelList.getGoods().getCode());
            reportsCancelListTable.setName(cancelList.getGoods().getName());
            reportsCancelListTable.setAmount(cancelList.getAmount());
            reportsCancelListTable.setPrice(cancelList.getPrice());
            // заполняем таблицу данными
            reportsCancelListData.add(reportsCancelListTable);
        }

        tableCancelList.setItems(reportsCancelListData);
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displaySmallCancelListResult(WriteoffList cancelList) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        codeCol.setCellValueFactory(new PropertyValueFactory<ReportsCancelListTable, String>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<ReportsCancelListTable, String>("name"));
        amountCol.setCellValueFactory(new PropertyValueFactory<ReportsCancelListTable, BigDecimal>("amount"));
        priceCol.setCellValueFactory(new PropertyValueFactory<ReportsCancelListTable, BigDecimal>("price"));

        ReportsCancelListTable reportsCancelListTable = new ReportsCancelListTable();

        for (int i = 0; i < 1; i++) {

            reportsCancelListTable.setCode(cancelList.getGoods().getCode());
            reportsCancelListTable.setName(cancelList.getGoods().getName());
            reportsCancelListTable.setAmount(cancelList.getAmount());
            reportsCancelListTable.setPrice(cancelList.getPrice());
            // заполняем таблицу данными
            reportsCancelListData.add(reportsCancelListTable);
        }

        tableCancelList.setItems(reportsCancelListData);
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

                dateCancel.setDate(LocalDate.now().toString());
                caList = dateCancel.displayResult();
                Writeoff cr = null;
                for (Writeoff gg1 : caList) {
                    cr = gg1;
                    if (cr != null) {
                        displayCancelResult(cr);
                        displayCancelListResult(cr);
                    }
                }
                getCodeNameGood();
            }
        });
    }
}
