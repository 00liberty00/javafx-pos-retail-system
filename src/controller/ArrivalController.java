/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.Pair;
import ml.Ml_FX;
import ml.authentication.GrantedAuth;
import ml.barcode.BarcodeConv;
import ml.dialog.DialogAlert;
import ml.dialog.DialogChoose;
import ml.dialog.DialogCombobox;
import ml.dialog.DialogTextInput;
import ml.dialog.DialogTextInputDontHidden;
import ml.kkt.KKT;
import ml.model.Arrival;
import ml.model.ArrivalList;
import ml.model.Barcode;
import ml.model.CaseRecord;
import ml.model.CashOut;
import ml.model.CategoryGoods;
import ml.model.Goods;
import ml.model.UserSwing;
import ml.printBarcode.PrintBarcode;
import ml.printBarcode.PrintPriceTag;
import ml.query.arrival.AddArrival;
import ml.query.arrival.AddArrivalList;
import ml.query.arrival.LastArrival;
import ml.query.barcode.AddBarcode;
import ml.query.barcode.LastBarcode;
import ml.query.caserecord.AddCaseRecord;
import ml.query.caserecord.DateCaseRecord;
import ml.query.cashout.AddCashOut;
import ml.query.categoryGood.AddCategoryGood;
import ml.query.categoryGood.CategoryGoodList;
import ml.query.categoryGood.CategoryGoodsByName;
import ml.query.goods.GoodByCode;
import ml.query.goods.NewGood;
import ml.query.goods.QueryAllGoodsList;
import ml.query.goods.UpdateGood;
import ml.query.goods.UpdateResidueGood;
import ml.query.user.IdUserByName;
import ml.table.ArrivalTable;
import ml.telegram.Bot;
import ml.util.CheckConnection;
import ml.util.CheckInternetConnection;
import ml.util.MoneyRegexp;
import ml.util.PlayAudio;
import ml.util.RegexpNameGoods;
import ml.util.SearchBarcode;
import ml.xml.XMLSearchAddress;
import ml.xml.XMLSettings;
import org.apache.commons.lang3.ArrayUtils;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class ArrivalController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private ComboBox<String> categoryGood;
    @FXML
    private TextField codeGood;
    @FXML
    private TextField nameGood;
    @FXML
    private TextField allowanceTextField;
    @FXML
    private TextField invoicePrice;
    @FXML
    private TextField amountGood;
    @FXML
    private TextField newPrice;
    @FXML
    private TextField sumCheck;
    @FXML
    private TextField sumCheckWithAllowance;
    @FXML
    private TextField paymentCheck;
    @FXML
    private TextField numberInvoice;
    @FXML
    private TableView<ArrivalTable> tableArrival;
    @FXML
    private TableColumn<ArrivalTable, String> codeArrivalColumn;
    @FXML
    private TableColumn<ArrivalTable, String> nameArrivalColumn;
    @FXML
    private TableColumn<ArrivalTable, BigDecimal> oldPriceArrivalColumn;
    @FXML
    private TableColumn<ArrivalTable, BigDecimal> newPriceArrivalColumn;
    @FXML
    private TableColumn<ArrivalTable, BigDecimal> invoicePriceArrivalColumn;
    @FXML
    private TableColumn<ArrivalTable, BigDecimal> amountArrivalColumn;
    @FXML
    private TableColumn<ArrivalTable, Boolean> markingArrivalColumn;
    @FXML
    private TableColumn<ArrivalTable, BigDecimal> residueArrivalColumn;
    @FXML
    private CheckBox weightCheckBox;
    @FXML
    private CheckBox markingCheckBox;
    @FXML
    private Button newArrival;
    @FXML
    private Button okArrival;
    @FXML
    private Button printBarcodeButton;
    @FXML
    private Button printPriceTagButton;
    @FXML
    private Label getConnDB;

    private CategoryGoodList categoryGoodList = new CategoryGoodList();
    private List<CategoryGoods> categoryList;
    private String textComboBox;
    private CategoryGoods category = new CategoryGoods();
    private ObservableList<String> options = FXCollections.observableArrayList();
    private ObservableList<ArrivalTable> arrivalData = FXCollections.observableArrayList();
    private List<Goods> goodsList;
    private BarcodeConv barcodeConv = new BarcodeConv();
    private String weight = "1";
    private GrantedAuth grantedAuth = new GrantedAuth();
    private Object auth = grantedAuth.role();
    private QueryAllGoodsList allGoodsList = new QueryAllGoodsList();
    private BigDecimal allowance = new BigDecimal(0);
    private BigDecimal invoice = new BigDecimal(0);
    private BigDecimal amount = new BigDecimal(1);
    private LastArrival lastArrival = new LastArrival();
    private DialogTextInput dialogTextInput = new DialogTextInput();
    private Stage primaryStage;
    private int selectRow = -1;
    private DialogAlert dialogAlert = new DialogAlert();
    private DialogChoose dialogChoose = new DialogChoose();
    private XMLSettings xmls = new XMLSettings();
    private DialogCombobox dialogCombobox = new DialogCombobox();
    private BigDecimal sumInvoice = new BigDecimal("0.00");
    private CheckConnection checkConnection = new CheckConnection();
    private Timeline timeline = new Timeline();
    boolean firstState = false;
    boolean lastState = false;
    private KKT kkt;
    private Bot bot;
    private RegexpNameGoods regexpNameGoods = new RegexpNameGoods();

    /**
     * Поиск товара по коду
     *
     * @param event
     */
    @FXML
    private void getCode(ActionEvent event) {
        String code = codeGood.getText();

        if (code.length() <= 13) {
            boolean weightGood = weightCheckBox.isSelected();

            if (weightGood == true) {

                boolean firstLetter = barcodeConv.firstLetters(code);
                if (firstLetter == true) {
                    code = barcodeConv.sixLetter(code);
                    weight = barcodeConv.fourLetter(codeGood.getText());
                    //residueGoodsTextField.setText(weight);

                }
                firstLetter = false;
                weightGood = false;
                amountGood.setText(weight);
                amount = new BigDecimal(weight);
            } else {
                amountGood.setText("1");
            }
            codeGood.requestFocus();
            codeGood.setText("");
            getGoodCode(code);
        } else {
            dialogAlert.alert("Внимание!!!", "", "Кол-во символов в коде не должно превышать 13-ти");

        }

    }

    /**
     * Поиск товара по наименованию
     *
     * @param event
     */
    @FXML
    private void getName(ActionEvent event) {
        String nameOrCode = nameGood.getText();
        boolean weightGood = weightCheckBox.isSelected();

        amountGood.setText(weight);
        amountGood.requestFocus();
        amountGood.selectAll();

        if (weightGood == true) {
            amount = new BigDecimal(weight);
        } else {
            amount = new BigDecimal("1");
        }
        nameGood.setText("");

        //Проверка первого символа
        if (regexpNameGoods.nameGoods(nameOrCode) == true) {
            getGoodName(nameOrCode);

        } else {
            getGoodCode(nameOrCode);

        }
    }

    /**
     * Берет текст из ComboBox
     */
    @FXML
    private void getTextCategory() {
        textComboBox = categoryGood.getValue();
        choose(textComboBox);

    }

    /**
     * Берет значение из поля Надбавка
     */
    @FXML
    private void getAllowance() {
        allowance = decimal("###.##", Double.parseDouble(allowanceTextField.getText()));
        codeGood.requestFocus();
        codeGood.setEditable(true);
        nameGood.setEditable(true);
        amountGood.setEditable(true);
        invoicePrice.setEditable(true);
        allowanceTextField.setEditable(true);
        newPrice.setEditable(true);
    }

    /**
     * Берет значение из поля Цена в накладной
     */
    @FXML
    private void getInvoice() {
        Integer roundingPrice = xmls.getRounding().intValue();

        invoicePrice.selectAll();
        String s = invoicePrice.getText();
        //Замена , на .
        String inv = s.replace(',', '.');

        int i = tableArrival.getItems().size();   // кол-во строк в таблице

        //если кол-во строк не 0
        if (i != 0) {
            int row = i - 1;                        // последняя строка

            //Кол-во * цена
            //BigDecimal price = new BigDecimal(arrivalData.get(row).getOldPrice().toString());
            BigDecimal invoice = decimal("###.###", Double.parseDouble(inv));
            //BigDecimal sum = decimal("###.###", Double.parseDouble(price.multiply(amount).toString()));;

            //Если выбрана строка то изменить в ней значение Цены в товаре
            if (selectRow >= 0) {

                //Вставка данных в последнюю ячейку "цена в накладной" таблицы
                arrivalData.get(selectRow).setInvoicePrice(invoice);
                BigDecimal newPr = allowance.divide(new BigDecimal(100)).
                        multiply(invoice).add(invoice);
                arrivalData.get(selectRow).setNewPrice(newPr.setScale(roundingPrice, BigDecimal.ROUND_HALF_UP)); // .setScale(2, BigDecimal.ROUND_HALF_UP) округление до 2-х знаков после запятой
                tableArrival.getItems().set(selectRow, arrivalData.get(selectRow));

                tableArrival.getSelectionModel().clearSelection();
                selectRow = -1;
            } else {
                arrivalData.get(row).setInvoicePrice(invoice);
                BigDecimal newPr = allowance.divide(new BigDecimal(100)).multiply(invoice).add(invoice);
                arrivalData.get(row).setNewPrice(newPr.setScale(roundingPrice, BigDecimal.ROUND_HALF_UP));
                tableArrival.getItems().set(row, arrivalData.get(row));
            }

            //считает сумму прихода
            sumCheckWithAllowance();
            //считает сумму накладной
            sumCheck();

            codeGood.requestFocus();

            tableArrival.getSelectionModel().clearSelection();

            //this.invoice = new BigDecimal(0);
        }
    }

    /**
     * Берет значение из поля Кол-во товара
     */
    @FXML
    private void getAmount() {
        amountGood.selectAll();
        String s = amountGood.getText();

        //Замена , на .
        String am = s.replace(',', '.');

        int i = tableArrival.getItems().size();   // кол-во строк в таблице
        //если кол-во строк не 0
        if (i != 0) {
            int row = i - 1;                        // последняя строка
            BigDecimal amount = decimal("###.###", Double.parseDouble(am));
            //Если выбрана строка то изменить в ней значение кол-ва
            if (selectRow >= 0) {
                //Вставка данных в выбранную строку в ячейку "кол-во"
                arrivalData.get(selectRow).setAmount(amount);
                //arrivalData.get(row).setNewPrice(allowance.multiply(invoice));s
                tableArrival.getItems().set(selectRow, arrivalData.get(selectRow));

                tableArrival.getSelectionModel().clearSelection();
                selectRow = -1;
            } else {
                //Вставка данных в последнюю ячейку "кол-во" и "сумма строки" таблицы
                arrivalData.get(row).setAmount(amount);
                //arrivalData.get(row).setNewPrice(allowance.multiply(invoice));s
                tableArrival.getItems().set(row, arrivalData.get(row));
            }

            //считает сумму прихода
            sumCheckWithAllowance();
            //считает сумму накладной
            sumCheck();

            codeGood.requestFocus();

            this.amount = new BigDecimal(1);

            tableArrival.getSelectionModel().clearSelection();

        }
    }

    /**
     * Берет значение из поля Изменить новую цену
     */
    @FXML
    private void getNewPrice() {
        newPrice.selectAll();
        String s = newPrice.getText();
        //Замена , на .
        String inv = s.replace(',', '.');

        int i = tableArrival.getItems().size();   // кол-во строк в таблице
        //если кол-во строк не 0
        if (i != 0) {
            int row = i - 1;                        // последняя строка

            //Кол-во * цена
            //BigDecimal price = new BigDecimal(arrivalData.get(row).getOldPrice().toString());
            BigDecimal newPrice = decimal("###.###", Double.parseDouble(inv));
            //BigDecimal sum = decimal("###.###", Double.parseDouble(price.multiply(amount).toString()));;

            //Если выбрана строка то изменить в ней значение новую цену
            if (selectRow >= 0) {
                //Вставка данных в последнюю ячейку "новая цена" таблицы
                arrivalData.get(selectRow).setNewPrice(newPrice);
                //arrivalData.get(selectRow).setNewPrice(allowance.divide(new BigDecimal(100)).multiply(invoice).add(invoice));
                tableArrival.getItems().set(selectRow, arrivalData.get(selectRow));

                tableArrival.getSelectionModel().clearSelection();
                selectRow = -1;
            } else {
                arrivalData.get(row).setNewPrice(newPrice);
                //arrivalData.get(row).setNewPrice(allowance.divide(new BigDecimal(100)).multiply(invoice).add(invoice));
                tableArrival.getItems().set(row, arrivalData.get(row));
            }

            //считает сумму прихода
            sumCheckWithAllowance();

            codeGood.requestFocus();

            tableArrival.getSelectionModel().clearSelection();

            //this.invoice = new BigDecimal(0);
        }
    }

    /**
     * Значение номера выбранной строки
     */
    @FXML
    private void getRow() {

        List<TablePosition> positions = tableArrival.getSelectionModel().getSelectedCells();
        if (!positions.isEmpty()) {

            int i = tableArrival.getItems().size();   // кол-во строк в таблице
            if (i > 0) {

                TablePosition pos = tableArrival.getSelectionModel().getSelectedCells().get(0);
                selectRow = pos.getRow();

//                            tableArrival.getSelectionModel().clearSelection();
            }
        }
    }

    /**
     * Кнопка Новый Приход обнуляет и приводит к первоначальному состоянию все
     * переменные и поля и тд в Приходе товара
     */
    @FXML
    private void newArrival() {
        //Список всего товара
        goodsList = allGoodsList.listGoods();
        allowance = new BigDecimal(0);
        invoice = new BigDecimal(0);
        codeGood.setText("");
        nameGood.setText("");
        allowanceTextField.setText("0");
        invoicePrice.setText("");
        amountGood.setText("");
        newPrice.setText("");
        sumCheck.setText("0.00");
        sumCheckWithAllowance.setText("0.00");
        paymentCheck.setText("0.00");
        numberInvoice.setText("№ ");

        //удаление строк в таблице
        for (int i = -1; i < tableArrival.getItems().size(); i++) {
            tableArrival.getItems().clear();

        }

    }

    /**
     * Кнопка ОК, запись в таблицы
     */
    @FXML
    private void okArrival() {

        int countTableRow = tableArrival.getItems().size();   // кол-во строк в таблице
        if (countTableRow != 0) {

            Arrival arrival = new Arrival();
            AddArrival addArrival = new AddArrival();
            AddArrivalList addArrivalList = new AddArrivalList();
            Goods goods = new Goods();
            UserSwing userSwing = new UserSwing();
            UpdateResidueGood updateResidueGood = new UpdateResidueGood();
            CategoryGoodsByName categoryGoodsByName = new CategoryGoodsByName();
            CategoryGoods cg = new CategoryGoods();
            UpdateGood updateGood = new UpdateGood();
            GoodByCode goodByCode = new GoodByCode();
            CaseRecord caseRecord = new CaseRecord();
            AddCaseRecord addCaseRecord = new AddCaseRecord();
            CashOut сashOut = new CashOut();
            AddCashOut addCashOut = new AddCashOut();
            MoneyRegexp moneyRegexp = new MoneyRegexp();

            IdUserByName idUserByName = new IdUserByName();
            Authentication authentication = SecurityContextHolder.getContext().
                    getAuthentication();
            String login = authentication.getName();
            Date date = new Date();
            List<Arrival> arrivalArrayList = new ArrayList<>();
            List<ArrivalList> arrivalListArrayList = new ArrayList<>();
            BigDecimal bg1, bg2;
            boolean markingGood = markingCheckBox.isSelected();

            String s = paymentCheck.getText();
            //Замена , на .
            String payment = s.replace(',', '.');
            //Проверка Оплата за товар должна быть 0,00 или больше
            if (moneyRegexp.check(payment) == true) {

                int res;
                bg1 = new BigDecimal(payment);
                bg2 = new BigDecimal(sumCheck.getText());
                res = bg1.compareTo(bg2); // compare bg1 with bg2
                if (res == 0) {

                    ////////////
                    idUserByName.setLoginUser(login);   //метод для idUser по логину
                    userSwing = idUserByName.displayResult(); //Возвращает пользователя

//                    arrival.setNote(categoryGood.getValue() + " / " + numberInvoice.getText());
                    arrival.setNote(categoryGood.getValue());
                    arrival.setDate(date);
                    arrival.setSumArrival(new BigDecimal(sumCheckWithAllowance.getText()));
                    arrival.setSumInvoice(new BigDecimal(sumCheck.getText()));
//                    userSwing.getArrivals().add(arrival);
                    arrival.setUserSwing(userSwing);
                    arrival.setNumberWaybill(numberInvoice.getText());
                    //Создает запись в Таблице Arrival
                    addArrival.add(arrival);

                    int i = tableArrival.getItems().size();   // кол-во строк в таблице
                    lastArrival.get();                    //метод для номера последней записи в Arrival (Последний номер прихода)

                    categoryGoodsByName.setName(categoryGood.getValue());   //категори товара по названию
                    cg = categoryGoodsByName.displayResult();
                    for (int j = 0; j < i; j++) {
                        ArrivalList arrivalList = new ArrivalList();

                        //Товар по коду из таблицы
                        goodByCode.setCode(codeArrivalColumn.getCellData(j));

                        goods = goodByCode.displayResult();
                        arrivalList.setGoods(goods);
                        goods.getArrivalLists().add(arrivalList);
                        arrivalList.setAmount(amountArrivalColumn.getCellData(j));
                        arrivalListArrayList.add(arrivalList);
                        arrivalList.setArrival(lastArrival.displayResult());
                        arrivalList.setPriceOpt(invoicePriceArrivalColumn.getCellData(j));
                        arrivalList.setPrice(newPriceArrivalColumn.getCellData(j));
                        arrival.getArrivalLists().add(arrivalList);

                        updateResidueGood.update(null, arrivalList, null, true);

                        //Сохранение списка прихода в БД
                        addArrivalList.add(arrivalList);

                        goods.setName(nameArrivalColumn.getCellData(j));
                        goods.setPriceOpt(invoicePriceArrivalColumn.getCellData(j));
                        goods.setPrice(newPriceArrivalColumn.getCellData(j));
                        categoryGoodsByName.setName(categoryGood.getValue());   //категория товара по названию
                        cg = categoryGoodsByName.displayResult();
                        goods.setCategoryGoods(cg);
                        goods.setMarking(markingGood);

                        //Обновляет категорию товара
                        updateGood.update(goods);
                    }
                    ////////////

                    sumInvoice = new BigDecimal(payment);

                    //Оплата за товар, внести в CashOut
                    //Запись в CashOut
                    сashOut.setSumCash(sumInvoice);
                    сashOut.setNote(categoryGood.getValue());

                    bot.checkCashOutBot("Вывод в приходе: " + sumInvoice.toString(), categoryGood.getValue());
                    addCashOut.add(сashOut);

                    //Выводит из ККТ
                    kkt.cashOut(payment);

                    сashOut.getCaseRecord().add(caseRecord);
                    caseRecord.setCashOut(сashOut);
                    //Запись в CaseRecord
                    arrival.getCaseRecord().add(arrival);
                    caseRecord.setArrival(arrival);
                    caseRecord.setDate(date);

                    caseRecord.setUserSwing(userSwing);
                    addCaseRecord.add(caseRecord);

                    bot.viewArrivalBot(arrival);

                    //Очищает все поля
                    //newArrival();
                    closeWindow();

                } // Если оплата за товар больше суммы накладной!!!
                else if (res == 1) {
                    dialogAlert.alert("Внимание!", "Оплата за товар", "Оплата за товар не может быть больше суммы накладной!");

                } // Если оплачена не вся сумма накладной, то разницу записать в долг
                else if (res == -1) {

                    //Если в оплате за накладную стоит 0, то
                    if (bg1.compareTo(BigDecimal.ZERO) == 0) {
                        //////////////////////////
                        dialogChoose.alert("Внимание!", "Оплата за товар", "Накладная раннее оплачена?");
                        boolean b = dialogChoose.display();
                        boolean okClicked = true;
                        //Если ДА
                        if (b == true) {

                            //Диалоговое окно выбора оплаты накладной
                            okClicked = dialogChooseSumInvoice(bg2);

                            //dialogCombobox.combo("Оплата", "Выберите оплату из списка", "Есть ли сумма оплаты в списке? ", choices);
                            //Оплата за товар, внести в CashOut
                            //Запись в CashOut
                            if (okClicked == true) {

                                ////////////
                                idUserByName.setLoginUser(login);   //метод для idUser по логину
                                userSwing = idUserByName.displayResult(); //Возвращает пользователя

//                                arrival.setNote(categoryGood.getValue() + " / " + numberInvoice.getText());
                                arrival.setNote(categoryGood.getValue());

                                arrival.setDate(date);
                                arrival.setSumArrival(new BigDecimal(sumCheckWithAllowance.getText()));
                                arrival.setSumInvoice(new BigDecimal(sumCheck.getText()));
//                                userSwing.getArrivals().add(arrival);
                                arrival.setUserSwing(userSwing);
                                arrival.setNumberWaybill(numberInvoice.getText());
                                //Создает запись в Таблице Arrival
                                addArrival.add(arrival);

                                int i = tableArrival.getItems().size();   // кол-во строк в таблице
                                lastArrival.get();                    //метод для номера последней записи в Arrival (Последний номер прихода)

                                categoryGoodsByName.setName(categoryGood.getValue());   //категори товара по названию
                                cg = categoryGoodsByName.displayResult();
                                for (int j = 0; j < i; j++) {
                                    ArrivalList arrivalList = new ArrivalList();

                                    //Товар по коду из таблицы
                                    goodByCode.setCode(codeArrivalColumn.getCellData(j));

                                    goods = goodByCode.displayResult();
                                    arrivalList.setGoods(goods);
                                    goods.getArrivalLists().add(arrivalList);
                                    arrivalList.setAmount(amountArrivalColumn.getCellData(j));
                                    arrivalList.setPrice(newPriceArrivalColumn.getCellData(j));
                                    arrivalList.setPriceOpt(invoicePriceArrivalColumn.getCellData(j));
                                    arrivalListArrayList.add(arrivalList);
                                    arrivalList.setArrival(lastArrival.displayResult());
                                    arrival.getArrivalLists().add(arrivalList);

                                    updateResidueGood.update(null, arrivalList, null, true);

                                    //Сохранение списка прихода в БД
                                    addArrivalList.add(arrivalList);

                                    goods.setName(nameArrivalColumn.getCellData(j));
                                    goods.setPriceOpt(invoicePriceArrivalColumn.getCellData(j));
                                    goods.setPrice(newPriceArrivalColumn.getCellData(j));
                                    categoryGoodsByName.setName(categoryGood.getValue());   //категория товара по названию
                                    cg = categoryGoodsByName.displayResult();
                                    goods.setCategoryGoods(cg);
                                    goods.setMarking(markingGood);

                                    //Обновляет категорию товара
                                    updateGood.update(goods);
                                }
                                ////////////

                                сashOut.setSumCash(sumInvoice);
                                сashOut.setNote(categoryGood.getValue());

                                bot.checkCashOutBot("Вывод в приходе: " + sumInvoice.toString(), categoryGood.getValue());
                                addCashOut.add(сashOut);

                                сashOut.getCaseRecord().add(caseRecord);
                                caseRecord.setCashOut(сashOut);
                                //Запись в CaseRecord
                                arrival.getCaseRecord().add(arrival);
                                caseRecord.setArrival(arrival);
                                caseRecord.setDate(date);

                                caseRecord.setUserSwing(userSwing);
                                addCaseRecord.add(caseRecord);

                                bot.viewArrivalBot(arrival);

                                /*//Если сумма оплаченных денег меньше чем сумма накладной, то остаток записать в долг
                            int ress;
                            ress = sumInvoice.compareTo(bg2); // compare sumInvoice with bg2
                            
                            if (ress == -1) {
                            //Выбор даты для долга
                            dateChooseForDebt(bg2, sumInvoice, arrival);
                            }*/
                                //Очищает все поля
//                            newArrival();
                                closeWindow();

                            }
                        } else {

                            //перенос следующих 9 строк из строки перед "Создает запись в таблице Arrival"
                            //////
                            idUserByName.setLoginUser(login);   //метод для idUser по логину
                            userSwing = idUserByName.displayResult(); //Возвращает пользователя

//                            arrival.setNote(categoryGood.getValue() + " / " + numberInvoice.getText());
                            arrival.setNote(categoryGood.getValue());

                            arrival.setDate(date);
                            arrival.setSumArrival(new BigDecimal(sumCheckWithAllowance.getText()));
                            arrival.setSumInvoice(new BigDecimal(sumCheck.getText()));
//                            userSwing.getArrivals().add(arrival);
                            arrival.setUserSwing(userSwing);
                            arrival.setNumberWaybill(numberInvoice.getText());
                            ///////

                            //Выбор даты для долга
                            sumInvoice = new BigDecimal(payment);

                            okClicked = dateChooseForDebt(bg2, sumInvoice, arrival);

                            if (okClicked == true) {

                                ////////////
                                //Создает запись в Таблице Arrival
                                addArrival.add(arrival);

                                int i = tableArrival.getItems().size();   // кол-во строк в таблице
                                lastArrival.get();                    //метод для номера последней записи в Arrival (Последний номер прихода)

                                categoryGoodsByName.setName(categoryGood.getValue());   //категори товара по названию
                                cg = categoryGoodsByName.displayResult();
                                for (int j = 0; j < i; j++) {
                                    ArrivalList arrivalList = new ArrivalList();

                                    //Товар по коду из таблицы
                                    goodByCode.setCode(codeArrivalColumn.getCellData(j));

                                    goods = goodByCode.displayResult();
                                    arrivalList.setGoods(goods);
                                    goods.getArrivalLists().add(arrivalList);
                                    arrivalList.setAmount(amountArrivalColumn.getCellData(j));
                                    arrivalList.setPrice(newPriceArrivalColumn.getCellData(j));
                                    arrivalList.setPriceOpt(invoicePriceArrivalColumn.getCellData(j));
                                    arrivalListArrayList.add(arrivalList);
                                    arrivalList.setArrival(lastArrival.displayResult());
                                    arrival.getArrivalLists().add(arrivalList);

                                    updateResidueGood.update(null, arrivalList, null, true);

                                    //Сохранение списка прихода в БД
                                    addArrivalList.add(arrivalList);

                                    goods.setName(nameArrivalColumn.getCellData(j));
                                    goods.setPriceOpt(invoicePriceArrivalColumn.getCellData(j));
                                    goods.setPrice(newPriceArrivalColumn.getCellData(j));
                                    categoryGoodsByName.setName(categoryGood.getValue());   //категория товара по названию
                                    cg = categoryGoodsByName.displayResult();
                                    goods.setCategoryGoods(cg);
                                    goods.setMarking(markingGood);

                                    //Обновляет категорию товара
                                    updateGood.update(goods);
                                }
                                ////////////

                                //Запись в CaseRecord
                                arrival.getCaseRecord().add(arrival);
                                caseRecord.setArrival(arrival);
                                caseRecord.setDate(date);

                                caseRecord.setUserSwing(userSwing);
                                addCaseRecord.add(caseRecord);

                                bot.viewArrivalBot(arrival);

                                //Очищает все поля
                                //newArrival();
                                closeWindow();

                            }

                        }
                    } // если разница в оплате товара и суммы накладной
                    else {
                        boolean okClicked = false;

                        idUserByName.setLoginUser(login);   //метод для idUser по логину
                        userSwing = idUserByName.displayResult(); //Возвращает пользователя

//                        arrival.setNote(categoryGood.getValue() + " / " + numberInvoice.getText());
                        arrival.setNote(categoryGood.getValue());

                        arrival.setDate(date);
                        arrival.setSumArrival(new BigDecimal(sumCheckWithAllowance.getText()));
                        arrival.setSumInvoice(new BigDecimal(sumCheck.getText()));
//                        userSwing.getArrivals().add(arrival);
                        arrival.setUserSwing(userSwing);
                        arrival.setNumberWaybill(numberInvoice.getText());

                        sumInvoice = new BigDecimal(payment);
                        //Если сумма оплаченных денег меньше чем сумма накладной, то остаток записать в долг
                        int ress;
                        ress = sumInvoice.compareTo(bg2); // compare sumInvoice with bg2

                        if (ress == -1) {
                            //Выбор даты для долга
                            okClicked = dateChooseForDebt(bg2, sumInvoice, arrival);
                        }
                        ////////////
                        if (okClicked == true) {

                            //Создает запись в Таблице Arrival
                            addArrival.add(arrival);

                            int i = tableArrival.getItems().size();   // кол-во строк в таблице
                            lastArrival.get();                    //метод для номера последней записи в Arrival (Последний номер прихода)

                            categoryGoodsByName.setName(categoryGood.getValue());   //категори товара по названию
                            cg = categoryGoodsByName.displayResult();
                            for (int j = 0; j < i; j++) {
                                ArrivalList arrivalList = new ArrivalList();

                                //Товар по коду из таблицы
                                goodByCode.setCode(codeArrivalColumn.getCellData(j));

                                goods = goodByCode.displayResult();
                                arrivalList.setGoods(goods);
                                goods.getArrivalLists().add(arrivalList);
                                arrivalList.setAmount(amountArrivalColumn.getCellData(j));
                                arrivalList.setPrice(newPriceArrivalColumn.getCellData(j));
                                arrivalList.setPriceOpt(invoicePriceArrivalColumn.getCellData(j));
                                arrivalListArrayList.add(arrivalList);
                                arrivalList.setArrival(lastArrival.displayResult());
                                arrival.getArrivalLists().add(arrivalList);

                                updateResidueGood.update(null, arrivalList, null, true);

                                //Сохранение списка прихода в БД
                                addArrivalList.add(arrivalList);

                                goods.setName(nameArrivalColumn.getCellData(j));
                                goods.setPriceOpt(invoicePriceArrivalColumn.getCellData(j));
                                goods.setPrice(newPriceArrivalColumn.getCellData(j));
                                categoryGoodsByName.setName(categoryGood.getValue());   //категория товара по названию
                                cg = categoryGoodsByName.displayResult();
                                goods.setCategoryGoods(cg);
                                goods.setMarking(markingGood);

                                //Обновляет категорию товара
                                updateGood.update(goods);
                            }
                            ////////////

                            сashOut.setSumCash(sumInvoice);
                            сashOut.setNote(categoryGood.getValue());

                            bot.checkCashOutBot("Вывод в приходе: " + sumInvoice.toString(), categoryGood.getValue());
                            addCashOut.add(сashOut);

                            //Выводит из ККТ
                            kkt.cashOut(payment);

                            сashOut.getCaseRecord().add(caseRecord);
                            caseRecord.setCashOut(сashOut);
                            //Запись в CaseRecord
                            arrival.getCaseRecord().add(arrival);
                            caseRecord.setArrival(arrival);
                            caseRecord.setDate(date);

                            caseRecord.setUserSwing(userSwing);
                            addCaseRecord.add(caseRecord);

                            bot.viewArrivalBot(arrival);

                            //Очищает все поля
                            //newArrival();
                            closeWindow();
                        }
                    }
                }

            } else {
                dialogAlert.alert("Внимание!!!", "Поле 'Оплата за товар' пустое или меньше 0", "Введите корректную сумму");
            }
        }

    }

    /**
     * Печать штрих-кода
     */
    @FXML
    private void printBarcodeAction() {
        if (selectRow >= 0) {

            String name = arrivalData.get(selectRow).getName();
            String code = arrivalData.get(selectRow).getCode();
//            String amnt = arrivalData.get(selectRow).getAmount().toString();
            String amnt = "1";

//            dialogTextInput.display();
            TextInputDialog dialog = new TextInputDialog(amnt);
            dialog.setTitle("Печать штрих-кода");
            dialog.setHeaderText("");
            dialog.setContentText("Введите кол-во печати:");

// Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                int am = Integer.parseInt(result.get());

                new PrintBarcode(name, code, am);

            } else {
                // ... user chose CANCEL or closed the dialog
            }
//            tableArrival.getSelectionModel().clearSelection();
//            selectRow = -1;
        }
    }

    /**
     * Печать ценника
     *
     * @param event
     */
    @FXML
    private void printBarcodeTagAction(ActionEvent event) {
        if (selectRow >= 0) {

            String nameGood = arrivalData.get(selectRow).getName();
            String codeGood = arrivalData.get(selectRow).getCode();
            String priceGood = arrivalData.get(selectRow).getNewPrice().toString();

//            String amnt = arrivalData.get(selectRow).getAmount().toString();
            String amnt = "1";

//            dialogTextInput.display();
            TextInputDialog dialog = new TextInputDialog(amnt);
            dialog.setTitle("Печать ценника");
            dialog.setHeaderText("");
            dialog.setContentText("Введите кол-во печати:");

// Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                int am = Integer.parseInt(result.get());

                new PrintPriceTag(nameGood, codeGood, am, priceGood);
            } else {
                // ... user chose CANCEL or closed the dialog
            }
//            tableArrival.getSelectionModel().clearSelection();
//            selectRow = -1;
        }
    }

    /**
     * Закрывает окно
     */
    private void closeWindow() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
        timeline.stop();
    }

    /**
     * Диалоговое окно для выбора оплаты суммы накладной
     */
    private boolean dialogChooseSumInvoice(BigDecimal bg2) {
        boolean okClicked = false;

        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Ml_FX.class.getResource("/ml/view/ChooseSumInvoice.fxml"));
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ввод суммы накладной");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            AnchorPane page = (AnchorPane) loader.load();
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //Список оплат за сегоняшний день
            List<CaseRecord> crList;
            DateCaseRecord dateCaseRecord = new DateCaseRecord();
            dateCaseRecord.setDate(LocalDate.now());
            crList = dateCaseRecord.displayResult();
            ChooseSumInvoiceController sumInvoiceController = loader.getController();

            sumInvoiceController.setChoose(crList, bg2, kkt);
            sumInvoiceController.setBot(bot);

            sumInvoiceController.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            this.sumInvoice = sumInvoiceController.displaySum();
            okClicked = sumInvoiceController.displayOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return okClicked;
    }

    /**
     * Диалоговое окно выбора даты для долга
     */
    private boolean dateChooseForDebt(BigDecimal bg2, BigDecimal sumInvoice, Arrival arrival) {
        boolean okClicked = false;
        try {
            BigDecimal residue = bg2.subtract(sumInvoice);
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Ml_FX.class.getResource("/ml/view/DebtPay.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Долг");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            AnchorPane page = (AnchorPane) loader.load();
            Scene scene = new Scene(page);
            dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {

                    // consume event
                    event.consume();

                    // show close dialog
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Информация");
                    alert.setHeaderText("Закрыть диалоговое окно?");
                    alert.initOwner(dialogStage);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        dialogStage.close();
                    }
                }
            });
            dialogStage.setScene(scene);
            DebtPayController debtPayController = loader.getController();

            arrival.setSumInvoice(residue);
            debtPayController.setArrival(arrival);
            debtPayController.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            okClicked = debtPayController.displayOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return okClicked;
    }

    /**
     * Считает и выводит на экран сумму прихода
     */
    private void sumCheckWithAllowance() {

        BigDecimal sum = new BigDecimal(0.00);
        BigDecimal price = new BigDecimal(0.00);
        BigDecimal oldPrice = new BigDecimal(0.00);
        BigDecimal amount = new BigDecimal(0.00);
        BigDecimal residue = new BigDecimal(0.00);

        for (int i = 0; i < tableArrival.getItems().size(); i++) {

            BigDecimal sumWithChange = new BigDecimal(0.00);

            oldPrice = new BigDecimal(arrivalData.get(i).getOldPrice().toString());
            price = new BigDecimal(arrivalData.get(i).getNewPrice().toString());
            amount = new BigDecimal(arrivalData.get(i).getAmount().toString());
            residue = new BigDecimal(arrivalData.get(i).getResidue().toString());

            //проверка на изменение цены товара
//            int res;
//            res = oldPrice.compareTo(price); // compare bg1 with bg2
//            if (res != 0) {
//                if (res == 1 && price.signum() != 0) {
//                    sumWithChange = decimal("###.##", Double.parseDouble((oldPrice.subtract(price)).multiply(residue).toString()));
//                } else if (res == -1) {
//                    sumWithChange = decimal("###.##", Double.parseDouble((price.subtract(oldPrice)).multiply(residue).toString()));
//                }
//            }
            sum = decimal("###.##", Double.parseDouble(sumWithChange.add(price.multiply(amount).add(sum)).toString()));
        }

        //Выводит на экран сумму прихода
        sumCheckWithAllowance.setText(sum.toString());
    }

    /**
     * Считает и выводит на экран сумму накладной
     */
    private void sumCheck() {

        BigDecimal sum = new BigDecimal(0);
        BigDecimal price = new BigDecimal(0.00);
        BigDecimal amount = new BigDecimal(0.00);

        for (int i = 0; i < tableArrival.getItems().size(); i++) {
            price = new BigDecimal(arrivalData.get(i).getInvoicePrice().toString());
            amount = new BigDecimal(arrivalData.get(i).getAmount().toString());

            sum = decimal("###.###", Double.parseDouble(price.multiply(amount).add(sum).toString()));
//amount = decimal("###.###", Double.parseDouble(am));
        }

        //Выводит на экран сумму прихода
        sumCheck.setText(sum.toString());
    }

    /**
     * Вовзращает данные о товаре по коду
     *
     * @param code
     */
    private void getGoodCode(String code) {
        boolean checkGood = false;
        Goods goods = null;
        PlayAudio pa = new PlayAudio();

        String codeFromTable = "";
        String codeFromTable1 = "";

        int numberRow = 0;
        boolean b = false;
        boolean bb = false;

        int r = tableArrival.getItems().size();   // кол-во строк в таблице
        for (int i = 0; i < r; i++) {
            codeFromTable1 = (String) arrivalData.get(i).getCode();
            if (code.equals(codeFromTable1)) {
                bb = true;
            }
        }

        if (goodsList.isEmpty()) {
            checkGood = bb == true;
        } else {

            for (Goods gg1 : goodsList) {
                if ((gg1.getCode().equals(code)) || (bb == true)) {
                    goods = gg1;
                    checkGood = true;
                    break;
                } else {
                    checkGood = false;
                }
            }
        }
        if (checkGood == false) {
            CategoryGoodsByName categoryGoodsByName = new CategoryGoodsByName();
            CategoryGoods cg = new CategoryGoods();
            NewGood newGood = new NewGood();
            SearchBarcode searchBarcode = new SearchBarcode();
            String answer = "";

            //Поиск штрих кода в интернете
            /*try {
            answer = searchBarcode.setBarcode(code);
            } catch (ParseException ex) {
            Logger.getLogger(ArrivalController.class.getName()).log(Level.SEVERE, null, ex);
            }*/

            Date date = new Date();
            RegexpNameGoods regexpNameGoods = new RegexpNameGoods();

            //проигрыш звука
            pa.playSound();

            DialogTextInputDontHidden alert = new DialogTextInputDontHidden();
            try {
                alert.dialog("Новый товар", "Внимание! Новый товар!", "Введите название товара", answer);

                alert.start(primaryStage);
            } catch (Exception ex) {
                Logger.getLogger(ArrivalController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //DialogAlert dialogAlert = new DialogAlert();
//            dialogTextInput.dialog("Новый товар", "Внимание! Новый товар!", "Введите название товара", answer);
//            dialogTextInput.start(primaryStage);
            String newName = alert.display();//dialogTextInput.display();

            //Проверка первого символа
            if (regexpNameGoods.nameGoods(newName) == true) {

                if (!"".equals(newName)) {
                    goods = new Goods();
                    goods.setName(newName);
                    goods.setCode(code);
                    goods.setPriceOpt(new BigDecimal(0));
                    goods.setPrice(new BigDecimal(0));
                    goods.setResidue(new BigDecimal(0));
                    goods.setDate(date);
                    amount = new BigDecimal(weight);
                    amountGood.setText(weight);

                    //Добавить сюда СОЗДАНИЕ ТОВАРА В БАЗЕ
                    categoryGoodsByName.setName(categoryGood.getValue());   //категори товара по названию
                    cg = categoryGoodsByName.displayResult();
                    goods.setCategoryGoods(cg);
                    //Обновляет категорию товара
                    newGood.add(goods);
                    goodsList = allGoodsList.listGoods();
                }
            } else {
                dialogAlert.alert("Внимание!", "Сообщение:", "Название товара должно начинаться с буквы!!!");
                codeGood.requestFocus();
            }

            for (Goods gg1 : goodsList) {
                if (gg1.getCode().equals(code)) {
                    goods = gg1;
                }
            }

        }

        //Сравнивает введеный код со списком кодов в таблице
        /*if (i > 0) {
        TablePosition pos = tableArrival.getSelectionModel().getSelectedCells().get(0);
        selectRow = pos.getRow();
        }*/
        int rowCount = tableArrival.getItems().size();   // кол-во строк в таблице
        for (int i = 0; i < rowCount; i++) {
            codeFromTable = (String) arrivalData.get(i).getCode();
            if (code.equals(codeFromTable)) {
                b = true;
                numberRow = i;
            }
        }
        //если код в таблице существует, то добавить к кол-ву +1 или +вес
        if (b == true) {
            //weight
            BigDecimal amountFromTable = arrivalData.get(numberRow).getAmount();
            arrivalData.get(numberRow).setAmount(amountFromTable.add(new BigDecimal(weight)));
            //arrivalData.get(row).setNewPrice(allowance.multiply(invoice));s
            tableArrival.getItems().set(numberRow, arrivalData.get(numberRow));
            codeGood.requestFocus();
            weight = "1";
        } else {
            if (goods != null) {
                displayResult(goods);
            }
        }

        //считает сумму прихода
        sumCheckWithAllowance();
        //считает сумму накладной
        sumCheck();
    }

    /**
     * Вовзращает данные о товаре по наименованию
     *
     * @param code
     */
    private void getGoodName(String name) {
        Goods goods = null;
        String nameFromTable = "";
        int numberRow = 0;
        boolean b = false;
        for (Goods gg1 : goodsList) {
            if (gg1.getName().equals(name)) {
                goods = gg1;
            }
        }
        //Сравнивает введеное наименование со списком наименований в таблице
        int rowCount = tableArrival.getItems().size();   // кол-во строк в таблице
        for (int i = 0; i < rowCount; i++) {
            nameFromTable = (String) arrivalData.get(i).getName();
            if (name.equals(nameFromTable)) {
                b = true;
                numberRow = i;
            }
        }
        //если код в таблице существует, то добавить к кол-ву +1 или +вес
        if (b == true) {
            //weight
            BigDecimal amountFromTable = arrivalData.get(numberRow).getAmount();
            arrivalData.get(numberRow).setAmount(amountFromTable.add(new BigDecimal(weight)));
            //arrivalData.get(row).setNewPrice(allowance.multiply(invoice));s
            tableArrival.getItems().set(numberRow, arrivalData.get(numberRow));
            codeGood.requestFocus();
            weight = "1";
        } else {
            if (goods != null) {
                displayResult(goods);
            }
        }

        //считает сумму прихода
        sumCheckWithAllowance();
        //считает сумму накладной
        sumCheck();
    }

    /**
     * Конвертирует String в BigDecimal и , в .
     */
    private BigDecimal decimal(String pattern, double value) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(value);

        //Замена , на .
        String gWithDot = output.replace(',', '.');
        return new BigDecimal(gWithDot);
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayResult(Goods goods) {
        BigDecimal priceOpt;

        Integer roundingPrice = xmls.getRounding().intValue();

        nameArrivalColumn.setCellValueFactory(new PropertyValueFactory<ArrivalTable, String>("name"));
        nameArrivalColumn.setCellFactory(TextFieldTableCell.<ArrivalTable>forTableColumn());
        nameArrivalColumn.setOnEditCommit(
                (CellEditEvent<ArrivalTable, String> t) -> {
                    ((ArrivalTable) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setName(t.getNewValue());
                });

        codeArrivalColumn.setCellValueFactory(new PropertyValueFactory<ArrivalTable, String>("code"));
        amountArrivalColumn.setCellValueFactory(new PropertyValueFactory<ArrivalTable, BigDecimal>("amount"));
        oldPriceArrivalColumn.setCellValueFactory(new PropertyValueFactory<ArrivalTable, BigDecimal>("oldPrice"));
        invoicePriceArrivalColumn.setCellValueFactory(new PropertyValueFactory<ArrivalTable, BigDecimal>("invoicePrice"));
        newPriceArrivalColumn.setCellValueFactory(new PropertyValueFactory<ArrivalTable, BigDecimal>("newPrice"));
        //newPriceArrivalColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        residueArrivalColumn.setCellValueFactory(new PropertyValueFactory<ArrivalTable, BigDecimal>("residue"));

        makeHeaderWrappable(newPriceArrivalColumn);
        makeHeaderWrappable(oldPriceArrivalColumn);
        makeHeaderWrappable(invoicePriceArrivalColumn);

        ArrivalTable arrivalTable = new ArrivalTable();
        for (int i = 0; i < 1; i++) {
            //BigDecimal price = goods.getPrice();
            //BigDecimal amount = decimal("###.###", Double.parseDouble(weight));
            if (goods.getPriceOpt() == null) {
                priceOpt = BigDecimal.ZERO;
            } else {
                priceOpt = goods.getPriceOpt();
            }
            BigDecimal newPrice = allowance.divide(new BigDecimal(100)).multiply(priceOpt).add(priceOpt);
            arrivalTable.setCode(goods.getCode());
            arrivalTable.setName(goods.getName());
            arrivalTable.setOldPrice(goods.getPrice());
            arrivalTable.setInvoicePrice(priceOpt);
            arrivalTable.setNewPrice(decimal("###.##", Double.parseDouble(newPrice.setScale(roundingPrice, BigDecimal.ROUND_HALF_UP).toString())));
            arrivalTable.setAmount(amount);
//2200838707746
//2200439409407
            arrivalTable.setResidue(goods.getResidue());

            // заполняем таблицу данными
            if (!"".equals(goods.getName())) {
                arrivalData.add(arrivalTable);
            }
        }

        tableArrival.setItems(arrivalData);

        //Просмотр последней строки в таблице
        int size = tableArrival.getItems().size();
        if (size > 0) {
            tableArrival.scrollTo(size - 1);
        }

        weight = "1";
        //Переход курсора на кол-во товара
        //amountGood.requestFocus();
        //amountGood.setText("1");
        //amountGood.selectAll(); //Выделяет текст в поле
    }

    /**
     * Multiline для колонки Цена в накладной
     *
     * @param col
     */
    private void makeHeaderWrappable(TableColumn col) {
        Label label = new Label(col.getText());
        label.setStyle("-fx-padding: 8px;");
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);

        StackPane stack = new StackPane();
        stack.getChildren().add(label);
        stack.prefWidthProperty().bind(col.widthProperty().subtract(5));
        label.prefWidthProperty().bind(stack.prefWidthProperty());
        col.setGraphic(stack);
    }

    /**
     * Выбор категории товара.
     *
     * @param text
     */
    private void choose(String text) {

        //Выбор "Создать категорию"
        if (text.equals("Создать категорию")) {

            dialogForCategoryGood();

        } else {
            allowanceTextField.requestFocus();
            allowanceTextField.setEditable(true);
        }
    }

    /**
     * Диалог для создания категории товара
     */
    private void dialogForCategoryGood() {

        AddCategoryGood acg = new AddCategoryGood();

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Введите название категории");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField from = new TextField();
        from.setPromptText("Название");
        TextField to = new TextField();
        to.setPromptText("Описание");

        gridPane.add(new Label("Название:"), 0, 0);
        gridPane.add(from, 1, 0);
        gridPane.add(new Label("Описание:"), 2, 0);
        gridPane.add(to, 3, 0);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> from.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(from.getText(), to.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            //Добавление категории в БД
            category.setName(pair.getKey());
            category.setNote(pair.getValue());
            acg.add(category);
            //Добавление категории в ComboBox
            options.add(pair.getKey());
            categoryGood.setItems(options);
        });
        /*result.ifPresent(pair -> {
        
        category.setName(pair.getKey());
        category.setNote(pair.getValue());
        acg.add(category);
        
        });*/
    }

    /**
     * Открывает окно для создания нового товара
     */
    private void getDialogNewGood() {
        CategoryGoodsByName categoryGoodsByName = new CategoryGoodsByName();
        CategoryGoods cg = new CategoryGoods();
        NewGood newGood = new NewGood();
        Date date = new Date();
        RegexpNameGoods regexpNameGoods = new RegexpNameGoods();
        Goods goods = null;
        Long newBarcode = null;
        dialogTextInput.dialog("Новый товар", "", "Введите название товара", "");
        dialogTextInput.start(primaryStage);
        dialogTextInput.display();

        String newName = dialogTextInput.display();

        //Проверка первого символа
        if (regexpNameGoods.nameGoods(newName) == true) {

            if (!"".equals(newName)) {

                //Создает новый штрих-код
                Barcode barcode = new Barcode();
                AddBarcode addBarcode = new AddBarcode();
                LastBarcode lastNumberCode = new LastBarcode();

                lastNumberCode.get();//Последний номер
                Barcode lastBarcode = lastNumberCode.displayResult();       // штрих-кода в таблице Barcode

                if (lastBarcode == null) {
                    newBarcode = Long.parseLong("110000");
                    barcode.setBarcode(newBarcode);
                } else {

                    newBarcode = Long.parseLong(lastBarcode.getBarcode().toString()) + 1;
                    barcode.setBarcode(newBarcode);
                }

                goods = new Goods();
                goods.setName(newName);
                goods.setCode(newBarcode.toString());
                goods.setPriceOpt(new BigDecimal(0));
                goods.setPrice(new BigDecimal(0));
                goods.setResidue(new BigDecimal(0));
                goods.setDate(date);
                amount = new BigDecimal(weight);
                amountGood.setText(weight);

                //Если поле нового товара не пустое, тогда создается новая запись в БД и новый штрих-код
                if (!(newName == null) && !("".equals(newName))) {
                    GoodByCode goodByCode = new GoodByCode();
                    Goods goodNewBarcode = new Goods();
                    //Добавить сюда СОЗДАНИЕ ТОВАРА В БАЗЕ
                    categoryGoodsByName.setName(categoryGood.getValue());   //категори товара по названию
                    cg = categoryGoodsByName.displayResult();
                    goods.setCategoryGoods(cg);
                    //добавляет новый товар
                    newGood.add(goods);

                    goodByCode.setCode(newBarcode.toString());
                    goodNewBarcode = goodByCode.displayResult();
                    barcode.setGoods(goodNewBarcode);
                    addBarcode.add(barcode);
                }
                codeGood.requestFocus();

                //String input = Dialogs.showInputDialog(stage, "Please enter your name:", "Input Dialog", "title");
                for (Goods gg1 : goodsList) {
                    if (gg1.getCode().equals(newBarcode.toString())) {
                        goods = gg1;
                    }
                }

                if (goods != null) {
                    displayResult(goods);
                }
            }
        } else {
            dialogAlert.alert("Внимание!", "Сообщение:", "Название товара должно начинаться с буквы!!!");
            codeGood.requestFocus();
            codeGood.selectAll();
        }

    }

    /**
     * Проверка соединения с БД
     */
    private void getConnection() {

        XMLSearchAddress address = new XMLSearchAddress();
        address.findAddress();
        CheckInternetConnection connection = new CheckInternetConnection();
        /*TimerTask task = new TimerTask() {
        @Override
        public void run() {
        System.out.println("Ответ сети : " + connection.call());
        if ("true".equals(connection.call())) {
        getConnDB.setText("Связь есть");
        } else {
        getConnDB.setText("Связи нет");
        }
        }
        };
        
        timer2.schedule(task, 100, 3000);*/

        timeline = new Timeline(new KeyFrame(Duration.seconds(5), ev -> {
            if ("true".equals(connection.call())) {
                getConnDB.setText("Соединение есть");
                getConnDB.setStyle("-fx-text-fill: BLUE;");
                okArrival.setDisable(false);
                newArrival.setDisable(false);

                firstState = true;

            } else {
                getConnDB.setText("Соединения нет");
                getConnDB.setStyle("-fx-text-fill: RED;");
                okArrival.setDisable(true);
                newArrival.setDisable(true);
                //checkConnection.closeConnection();
                firstState = false;
                lastState = true;
            }

            if ((firstState == true) && (lastState == true)) {
                firstState = false;
                lastState = false;
                checkConnection.restartConnection();
            }
        }));

        timeline.setCycleCount(100);
        timeline.play();

    }

    /**
     * Список товара по названию в поле nameGood
     *
     * Список товара по коду в поле codeGood
     */
    private void listGoodsByNameAndCode(List<Goods> someList) {

//        //Список товара по названию в поле nameGood
//        String[] sName = new String[someList.size()];
//        for (int i = 0; i < someList.size(); i++) {
//            sName[i] = (String) someList.get(i).getName();
//        }
//        Arrays.sort(sName);
//        TextFields.bindAutoCompletion(nameGood, sName);
//
//        //Список товара по коду в поле codeGood
//        String[] sCode = new String[someList.size()];
//        for (int i = 0; i < someList.size(); i++) {
//            sCode[i] = (String) someList.get(i).getCode();
//        }
//        Arrays.sort(sCode);
//        TextFields.bindAutoCompletion(codeGood, sCode);
        //Список товара по названию в поле codeGood
        String[] sName = new String[goodsList.size()];
        String[] sCode = new String[goodsList.size()];
        for (int i = 0; i < goodsList.size(); i++) {
            sName[i] = (String) goodsList.get(i).getName();
            sCode[i] = (String) goodsList.get(i).getCode();
        }
        String[] both = (String[]) ArrayUtils.addAll(sName, sCode);
        Arrays.sort(both);
//
        TextFields.bindAutoCompletion(nameGood, both).setVisibleRowCount(10);

    }

    public void setCompCard(KKT kkt) {
        this.kkt = kkt;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    /**
     * Отображение в таблице CheckBox
     */
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                //Ввод поле КОЛ_ВО только цифр точки или запятой
                amountGood.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d*([.,]\\d*)?")) {
                            amountGood.setText(oldValue);
                        }
                    }
                });

                //Ввод поле КОД ТОВАРА только цифр
                codeGood.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d*")) {
                            codeGood.setText(oldValue);
                        }
                    }
                });

                //Ввод поле НАДБАВКА только цифр
                allowanceTextField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d*")) {
                            allowanceTextField.setText(oldValue);
                        }
                    }
                });

                //Ввод поле ЦЕНА_В_НАКЛ только цифр
                invoicePrice.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d*([.,]\\d*)?")) {
                            invoicePrice.setText(oldValue);
                        }
                    }
                });

                //Ввод поле НОВАЯ_ЦЕНА только цифр
                newPrice.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d*([.,]\\d*)?")) {
                            newPrice.setText(oldValue);
                        }
                    }
                });

                //Ввод поле ОПЛАТА_ЗА_ТОВАР только цифр
                paymentCheck.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d*([.,]\\d*)?")) {
                            paymentCheck.setText(oldValue);
                        }
                    }
                });

                //проверка соединения с БД
                getConnection();
                //Если закрыть окно, то проверка соединения окончена
                Stage stage = (Stage) borderPane.getScene().getWindow();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        timeline.stop();
                    }
                });

                //Multiline для колонок
                makeHeaderWrappable(invoicePriceArrivalColumn);
                makeHeaderWrappable(newPriceArrivalColumn);
                makeHeaderWrappable(oldPriceArrivalColumn);

                tableArrival.setEditable(true);
                if ("ROLE_ADMIN".equals(auth.toString())) {
                    residueArrivalColumn.setVisible(true);
                } else {
                    residueArrivalColumn.setVisible(false);
                }

                //Список всего товара
                goodsList = allGoodsList.listGoods();

                //Список всего товара
                categoryList = categoryGoodList.getList();
                for (CategoryGoods cg : categoryList) {
                    options.add(cg.getName());
                }
                options.add("Создать категорию");

                //Выделяет красным цветом СОЗДАТЬ КАТЕГОРИЮ
                categoryGood.setCellFactory(
                        new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item,
                                    boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.contains("Создать")) {
                                        setTextFill(Color.RED);
                                    } else {
                                        setTextFill(Color.BLACK);
                                    }
                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                });

                categoryGood.setItems(options);

                //Выводит список товара по названию и коду
                listGoodsByNameAndCode(goodsList);

                //Список товара по названию в поле nameCategory
                String[] sNameCategory = new String[categoryList.size()];
                for (int i = 0; i < categoryList.size(); i++) {
                    sNameCategory[i] = (String) categoryList.get(i).getName();
                }
                Arrays.sort(sNameCategory);
                TextFields.bindAutoCompletion(categoryGood.getEditor(), sNameCategory);
                //По нажатию клавиш
                borderPane.setOnKeyPressed(
                        event -> {
                            switch (event.getCode()) {

                                case F7:
//                                    if (event.isControlDown()) {
                                    getDialogNewGood();
//                                    System.out.println("КНОПКА F7 НАЖАТА!!!");
//                                    }
                                    break;
                                case DELETE:
                                    //Удаление строки
                                    TablePosition pos = tableArrival.getSelectionModel().getSelectedCells().get(0);
                                    int row = pos.getRow();
                                    arrivalData.remove(row);
                                    codeGood.requestFocus();
                                    //считает сумму прихода
                                    sumCheckWithAllowance();
                                    //считает сумму накладной
                                    sumCheck();
                                    break;
                            }
                        });

                //При нажатии пробела в поле Код товара - переход на кол-во товара
                codeGood.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        switch (event.getCode()) {
                            case ALT_GRAPH:
                                amountGood.requestFocus();
                                amountGood.selectAll();
                                break;
                            case F7:
//                                    if (event.isControlDown()) {
//                                getDialogNewGood();
//                                System.out.println("КНОПКА F7 НАЖАТА!!!");
//                                    }
                                break;
                        }
                    }
                });

                //При нажатии F7 в поле Название товара - диалог новый товар
                nameGood.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        switch (event.getCode()) {
//                            case ALT_GRAPH:
//                                amountGood.requestFocus();
//                                amountGood.selectAll();
//                                break;
                            case F7:
//                                    if (event.isControlDown()) {
//                                getDialogNewGood();
//                                System.out.println("КНОПКА F7 НАЖАТА!!!");
//                                    }
                                break;
                        }
                    }
                });
            }
        });

    }

}
