/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ml.dialog.DialogAlert;
import ml.model.Arrival;
import ml.model.ArrivalList;
import ml.model.CaseRecord;
import ml.model.CategoryGoods;
import ml.model.Check;
import ml.model.CheckList;
import ml.model.GeneralReportsModel;
import ml.model.Goods;
import ml.model.GoodsAccounting;
import ml.model.Writeoff;
import ml.model.WriteoffList;
import ml.query.accounting.AllListGoodsAccounting;
import ml.query.accounting.GoodAccountingByCode;
import ml.query.accounting.LastDateAccounting;
import ml.query.accounting.ListGoodsForAccounting;
import ml.query.accounting.SpareMoneyDateByDate;
import ml.query.arrival.BetweenDatesArrival;
import ml.query.cancellation.BetweenDatesCancellation;
import ml.query.caserecord.BetweenDatesCaseRecords;
import ml.query.categoryGood.CategoryGoodList;
import ml.query.categoryGood.CategoryGoodsByName;
import ml.query.check.BetweenDatesCheck;
import ml.query.check.CheckListByIdGood;
import ml.query.goods.GoodsListByCategory;
import ml.table.ReportsGeneralTable;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class ReportsGeneralController implements Initializable {

    @FXML
    private TableView<ReportsGeneralTable> tableReportsGeneral;
    @FXML
    private TableColumn<ReportsGeneralTable, String> nameColumn;
    @FXML
    private TableColumn<ReportsGeneralTable, BigDecimal> sumPriceAmountColumn;
    @FXML
    private TableColumn<ReportsGeneralTable, BigDecimal> sumProfitColumn;
    @FXML
    private TableColumn<ReportsGeneralTable, BigDecimal> quantityAfterAccountingColumn;
    @FXML
    private TableColumn<ReportsGeneralTable, BigDecimal> quantitySoldColumn;
    @FXML
    private TableColumn<ReportsGeneralTable, BigDecimal> quantityArrivalColumn;
    @FXML
    private TableColumn<ReportsGeneralTable, BigDecimal> quantityCancelColumn;
    @FXML
    private TableColumn<ReportsGeneralTable, BigDecimal> balanceColumn;
    @FXML
    private ComboBox<String> categoryGood;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;
    @FXML
    private Button okReports;
    @FXML
    private Label sumSalesLabel;
    @FXML
    private Label sumArrivalLabel;
    @FXML
    private Label sumCancellLabel;
    @FXML
    private Label sumGoodSalesLabel;
    @FXML
    private Label sumGoodArrivalLabel;
    @FXML
    private Label sumGoodCancellLabel;
    @FXML
    private Label sumProfitLabel;
    @FXML
    private Label sumProfitPeriodLabel;
    @FXML
    private Label spareLabel;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab reportOfGoodsTabPane;
    @FXML
    private Tab reportOfMoneyTabPane;
    @FXML
    private Tab reportMoveOfGoodsTabPane;

    private String textComboBox;
    private List<CategoryGoods> categoryList;
    private CheckList chl;
    private List<CheckList> checkList;
    private List<Check> check;
    private List<Arrival> arrival;
    private List<ArrivalList> arrivalList;
    private List<WriteoffList> cancellation;
    private List<WriteoffList> cancellationList;
    private List<CaseRecord> caseRecords;
    private BigDecimal sumProfitFromTable = new BigDecimal(0.00);
    private CategoryGoodList categoryGoodList = new CategoryGoodList();
    private ObservableList<String> options = FXCollections.observableArrayList();
    private CategoryGoodsByName categoryGoodsByName = new CategoryGoodsByName();
    private CheckListByIdGood checkListByIdGood = new CheckListByIdGood();
    private CategoryGoods categoryGoods = new CategoryGoods();
    private Goods goods = new Goods();
    private List<Goods> listGoods;
    private GoodsListByCategory goodsListByCategory = new GoodsListByCategory();
    private ObservableList<ReportsGeneralTable> reportsGeneralData = FXCollections.observableArrayList();
    private BetweenDatesCheck betweenDatesCheck = new BetweenDatesCheck();
    private BetweenDatesArrival betweenDatesArrival = new BetweenDatesArrival();
    private BetweenDatesCancellation betweenDatesCancellation = new BetweenDatesCancellation();
    private BetweenDatesCaseRecords betweenDatesCaseRecords = new BetweenDatesCaseRecords();
    private DialogAlert dialogAlert = new DialogAlert();
    private Date startDate;

    private GeneralReportsModel generalReportsModel = new GeneralReportsModel();

    @FXML
    private void getCategoryGood(ActionEvent event) {

        //возврат выбранной из списка id_category
        textComboBox = categoryGood.getValue();
        if (textComboBox != null) {
            categoryGoodsByName.setName(textComboBox);
            categoryGoods = categoryGoodsByName.displayResult();
            System.out.println("ml.controller.ReportsGeneralController.getCategoryGood()");
            System.out.println("Категория : " + categoryGoods.getName());
        }

    }

    @FXML
    private void getDateFrom(ActionEvent event) {
        //getDate(dateFrom.getValue());
    }

    @FXML
    private void getDateTo(ActionEvent event) {
        //getDate(dateTo.getValue());
    }

    @FXML
    private void getOk(ActionEvent event) {
        if (reportOfGoodsTabPane.isSelected()) {
            if (textComboBox == null) {
                dialogAlert.alert("Внимание!!!", "", "Выберите категорию товара");

            } else {
                reportsOfGoods();

            }
        }
        if (reportOfMoneyTabPane.isSelected()) {
            //Convert LocalDate to Date
            Date dateStart = Date.from(dateFrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dateEnd = Date.from(dateTo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            spareMoneyDateByDate(dateStart, dateEnd);
        }
        if (reportMoveOfGoodsTabPane.isSelected()) {
            reportsMoveOfGoods();
        }
    }

    //Отчет по движению товара
    private void reportsMoveOfGoods() {

        BigDecimal sumCheck = new BigDecimal(0.00);
        BigDecimal sumArrival = new BigDecimal(0.00);
        BigDecimal sumCancellation = new BigDecimal(0.00);

        Arrival a;
        Writeoff w;
        //список CaseRecords по дате
        betweenDatesCaseRecords.setDate(dateFrom.getValue().toString(), dateTo.getValue().toString());

        betweenDatesCaseRecords.setDates(dateFrom.getValue(), dateTo.getValue());

        caseRecords = betweenDatesCaseRecords.displayResult();

        //список чеков по дате
        betweenDatesCheck.setDateMoveGoods(dateFrom.getValue(), dateTo.getValue());

        for (int i = 0; i < caseRecords.size(); i++) {
            a = caseRecords.get(i).getArrival();
            w = caseRecords.get(i).getWriteOff();
            if (a != null) {
                sumArrival = sumArrival.add(a.getSumArrival());
            }
            if (w != null) {
                sumCancellation = sumCancellation.add(w.getSum());
            }
        }

        sumSalesLabel.setText(betweenDatesCheck.displayResult().toString());
        sumArrivalLabel.setText(sumArrival.toString());
        sumCancellLabel.setText(sumCancellation.toString());
        sumProfitPeriodLabel.setText(betweenDatesCaseRecords.displayResultX().toString());

    }

    //Добавить  00:00:00
    private Date addStartDay(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();

    }

    //Добавить  23:59:59
    private Date addEndDay(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();

    }

    //Отчет по товару
    private void reportsOfGoods() {
        List<String> nameCheck = new ArrayList<String>();
        List<BigDecimal> amountCheck = new ArrayList<BigDecimal>();

        List<String> nameNewCheck = new ArrayList<String>();
        List<BigDecimal> amountNewCheck = new ArrayList<BigDecimal>();
        List<String> nameArrival = new ArrayList<String>();
        List<BigDecimal> amountArrival = new ArrayList<BigDecimal>();

        List<String> nameNewArrival = new ArrayList<String>();
        List<BigDecimal> amountNewArrival = new ArrayList<BigDecimal>();
        List<String> nameCancell = new ArrayList<String>();
        List<BigDecimal> amountCancell = new ArrayList<BigDecimal>();
        List<String> nameNewCancell = new ArrayList<String>();
        List<BigDecimal> amountNewCancell = new ArrayList<BigDecimal>();
        List<BigDecimal> balance = new ArrayList<BigDecimal>();
        List<BigDecimal> balanceArrival = new ArrayList<BigDecimal>();
        List<BigDecimal> balanceCancell = new ArrayList<BigDecimal>();

        List<BigDecimal> balanceNew = new ArrayList<BigDecimal>();
        List<BigDecimal> profit = new ArrayList<BigDecimal>();
        List<BigDecimal> profitNew = new ArrayList<BigDecimal>();

        BigDecimal sumCheck = new BigDecimal(0.00);
        BigDecimal sumArrival = new BigDecimal(0.00);
        BigDecimal sumCancellation = new BigDecimal(0.00);

        List<BigDecimal> quantAfterAccountingNew = new ArrayList<BigDecimal>();
        List<BigDecimal> quantAfterAccountingCheck = new ArrayList<BigDecimal>();
        List<BigDecimal> quantAfterAccountingArrival = new ArrayList<BigDecimal>();
        List<BigDecimal> quantAfterAccountingCancel = new ArrayList<BigDecimal>();

        GoodAccountingByCode goodAccountingByCode = new GoodAccountingByCode();
        AllListGoodsAccounting lga = new AllListGoodsAccounting();

        List<GoodsAccounting> listGoodsAccounting = new ArrayList<>();

        String nCheck;
        BigDecimal amCheck;
        String nArrival;
        BigDecimal amArrival;
        String nCancell;
        BigDecimal amCancell;
        BigDecimal pr;
        BigDecimal b;
        BigDecimal a;

        //удаление строк в таблице
        for (int i = -1; i < tableReportsGeneral.getItems().size(); i++) {
            tableReportsGeneral.getItems().clear();
        }

        //Convert LocalDate to Date
        Date dateStart = Date.from(dateFrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateEnd = Date.from(dateTo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        //список чеков по дате
        betweenDatesCheck.setDate(dateFrom.getValue(), dateTo.getValue(), categoryGoods);
        checkList = betweenDatesCheck.displayResultPlus();

        //список прихода по дате
        betweenDatesArrival.setDate(dateFrom.getValue(), dateTo.getValue(), categoryGoods);
        arrivalList = betweenDatesArrival.displayResultPlus();

        //список списания по дате
        betweenDatesCancellation.setDate(dateFrom.getValue(), dateTo.getValue(), categoryGoods);
        cancellationList = betweenDatesCancellation.displayResultPlus();

        //Список информации товаров в таблице учета
        listGoodsAccounting = lga.allListGoodsForAccounting();

//        GoodsAccounting findGoodsAccounting = listGoodsAccounting.stream()
//                .filter(ga -> "James".equals(ga.getGoods().getName()))
//                .findAny()
//                .orElse(null);

        //Вывод списка проданного товара по категории
        for (int i = 0; i < checkList.size(); i++) {
            //Вывод на экран если категориии одинаковы с выбранной категорией
            if (categoryGoods == checkList.get(i).getGoods().getCategoryGoods()) {

                nameCheck.add(checkList.get(i).getGoods().getName());
                amountCheck.add(checkList.get(i).getAmount());
                profit.add(checkList.get(i).getProfit());
                balance.add(checkList.get(i).getGoods().getResidue());

//                goodAccountingByCode.setCode(checkList.get(i).getGoods());
//                if (goodAccountingByCode.displayResult() != null) {
//                    quantAfterAccountingCheck.add(goodAccountingByCode.displayResult().getResidueNew());
//                } else {
//                    quantAfterAccountingCheck.add(new BigDecimal("0.00"));
//                }
                String code = checkList.get(i).getGoods().getCode();
                GoodsAccounting findGoodsAcc = listGoodsAccounting.stream()
                        .filter(ga -> code.equals(ga.getGoods().getCode()))
                        .findAny()
                        .orElse(null);
                if (findGoodsAcc != null) {
                    quantAfterAccountingCheck.add(findGoodsAcc.getResidueNew());
                } else {
                    quantAfterAccountingCheck.add(new BigDecimal("0.00"));
                }

            }
        }

        //Сумма проданного товара по категории
        sumCheck = betweenDatesCheck.displayResultSumCheck();

        //Сумма приходного товара по категории
        sumArrival = betweenDatesArrival.displayResultSumArrival();

        //Сумма списанного товара по категории
        sumCancellation = betweenDatesCancellation.displayResultSumCancellation();

        //Вывод списка приходного товара по категории
        for (int i = 0; i < arrivalList.size(); i++) {
            //Вывод на экран если категориии одинаковы с выбранной категорией
            if (categoryGoods == arrivalList.get(i).getGoods().getCategoryGoods()) {

                nameArrival.add(arrivalList.get(i).getGoods().getName());
                amountArrival.add(arrivalList.get(i).getAmount());
                balanceArrival.add(arrivalList.get(i).getGoods().getResidue());

//                goodAccountingByCode.setCode(arrivalList.get(i).getGoods());
//
//                if (goodAccountingByCode.displayResult() != null) {
//                    quantAfterAccountingArrival.add(goodAccountingByCode.displayResult().getResidueNew());
//                } else {
//                    quantAfterAccountingArrival.add(new BigDecimal("0.00"));
//                }
                String code = arrivalList.get(i).getGoods().getCode();
                GoodsAccounting findGoodsAcc = listGoodsAccounting.stream()
                        .filter(ga -> code.equals(ga.getGoods().getCode()))
                        .findAny()
                        .orElse(null);
                if (findGoodsAcc != null) {
                    quantAfterAccountingArrival.add(findGoodsAcc.getResidueNew());
                } else {
                    quantAfterAccountingArrival.add(new BigDecimal("0.00"));
                }
                //Сумма приходного товара по категории
                //sumArrival = sumArrival.add(arrivalList.get(i).getGoods().getPriceOpt().multiply(arrivalList.get(i).getAmount()));
            }
        }

        //Вывод списка списанного товара по категории
        for (int i = 0; i < cancellationList.size(); i++) {
            //Вывод на экран если категориии одинаковы с выбранной категорией
            if (categoryGoods == cancellationList.get(i).getGoods().getCategoryGoods()) {

                nameCancell.add(cancellationList.get(i).getGoods().getName());
                amountCancell.add(cancellationList.get(i).getAmount());
                balanceCancell.add(cancellationList.get(i).getGoods().getResidue());

//                goodAccountingByCode.setCode(cancellationList.get(i).getGoods());
//
//                if (goodAccountingByCode.displayResult() != null) {
//                    quantAfterAccountingCancel.add(goodAccountingByCode.displayResult().getResidueNew());
//                } else {
//                    quantAfterAccountingCancel.add(new BigDecimal("0.00"));
//                }
                String code = cancellationList.get(i).getGoods().getCode();
                GoodsAccounting findGoodsAcc = listGoodsAccounting.stream()
                        .filter(ga -> code.equals(ga.getGoods().getCode()))
                        .findAny()
                        .orElse(null);
                if (findGoodsAcc != null) {
                    quantAfterAccountingCancel.add(findGoodsAcc.getResidueNew());
                } else {
                    quantAfterAccountingCancel.add(new BigDecimal("0.00"));
                }
                //Сумма приходного товара по категории
                //sumArrival = sumArrival.add(arrivalList.get(i).getGoods().getPriceOpt().multiply(arrivalList.get(i).getAmount()));
            }
        }

        //Запись в новый список названия проданного товара
        for (int i = 0; i < nameCheck.size(); i++) {
            nCheck = nameCheck.get(i);
            //amCheck = amountCheck.get(i);
            b = balance.get(i);
            a = quantAfterAccountingCheck.get(i);
            //если новый список пуст, то записать в него первое название товара и кол-во 0
            if (nameNewCheck.isEmpty()) {
                nameNewCheck.add(nCheck);
                amountNewCheck.add(new BigDecimal(0.00));
                amountNewArrival.add(new BigDecimal(0.00));
                amountNewCancell.add(new BigDecimal(0.00));
                profitNew.add(new BigDecimal(0.00));
                balanceNew.add(b);
                quantAfterAccountingNew.add(a);
            }
            //Запись в новый List не одинаковых названий продуктов
            for (int j = 0; j < nameNewCheck.size(); j++) {

                if (!nameNewCheck.contains(nCheck)) {
                    nameNewCheck.add(nCheck);
                    amountNewCheck.add(new BigDecimal(0.00));
                    amountNewArrival.add(new BigDecimal(0.00));
                    amountNewCancell.add(new BigDecimal(0.00));
                    profitNew.add(new BigDecimal(0.00));
                    balanceNew.add(b);
                    quantAfterAccountingNew.add(a);

                }
            }
        }

        //Запись в новый список названия приходного товара
        for (int i = 0; i < nameArrival.size(); i++) {
            nArrival = nameArrival.get(i);
            //amArrival = amountArrival.get(i);
            b = balanceArrival.get(i);
            a = quantAfterAccountingArrival.get(i);
            //если новый список пуст, то записать в него первое название товара и кол-во 0
            if (nameNewCheck.isEmpty()) {
                nameNewCheck.add(nArrival);
                amountNewCheck.add(new BigDecimal(0.00));
                amountNewArrival.add(new BigDecimal(0.00));
                amountNewCancell.add(new BigDecimal(0.00));
                profitNew.add(new BigDecimal(0.00));
                balanceNew.add(b);
                quantAfterAccountingNew.add(a);
            }
            //Запись в новый List не одинаковых названий продуктов
            for (int j = 0; j < nameNewCheck.size(); j++) {

                if (!nameNewCheck.contains(nArrival)) {
                    nameNewCheck.add(nArrival);
                    amountNewCheck.add(new BigDecimal(0.00));
                    amountNewArrival.add(new BigDecimal(0.00));
                    amountNewCancell.add(new BigDecimal(0.00));
                    profitNew.add(new BigDecimal(0.00));
                    balanceNew.add(b);
                    quantAfterAccountingNew.add(a);

                }
            }
        }

        //Запись в новый список названия списанного товара
        for (int i = 0; i < nameCancell.size(); i++) {
            nCancell = nameCancell.get(i);
            //amCancell = amountCancell.get(i);
            b = balanceCancell.get(i);
            a = quantAfterAccountingCancel.get(i);

            //если новый список пуст, то записать в него первое название товара и кол-во 0
            if (nameNewCheck.isEmpty()) {
                nameNewCheck.add(nCancell);
                amountNewCheck.add(new BigDecimal(0.00));
                amountNewArrival.add(new BigDecimal(0.00));
                amountNewCancell.add(new BigDecimal(0.00));
                profitNew.add(new BigDecimal(0.00));
                balanceNew.add(b);
                quantAfterAccountingNew.add(a);

            }
            //Запись в новый List не одинаковых названий продуктов
            for (int j = 0; j < nameNewCheck.size(); j++) {

                if (!nameNewCheck.contains(nCancell)) {
                    nameNewCheck.add(nCancell);
                    amountNewCheck.add(new BigDecimal(0.00));
                    amountNewArrival.add(new BigDecimal(0.00));
                    amountNewCancell.add(new BigDecimal(0.00));
                    profitNew.add(new BigDecimal(0.00));
                    balanceNew.add(b);
                    quantAfterAccountingNew.add(a);
                }
            }
        }

        //подссчет кол-ва и остатка товара по названию
        for (int i = 0; i < nameNewCheck.size(); i++) {
            String nn = nameNewCheck.get(i);

            for (int j = 0; j < nameCheck.size(); j++) {

                if (nn.equals(nameCheck.get(j))) {
                    BigDecimal soldGood;
                    BigDecimal soldProfit;
                    BigDecimal arrivalGood;
                    BigDecimal cancellGood;
                    soldGood = amountNewCheck.get(i);
                    soldGood = soldGood.add(amountCheck.get(j));
                    soldProfit = profitNew.get(i);
                    soldProfit = soldProfit.add(profit.get(j));
                    arrivalGood = amountNewArrival.get(i);
                    arrivalGood = arrivalGood.add(new BigDecimal(0.00));
                    cancellGood = amountNewCancell.get(i);
                    cancellGood = cancellGood.add(new BigDecimal(0.00));
                    amountNewCheck.set(i, soldGood);
                    profitNew.set(i, soldProfit);
                    amountNewArrival.set(i, arrivalGood);
                }
            }
            for (int j = 0; j < nameArrival.size(); j++) {
                if (nn.equals(nameArrival.get(j))) {
                    BigDecimal arrivalGood;
                    arrivalGood = amountNewArrival.get(i);
                    arrivalGood = arrivalGood.add(amountArrival.get(j));
                    amountNewArrival.set(i, arrivalGood);
                }
            }
            for (int j = 0; j < nameCancell.size(); j++) {
                if (nn.equals(nameCancell.get(j))) {
                    BigDecimal cancellGood;
                    cancellGood = amountNewCancell.get(i);
                    cancellGood = cancellGood.add(amountCancell.get(j));
                    amountNewCancell.set(i, cancellGood);
                }
            }
        }

        sumGoodSalesLabel.setText(sumCheck.toString());
        sumGoodArrivalLabel.setText(sumArrival.toString());
        sumGoodCancellLabel.setText(sumCancellation.toString());

        //Расчет суммы прибыли
        sumProfitFromTable = profitNew.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        sumProfitLabel.setText(sumProfitFromTable.toString());

        //запись в таблицу
        for (int i = 0; i < nameNewCheck.size(); i++) {

            generalReportsModel.setNameColumn(nameNewCheck.get(i));
            generalReportsModel.setQuantityAfterAccountingColumn(new BigDecimal(quantAfterAccountingNew.get(i).toString()));
            generalReportsModel.setQuantitySoldColumn(new BigDecimal(amountNewCheck.get(i).toString()));
            generalReportsModel.setQuantityArrivalColumn(new BigDecimal(amountNewArrival.get(i).toString()));
            generalReportsModel.setQuantityCancelColumn(new BigDecimal(amountNewCancell.get(i).toString()));
            generalReportsModel.setBalanceColumn(balanceNew.get(i));
            generalReportsModel.setSumProfitColumn(new BigDecimal(profitNew.get(i).toString()));

            displayResult(generalReportsModel);
        }

    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayResult(GeneralReportsModel generalReportsModel) {

        nameColumn.setCellValueFactory(new PropertyValueFactory<ReportsGeneralTable, String>("name"));
        sumPriceAmountColumn.setCellValueFactory(new PropertyValueFactory<ReportsGeneralTable, BigDecimal>("sumPriceAmount"));
        sumProfitColumn.setCellValueFactory(new PropertyValueFactory<ReportsGeneralTable, BigDecimal>("sumProfit"));
        quantityAfterAccountingColumn.setCellValueFactory(new PropertyValueFactory<ReportsGeneralTable, BigDecimal>("quantityAfterAccounting"));
        quantitySoldColumn.setCellValueFactory(new PropertyValueFactory<ReportsGeneralTable, BigDecimal>("quantitySold"));
        quantityArrivalColumn.setCellValueFactory(new PropertyValueFactory<ReportsGeneralTable, BigDecimal>("quantityArrival"));
        quantityCancelColumn.setCellValueFactory(new PropertyValueFactory<ReportsGeneralTable, BigDecimal>("quantityCancel"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<ReportsGeneralTable, BigDecimal>("balance"));

        ReportsGeneralTable reports = new ReportsGeneralTable();
        for (int i = 0; i < 1; i++) {

            reports.setName(generalReportsModel.getNameColumn());
            reports.setQuantitySold(generalReportsModel.getQuantitySoldColumn());
            reports.setQuantityAfterAccounting(generalReportsModel.getQuantityAfterAccountingColumn());
            reports.setQuantityArrival(generalReportsModel.getQuantityArrivalColumn());
            reports.setQuantityCancel(generalReportsModel.getQuantityCancelColumn());
            reports.setBalance(generalReportsModel.getBalanceColumn());
            reports.setSumProfit(generalReportsModel.getSumProfitColumn());
            // заполняем таблицу данными
            reportsGeneralData.add(reports);
        }

        tableReportsGeneral.setItems(reportsGeneralData);
    }

    /**
     * Выводит лишние деньги с последнего учета
     */
    private void spareMoney() {

        // Дата последнего учета
        LastDateAccounting lda = new LastDateAccounting();
        lda.setDate();
        startDate = lda.getLastDateAccounting();
        String nowDateString = convertDate(startDate);
        String lastDateString = convertDate(new Date());
        if (lastDateString.equals(nowDateString)) {
            lda.setPenultimateDate();
            startDate = lda.getPenultimateLastDateAccounting();
        }

        SpareMoneyDateByDate smdbd = new SpareMoneyDateByDate();
        Calendar c = Calendar.getInstance();

        c.setTime(startDate);
        c.add(Calendar.DATE, 1); //same with c.add(Calendar.DAY_OF_MONTH, 1);
        startDate = c.getTime();

        System.out.println("Первая дата: " + lastDateString);
        System.out.println("Ыторая дата: " + nowDateString);

        smdbd.setDates(nowDateString, lastDateString);

        spareLabel.setText(smdbd.displayResult().toString());
    }

    /**
     * Выводит лишние деньги по датам
     *
     * @param startD
     * @param lastD
     */
    private void spareMoneyDateByDate(Date startD, Date lastD) {

        // Дата последнего учета
        LastDateAccounting lda = new LastDateAccounting();
        lda.setDate();
        startDate = lda.getLastDateAccounting();
        String nowDateString = convertDate(startD);
        String lastDateString = convertDate(lastD);
        if (lastDateString.equals(nowDateString)) {
            lda.setPenultimateDate();
            startDate = lda.getPenultimateLastDateAccounting();
        }

        SpareMoneyDateByDate smdbd = new SpareMoneyDateByDate();
        Calendar c = Calendar.getInstance();

        c.setTime(startDate);
        c.add(Calendar.DATE, 1); //same with c.add(Calendar.DAY_OF_MONTH, 1);
        startDate = c.getTime();

        System.out.println("Первая дата: " + lastDateString);
        System.out.println("Ыторая дата: " + nowDateString);

        smdbd.setDates(nowDateString, lastDateString);

        spareLabel.setText(smdbd.displayResult().toString());
    }

    /**
     * Конвертирование даты
     */
    private String convertDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dateFrom.setValue(LocalDate.now());
        dateTo.setValue(LocalDate.now());

        //Список всего товара
        System.out.println("Start: Список категорий всего товара");
        categoryList = categoryGoodList.getList();
        categoryList.forEach((cg) -> {
            options.add(cg.getName());
        });
        categoryGood.setItems(options);
        System.out.println("Fin: Список категорий всего товара");
        System.out.println("");

        //Заполнение ComboBox в зависимости от выбраного Tab
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                String nameTab = ov.getValue().getId();

                switch (nameTab) {
                    case "reportOfGoodsTabPane":
                        categoryGood.getItems().clear();

                        //Список всего товара
                        categoryList = categoryGoodList.getList();
                        categoryList.forEach((cg) -> {
                            options.add(cg.getName());
                        });
                        categoryGood.setItems(options);
                        break;
                    case "reportOfMoneyTabPane":
                        spareMoney();
                        categoryGood.getItems().clear();
                        break;
                    case "reportMoveOfGoodsTabPane":

                        categoryGood.getItems().clear();
                        break;
                    default:
                        break;
                }
            }

        });
    }

}
