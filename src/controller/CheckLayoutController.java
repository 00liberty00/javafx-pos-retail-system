/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import ml.Ml;
import ml.Ml_FX;
import ml.authentication.GrantedAuth;
import ml.barcode.BarcodeConv;
import ml.dialog.DialogAlert;
import ml.json.JsonGoods;
import ml.kkt.KKT;
import ml.markingcode.SymbolsMarking;
import ml.model.CategoryFavorite;
import ml.model.CategoryGoods;
import ml.model.Check;
import ml.model.CheckList;
import ml.model.Discount;
import ml.model.Favorite;
import ml.model.Goods;
import ml.model.UserSwing;
import ml.modelLicense.Comp;
import ml.query.categoryGood.CategoryGoodList;
import ml.query.compCard.CompMessage;
import ml.query.favorite.CategoryByName;
import ml.query.favorite.CategoryFavoriteList;
import ml.query.goods.GoodByCode;
import ml.query.goods.QueryAllGoodsList;
import ml.table.CheckTable;
import ml.telegram.Bot;
import ml.telegram.BotClient;
import ml.util.ChangeKeyboardLayout;
import ml.util.CheckConnection;
import ml.util.CheckCyrillic;
import ml.util.CheckInternetConnection;
import ml.util.RegexpNameGoods;
import ml.util.Screen;
import ml.util.SearchCovidQRCode;
import ml.util.WorkWithMarkBarcode;
import ml.xml.XMLSearchAddress;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author dave
 */
public class CheckLayoutController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private Pane checkPane;
    @FXML
    private Label checkLabel;
    @FXML
    private TextField codeGood;
    @FXML
    private TextField sumCheck;
    @FXML
    private TextField sumCheckWithDiscount;
    @FXML
    private TableView<CheckTable> tableCheck;
    @FXML
    private TableColumn<CheckTable, String> codeCheckColumn;
    @FXML
    private TableColumn<CheckTable, String> nameCheckColumn;
    @FXML
    private TableColumn<CheckTable, BigDecimal> priceCheckColumn;
    @FXML
    private TableColumn<CheckTable, BigDecimal> amountCheckColumn;
    @FXML
    private TableColumn<CheckTable, BigDecimal> sumCheckColumn;
    @FXML
    private TableColumn<CheckTable, BigDecimal> residueCheckColumn;
    @FXML
    private TableColumn<CheckTable, String> markingCheckColumn;
    @FXML
    private TableColumn<CheckTable, Boolean> checkFreePriceCheckColumn;
    @FXML
    private TextField amountGood;
    @FXML
    private TextField codeOfGoodTextField;
    @FXML
    private Label checkLabel2;
    @FXML
    private Label infoFirstLabel;
    @FXML
    private Label infoSecondLabel;
    @FXML
    private Label checkLabel11;
    @FXML
    private Button firstFavorite;
    @FXML
    private Button secondFavorite;
    @FXML
    private Button thirdFavorite;
    @FXML
    private Button fourthFavorite;
    @FXML
    private Button fifthFavorite;
    @FXML
    private Button sixthFavorite;
    @FXML
    private Button newCheck;
    @FXML
    private Label getConnDB;
    @FXML
    private Label message;
    @FXML
    private Text bankText;
    @FXML
    private Text partialPaymentText;

    private Stage primaryStage;
    private List<Goods> goodsList;
    private JsonGoods jg = new JsonGoods();
    private QueryAllGoodsList allGoodsList = new QueryAllGoodsList();
    private List<CategoryFavorite> catFavorList;
    private CategoryFavoriteList favoriteList = new CategoryFavoriteList();
    private CategoryFavorite catFavor = new CategoryFavorite();
    private ObservableList<CheckTable> checkData = FXCollections.observableArrayList();
    private BarcodeConv barcodeConv = new BarcodeConv();
    private String weight = "1";
    private BigDecimal percent = new BigDecimal(0.00);
    private Discount discount = new Discount();
    private GrantedAuth grantedAuth = new GrantedAuth();
    private Object auth = grantedAuth.role();
    private String nameCategory;
    private CategoryFavorite cf;
    private CategoryByName categoryByName = new CategoryByName();
    private RegexpNameGoods regexpNameGoods = new RegexpNameGoods();
    private int selectRow = -1;
    private boolean checkNewPrice = false;
    private BigDecimal newPrice = new BigDecimal(0.00);
    private CheckConnection checkConnection = new CheckConnection();
    private Timeline timeline = new Timeline();
    private Timeline timelineForMessage = new Timeline();
    private boolean okClicked = false;

    boolean firstState = false;
    boolean lastState = false;
    private CompMessage compMessage = new CompMessage();
    private Comp comp = new Comp();
    private KKT kkt;
    private Bot bot;
    private BotClient botClient;
    private String oldDiscount;
    private String note;

    boolean checkBank = false;
    private static final Logger log = Logger.getLogger(CheckLayoutController.class);
//    private static final Logger loggerMail = Logger.getLogger(CheckLayoutController.class);

    private CategoryGoods category = new CategoryGoods();
    private List<CategoryGoods> categoryList;
    private CategoryGoodList categoryGoodList = new CategoryGoodList();
    private List<Favorite> favList;
    private DialogAlert dialogAlert = new DialogAlert();
    boolean partialPayment = false;
    private UserSwing userSwing = new UserSwing();

    @FXML
    private Label checkLabel21;
    @FXML
    private Label checkLabel22;
    @FXML
    private CheckBox bankCheckBox;

    /**
     * Поиск товара в спрятаном поле
     *
     * @param event
     */
    @FXML
    private void getCodeOfCode(ActionEvent event) {

        String fullCode = codeOfGoodTextField.getText();

        if (fullCode.length() <= 14) {
            if (!fullCode.equals("")) {
                //Проверка первого символа
                if (regexpNameGoods.nameGoods(fullCode) == true) {
                    getGoodName(fullCode);
                } else {
                    getGoodCode(fullCode, null);
                }
            }
        } else {

            SymbolsMarking insSymbolsMarking = new SymbolsMarking(fullCode);

            String codeWithMarkGS = insSymbolsMarking.getMarkWithGS();
//            System.out.println("!!!!!!!!!!!!!!!! : " + codeWithMarkGS);

            WorkWithMarkBarcode wwmb = new WorkWithMarkBarcode();

            CheckCyrillic chCyrillic = new CheckCyrillic();
            if (codeWithMarkGS != null) {
                if (chCyrillic.checkCyrillic(codeWithMarkGS) == false) {
//                System.out.println("НАШ КОД : " + fullCode);
                    String newCode = wwmb.nameGoods(codeWithMarkGS);
//                System.out.println("НОВЫЙ КОД : " + newCode);

                    if (!newCode.equals("")) {
                        //Проверка первого символа
                        if (regexpNameGoods.nameGoods(newCode) == true) {
                            getGoodName(newCode);
                        } else {
                            getGoodCode(newCode, codeWithMarkGS);
                        }
                    }
                } else {
                    dialogAlert.alert("Внимание!", "Не верная маркировка!", "Введите правильную маркировку");
                    codeOfGoodTextField.setText("");
                }
            }

        }

    }

    /**
     * Поиск товара по коду, по наименованию
     *
     * @param event
     */
    @FXML
    private void getCode(ActionEvent event) {
        String text = codeGood.getText();

        if (!text.equals("")) {
            //Проверка первого символа
            if (regexpNameGoods.nameGoods(text) == true) {
                getGoodName(text);
            } else {
                getGoodCode(text, null);
            }
        }

    }

    /**
     * Возвращает кол-во товара в таблицу чека.
     */
    @FXML
    private void getAmount() {
        String g = amountGood.getText();
        int i = tableCheck.getItems().size();   // кол-во строк в таблице

        //Замена , на .
        String gWithDot = g.replace(',', '.');
        gWithDot = gWithDot.replaceAll("[^0-9\\.]", "");

        //если кол-во строк не 0
        if (i != 0) {
            int row = i - 1;                        // последняя строка

            //Если выбрана строка то изменить в ней значение Кол-во
            if (selectRow >= 0) {
                //Кол-во * цена
                BigDecimal price = new BigDecimal(checkData.get(selectRow).getPrice().toString());
                BigDecimal amount = decimal("###.###", Double.parseDouble(gWithDot));
                BigDecimal sum = decimal("###.###", Double.parseDouble(price.multiply(amount).toString()));

                //Вставка данных в последнюю ячейку "кол-во" и "сумма строки" таблицы
                checkData.get(selectRow).setAmount(decimal("##.###", Double.parseDouble(gWithDot)));
                checkData.get(selectRow).setSum(sum);
                tableCheck.getItems().set(selectRow, checkData.get(selectRow));

                tableCheck.getSelectionModel().clearSelection();
                selectRow = -1;
            } else {
                //Кол-во * цена
                BigDecimal price = new BigDecimal(checkData.get(row).getPrice().toString());
                BigDecimal amount = decimal("###.###", Double.parseDouble(gWithDot));
                BigDecimal sum = decimal("###.###", Double.parseDouble(price.multiply(amount).toString()));
                //Вставка данных в последнюю ячейку "кол-во" и "сумма строки" таблицы
                checkData.get(row).setAmount(decimal("##.###", Double.parseDouble(gWithDot)));
                checkData.get(row).setSum(sum);
                tableCheck.getItems().set(row, checkData.get(row));
            }

            //Вывод на экран суммы скидок
            sumForTextField();

            codeOfGoodTextField.requestFocus();
        }
    }

    /**
     * Значение номера выбранной строки
     */
    @FXML
    private void getRow() {
        List<TablePosition> positions = tableCheck.getSelectionModel().getSelectedCells();
        if (!positions.isEmpty()) {

            int i = tableCheck.getItems().size();   // кол-во строк в таблице
            if (i > 0) {

                TablePosition pos = tableCheck.getSelectionModel().getSelectedCells().get(0);
                selectRow = pos.getRow();

//                            tableArrival.getSelectionModel().clearSelection();
            }
        }
    }

    /**
     * Выводит на экран кнопки избранного товара.
     */
    private void getFavButton() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Ml_FX.class.getResource("/ml/view/FavoriteButton.fxml"));

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Избранное");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            AnchorPane page = (AnchorPane) loader.load();
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            FavoriteButtonController controller = loader.getController();
            controller.setFavList(favList);
            controller.getButton(cf);
            controller.setDialogStage(dialogStage);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            //System.out.println("Кнопка : " + controller.getTextButtonFav());
            getGoodName(controller.getTextButtonFav());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void category1(ActionEvent event) {
        firstFavorite.setStyle("-fx-base: #b6e7c9;");
        secondFavorite.setStyle("");
        thirdFavorite.setStyle("");
        fourthFavorite.setStyle("");
        fifthFavorite.setStyle("");
        sixthFavorite.setStyle("");

        nameCategory = firstFavorite.getText();
//        System.out.println("!!!!!!!!! " + nameCategory);
        listButtonName(nameCategory);
        getFavButton();

//        listFavCateg(cf);
    }

    @FXML
    private void category2(ActionEvent event) {
        firstFavorite.setStyle("");
        secondFavorite.setStyle("-fx-base: #b6e7c9;");
        thirdFavorite.setStyle("");
        fourthFavorite.setStyle("");
        fifthFavorite.setStyle("");
        sixthFavorite.setStyle("");
        nameCategory = secondFavorite.getText();
        listButtonName(nameCategory);
        getFavButton();
    }

    @FXML
    private void category3(ActionEvent event) {
        firstFavorite.setStyle("");
        secondFavorite.setStyle("");
        thirdFavorite.setStyle("-fx-base: #b6e7c9;");
        fourthFavorite.setStyle("");
        fifthFavorite.setStyle("");
        sixthFavorite.setStyle("");
        nameCategory = thirdFavorite.getText();
        listButtonName(nameCategory);
        getFavButton();
    }

    @FXML
    private void category4(ActionEvent event) {
        firstFavorite.setStyle("");
        secondFavorite.setStyle("");
        thirdFavorite.setStyle("");
        fourthFavorite.setStyle("-fx-base: #b6e7c9;");
        fifthFavorite.setStyle("");
        sixthFavorite.setStyle("");
        nameCategory = fourthFavorite.getText();
        listButtonName(nameCategory);
        getFavButton();
    }

    @FXML
    private void category5(ActionEvent event) {
        firstFavorite.setStyle("");
        secondFavorite.setStyle("");
        thirdFavorite.setStyle("");
        fourthFavorite.setStyle("");
        fifthFavorite.setStyle("-fx-base: #b6e7c9;");
        sixthFavorite.setStyle("");
        nameCategory = fifthFavorite.getText();
        listButtonName(nameCategory);
        getFavButton();
    }

    @FXML
    private void category6(ActionEvent event) {
        firstFavorite.setStyle("");
        secondFavorite.setStyle("");
        thirdFavorite.setStyle("");
        fourthFavorite.setStyle("");
        fifthFavorite.setStyle("");
        sixthFavorite.setStyle("-fx-base: #b6e7c9;");
        nameCategory = sixthFavorite.getText();
        listButtonName(nameCategory);
        getFavButton();
    }

    /**
     * Выводит на экран сумму столбца СУММА и СУММА СО СКИДКОЙ
     */
    private void sumForTextField() {
        //Сумма столбца СУММА и СУММА СО СКИДКОЙ и вывод на экран
        BigDecimal totalSum = new BigDecimal(0.00);
        BigDecimal totalSumDisc = new BigDecimal(0.00);
        for (CheckTable item : tableCheck.getItems()) {
            totalSum = totalSum.add(item.getSum()).setScale(2, RoundingMode.HALF_UP);
//            totalSum.setScale(2, RoundingMode.HALF_UP);
            if (percent.signum() != 0) {
                totalSumDisc = totalSum.subtract(totalSum.multiply(percent.divide(new BigDecimal(100)))).setScale(2, RoundingMode.HALF_UP);
//                totalSumDisc.setScale(2, RoundingMode.HALF_UP);
                sumCheckWithDiscount.setText(totalSumDisc.toString());
            }
        }
        sumCheck.setText(totalSum.toString());
    }

    @FXML
    private void newCheck() {
        bot.checkNewCheck(sumCheck.getText(), sumCheckWithDiscount.getText(), tableCheck);

        log.info("Нажата кнопка Новый чек");
        while (tableCheck.getItems().size() > 0) {
            checkData.remove(0);
        }

        infoFirstLabel.setText("");
        infoSecondLabel.setText("");
        percent = new BigDecimal(0.00);
        codeOfGoodTextField.requestFocus();
        discount = new Discount();
        sumCheck.setText("0.00");
        sumCheckWithDiscount.setText("0.00");
        bankText.setFill(Color.BLACK);
        bankText.setText("Оплата наличными (F5)");
//        partialPaymentText.setFill(Color.BLACK);
//        partialPaymentText.setText("Частичная оплата (F6)");
        //Список всего товара
        goodsList = allGoodsList.listGoods();
        checkBank = false;
        bankCheckBox.setSelected(false);
        partialPayment = false;
        selectRow = -1;

    }

    @FXML
    private void bankCheckBoxAction(ActionEvent event) {
        if (!bankCheckBox.isSelected()) {
            bankText.setFill(Color.BLACK);
            bankText.setText("Оплата наличными (F5)");
            log.info("Оплата наличными");
        } else {
            bankText.setFill(Color.RED);
            bankText.setText("Оплата по карте (F5)");
            log.info("Оплата по безналу");

        }
    }

    /**
     * Вовзращает данные о товаре по коду
     *
     * @param code
     */
    private void getGoodCode(String code, String markingCode) {
        ChangeKeyboardLayout changeKeyboardLayout = new ChangeKeyboardLayout();

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
            String mark = "Б/М";
            //Если товар маркирован
            if (goods.getMarking() == true) {
//                RegexMarkingCode regexMarkingCode = new RegexMarkingCode();

                changeKeyboardLayout.changeRU_EN();
                try {
//                    DialogTextInputDontHidden dialogTextInputDontHidden = new DialogTextInputDontHidden();
//                    dialogTextInputDontHidden.dialog("Маркировка товара", "", "Введите маркировку товара:", "");
//                    dialogTextInputDontHidden.start(primaryStage);
//
//                    mark = dialogTextInputDontHidden.display();
                    mark = markingCode;

                    if (!"".equals(mark)) {
                        //если разрешена продажа
                        if (checkCategoryRestrict(goods) == true) {
                            displayResult(goods, mark);
                        } else {
                            dialogAlert.alert("Внимание!", "Продажа товара запрещена!", "Ограничение по времени");
                        }
                    }

                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(CheckLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
//                changeKeyboardLayout.changeEN_RU();
            } else {
                //если разрешена продажа
                if (checkCategoryRestrict(goods) == true) {
                    displayResult(goods, mark);
                } else {
                    dialogAlert.alert("Внимание!", "Продажа товара запрещена!", "Ограничение по времени");

                }

            }

        }
        sumForTextField();
        codeOfGoodTextField.requestFocus();
        codeOfGoodTextField.setText("");
        codeGood.setText(""); //Выделяет текст в поле

    }

    /**
     * Вовзращает данные о товаре по наименованию
     *
     * @param code
     */
    private void getGoodName(String name) {
        Goods goods = null;
        String mark = "";
        for (Goods gg1 : goodsList) {
            if (gg1.getName().equals(name)) {
                goods = gg1;
            }
        }
        if (goods != null) {
            //если разрешена продажа
            if (checkCategoryRestrict(goods) == true) {
                displayResult(goods, mark);
            } else {
                dialogAlert.alert("Внимание!", "Продажа товара запрещена!", "Ограничение по времени");

            }

//            displayResult(goods, mark);
        }
        sumForTextField();
        codeOfGoodTextField.requestFocus();
        codeOfGoodTextField.setText("");
        codeGood.setText(""); //Выделяет текст в поле
    }

    /**
     * Конвертирует String в BigDecimal и , в .
     */
    private BigDecimal decimal(String pattern, double value) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(value);
//        System.out.println(value + "  " + pattern + "  " + output);

        //Замена , на .
        String gWithDot = output.replace(',', '.');
        return new BigDecimal(gWithDot);
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayResult(Goods goods, String mark) {
        nameCheckColumn.setCellValueFactory(new PropertyValueFactory<CheckTable, String>("name"));
        codeCheckColumn.setCellValueFactory(new PropertyValueFactory<CheckTable, String>("code"));
        amountCheckColumn.setCellValueFactory(new PropertyValueFactory<CheckTable, BigDecimal>("amount"));
        priceCheckColumn.setCellValueFactory(new PropertyValueFactory<CheckTable, BigDecimal>("price"));
        sumCheckColumn.setCellValueFactory(new PropertyValueFactory<CheckTable, BigDecimal>("sum"));
        markingCheckColumn.setCellValueFactory(new PropertyValueFactory<CheckTable, String>("marking"));
        checkFreePriceCheckColumn.setCellValueFactory(new PropertyValueFactory<CheckTable, Boolean>("checkFreePrice"));
        if ("ROLE_ADMIN".equals(auth.toString())) {
            residueCheckColumn.setCellValueFactory(new PropertyValueFactory<CheckTable, BigDecimal>("residue"));

        }

        CheckTable checkTable = new CheckTable();
        for (int i = 0; i < 1; i++) {
            BigDecimal price = goods.getPrice();
            BigDecimal amount = decimal("###.###", Double.parseDouble(weight));
            BigDecimal sum = price.multiply(amount);
            checkTable.setCode(goods.getCode());
            checkTable.setName(goods.getName());
            checkTable.setPrice(price);
            checkTable.setAmount(amount);
            checkTable.setSum(sum);
            if (goods.getMarking() == true) {
                checkTable.setMarking(mark);
            } else {
                checkTable.setMarking("Б/М");
            }

            checkTable.setCheckFreePrice(checkNewPrice);
            if ("ROLE_ADMIN".equals(auth.toString())) {
                checkTable.setResidue(goods.getResidue());
            }
            log.info("Выбран товар: " + goods.getName());
            // заполняем таблицу данными
            if (!"".equals(goods.getName())) {
                checkData.add(checkTable);
            }
        }

        tableCheck.setItems(checkData);

        //Переход курсора на кол-во товара
        //amountGood.requestFocus();
        amountGood.setText(weight);
        amountGood.selectAll(); //Выделяет текст в поле
        weight = "1";
    }

    /**
     * Вызов диалогового окна по нажатию F3.
     */
    private void getDialogDiscount() {
        boolean okClick = false;

        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Ml.class.getResource("/ml/view/DialogDiscountClient.fxml"));

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Карта клиента");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            BorderPane page = (BorderPane) loader.load();
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            DialogDiscountClientController controller = loader.getController();
//            controller.setValues(master, cellNumber, time, ld, bot);

//            controller.setIndexCells(listIndexCells);
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            this.oldDiscount = controller.oldDiscount();
            this.note = controller.getNoteText();

            okClick = controller.displayOkClicked();

            if (okClick == true) {
//             
                discount = controller.getDiscount();

                if (discount.getIdDiscount() != null) {
                    percent = decimal("###.##", Double.parseDouble(discount.getPercent()));

                    infoFirstLabel.setText("Дисконтная карта №" + discount.getNumcard());
                    log.info("Выбрана дисконтная карта №" + discount.getNumcard());

                    infoSecondLabel.setText("Скидка: " + percent + "%");

                } else {
                    infoFirstLabel.setText("Дисконтная карта №" + discount.getNumcard());
                    log.info("Выбрана дисконтная карта №" + discount.getNumcard() + "Не существует!!!");

                    infoSecondLabel.setText("Не существует!!!");
                }
                sumForTextField();
            }
//            checkTechMap = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* NumberDiscount numberDiscount = new NumberDiscount();
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Карта клиента");
        dialog.setHeaderText("Введите номер карты или номер телефона");
        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(400);
        Optional<String> result = dialog.showAndWait();
        // The Java 8 way to get the response value (with lambda expression).
        result.ifPresent(code -> numberDiscount.numberDiscount(code));
        discount = numberDiscount.displayResult();
        if (discount.getIdDiscount() != null) {
        percent = decimal("###.###", Double.parseDouble(discount.getPercent()));
        
        result.ifPresent(numberCard -> infoFirstLabel.setText("Дисконтная карта №" + numberCard));
        result.ifPresent(numberCard -> log.info("Выбрана дисконтная карта №" + numberCard));
        
        infoSecondLabel.setText("Скидка: " + percent + "%");
        
        } else {
        result.ifPresent(numberCard -> infoFirstLabel.setText("Дисконтная карта №" + numberCard));
        result.ifPresent(numberCard -> log.info("Дисконтная карта №" + numberCard + "Не существует!!!"));
        
        infoSecondLabel.setText("Не существует!!!");
        }
        
        sumForTextField();*/
    }

    /**
     * Вызов диалогового окна по нажатию CTRL+N.
     */
    private void getDialogFreePrice() {

        //StartMain sm = new StartMain();
        //sm.showDiscountDialog();
        TextInputDialog dialog = new TextInputDialog("Введите свободную цену");
        dialog.setTitle("Свободная цена");
        dialog.setHeaderText("Свободная цена");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            infoFirstLabel.setText("Вы ввели новую цену: " + result.get());
            String newPrice = result.get();
            String newPriceWithDot = newPrice.replace(',', '.');

            int i = tableCheck.getItems().size();   // кол-во строк в таблице
            int row = i - 1;                        // последняя строка

            //Кол-во * цена
            BigDecimal price = new BigDecimal(newPriceWithDot);
            //BigDecimal amount = decimal("###.###", Double.parseDouble(gWithDot));
            BigDecimal sum = decimal("###.###", Double.parseDouble(price.multiply(checkData.get(row).getAmount()).toString()));;

            //Вставка данных в последнюю ячейку "кол-во" и "сумма строки" таблицы
            checkData.get(row).setPrice(decimal("##.###", Double.parseDouble(newPriceWithDot)));
            checkData.get(row).setSum(sum);
            checkData.get(row).setCheckFreePrice(true);
            tableCheck.getItems().set(row, checkData.get(row));

            log.info("Введена новая цена " + newPriceWithDot);
            sumForTextField();

            //checkNewPrice = true;
            this.newPrice = price;
            //discountNumberTextField.setText(percent.toString() + "%");
        }

    }

    /**
     * Открывает окно завершения покупки.
     */
    //Вызов диалогового окна по нажатию ESC
    private void getDialogEnd() {

        log.info("--Начало закрытия чека--");
        Check check = new Check();
        Goods goods = new Goods();
//        UserSwing userSwing = new UserSwing();
//        BarcodeIdGoodsByCode barcodeIdGoodsByCode = new BarcodeIdGoodsByCode();
        boolean checkTechMap = false;
        GoodByCode goodByCode = new GoodByCode();

//        IdUserByName idUserByName = new IdUserByName();
//        Authentication authentication = SecurityContextHolder.getContext().
//                getAuthentication();
//        String login = authentication.getName();
        Date date = new Date();
        List<Check> checkArrayList = new ArrayList<>();
        List<CheckList> checkListArrayList = new ArrayList<>();

        BigDecimal sum = new BigDecimal(0.00);
        BigDecimal sumWithDisсount = new BigDecimal(0.00);
//        idUserByName.setLoginUser(login);   //метод для idUser по логину

//        userSwing = idUserByName.displayResult(); //Возвращает пользователя
        //Получаем значение из таблицы
        int i = tableCheck.getItems().size();   // кол-во строк в таблице

        for (int j = 0; j < i; j++) {
            CheckList checkList = new CheckList();

            checkTechMap = false;
            if (percent.signum() != 0) {

                BigDecimal price = priceCheckColumn.getCellData(j);
                BigDecimal priceWithDiscount = priceWithDisount(priceCheckColumn.getCellData(j));
                BigDecimal amount = amountCheckColumn.getCellData(j);
                //Сумма чека без скидки
                sum = price.multiply(amount).add(sum);
                //Сумма чека со скидкой
                sumWithDisсount = priceWithDiscount.multiply(amount).add(sumWithDisсount);
                check.setSum(sumWithDisсount);
            } else {
                BigDecimal price = priceCheckColumn.getCellData(j);
                BigDecimal amount = amountCheckColumn.getCellData(j);
                sum = price.multiply(amount).add(sum);
                check.setSum(sum);
            }

//            barcodeIdGoodsByCode.setCode(codeCheckColumn.getCellData(j));
//            if (barcodeIdGoodsByCode.displayResult() == true) {
//                System.out.println("Товар приготовлен");
//                checkTechMap = true;
//            } else {
//                System.out.println("Товар на продажу");
//                checkTechMap = false;
//
//            }
            //Товар по коду из таблицы
            goodByCode.setCode(codeCheckColumn.getCellData(j));

            Boolean checkFreePrice = checkFreePriceCheckColumn.getCellData(j);

            //Прибыль с одной еденицы товара по коду
            BigDecimal profit = priceCheckColumn.getCellData(j)
                    .subtract(goodByCode.displayResult().getPriceOpt());

            //Прибыль с одного еденицы товара * кол-во
            profit = profit.multiply(amountCheckColumn.getCellData(j));
            //Список товара в чеке

            checkList.setGoods(goodByCode.displayResult());

//            goods.getCheckLists().add(checkList);
            checkList.setAmount(amountCheckColumn.getCellData(j));
            checkList.setProfit(profit);
            checkList.setPrice(priceCheckColumn.getCellData(j));
            checkList.setNewPrice(checkFreePrice);
            checkList.setMarking(markingCheckColumn.getCellData(j));
            checkListArrayList.add(checkList);

        }

        //ЧЕК
        check.setDate(date);
        check.setNote(null);

//        userSwing.getChecks().add(check);
        check.setUserSwing(userSwing);

        checkArrayList.add(check);

        String convertSum = decimal("###.##", Double.parseDouble(sum.toString())).toString();
        String convertSumWithDiscount = decimal("###.##", Double.parseDouble(sumWithDisсount.toString())).toString();
        boolean okClicked = true;
        Screen scr = new Screen();

        try {

            FXMLLoader loader;

            if (scr.screenSize() == true) {
                loader = new FXMLLoader(getClass().getResource("/ml/view/EndCheck.fxml"));

            } else {
                loader = new FXMLLoader(getClass().getResource("/ml/view/EndCheck_1024_600.fxml"));

            }

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Конец продажи");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            AnchorPane page = (AnchorPane) loader.load();
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            EndCheckController controller = loader.getController();

            controller.setSumText(convertSum, convertSumWithDiscount);
            controller.setDialogStage(userSwing, comp, dialogStage, kkt, bankCheckBox.isSelected(), bot, botClient, partialPayment, note);

            controller.setAddCheck(tableCheck, checkArrayList, checkListArrayList, discount, checkNewPrice, newPrice);

            // Show the dialog and wait until the user closes it
            if (!convertSum.equals("0")) {
                this.discount.setPercent(oldDiscount);
                dialogStage.showAndWait();
            }
            okClicked = controller.displayOkClicked();
            checkTechMap = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Удаляет все строки в таблице
        if (okClicked == true) {

            while (tableCheck.getItems().size() > 0) {
                checkData.remove(0);
            }

            infoFirstLabel.setText("");
            infoSecondLabel.setText("");
            percent = new BigDecimal(0.00);
            codeOfGoodTextField.requestFocus();
            discount = new Discount();
            sumCheck.setText("0.00");
            sumCheckWithDiscount.setText("0.00");
            checkNewPrice = false;
            newPrice = new BigDecimal(0.00);
            checkBank = false;
            bankCheckBox.setSelected(false);
            partialPayment = false;
            bankText.setFill(Color.BLACK);
            bankText.setText("Оплата наличными (F5)");
//            partialPaymentText.setFill(Color.BLACK);
//            partialPaymentText.setText("Частичная оплата (F6)");
            //Список всего товара
            goodsList = allGoodsList.listGoods();
            selectRow = -1;

        }
        allGoodsList.clearList();

    }

    /**
     * Возвращает цену со скидкой
     *
     * @param price
     * @return
     */
    private BigDecimal priceWithDisount(BigDecimal price) {

        BigDecimal perc = price.multiply(this.percent).divide(new BigDecimal(100));
        price = price.subtract(perc);

        return price;
    }

    /**
     * Список имен для кнопок избранного
     *
     * @param name
     */
    private void listButtonName(String name) {
        cf = new CategoryFavorite();

        categoryByName.setName(name);
        cf = categoryByName.displayResult();
    }

    /**
     * Проверка соединения с БД
     */
    private void getConnection() {

        //Проверка соединения с 
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
                newCheck.setDisable(false);
                firstFavorite.setDisable(false);
                secondFavorite.setDisable(false);
                thirdFavorite.setDisable(false);
                fourthFavorite.setDisable(false);
                fifthFavorite.setDisable(false);
                sixthFavorite.setDisable(false);

                firstState = true;

            } else {
                getConnDB.setText("Соединения нет");
                getConnDB.setStyle("-fx-text-fill: RED;");
                newCheck.setDisable(true);
                firstFavorite.setDisable(true);
                secondFavorite.setDisable(true);
                thirdFavorite.setDisable(true);
                fourthFavorite.setDisable(true);
                fifthFavorite.setDisable(true);
                sixthFavorite.setDisable(true);
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
     * Оплата наличными или по безналу
     *
     * @param checkBank
     */
    private void setCheckBank(boolean checkBank) {
        System.out.println("CHECKBANK_1: " + checkBank);
        if (checkBank == false) {
            System.out.println("CHECKBANK_2: syuda");

            bankCheckBox.setSelected(false);

            bankText.setFill(Color.BLACK);
            bankText.setText("Оплата наличными (F5)");
            log.info("Оплата наличными");

        } else {
            System.out.println("CHECKBANK_3: tuda");

            bankCheckBox.setSelected(true);

            bankText.setFill(Color.RED);
            bankText.setText("Оплата по карте (F5)");
            log.info("Оплата по безналу");

        }

    }

    /**
     * Оплата частичная
     *
     * @param partialPayment
     */
    private void setPartialPayment(boolean partialPayment) {
        if (partialPayment == false) {
            partialPaymentText.setFill(Color.BLACK);
            partialPaymentText.setText("Частичная оплата (F6)");
            log.info("Оплата не частичная");
//            System.out.println("Оплата не частичная");

        } else {
            partialPaymentText.setFill(Color.RED);
            partialPaymentText.setText("Частичная оплата (F6)");
            log.info("Оплата частичная");
//            System.out.println("Оплата частичная");

        }

    }

    public void setCompCard(Comp comp, KKT kkt) {
        this.comp = comp;
        this.kkt = kkt;
    }

    /**
     * Проверка запрета/разрешения продажи по категории товара
     */
    private boolean checkCategoryRestrict(Goods g) {
        boolean b = false;
        for (CategoryGoods cg : categoryList) {
            if (Objects.equals(cg.getIdCategoryGoods(), g.getCategoryGoods().getIdCategoryGoods())) {
//                System.out.println("Категори товара: " + cg.getName());
//                getPrintKKT(cg.getNote());

                b = getTimeInCategory(cg.getNote());
            }
        }
        return b;
    }

    /**
     * Берет значение из описания категории товара Выводит разрешение на продажу
     * товара по времени
     */
    private boolean getTimeInCategory(String note) {

        boolean b = false;
        String str = note;
        String timeStart = "";
        String timeFinish = "";

        String hSt = "";
        String mSt = "";
        String hFt = "";
        String mFt = "";
        String fullSt = "";
        String fullFt = "";

        int indexStartTime = str.indexOf("/1: ");
        int indexFinishTime = str.indexOf(" :1|");

        if (indexStartTime == -1) {
        } else {
            timeStart = str.substring(indexStartTime, indexFinishTime + 4);
            timeFinish = str.substring(indexStartTime, indexFinishTime + 4);

        }

        if (!"".equals(timeStart)) {
            hSt = timeStart.substring(4, 6);
            mSt = timeStart.substring(7, 9);

            fullSt = hSt + ":" + mSt;
//            System.out.println("Начало продаж: " + fullSt);

        }

        if (!"".equals(timeFinish)) {
            hFt = timeFinish.substring(11, 13);
            mFt = timeFinish.substring(14, 16);
            fullFt = hFt + ":" + mFt;
//            System.out.println("Конец продаж: " + fullFt);

        }
        if (!"".equals(fullSt) && !"".equals(fullFt)) {
            LocalTime st = LocalTime.parse(fullSt);
            LocalTime fn = LocalTime.parse(fullFt);
            LocalTime now = LocalTime.now();
            if (now.isAfter(st) && now.isBefore(fn)) {
                b = true;
            }
        } else {
            b = true;
        }

        return b;
    }

    /**
     * Берет значение из описания категории товара Вывод печатать чек ККТ или
     * нет
     */
    private void getPrintKKT(String note) {
        String str = note;
        String kkt = "";
        String checkKKTPrint = "0";
        int indexStartPrintKKT = str.indexOf("/2: ");
        int indexFinishPrintKKT = str.indexOf(" :2|");

        if (indexStartPrintKKT == -1) {
//                    System.out.println("Вывод: " + category.getNote());
        } else {
//            System.out.println("Вывод_2: " + str.substring(0, indexStartPrintKKT));
//            System.out.println("Вывод_2: " + str.substring(indexStartPrintKKT, indexFinishPrintKKT + 4));
            kkt = str.substring(indexStartPrintKKT, indexFinishPrintKKT + 4);
        }

        if (!"".equals(kkt)) {
//            System.out.println("Вывод_печати_чека: " + kkt.substring(4, 6));
            checkKKTPrint = kkt.substring(4, 6);
        } else {
        }

        if (null != checkKKTPrint) {
            switch (checkKKTPrint) {
                case "tr":
//                    printCheckKKTCheckBox.setSelected(true);
                    break;
                case "fa":
//                    printCheckKKTCheckBox.setSelected(false);
                    break;
                default:
//                    printCheckKKTCheckBox.setSelected(false);
                    break;
            }
        }

    }

    /**
     * Поиск сертификата вакцинации от COVID-19
     *
     * @param url
     */
    private void getDialogSearchCovidQRCode() {

        SearchCovidQRCode covidQRCode = new SearchCovidQRCode();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Сертификат вакцинации от COVID-19");
        dialog.setHeaderText("Отсканируйте QR-код");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(code -> covidQRCode.openWebpage(code));

//https://www.gosuslugi.ru/covid-cert/verify/9910000036637151?lang=ru&ck=3808fc97a43ba9f481624fd9b5f31007
    }

    /**
     * Ввести клента
     */
    private void getClientNumber() {

    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public void setBotClient(BotClient botClient) {
        this.botClient = botClient;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public void setCategoryGoodsList(List<CategoryGoods> categoryList) {
        this.categoryList = categoryList;
    }

    public void setUserSwing(UserSwing user) {
        this.userSwing = user;
    }

    public void setСatFavorList(List<CategoryFavorite> cf) {
        this.catFavorList = cf;
    }

    public void setFavButtonList(List<Favorite> favList) {
        this.favList = favList;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
///////////////////////////////
//        //Сообщение в приложении
//        timelineForMessage = new Timeline(new KeyFrame(Duration.seconds(10), ev -> {
//
//            compMessage.setComp(comp);
//            message.setText(compMessage.displayResult().getMessage());
//
//        }));
//        timelineForMessage.setCycleCount(100);
//        timelineForMessage.play();
///////////////////////////////
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                //проверка соединения с БД
//                getConnection();
//                categoryList = categoryGoodList.getList();
                //Список категорий избранного
//                catFavorList = favoriteList.listFavoriteCategory();
                if (!catFavorList.isEmpty()) {
                    for (int i = 0; i < catFavorList.size(); i++) {
                        if (i == 0) {
                            firstFavorite.setText(catFavorList.get(0).getName());
                        }
                        if (i == 1) {
                            secondFavorite.setText(catFavorList.get(1).getName());
                        }
                        if (i == 2) {
                            thirdFavorite.setText(catFavorList.get(2).getName());
                        }
                        if (i == 3) {
                            fourthFavorite.setText(catFavorList.get(3).getName());
                        }
                        if (i == 4) {
                            fifthFavorite.setText(catFavorList.get(4).getName());
                        }
                        if (i == 5) {
                            sixthFavorite.setText(catFavorList.get(5).getName());
                        }
                    }
                }

                //Ввод поле КОЛ_ВО только цифр точки или запятой
                amountGood.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d*([.,]\\d*)?")) {
                            amountGood.setText(oldValue);
                        }
                    }
                });

                //Если закрыть окно, то проверка соединения окончена
                Stage stage = (Stage) borderPane.getScene().getWindow();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        timeline.stop();
                        bot.checkNewCheck(sumCheck.getText(), sumCheckWithDiscount.getText(), tableCheck);

                    }
                });

                //Список всего товара
//                goodsList = allGoodsList.listGoods();
                //Фокус на поле Код товара
                codeOfGoodTextField.requestFocus();

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
                TextFields.bindAutoCompletion(codeGood, both).setVisibleRowCount(10);
//TextFields.bindAutoCompletion(
//            codeGood,
//            suggestionRequest -> matchingItems(both, suggestionRequest.getUserText())
//        );

                //Включение диалоговых окон по нажатию клавиш
                borderPane.setOnKeyPressed(
                        event -> {
                            switch (event.getCode()) {
                                case F2:
                                    codeGood.requestFocus();
                                    break;

                                case F3:
//                                    if (event.isControlDown()) {
                                    getDialogDiscount();
//                                    }
                                    break;
                                case F4:
//                                    getClientNumber();
                                    break;
//                                case F5:
//                                    checkBank = checkBank == false;
//                                    setCheckBank(checkBank);
//                                    break;
//                                case F6:
//                                    partialPayment = partialPayment == false;
//                                    setPartialPayment(partialPayment);
//                                    break;
                                case F10:
//                                    if (event.isControlDown()) {
                                    getDialogSearchCovidQRCode();
//                                    }
                                    break;
                                case ESCAPE:
//                                    if ("Соединение есть".equals(getConnDB.getText())) {
//                                        getDialogEnd();
//                                    }
                                    getDialogEnd();

                                    break;

                                case F:
                                    if (event.isControlDown()) {
                                        //getDialogFavofite();
                                    }
                                    break;
                                case HOME:
                                    getDialogFreePrice();
                                    break;
                                case DELETE:
                                    //Удаление строки
                                    TablePosition pos = tableCheck.getSelectionModel().getSelectedCells().get(0);
                                    int row = pos.getRow();
                                    log.info("Удалена позиция " + checkData.get(row).getName() + " в кол-ве " + checkData.get(row).getAmount());
//                                    loggerMail.info("Удалена позиция " + checkData.get(row).getName() + " в кол-ве " + checkData.get(row).getAmount());

                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                                    LocalDateTime now = LocalDateTime.now();
//                                    System.out.println(dtf.format(now));

                                    bot.checkDeleteButton(checkData.get(row).getName() + " в кол-ве " + checkData.get(row).getAmount() + "; время " + dtf.format(now));
                                    checkData.remove(row);
                                    codeOfGoodTextField.requestFocus();

                                    sumForTextField();
                                    codeOfGoodTextField.requestFocus();
                                    selectRow = -1;
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
                                amountGood.selectAll(); //Выделяет текст в поле
                                break;
                            case F2:
                                codeGood.requestFocus();
                                break;
                            case F3:
//                                    if (event.isControlDown()) {
                                getDialogDiscount();
                                System.err.println("qqq");

//                                    }
                                break;
                            case F4:
//                                getClientNumber();

                                break;
                            case F5:
                                checkBank = checkBank == false;
                                setCheckBank(checkBank);
                                break;
//                            case F6:
//                                partialPayment = partialPayment == false;
//                                setPartialPayment(partialPayment);
//                                break;
                            case F10:
//                                    if (event.isControlDown()) {
//                                getDialogSearchCovidQRCode();
//                                    }
                                break;
                            case HOME:
                                getDialogFreePrice();
                                break;
                            case ESCAPE:
//                                if ("Соединение есть".equals(getConnDB.getText())) {
//                                    getDialogEnd();
//                                }
                                getDialogEnd();
                                break;
                        }
                    }
                });

                codeOfGoodTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        switch (event.getCode()) {
                            case ALT_GRAPH:
                                amountGood.requestFocus();
                                amountGood.selectAll(); //Выделяет текст в поле
                                break;
                            case F2:
                                codeGood.requestFocus();
                                break;
                            case F3:
//                                    if (event.isControlDown()) {
//                                getDialogDiscount();

//                                    }
                                break;
                            case F4:
//                                getClientNumber();

                                break;
                            case F5:

                                checkBank = checkBank == false;
                                setCheckBank(checkBank);
                                break;
//                            case F6:
//                                partialPayment = partialPayment == false;
//                                setPartialPayment(partialPayment);
//                                break;
                            case F10:
//                                    if (event.isControlDown()) {
//                                getDialogSearchCovidQRCode();
//                                    }
                                break;
                            case HOME:
                                getDialogFreePrice();
                                break;
                            case ESCAPE:
//                                if ("Соединение есть".equals(getConnDB.getText())) {
//                                    getDialogEnd();
//                                }
//                                getDialogEnd();

                                break;
                        }
                    }
                });

                //Список категорий
                /*queryCategoryList.runQueryBasedOnCategory();
                List result = queryCategoryList.displayResult();
                displayButtonCategoryResult(result);*/
            }
        });
    }

    @FXML
    private void codeGoodOnKeyPressed(KeyEvent event) {
    }
}
