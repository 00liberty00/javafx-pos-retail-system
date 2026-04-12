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
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import ml.authentication.GrantedAuth;
import ml.model.ArrivalDishList;
import ml.model.ArrivalList;
import ml.model.CaseRecord;
import ml.model.CheckList;
import ml.model.Total;
import ml.query.caserecord.DateCaseRecord;
import ml.query.check.DateCheck;
import ml.query.check.Proceeds;
import ml.query.check.SumCheckByDate;
import ml.query.check.SumCheckInBankByDate;
import ml.query.goods.SumGoods;
import ml.query.total.DateTotal;
import ml.table.ReportsDayTable;
import ml.window.ViewArrivalFromReportsWindow;
import ml.window.ViewCancelFromReportsWindow;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class ReportsDayController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private DatePicker dateReport = new DatePicker();
    @FXML
    private Label proceedsText;
    @FXML
    private Label cashText;
    @FXML
    private Label sumGoodsText;
    @FXML
    private Label bankText;
    @FXML
    private Label cashLabel;
    @FXML
    private Label spareText;
    @FXML
    private Label profitText;
    @FXML
    private Label bnalLabel;
    @FXML
    private Label spareLabel;
    @FXML
    private Label profitLabel;
    @FXML
    private TableView<ReportsDayTable> tableReportsDay;
    @FXML
    private TableColumn<ReportsDayTable, BigDecimal> sumInReportsColumn;
    @FXML
    private TableColumn<ReportsDayTable, BigDecimal> sumOutReportsColumn;
    @FXML
    private TableColumn<ReportsDayTable, BigDecimal> invoiceReportsColumn;
    @FXML
    private TableColumn<ReportsDayTable, BigDecimal> arrivalReportsColumn;
    @FXML
    private TableColumn<ReportsDayTable, BigDecimal> cancelReportsColumn;
    @FXML
    private TableColumn<ReportsDayTable, String> noteReportsColumn;
    @FXML
    private TableColumn<ReportsDayTable, Date> dateReportsColumn;
    @FXML
    private TableColumn<ReportsDayTable, Long> numberReportsColumn;

    private ObservableList<ReportsDayTable> reportsDayData = FXCollections.observableArrayList();
    private List<CaseRecord> crList;
    private List<CheckList> checkList;
    private Proceeds pr;
    private GrantedAuth grantedAuth = new GrantedAuth();
    private Object auth = grantedAuth.role();
    private BigDecimal proceedsCheck = new BigDecimal("0.00");          //Выручка по чекам
    private List<ArrivalList> arrivalViewList;
    private List<ArrivalDishList> arrivalDishViewList;
    private SumCheckByDate sumCheckByDate = new SumCheckByDate();
    private SumCheckInBankByDate sumCheckInBankByDate = new SumCheckInBankByDate();

    /**
     * Берет дату
     */
    @FXML
    private void getDate() {

        //удаление строк в таблице
        for (int i = -1; i < tableReportsDay.getItems().size(); i++) {
            tableReportsDay.getItems().clear();

        }
        proceedsText.setText("0.00");
        bankText.setText("0.00");
        sumGoodsText.setText("0.00");
        cashText.setText("0.00");
        getCRDate(dateReport.getValue());
    }

    /**
     * Вовзращает данные отчетов по дням
     *
     * @param code
     */
    private void getCRDate(LocalDate date) {
        sumCheckByDate = new SumCheckByDate();
        BigDecimal sumCheck = new BigDecimal(0.00);
        pr = new Proceeds();
        DateTotal dateTotal = new DateTotal();
        DateCaseRecord dateCaseRecord = new DateCaseRecord();
        dateCaseRecord.setDate(date);
        //dateCaseRecord.totalSalary(date);
        crList = dateCaseRecord.displayResult();
        BigDecimal sumCashIn = new BigDecimal(0.00);
        BigDecimal sumCashOut = new BigDecimal(0.00);
        BigDecimal sumCheckInBank = new BigDecimal(0.00);
        BigDecimal proceeds = new BigDecimal(0.00);

        BigDecimal profitSum = new BigDecimal(0.00);

        DateCheck dateCheck = new DateCheck();
        SumGoods sumG = new SumGoods();
//        BigDecimal profitSum = new BigDecimal(0.00);

        //Вывод списка чеков проданного товара по дате
        for (int i = 0; i < crList.size(); i++) {
            if (crList != null) {
                displayResult(crList.get(i));
                //Сумма ввода денег в кассу
                if (crList.get(i).getCashIn() != null) {
                    sumCashIn = sumCashIn.add(crList.get(i).getCashIn().getSumCash());
                }
                //Сумма вывода денег из кассы
                if (crList.get(i).getCashOut() != null) {
                    sumCashOut = sumCashOut.add(crList.get(i).getCashOut().getSumCash());
                }
            }
        }

        /*     CaseRecord cr = null;
        
        for (CaseRecord gg1 : crList) {
        System.out.println("QQQQQQQQQQQQQQ");
        cr = gg1;
        if (cr != null) {
        displayResult(cr);
        //Сумма ввода денег в кассу
        if (cr.getCashIn() != null) {
        sumCashIn = sumCashIn.add(cr.getCashIn().getSumCash());
        }
        //Сумма вывода денег из кассы
        if (cr.getCashOut() != null) {
        sumCashOut = sumCashOut.add(cr.getCashOut().getSumCash());
        }
        
        }
        }*/
        dateTotal.setDate(date.toString());
        Total total = new Total();
        total = dateTotal.displayResult();

        //Возвращает выручку по дате
        //Если дата сегодня, то показывает информацию на сегодняшнюю дату
        if ("ROLE_ADMIN".equals(auth.toString())) {

            if ((date.isEqual(LocalDate.now()) == true) && (total == null)) {
                cashText.setVisible(true);
                sumCheckByDate.setDate(date);
                sumCheckInBankByDate.setDate(date);
                sumCheck = sumCheckByDate.getProceeds();
                sumCheckInBank = sumCheckInBankByDate.getProceeds();
                proceeds = sumCheck.add(sumCheckInBank);
                proceedsText.setText(proceeds.toString());
                bankText.setText(sumCheckInBank.toString());
                cashText.setText(sumCheck.add(sumCashIn).subtract(sumCashOut).toString());
                profitText.setVisible(false);
                spareText.setVisible(false);
                spareLabel.setVisible(false);
                profitLabel.setVisible(false);
                //Расчет прибыли

                //checkList = profitCheck.listCheckForProfit(date);
                dateCheck.setDate(date);
                checkList = dateCheck.displayResultCheckList();

//                for (CheckList c1 : checkList) {
//                    profitSum = profitSum.add(c1.getProfit());
//                }
                bankText.setText(sumCheckInBank.toString());
                //Сумма денег в товаре
                sumG.sumGoods();
                sumGoodsText.setText(sumG.getSumGoods().toString());
            } else {
                if (total.getNal() == null) {
                    ///СТАРАЯ ВЕРСИЯ ОТЧЕТОВ ЗА ДЕНЬ ИЗ TOTALs
                    //cashLabel.setVisible(false);
//                    cashLabel.setText("Лишние деньги");
                    //cash.setVisible(false);
                    BigDecimal sCheck = new BigDecimal(0.00);
                    BigDecimal sCheckInBank = new BigDecimal(0.00);
                    sumCheckByDate.setDate(date);
                    sumCheckInBankByDate.setDate(date);
                    sCheck = sumCheckByDate.getProceeds();
                    sCheckInBank = sumCheckInBankByDate.getProceeds();

                    if (total != null) {
                        //Возвращает выручку по дате
                        pr.setDate(date);
                        proceedsCheck = pr.getProceeds();
                        proceedsText.setText(proceedsCheck.add(total.getSpare()).toString());
                        spareLabel.setVisible(true);
                        spareText.setVisible(true);
                        spareText.setText(total.getSpare().toString());
                        sumGoodsText.setText(total.getSumGoods().toString());
                        profitLabel.setVisible(true);
                        profitText.setVisible(true);
                        profitText.setText(total.getProfit().toString());
                        cashText.setText(sCheck.toString());
                        bankText.setText(sCheckInBank.toString());
                    }
                } else {

                    //НОВАЯ ВЕРСИЯ ОТЧЕТОВ ИЗ TOTAL
                    //cashLabel.setVisible(false);
//                  cashLabel.setText("Лишние деньги");
                    //cash.setVisible(false);
                    if (total != null) {
                        spareLabel.setVisible(true);
                        profitLabel.setVisible(true);
                        spareText.setVisible(true);
                        profitText.setVisible(true);

                        proceedsText.setText(total.getProceeds().toString());
                        cashText.setText(total.getNal().toString());
                        sumGoodsText.setText(total.getSumGoods().toString());
                        bankText.setText(total.getBnal().toString());
                        profitText.setText(total.getProfit().toString());
                        spareText.setText(total.getSpare().toString());

                    }
                }
            }

        }
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayResult(CaseRecord cr) {
        sumInReportsColumn.setCellValueFactory(new PropertyValueFactory<ReportsDayTable, BigDecimal>("sumIn"));
        sumOutReportsColumn.setCellValueFactory(new PropertyValueFactory<ReportsDayTable, BigDecimal>("sumOut"));
        invoiceReportsColumn.setCellValueFactory(new PropertyValueFactory<ReportsDayTable, BigDecimal>("sumInvoice"));
        if ("ROLE_ADMIN".equals(auth.toString())) {
            arrivalReportsColumn.setCellValueFactory(new PropertyValueFactory<ReportsDayTable, BigDecimal>("sumArrival"));
        }
        cancelReportsColumn.setCellValueFactory(new PropertyValueFactory<ReportsDayTable, BigDecimal>("sumCancel"));
        noteReportsColumn.setCellValueFactory(new PropertyValueFactory<ReportsDayTable, String>("note"));
        dateReportsColumn.setCellValueFactory(new PropertyValueFactory<ReportsDayTable, Date>("date"));
        numberReportsColumn.setCellValueFactory(new PropertyValueFactory<ReportsDayTable, Long>("number"));
        //newPriceArrivalColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        /*if ("ROLE_ADMIN".equals(auth.toString())) {
        residueArrivalColumn.setCellValueFactory(new PropertyValueFactory<ArrivalTable, BigDecimal>("residue"));
        }*/
        ReportsDayTable reportsDayTable = new ReportsDayTable();
        for (int i = 0; i < 1; i++) {
            //BigDecimal price = goods.getPrice();
            //BigDecimal amount = decimal("###.###", Double.parseDouble(weight));
            if (cr.getCashIn() != null) {
                reportsDayTable.setSumIn(cr.getCashIn().getSumCash());
                reportsDayTable.setNote(cr.getCashIn().getNote());
            }
            if (cr.getCashOut() != null) {
                reportsDayTable.setSumOut(cr.getCashOut().getSumCash());
                reportsDayTable.setNote(cr.getCashOut().getNote());
            }
            if (cr.getArrival() != null) {
                reportsDayTable.setSumInvoice(cr.getArrival().getSumInvoice());
                reportsDayTable.setNote(cr.getArrival().getNote() + " / " + cr.getArrival().getNumberWaybill());
                reportsDayTable.setNumber(cr.getArrival().getIdArrival());
            }
            if (cr.getArrivalDish() != null) {
                reportsDayTable.setSumInvoice(cr.getArrivalDish().getSumInvoice());
                reportsDayTable.setNote("Продукт " + " / " + cr.getArrivalDish().getNumberWaybill());
                reportsDayTable.setNumber(cr.getArrivalDish().getIdArrivalDish());
            }
            if ("ROLE_ADMIN".equals(auth.toString())) {
                if (cr.getArrival() != null) {
                    reportsDayTable.setSumArrival(cr.getArrival().getSumArrival());
                    reportsDayTable.setNote(cr.getArrival().getNote() + " / " + cr.getArrival().getNumberWaybill());
                    reportsDayTable.setNumber(cr.getArrival().getIdArrival());
                }
            }
            if (cr.getWriteOff() != null) {
                reportsDayTable.setSumCancel(cr.getWriteOff().getSum());
                reportsDayTable.setNote(cr.getWriteOff().getNote());
                reportsDayTable.setNumber(cr.getWriteOff().getIdWriteoff());
            }

            reportsDayTable.setDate(cr.getDate());

            /*if ("ROLE_ADMIN".equals(auth.toString())) {
            reportsDayTable.setResidue(goods.getResidue());
            }*/
            // заполняем таблицу данными
            reportsDayData.add(reportsDayTable);
        }
        tableReportsDay.setItems(reportsDayData);
    }

    @FXML
    private void viewReports() {
        ReportsDayTable table = new ReportsDayTable();
        table = tableReportsDay.getSelectionModel().getSelectedItem();
        if (table.getSumArrival() != null) {
            //new ArrivalReportsWindow(table.getNumber());
            ViewArrivalFromReportsWindow arw = new ViewArrivalFromReportsWindow();
            arw.view(table.getNumber());
        }
        if (table.getSumCancel() != null) {
            ViewCancelFromReportsWindow crw = new ViewCancelFromReportsWindow();
            crw.view(table.getNumber());
        }
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
                //Date nowDate = new Date();
                dateReport.setValue(LocalDate.now());
                if ("ROLE_USER".equals(auth.toString())) {
                    arrivalReportsColumn.setVisible(false);
                }
                //getCRDate(LocalDate.now());
            }
        });

    }
}
