/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ml.authentication.GrantedAuth;
import ml.barcode.BarcodeConv;
import ml.dialog.DialogAlert;
import ml.model.CaseRecord;
import ml.model.Goods;
import ml.model.UserSwing;
import ml.model.Writeoff;
import ml.model.WriteoffList;
import ml.query.barcode.BarcodeIdGoodsByCode;
import ml.query.cancellation.AddCancel;
import ml.query.cancellation.AddCancelList;
import ml.query.cancellation.LastCancel;
import ml.query.caserecord.AddCaseRecord;
import ml.query.goods.GoodByCode;
import ml.query.goods.QueryAllGoodsList;
import ml.query.goods.UpdateGood;
import ml.query.goods.UpdateResidueDish;
import ml.query.goods.UpdateResidueGood;
import ml.query.user.IdUserByName;
import ml.table.CancelTable;
import ml.telegram.Bot;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Списание товара
 *
 * @author Dave
 */
public class CancelController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField codeGood;
    @FXML
    private TextField nameGood;
    @FXML
    private TextField amountGood;
    @FXML
    private TextField sumCancel;
    @FXML
    private TextField noteCancel;
    @FXML
    private Button okCancel;
    @FXML
    private Button newCancel;
    @FXML
    private TableView<CancelTable> tableCancel;
    @FXML
    private TableColumn<CancelTable, String> codeCancelColumn;
    @FXML
    private TableColumn<CancelTable, String> nameCancelColumn;
    @FXML
    private TableColumn<CancelTable, BigDecimal> priceCancelColumn;
    @FXML
    private TableColumn<CancelTable, BigDecimal> amountCancelColumn;
    @FXML
    private TableColumn<CancelTable, BigDecimal> residueCancelColumn;

    private List<Goods> goodsList;
    private BarcodeConv barcodeConv = new BarcodeConv();
    private String weight = "1";
    private ObservableList<CancelTable> cancelData = FXCollections.observableArrayList();
    private GrantedAuth grantedAuth = new GrantedAuth();
    private Object auth = grantedAuth.role();
    private QueryAllGoodsList allGoodsList = new QueryAllGoodsList();
    private BigDecimal allowance = new BigDecimal(0);
    private BigDecimal invoice = new BigDecimal(0);
    private BigDecimal amount = new BigDecimal(1);
    private LastCancel lastCancel = new LastCancel();
    private DialogAlert dialogAlert = new DialogAlert();
    private Stage primaryStage;
    private int selectRow = -1;
    private Bot bot;

    boolean checkTechMap = false;

    /**
     * Поиск товара по коду
     *
     * @param event
     */
    @FXML
    private void getCode(ActionEvent event) {
        String code = codeGood.getText();
        if (!code.equals("")) {

            getGoodCode(code);
        }
    }

    /**
     * Поиск товара по наименованию
     *
     * @param event
     */
    @FXML
    private void getName(ActionEvent event) {
        String name = nameGood.getText();
        if (!name.equals("")) {

            getGoodName(name);
        }
    }

    /**
     * Возвращает кол-во товара в таблицу чека.
     */
    @FXML
    private void getAmount() {
        String g = amountGood.getText();
        //Замена , на .
        String gWithDot = g.replace(',', '.');

        int i = tableCancel.getItems().size();   // кол-во строк в таблице
        //если кол-во строк не 0
        if (i != 0) {
            int row = i - 1;                        // последняя строка

            //Вставка данных в последнюю ячейку "кол-во" таблицы
            cancelData.get(row).setAmount(decimal("##.###", Double.parseDouble(gWithDot)));
            tableCancel.getItems().set(row, cancelData.get(row));

            codeGood.requestFocus();

            sumCancel();
        }
    }

    @FXML
    private void getRow(MouseEvent event) {
    }

    /**
     * Вовзращает данные о товаре по коду
     *
     * @param code
     */
    private void getGoodCode(String code) {
        boolean checkGood = false;
        Goods goods = null;
        //qg.runQueryCodeGoods(code);
        //goods = qg.displayResultCode();

        for (Goods gg1 : goodsList) {
            if (gg1.getCode().equals(code)) {
                goods = gg1;
                checkGood = true;
            } else {
                checkGood = false;
            }
        }

        if (checkGood == false) {

            boolean firstLetter = barcodeConv.firstLetters(code);
            if (firstLetter == true) {
                String codeBarCode = barcodeConv.sixLetter(code);
                weight = barcodeConv.fourLetter(code);
                if ("0.001".equals(weight)) {
                    weight = "1";
                }
                for (Goods gg1 : goodsList) {
                    if (gg1.getCode().equals(codeBarCode)) {
                        goods = gg1;
                    }
                }
            }

        }
        if (goods != null) {
            displayResult(goods);
        }
        codeGood.requestFocus();
        codeGood.setText("");
        sumCancel();
    }

    /**
     * Вовзращает данные о товаре по наименованию
     *
     * @param code
     */
    private void getGoodName(String name) {
        Goods goods = null;

        for (Goods gg1 : goodsList) {
            if (gg1.getName().equals(name)) {
                goods = gg1;
            }
        }
        if (goods != null) {
            displayResult(goods);
        }
        nameGood.selectAll(); //Выделяет текст в поле
        sumCancel();
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayResult(Goods goods) {
        nameCancelColumn.setCellValueFactory(new PropertyValueFactory<CancelTable, String>("name"));
        codeCancelColumn.setCellValueFactory(new PropertyValueFactory<CancelTable, String>("code"));
        priceCancelColumn.setCellValueFactory(new PropertyValueFactory<CancelTable, BigDecimal>("price"));
        amountCancelColumn.setCellValueFactory(new PropertyValueFactory<CancelTable, BigDecimal>("amount"));
        if ("ROLE_ADMIN".equals(auth.toString())) {
            residueCancelColumn.setCellValueFactory(new PropertyValueFactory<CancelTable, BigDecimal>("residue"));
        }

        CancelTable cancelTable = new CancelTable();
        for (int i = 0; i < 1; i++) {
            BigDecimal price = goods.getPrice();
            BigDecimal amount = decimal("###.###", Double.parseDouble(weight));
            cancelTable.setCode(goods.getCode());
            cancelTable.setName(goods.getName());
            cancelTable.setPrice(price);
            cancelTable.setAmount(amount);
            cancelTable.setResidue(goods.getResidue());
            if ("ROLE_ADMIN".equals(auth.toString())) {
                cancelTable.setResidue(goods.getResidue());
            }

            // заполняем таблицу данными
            if (!"".equals(goods.getName())) {
                cancelData.add(cancelTable);
            }
        }

        tableCancel.setItems(cancelData);

        //Переход курсора на кол-во товара
        //amountGood.requestFocus();
        amountGood.setText(weight);
        amountGood.selectAll(); //Выделяет текст в поле
        weight = "1";
    }

    /**
     * Конвертирует String в BigDecimal и , в .
     */
    private BigDecimal decimal(String pattern, double value) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(value);
        System.out.println(value + "  " + pattern + "  " + output);

        //Замена , на .
        String gWithDot = output.replace(',', '.');
        return new BigDecimal(gWithDot);
    }

    /**
     * Считает и выводит на экран сумму накладной
     */
    private void sumCancel() {
        BigDecimal sum = new BigDecimal(0);
        BigDecimal price = new BigDecimal(0.00);
        BigDecimal amount = new BigDecimal(0.00);

        for (int i = 0; i < tableCancel.getItems().size(); i++) {
            price = new BigDecimal(cancelData.get(i).getPrice().toString());
            amount = new BigDecimal(cancelData.get(i).getAmount().toString());

            sum = decimal("###.###", Double.parseDouble(price.multiply(amount).add(sum).toString()));
//amount = decimal("###.###", Double.parseDouble(am));
        }

        //Выводит на экран сумму прихода
        sumCancel.setText(sum.toString());
    }

    /**
     * Кнопка Новое списание
     */
    @FXML
    private void newCancel() {

        bot.checkNewCancel(sumCancel.getText(), tableCancel);

        //Очищает все поля
        goodsList = allGoodsList.listGoods();
        allowance = new BigDecimal(0);
        invoice = new BigDecimal(0);
        codeGood.setText("");
        nameGood.setText("");
        amountGood.setText("");
        noteCancel.setText("");
        codeGood.requestFocus();
        sumCancel.setText("0.00");

        //удаление строк в таблице
        for (int ii = -1; ii < tableCancel.getItems().size(); ii++) {
            tableCancel.getItems().clear();

        }
    }

    /**
     * Кнопка ОК, запись в таблицы
     */
    @FXML
    private void okCancel() {

        int countTableRow = tableCancel.getItems().size();   // кол-во строк в таблице
        if (countTableRow != 0) {

            if (!"".equals(noteCancel.getText())) {
                Writeoff cancel = new Writeoff();
                AddCancel addCancel = new AddCancel();
                AddCancelList addCancelList = new AddCancelList();
                Goods goods = new Goods();
                UserSwing userSwing = new UserSwing();
                UpdateResidueGood updateResidueGood = new UpdateResidueGood();
                UpdateResidueDish updateResidueDish = new UpdateResidueDish();
                BarcodeIdGoodsByCode barcodeIdGoodsByCode = new BarcodeIdGoodsByCode();
                UpdateGood updateGood = new UpdateGood();
                GoodByCode goodByCode = new GoodByCode();
                CaseRecord caseRecord = new CaseRecord();
                AddCaseRecord addCaseRecord = new AddCaseRecord();

                IdUserByName idUserByName = new IdUserByName();
                Authentication authentication = SecurityContextHolder.getContext().
                        getAuthentication();
                String login = authentication.getName();
                Date date = new Date();
                List<Writeoff> cancelArrayList = new ArrayList<>();
                List<WriteoffList> cancelListArrayList = new ArrayList<>();

                idUserByName.setLoginUser(login);   //метод для idUser по логину
                userSwing = idUserByName.displayResult(); //Возвращает пользователя

                cancel.setDate(date);
                cancel.setNote(noteCancel.getText());
                cancel.setSum(new BigDecimal(sumCancel.getText()));
//                userSwing.getCancels().add(cancel);
                cancel.setUserSwing(userSwing);
                //Создает запись в Таблице Writeoff
                addCancel.add(cancel);

                int i = tableCancel.getItems().size();   // кол-во строк в таблице
                lastCancel.get();                    //метод для номера последней записи в Arrival (Последний номер прихода)

                for (int j = 0; j < i; j++) {
                    WriteoffList cancelList = new WriteoffList();

                    //Товар по коду из таблицы
                    goodByCode.setCode(codeCancelColumn.getCellData(j));

                    goods = goodByCode.displayResult();
                    cancelList.setGoods(goods);
                    goods.getWriteoffLists().add(cancelList);
                    cancelList.setAmount(amountCancelColumn.getCellData(j));
                    cancelListArrayList.add(cancelList);
                    cancelList.setPrice(priceCancelColumn.getCellData(j));
                    cancelList.setWriteoff(lastCancel.displayResult());
                    cancel.getWriteoffLists().add(cancelList);

                    barcodeIdGoodsByCode.setCode(cancelList.getGoods().getCode());
                    if (barcodeIdGoodsByCode.displayResult() == true) {
                        System.out.println("Товар приготовлен");
                        checkTechMap = true;
                    } else {
                        System.out.println("Товар на продажу");
                        checkTechMap = false;

                    }
                    //вычитание из остатка

                    if (checkTechMap == false) {
                        updateResidueGood.update(null, null, cancelList, false);
                    } else {

                        updateResidueDish.update(null, null, cancelList, false);
                    }

                    //Сохранение списка прихода в БД
                    addCancelList.add(cancelList);
                    //Обновляет категорию товара
                    updateGood.update(goods);

                }

                //Запись в CaseRecord
                caseRecord.setDate(date);
                cancel.getCaseRecord().add(cancel);
                caseRecord.setWriteOff(cancel);
                caseRecord.setUserSwing(userSwing);
                addCaseRecord.add(caseRecord);
                
                bot.checkCancel(sumCancel.getText(), tableCancel, noteCancel.getText());
                //Очищает все поля
                goodsList = allGoodsList.listGoods();
                allowance = new BigDecimal(0);
                invoice = new BigDecimal(0);
                codeGood.setText("");
                nameGood.setText("");
                amountGood.setText("");
                noteCancel.setText("");
                codeGood.requestFocus();
                sumCancel.setText("0.00");

                //удаление строк в таблице
                for (int ii = -1; ii < tableCancel.getItems().size(); ii++) {
                    tableCancel.getItems().clear();

                }
            } else {
                dialogAlert.alert("Внимание!!!", "Поле 'Описание' пустое", "Введите описание");
            }
        }
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                 //Если закрыть окно, то проверка соединения окончена
                Stage stage = (Stage) borderPane.getScene().getWindow();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        bot.checkNewCancel(sumCancel.getText(), tableCancel);

                    }
                });
                
                
                //Ввод поле КОЛ_ВО только цифр точки или запятой
                amountGood.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d*([.,]\\d*)?")) {
                            amountGood.setText(oldValue);
                        }
                    }
                });

                //Ввод поле КОД ТОВАРА только цифр точки или запятой
                codeGood.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d*")) {
                            codeGood.setText(oldValue);
                        }
                    }
                });

                //Список всего товара
                goodsList = allGoodsList.listGoods();

                //Список товара по названию в поле nameGood
                String[] sName = new String[goodsList.size()];
                for (int i = 0; i < goodsList.size(); i++) {
                    sName[i] = (String) goodsList.get(i).getName();
                }
                TextFields.bindAutoCompletion(nameGood, sName);

                //Список товара по коду в поле codeGood
                String[] sCode = new String[goodsList.size()];
                for (int i = 0; i < goodsList.size(); i++) {
                    sCode[i] = (String) goodsList.get(i).getCode();
                }
                TextFields.bindAutoCompletion(codeGood, sCode);

                borderPane.setOnKeyPressed(
                        event -> {
                            switch (event.getCode()) {
                                case DELETE:
                                    //Удаление строки
                                    TablePosition pos = tableCancel.getSelectionModel().getSelectedCells().get(0);
                                    int row = pos.getRow();
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                                    LocalDateTime now = LocalDateTime.now();
                                    System.out.println(dtf.format(now));
                                    bot.cancelDeleteCancelButton(cancelData.get(row).getName() + " в кол-ве " + cancelData.get(row).getAmount() + "; время " + dtf.format(now));
                                    cancelData.remove(row);
                                    codeGood.requestFocus();

                                    sumCancel();
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
                        }
                    }
                });

                //При нажатии пробела в поле Название товара - переход на кол-во товара
                nameGood.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        switch (event.getCode()) {
                            case ALT_GRAPH:
                                amountGood.requestFocus();
                                amountGood.selectAll();
                                break;
                        }
                    }
                });
            }
        });
    }
}
