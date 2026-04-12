/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ml.model.Check;
import ml.model.CheckList;
import ml.model.Discount;
import ml.modelLicense.Comp;
import ml.print.Print;
import ml.qr.CreateQRImage;
import ml.telegram.BotClientCommand;
import ml.xml.XMLPrinter;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class PrintCheckController implements Initializable {

    @FXML
    private GridPane checkPane;

    private Stage dialogStage;
    private boolean okClicked = false;
    private List<Check> checkArrayList = new ArrayList<Check>();
    private List<CheckList> checkListArrayList = new ArrayList<CheckList>();
    private Print p = new Print();
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final Date date = new Date();
    private final String nowDate = dateFormat.format(date);
    private XMLPrinter xmlPrinter = new XMLPrinter();
    private Comp comp;
    private BotClientCommand bot = new BotClientCommand();

    public boolean displayOkClicked() {

        return okClicked;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /*
    public void setSumText(String convertSum, String convertSumWithDiscount) {
    // set text from another class
    System.out.println("Получаем мы: " + convertSum);
    sumCheckTextField.setText(convertSum);
    discountCheckTextField.setText(convertSumWithDiscount);
    }*/
    public void setPrintCheck(Comp comp, List<CheckList> checkListArrayList, List<Check> checkArrayList,
            Discount discount, BigDecimal sumChecktText, BigDecimal discountText,
            BigDecimal cash, BigDecimal shortChange, String note) {
        // set text from another class
        this.comp = comp;

        Label headerLabel = new Label(xmlPrinter.getHeaderPrinter());
        Label adressLabel = new Label(xmlPrinter.getAdressPrinter());
        Label infoLabel = new Label(xmlPrinter.getInfoPrinter());
        Label underline1Label = new Label("________________");
        Label underline2Label = new Label("________________");
        Label underline3Label = new Label("________________");

        Label footerLabel = new Label(xmlPrinter.getFooterPrinter());
        Label numberCheckLabel = new Label();
        Label sumLabel = new Label();
        Label sumCheckLabel = new Label();
        Label discountLabel = new Label();
        Label discountCheckLabel = new Label();
        Label discountCheckTextLabel = new Label();
        Label discountAddress = new Label();
        Label discountNote = new Label();
        Label discountQR = new Label();
        Label discountNumber = new Label();
        Label discountBot = new Label();
        Label discountPhone = new Label();
        Label cashLabel = new Label();
        Label cashText = new Label();
        Label shortChangeLabel = new Label();
        Label shortChangeText = new Label();
        Label dateTextLabel = new Label();
        Label casier = new Label();
        int i = 0;

        headerLabel.setFont(new Font("Arial", 8));
        checkPane.add(headerLabel, 0, i);

        adressLabel.setFont(new Font("Arial", 8));
        checkPane.add(adressLabel, 0, ++i);

        infoLabel.setFont(new Font("Arial", 8));
        checkPane.add(infoLabel, 0, ++i);

//        casier.setText("Кассир: " + checkArrayList.get(0).getUserSwing().getName());
//        casier.setText("Кассир: " + "Исаева Г.В.");
        casier.setFont(new Font("Arial", 8));
        checkPane.add(casier, 0, ++i);

        underline1Label.setFont(new Font("Arial", 8));
        checkPane.add(underline1Label, 0, ++i);

        for (CheckList check1 : checkListArrayList) {

            Label name = new Label();
            Label aboutBuy = new Label();
            Label price = new Label();
            Label sum = new Label();

            i = ++i;

            name.setText(check1.getGoods().getName());
            name.setFont(new Font("Arial", 8));
            aboutBuy.setText(check1.getAmount().toString() + "  x  "
                    + check1.getGoods().getPrice().toString() + "  =  "
                    + check1.getAmount().multiply(check1.getGoods().getPrice()).setScale(2, RoundingMode.HALF_UP).toString());
            aboutBuy.setFont(new Font("Arial", 8));
            checkPane.setHalignment(aboutBuy, HPos.RIGHT);
            checkPane.add(name, 0, i);
            checkPane.add(aboutBuy, 0, ++i);

        }

        underline2Label.setFont(new Font("Arial", 8));
        checkPane.add(underline2Label, 0, ++i);

        //Если есть дисконт, то
        if (discount.getNumcard() != null) {
            discountLabel.setText("Скидка " + discount.getPercent() + "% : " + sumChecktText.subtract(discountText));
            discountLabel.setFont(new Font("Arial", 8));
            checkPane.add(discountLabel, 0, ++i);

            sumLabel.setText("Сумма чека: ");
            sumLabel.setFont(Font.font("Arial", 8));
            checkPane.add(sumLabel, 0, ++i);

            sumChecktText.setScale(2, RoundingMode.UP);
            sumCheckLabel.setText(sumChecktText.toString());
            sumCheckLabel.setFont(Font.font("Arial", 8));
            checkPane.setHalignment(sumCheckLabel, HPos.RIGHT);
            checkPane.add(sumCheckLabel, 0, i);

            discountCheckLabel.setText("Cумма со скидкой: ");
            discountCheckLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            //checkPane.setHalignment(discountCheckLabel, HPos.RIGHT);
            checkPane.add(discountCheckLabel, 0, ++i);

            discountText.setScale(2, RoundingMode.UP);
            discountCheckTextLabel.setText(discountText.toString());
            discountCheckTextLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            checkPane.setHalignment(discountCheckTextLabel, HPos.RIGHT);
            checkPane.add(discountCheckTextLabel, 0, i);

//            discountText.setScale(2, RoundingMode.UP);
            discountAddress.setText(discount.getNote());
            discountAddress.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            checkPane.setHalignment(discountAddress, HPos.RIGHT);
            checkPane.add(discountAddress, 0, ++i);

            discountPhone.setText(discount.getPhone());
            discountPhone.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            checkPane.setHalignment(discountPhone, HPos.RIGHT);
            checkPane.add(discountPhone, 0, ++i);

            discountNote.setText(note);
            discountNote.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            checkPane.setHalignment(discountNote, HPos.RIGHT);
            checkPane.add(discountNote, 0, ++i);

            //если у компа есть разрешение на отправку сообщений от телеграм-бота
            //создается QR-код
            if (discount.getTelegramIdChat() == null) {

                File f = new File("src/ml/resources/settings/telegramClientSettings.json");
                if (f.exists()) {
                    if (comp.getTlgrm() == true) {

                        CreateQRImage qr = new CreateQRImage();
                        String qrCodeText = "https://t.me/" + bot.getBotUsername();
                        String filePath = "src/ml/resources/qr/qrbot.png";
                        int size = 50;
                        String fileType = "png";
                        File qrFile = new File(filePath);
                        qr.createQRImage(qrFile, qrCodeText, size, fileType);
                        System.out.println("DONE");

                        Image image = new Image(new File("src/ml/resources/qr/qrbot.png").toURI().toString());

//creating ImageView for adding image
                        ImageView imageView = new ImageView();
                        imageView.setImage(image);
                        imageView.setFitWidth(70);
                        imageView.setPreserveRatio(true);
                        imageView.setSmooth(true);
                        imageView.setCache(true);

//            discountQR.setText(qr.image());
                        discountQR.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                        checkPane.setHalignment(imageView, HPos.CENTER);
                        checkPane.add(imageView, 0, ++i);
//            
                        discountBot.setText("Телеграм-Бот: @" + bot.getBotUsername());
                        discountBot.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                        checkPane.setHalignment(discountBot, HPos.CENTER);
                        checkPane.add(discountBot, 0, ++i);

                        discountNumber.setText("Ваш номер дисконта: " + discount.getNumcard());
                        discountNumber.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                        checkPane.setHalignment(discountNumber, HPos.CENTER);
                        checkPane.add(discountNumber, 0, ++i);
                    }
                }
            }
        } else {
            sumLabel.setText("Сумма чека: ");
            sumLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            checkPane.add(sumLabel, 0, ++i);

            sumCheckLabel.setText(sumChecktText.toString());
            sumCheckLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            checkPane.setHalignment(sumCheckLabel, HPos.RIGHT);
            checkPane.add(sumCheckLabel, 0, i);
        }

        cashLabel.setText("Наличными: ");
        cashLabel.setFont(new Font("Arial", 8));
        checkPane.add(cashLabel, 0, ++i);

        cash.setScale(2, RoundingMode.UP);
        cashText.setText(cash.toString());
        cashText.setFont(Font.font("Arial", 8));
        checkPane.setHalignment(cashText, HPos.RIGHT);
        checkPane.add(cashText, 0, i);

        shortChangeLabel.setText("Сдача: ");
        shortChangeLabel.setFont(new Font("Arial", 8));
        checkPane.add(shortChangeLabel, 0, ++i);

        shortChange.setScale(2, RoundingMode.UP);
        shortChangeText.setText(shortChange.toString());
        shortChangeText.setFont(Font.font("Arial", 8));
        checkPane.setHalignment(shortChangeText, HPos.RIGHT);
        checkPane.add(shortChangeText, 0, i);

        underline3Label.setFont(new Font("Arial", 8));
        checkPane.add(underline3Label, 0, ++i);

        dateTextLabel.setText(nowDate);
        dateTextLabel.setFont(Font.font("Arial", FontWeight.BOLD, 8));
        checkPane.setHalignment(dateTextLabel, HPos.RIGHT);
        checkPane.add(dateTextLabel, 0, ++i);

        numberCheckLabel.setText("Чека №: " + checkArrayList.get(0).getIdCheck());
        numberCheckLabel.setFont(new Font("Arial", 8));
        checkPane.add(numberCheckLabel, 0, ++i);

        footerLabel.setFont(new Font("Arial", 8));
        checkPane.setHalignment(footerLabel, HPos.CENTER);
        checkPane.add(footerLabel, 0, ++i);
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
                p.pageSetup(checkPane, dialogStage);

                if (p.getPrint() == true) {
                    dialogStage.close();
                }
            }
        });

    }

}
