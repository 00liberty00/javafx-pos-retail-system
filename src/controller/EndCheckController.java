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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ml.Ml_FX;
import ml.json.JsonCheck;
import ml.kkt.KKT;
import ml.model.Check;
import ml.model.CheckDiscount;
import ml.model.CheckList;
import ml.model.CheckListNewPrice;
import ml.modelLicense.Comp;
import ml.model.Discount;
import ml.model.UserSwing;
import ml.print.Print;
import ml.query.barcode.BarcodeIdGoodsByCode;
import ml.query.check.AddCheck;
import ml.query.check.AddCheckList;
import ml.query.check.LastCheck;
import ml.query.checkDiscount.AddCheckDiscount;
import ml.query.checkNewPrice.AddCheckNewPrice;
import ml.query.discount.UpdateSumDiscount;
import ml.query.goods.UpdateResidueDish;
import ml.query.goods.UpdateResidueGood;
import ml.table.CheckTable;
import ml.telegram.Bot;
import ml.telegram.BotClient;
import ml.telegram.BotClientCommand;
import ml.util.MoneyRegexp;
import ml.util.ValidTextField;
import ml.xml.XMLSettings;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 * FXML Controller class
 *
 * @author dave
 */
public class EndCheckController implements Initializable {

    @FXML
    private Pane partPayPane;
    @FXML
    private Pane fullPayPane;
    @FXML
    private Label numLabel;
    @FXML
    private Label numberCheckLabel;
    @FXML
    private Label sumLabel;
    @FXML
    private TextField sumCheckTextField;
    @FXML
    private Label discLabel;
    @FXML
    private TextField discountCheckTextField;
    @FXML
    private Label cashLabel;
    @FXML
    private TextField cashCheckTextField;
    @FXML
    private Label shortChangeLabel;
    @FXML
    private TextField shortChangeCheckTextField;
    @FXML
    private TextField cashPartCheckTextField;
    @FXML
    private TextField bankPartCheckTextField;
    @FXML
    private Button cancelCheckButton;
    @FXML
    private Button okCheckButton;
    @FXML
    private Label message;
    @FXML
    private Label kktPrintLabel;
    @FXML
    private Text bnalText;
    @FXML
    private Text nalText;
    @FXML
    private TextField phoneEmailClientCheckTextField;
    @FXML
    private Text printText;

    private TableView<CheckTable> tableCheck;
    private Stage dialogStage;
    private List<Check> checkArrayList = new ArrayList<Check>();
    private List<CheckList> checkListArrayList = new ArrayList<CheckList>();
    private boolean inetConnect;
    private Check check = new Check();
    private CheckList checkList = new CheckList();
    private Discount discount = new Discount();
    private boolean okClicked = false;
    private AddCheckList addCheckList = new AddCheckList();
    private AddCheck addCheck = new AddCheck();
    private LastCheck lastCheck = new LastCheck();
    private ValidTextField validTextField = new ValidTextField();
    private UpdateSumDiscount dis = new UpdateSumDiscount();
    private boolean checkNewPrice = false;
    private BigDecimal newPrice = new BigDecimal(0.00);
    private KKT kkt;
    boolean checkBank;
    boolean checkKKT = true;
    private Bot bot;
    private BotClient botClient;
    boolean checkPrint;
    private Comp comp;
//    !!!!!private CheckForJson checkForJson = new CheckForJson();

    private XMLSettings xmls = new XMLSettings();
    private static final Logger log = Logger.getLogger(EndCheckController.class);
    private BigDecimal sumDiscount = new BigDecimal("0.00");
    private boolean partialPayment;
    private String note;
    private UserSwing userSwing;
//    private Map<Integer, Check> mapCheck = new HashMap();
//    private Map<Integer, CheckList> mapCheckList = new HashMap();
//    private Map<Integer, CheckDiscount> mapCheckDiscount = new HashMap();
//    private Map<Integer, CheckListNewPrice> mapCheckListNewPrice = new HashMap();

    public boolean displayOkClicked() {

        return okClicked;
    }

    public void setDialogStage(UserSwing us, Comp comp, Stage dialogStage, KKT kkt, boolean checkBank, Bot bot, BotClient botClient, boolean partialPayment, String note) {
        this.userSwing = us;
        this.comp = comp;
        this.dialogStage = dialogStage;
        this.checkBank = checkBank;
        this.kkt = kkt;
        this.bot = bot;
        this.botClient = botClient;
        this.partialPayment = partialPayment;
        this.note = note;
    }

    public void setSumText(String convertSum, String convertSumWithDiscount) {
        // set text from another class
        sumCheckTextField.setText(convertSum);
        discountCheckTextField.setText(convertSumWithDiscount);
    }

    public void setAddCheck(TableView<CheckTable> tableCheck, List<Check> check, List<CheckList> checkList, Discount discount, boolean checkNewPrice, BigDecimal newPrice) {
        // set text from another class
        this.tableCheck = tableCheck;
        this.checkArrayList = check;
        this.checkListArrayList = checkList;
        this.discount = discount;
        this.checkNewPrice = checkNewPrice;
        this.newPrice = newPrice;
    }
//
//    public void setTechMap(boolean checkTechMap) {
//        this.checkTechMap = checkTechMap;
//    }

    @FXML
    private void getShortChange(KeyEvent event) {
        if ("0".equals(discountCheckTextField.getText())) {

            BigDecimal sumCheck = new BigDecimal(sumCheckTextField.getText());
            BigDecimal cashCheck = new BigDecimal(0);
            if (!"".equals(cashCheckTextField.getText())) {
                cashCheck = new BigDecimal(cashCheckTextField.getText());
            }
            if (checkBank == true) {
                shortChangeCheckTextField.setText("0.00");
            } else {
                shortChangeCheckTextField.setText(cashCheck.subtract(sumCheck).toString());
            }

        } else {
            BigDecimal sumWithDiscountCheck = new BigDecimal(discountCheckTextField.getText());
            BigDecimal cashCheck = new BigDecimal(0);
            if (!"".equals(cashCheckTextField.getText())) {
                cashCheck = new BigDecimal(cashCheckTextField.getText());
            }
            if (checkBank == true) {
                shortChangeCheckTextField.setText("0.00");
            } else {
                shortChangeCheckTextField.setText(cashCheck.subtract(sumWithDiscountCheck).toString());

            }
        }
    }

    @FXML
    private void getBankPartMoney(KeyEvent event) {
        if ("0".equals(discountCheckTextField.getText())) {

            BigDecimal sumCheck = new BigDecimal(sumCheckTextField.getText());
            BigDecimal cashCheck = new BigDecimal(0);
            if (!"".equals(cashPartCheckTextField.getText())) {
                cashCheck = new BigDecimal(cashPartCheckTextField.getText());
            }

            bankPartCheckTextField.setText(sumCheck.subtract(cashCheck).toString());

        } else {
            BigDecimal sumWithDiscountCheck = new BigDecimal(discountCheckTextField.getText());
            BigDecimal cashCheck = new BigDecimal(0);
            if (!"".equals(cashPartCheckTextField.getText())) {
                cashCheck = new BigDecimal(cashPartCheckTextField.getText());
            }

            bankPartCheckTextField.setText(sumWithDiscountCheck.subtract(cashCheck).toString());

        }
    }

    @FXML
    private boolean isCancelClicked(ActionEvent event) {

        okClicked = false;
        dialogStage.close();
        return okClicked;
    }

    @Transactional
    @FXML
    private boolean isOkClicked(ActionEvent event) {

        String defaultValue = xmls.getPrinter();
        Boolean kitchenCheck = xmls.getPrinterKitchenCheck();

        //grid.add(first, 1, 1);
        //grid.add(second, 2, 2);
        Print p = new Print();
        MoneyRegexp moneyRegexp = new MoneyRegexp();
        UpdateResidueGood updateResidueGood = new UpdateResidueGood();
        UpdateResidueDish updateResidueDish = new UpdateResidueDish();
        BarcodeIdGoodsByCode barcodeIdGoodsByCode = new BarcodeIdGoodsByCode();

        JsonCheck jsonCheck = new JsonCheck();

        boolean checkTechMap = false;

        boolean b = moneyRegexp.check(cashCheckTextField.getText());
        boolean bb = moneyRegexp.check(cashPartCheckTextField.getText());

        String phoneOrEmail = phoneEmailClientCheckTextField.getText();

        if (b == false && bb == false) {
            message.setText("Введите сумму!!!");
        } else {

            for (Check check1 : checkArrayList) {
                BigDecimal money = new BigDecimal("0.00");
                check = check1;
                //addC.addCheck(check);
                //check.setSum(sum);
                if (partialPayment) {
                    money = new BigDecimal(cashPartCheckTextField.getText());
                    check.setNal(new BigDecimal(cashPartCheckTextField.getText()));
                    check.setBnal(new BigDecimal(bankPartCheckTextField.getText()));
                    check.setMoney(money);
                    checkBank = true;
                } else {
                    money = new BigDecimal(cashCheckTextField.getText());
                    if (checkBank) {
                        check.setNal(new BigDecimal("0.00"));
                        check.setBnal(check.getSum());
                    } else {
                        check.setNal(check.getSum());
                        check.setBnal(new BigDecimal("0.00"));
                    }
                    check.setMoney(money);
                }

                check.setBank(checkBank);

                addCheck.add(check);

//    !!!!!!!!            checkForJson.setCheck(check);
                log.info("Сумма чека: " + check.getSum());
                log.info("Клиент дал наличные: " + check.getMoney());

                //printerCheck.printer(check.getName(), check.getAmount(), check.getPrice());
            }

            //Если есть дисконт, то запись
            if (discount.getNumcard() != null) {
                BotClientCommand botClientCommand = new BotClientCommand();

                AddCheckDiscount addCheckDiscount = new AddCheckDiscount();
                CheckDiscount cd = new CheckDiscount();
                UpdateSumDiscount updateSumDiscount = new UpdateSumDiscount();
                if (!discountCheckTextField.getText().equals("0")) {
                    this.sumDiscount = new BigDecimal(discountCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP);

                } else {
                    this.sumDiscount = new BigDecimal(sumCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP);

                }

                cd.setCheck(check);
                cd.setDiscount(discount);
                updateSumDiscount.update(discount, sumDiscount);
                addCheckDiscount.add(cd);

//           !!!!!!     checkForJson.setCheckDiscount(cd);
//            !!!!!!!    checkForJson.setDiscount(discount);
            }

            lastCheck.get();                    //метод для номера последней записи в Check (Последний номер чека)
            for (CheckList check1 : checkListArrayList) {
                checkList = check1;
                checkList.setCheck(lastCheck.displayResult());
                check.getCheckLists().add(checkList);

                addCheckList.add(checkList);

                //Запись в JSON
//                jsonCheck.setCheckList(checkList);
                barcodeIdGoodsByCode.setCode(checkList.getGoods().getCode());
                if (barcodeIdGoodsByCode.displayResult() == true) {
//                    System.out.println("Товар приготовлен");
                    checkTechMap = true;
                } else {
//                    System.out.println("Товар на продажу");
                    checkTechMap = false;

                }

                if (checkTechMap == false) {
                    updateResidueGood.update(checkList, null, null, false);
                } else {

                    updateResidueDish.update(checkList, null, null, false);
                }
                //printerCheck.printer(check.getName(), check.getAmount(), check.getPrice());

                if (checkList.getNewPrice() == true) {
                    CheckListNewPrice checkListNewPrice = new CheckListNewPrice();
                    AddCheckNewPrice addCheckNewPrice = new AddCheckNewPrice();
                    checkListNewPrice.setCheckList(checkList);
                    checkListNewPrice.setNewPrice(newPrice);
                    //newPrice
                    addCheckNewPrice.add(checkListNewPrice);

//           !!!!!!!         checkForJson.setCheckListNewPrice(checkListNewPrice);
                }

            }

//            checkForJson.setCheckList(checkListArrayList);
            //Запись в JSON
//           !!!!!!!! jsonCheck.setCheck(checkForJson);
            //p.pageSetup(grid, dialogStage);
            //printerCheck.finishPrint(check.getNumber() + 1, sumCheckTextField.getText(),
            //discountCheckTextField.getText(), percent.toString());
            bot.viewCheckBot(checkListArrayList, new BigDecimal(sumCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP), sumDiscount);
            //отправка клиенту его заказа 
            if (discount.getTelegramIdChat() != null) {
                botClient.sendOrderClient(discount, checkListArrayList, new BigDecimal(sumCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP), sumDiscount);
            }
            okClicked = true;
            dialogStage.close();

            //Проверка, каким принтером печатать чек
            int i0 = defaultValue.compareTo("Без печати");
            int i1 = defaultValue.compareTo("ККТ (WI-FI)");
            int i2 = defaultValue.compareTo("ККТ (USB)");

            int i3 = defaultValue.compareTo("Принтер чеков");
            if (i0 == 0) {
//                System.out.println("Чек не печатается!!! " + cashCheckTextField.getText());
            }
            if (i1 == 0) {
                if (checkKKT == true) {
                    printCheckByККТ(checkBank, checkPrint, phoneOrEmail);
                }
            }
            if (i2 == 0) {
                if (checkKKT == true) {
                    printCheckByККТ(checkBank, checkPrint, phoneOrEmail);
                }
            }
            if (i3 == 0) {
                printCheckByPrinter(comp);
                if (kitchenCheck == true) {
                    printKitchenCheckByPrinter();
                }

            }
        }

        return okClicked;
    }

    /**
     * Печать чеков принтером
     */
    private void printCheckByPrinter(Comp comp) {

        //PrinterCheck printerCheck = new PrinterCheck();
        //Проверка соединения с интернетом
        //printerCheck.startPrint();
        try {
            FXMLLoader loaderPrintCheck = new FXMLLoader();
            loaderPrintCheck.setLocation(Ml_FX.class.getResource("/ml/view/PrintCheck.fxml"));
// Create the dialog Print.
            Stage dialogStagePrint = new Stage();
            dialogStagePrint.setTitle("Печать чека");
            dialogStagePrint.initModality(Modality.WINDOW_MODAL);
            dialogStagePrint.initOwner(dialogStage);
            AnchorPane pagePrint = (AnchorPane) loaderPrintCheck.load();
            Scene scenePrint = new Scene(pagePrint);
            dialogStagePrint.setScene(scenePrint);

            PrintCheckController controllerPrint = loaderPrintCheck.getController();

            controllerPrint.setPrintCheck(comp, checkListArrayList, checkArrayList, discount,
                    new BigDecimal(sumCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                    new BigDecimal(discountCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                    new BigDecimal(cashCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                    new BigDecimal(shortChangeCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                    note);
            controllerPrint.setDialogStage(dialogStagePrint);

            dialogStagePrint.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Печать чека на кухню
     */
    private void printKitchenCheckByPrinter() {

        //PrinterCheck printerCheck = new PrinterCheck();
        //Проверка соединения с интернетом
        //printerCheck.startPrint();
        try {
            FXMLLoader loaderPrintCheck = new FXMLLoader();
            loaderPrintCheck.setLocation(Ml_FX.class.getResource("/ml/view/PrintKitchenCheck.fxml"));
// Create the dialog Print.
            Stage dialogStagePrint = new Stage();
            dialogStagePrint.setTitle("Печать чека");
            dialogStagePrint.initModality(Modality.WINDOW_MODAL);
            dialogStagePrint.initOwner(dialogStage);
            AnchorPane pagePrint = (AnchorPane) loaderPrintCheck.load();
            Scene scenePrint = new Scene(pagePrint);
            dialogStagePrint.setScene(scenePrint);

            PrintKitchenCheckController controllerPrint = loaderPrintCheck.getController();

            controllerPrint.setPrintKitchenCheck(checkListArrayList, checkArrayList);
            controllerPrint.setDialogStage(dialogStagePrint);

            dialogStagePrint.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Печать чеков ККТ
     */
    private void printCheckByККТ(boolean checkBank, boolean checkPrint, String phoneOrEmail) {
        log.info("Печать ККТ");

        if (partialPayment) {
            kkt.printFiscalCheck(checkListArrayList, checkArrayList, discount,
                    new BigDecimal(sumCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                    new BigDecimal(discountCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                    new BigDecimal(bankPartCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                    new BigDecimal("0.00").setScale(2, RoundingMode.HALF_UP),
                    checkBank, checkPrint, phoneOrEmail, userSwing);
        } else {
            //Печать фисального чека
            kkt.printFiscalCheck(checkListArrayList, checkArrayList, discount,
                    new BigDecimal(sumCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                    new BigDecimal(discountCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                    new BigDecimal(cashCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                    new BigDecimal(shortChangeCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                    checkBank, checkPrint, phoneOrEmail, userSwing);
        }
        //kkt.destroyDriver();

        /*
        //Печать нефискального чека
        kkt.startNonFiscalCheck();
        kkt.printNonFiscalCheck(checkListArrayList, checkArrayList, discount,
                new BigDecimal(sumCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                new BigDecimal(discountCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                new BigDecimal(cashCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP),
                new BigDecimal(shortChangeCheckTextField.getText()).setScale(2, RoundingMode.HALF_UP));
        kkt.endNonFiscalCheck();
        kkt.destroyDriver();
         */
    }

    /**
     * Оплата наличными или по безналу
     *
     * @param checkPrint
     */
    private void checkPrintCheck(boolean checkPrint) {
        if (checkPrint == false) {
            printText.setFill(Color.BLACK);
            printText.setText("Чек не печатается");
            log.info("Чек не печатается");

        } else {
            printText.setFill(Color.RED);
            printText.setText("Чек печатается");
            log.info("Чек печатается");

        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Ввод в поле только цифры и точку
        cashCheckTextField.addEventFilter(KeyEvent.KEY_TYPED, validTextField.numeric_Validation(10));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                dialogStage.setAlwaysOnTop(true);

                if (partialPayment == false) {

                    cashCheckTextField.requestFocus();

                    if (checkBank == true) {
                        cashLabel.setText("Оплата картой");
                        cashCheckTextField.setText("0.00");
                        cashCheckTextField.setEditable(false);
                        shortChangeCheckTextField.setText("0.00");
                    }
                    //cashCheckTextField.setStyle("-fx-text-inner-color: red;");
                    //Включение диалоговых окон по нажатию клавиш
                    cashCheckTextField.setOnKeyPressed(
                            event -> {
                                switch (event.getCode()) {
                                    case ESCAPE:
                                        dialogStage.close();
                                        break;
                                    case ENTER:
                                        //isOkClicked();
                                        break;
                                    case F7:
                                        checkPrint = checkPrint == false;
                                        checkPrintCheck(checkPrint);
                                        break;
                                    case F5:
                                        String defaultValue = xmls.getPrinter();
                                        int i0 = defaultValue.compareTo("Без печати");
                                        int i1 = defaultValue.compareTo("ККТ (WI-FI)");
                                        int i2 = defaultValue.compareTo("ККТ (USB)");
                                        int i3 = defaultValue.compareTo("Принтер чеков");
                                        if (i0 == 0) {
//                                        kktPrintLabel.setText("через ККТ");
//                                        checkKKT = true;
                                        }
                                        if (i1 == 0) {
                                            kktPrintLabel.setText("б/ч");
                                            checkKKT = false;

                                        }
                                        if (i2 == 0) {
                                            kktPrintLabel.setText("б/ч");
                                            checkKKT = false;

                                        }
                                        if (i3 == 0) {
//                                        kktPrintLabel.setText("через ККТ");
//                                        checkKKT = true;

                                        }
                                        break;
                                }
                            });

                } else {
                    nalText.setVisible(true);
                    bnalText.setVisible(true);
                    partPayPane.setVisible(true);
                    fullPayPane.setVisible(false);
                    cashPartCheckTextField.requestFocus();
                }
            }
        });
    }
}
