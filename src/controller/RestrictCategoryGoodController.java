/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ml.model.CategoryGoods;
import ml.query.categoryGood.UpdateCategoryGood;

/**
 * Ограничения категории товаров
 *
 * @author Dave
 */
public class RestrictCategoryGoodController implements Initializable {

    @FXML
    private Text nameCategoryText;
    @FXML
    private Text noteCategoryText;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ComboBox<String> hourStartComboBox;
    @FXML
    private ComboBox<String> minuteStartComboBox;
    @FXML
    private ComboBox<String> hourFinishComboBox;
    @FXML
    private ComboBox<String> minuteFinishComboBox;
    @FXML
    private CheckBox printCheckKKTCheckBox;

    private ObservableList<String> optionsHourStart = FXCollections.observableArrayList();
    private ObservableList<String> optionsMinuteStart = FXCollections.observableArrayList();
    private ObservableList<String> optionsHourFinish = FXCollections.observableArrayList();
    private ObservableList<String> optionsMinuteFinish = FXCollections.observableArrayList();
    private CategoryGoods category = new CategoryGoods();
    private Stage dialogStage;
    private boolean okClicked = false;

    public void setCategoryGoods(CategoryGoods category) {
        this.category = category;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                nameCategoryText.setText(category.getName());
                noteCategoryText.setText(category.getNote());

                optionsHourStart.add("00");
                optionsHourStart.add("01");
                optionsHourStart.add("02");
                optionsHourStart.add("03");
                optionsHourStart.add("04");
                optionsHourStart.add("05");
                optionsHourStart.add("06");
                optionsHourStart.add("07");
                optionsHourStart.add("08");
                optionsHourStart.add("09");
                optionsHourStart.add("10");
                optionsHourStart.add("11");
                optionsHourStart.add("12");
                optionsHourStart.add("13");
                optionsHourStart.add("14");
                optionsHourStart.add("15");
                optionsHourStart.add("16");
                optionsHourStart.add("17");
                optionsHourStart.add("18");
                optionsHourStart.add("19");
                optionsHourStart.add("20");
                optionsHourStart.add("21");
                optionsHourStart.add("22");
                optionsHourStart.add("23");

                optionsMinuteStart.add("00");
                optionsMinuteStart.add("15");
                optionsMinuteStart.add("30");
                optionsMinuteStart.add("45");

                hourStartComboBox.setItems(optionsHourStart);
                minuteStartComboBox.setItems(optionsMinuteStart);

                optionsHourFinish.add("00");
                optionsHourFinish.add("01");
                optionsHourFinish.add("02");
                optionsHourFinish.add("03");
                optionsHourFinish.add("04");
                optionsHourFinish.add("05");
                optionsHourFinish.add("06");
                optionsHourFinish.add("07");
                optionsHourFinish.add("08");
                optionsHourFinish.add("09");
                optionsHourFinish.add("10");
                optionsHourFinish.add("11");
                optionsHourFinish.add("12");
                optionsHourFinish.add("13");
                optionsHourFinish.add("14");
                optionsHourFinish.add("15");
                optionsHourFinish.add("16");
                optionsHourFinish.add("17");
                optionsHourFinish.add("18");
                optionsHourFinish.add("19");
                optionsHourFinish.add("20");
                optionsHourFinish.add("21");
                optionsHourFinish.add("22");
                optionsHourFinish.add("23");

                optionsMinuteFinish.add("00");
                optionsMinuteFinish.add("15");
                optionsMinuteFinish.add("30");
                optionsMinuteFinish.add("45");

                hourFinishComboBox.setItems(optionsHourFinish);
                minuteFinishComboBox.setItems(optionsMinuteFinish);

                getTimeInComboBox();
                getPrintKKT();

            }

        });
    }

    @FXML
    private void getOk(ActionEvent event) {
        UpdateCategoryGood update = new UpdateCategoryGood();
        String str = category.getNote();
        String catNote = "";
        int indexStartTime = str.indexOf("/1: ");
        int indexFinishTime = str.indexOf(" :1|");
        int indexStartPrintKKT = str.indexOf("/2: ");
        int indexFinishPrintKKT = str.indexOf(" :2|");
        if (indexStartTime == -1) {
            System.out.println("Вывод: " + category.getNote());
        } else {
            System.out.println("Вывод_1: " + str.substring(0, indexStartTime));
            catNote = str.substring(0, indexStartTime);
            System.out.println("Вывод_1: " + str.substring(indexStartTime, indexFinishTime + 4));
        }

        if (indexStartPrintKKT == -1) {
//                    System.out.println("Вывод: " + category.getNote());
        } else {
            System.out.println("Вывод_2: " + str.substring(0, indexStartPrintKKT));
            System.out.println("Вывод_2: " + str.substring(indexStartPrintKKT, indexFinishPrintKKT + 4));
        }

        String startTime = "/1: ";
        String finishTime = " ";
        String hmStart = hourStartComboBox.getValue() + ":" + minuteStartComboBox.getValue();
        String hmFinish = hourFinishComboBox.getValue() + ":" + minuteFinishComboBox.getValue();
        String startKKT = "/2: ";
        category.setNote(catNote + " " + startTime + hmStart + " " + finishTime + hmFinish + " :1| " + startKKT + printCheckKKTCheckBox.isSelected() + " :2| ");

        update.update(category);

        okClicked = true;
        dialogStage.close();

    }

    @FXML
    private void getCancel(ActionEvent event) {
        dialogStage.close();

    }

    /**
     * Вывод в во все ComboBox время(часы и минуты)
     */
    private void getTimeInComboBox() {

        String str = category.getNote();
        String timeStart = "";
        String timeFinish = "";

        String hSt = "";
        String mSt = "";

        String hFt = "";
        String mFt = "";
        int indexStartTime = str.indexOf("/1: ");
        int indexFinishTime = str.indexOf(" :1|");

        if (indexStartTime == -1) {
            System.out.println("Вывод: " + category.getNote());
        } else {
            System.out.println("Вывод_1: " + str.substring(0, indexStartTime));
            System.out.println("Вывод_1: " + str.substring(indexStartTime, indexFinishTime + 4));
            timeStart = str.substring(indexStartTime, indexFinishTime + 4);
            timeFinish = str.substring(indexStartTime, indexFinishTime + 4);

        }

        if (!"".equals(timeStart)) {
            System.out.println("Вывод_часа_старт: " + timeStart.substring(4, 6));
            System.out.println("Вывод_минуты_старт: " + timeStart.substring(7, 10));
            hSt = timeStart.substring(4, 6);
            mSt = timeStart.substring(7, 9);
        }

        if (!"".equals(timeFinish)) {
            System.out.println("Вывод_часа_конец: " + timeFinish.substring(11, 13));
            System.out.println("Вывод_минуты_конец: " + timeFinish.substring(14, 16));
            hFt = timeFinish.substring(11, 13);
            mFt = timeFinish.substring(14, 16);
        }

        if (null != hSt) {
            switch (hSt) {
                case "00":
                    hourStartComboBox.getSelectionModel().select(0);
                    break;
                case "01":
                    hourStartComboBox.getSelectionModel().select(1);
                    break;
                case "02":
                    hourStartComboBox.getSelectionModel().select(2);
                    break;
                case "03":
                    hourStartComboBox.getSelectionModel().select(3);
                    break;
                case "04":
                    hourStartComboBox.getSelectionModel().select(4);
                    break;
                case "05":
                    hourStartComboBox.getSelectionModel().select(5);
                    break;
                case "06":
                    hourStartComboBox.getSelectionModel().select(6);
                    break;
                case "07":
                    hourStartComboBox.getSelectionModel().select(7);
                    break;
                case "08":
                    hourStartComboBox.getSelectionModel().select(8);
                    break;
                case "09":
                    hourStartComboBox.getSelectionModel().select(9);
                    break;
                case "10":
                    hourStartComboBox.getSelectionModel().select(10);
                    break;
                case "11":
                    hourStartComboBox.getSelectionModel().select(11);
                    break;
                case "12":
                    hourStartComboBox.getSelectionModel().select(12);
                    break;
                case "13":
                    hourStartComboBox.getSelectionModel().select(13);
                    break;
                case "14":
                    hourStartComboBox.getSelectionModel().select(14);
                    break;
                case "15":
                    hourStartComboBox.getSelectionModel().select(15);
                    break;
                case "16":
                    hourStartComboBox.getSelectionModel().select(16);
                    break;
                case "17":
                    hourStartComboBox.getSelectionModel().select(17);
                    break;
                case "18":
                    hourStartComboBox.getSelectionModel().select(18);
                    break;
                case "19":
                    hourStartComboBox.getSelectionModel().select(19);
                    break;
                case "20":
                    hourStartComboBox.getSelectionModel().select(20);
                    break;
                case "21":
                    hourStartComboBox.getSelectionModel().select(21);
                    break;
                case "22":
                    hourStartComboBox.getSelectionModel().select(22);
                    break;
                case "23":
                    hourStartComboBox.getSelectionModel().select(23);
                    break;
                default:
                    hourStartComboBox.getSelectionModel().select(0);
                    break;
            }
        }

        if (null != mSt) {
            switch (mSt) {
                case "00":
                    minuteStartComboBox.getSelectionModel().select(0);
                    break;
                case "15":
                    minuteStartComboBox.getSelectionModel().select(1);
                    break;
                case "30":
                    minuteStartComboBox.getSelectionModel().select(2);
                    break;
                case "45":
                    minuteStartComboBox.getSelectionModel().select(3);
                    break;
                default:
                    minuteStartComboBox.getSelectionModel().select(0);
                    break;
            }
        }

        if (null != hFt) {
            switch (hFt) {
                case "00":
                    hourFinishComboBox.getSelectionModel().select(0);
                    break;
                case "01":
                    hourFinishComboBox.getSelectionModel().select(1);
                    break;
                case "02":
                    hourFinishComboBox.getSelectionModel().select(2);
                    break;
                case "03":
                    hourFinishComboBox.getSelectionModel().select(3);
                    break;
                case "04":
                    hourFinishComboBox.getSelectionModel().select(4);
                    break;
                case "05":
                    hourFinishComboBox.getSelectionModel().select(5);
                    break;
                case "06":
                    hourFinishComboBox.getSelectionModel().select(6);
                    break;
                case "07":
                    hourFinishComboBox.getSelectionModel().select(7);
                    break;
                case "08":
                    hourFinishComboBox.getSelectionModel().select(8);
                    break;
                case "09":
                    hourFinishComboBox.getSelectionModel().select(9);
                    break;
                case "10":
                    hourFinishComboBox.getSelectionModel().select(10);
                    break;
                case "11":
                    hourFinishComboBox.getSelectionModel().select(11);
                    break;
                case "12":
                    hourFinishComboBox.getSelectionModel().select(12);
                    break;
                case "13":
                    hourFinishComboBox.getSelectionModel().select(13);
                    break;
                case "14":
                    hourFinishComboBox.getSelectionModel().select(14);
                    break;
                case "15":
                    hourFinishComboBox.getSelectionModel().select(15);
                    break;
                case "16":
                    hourFinishComboBox.getSelectionModel().select(16);
                    break;
                case "17":
                    hourFinishComboBox.getSelectionModel().select(17);
                    break;
                case "18":
                    hourFinishComboBox.getSelectionModel().select(18);
                    break;
                case "19":
                    hourFinishComboBox.getSelectionModel().select(19);
                    break;
                case "20":
                    hourFinishComboBox.getSelectionModel().select(20);
                    break;
                case "21":
                    hourFinishComboBox.getSelectionModel().select(21);
                    break;
                case "22":
                    hourFinishComboBox.getSelectionModel().select(22);
                    break;
                case "23":
                    hourFinishComboBox.getSelectionModel().select(23);
                    break;
                default:
                    hourFinishComboBox.getSelectionModel().select(0);
                    break;
            }
        }

        if (null != mFt) {
            switch (mFt) {
                case "00":
                    minuteFinishComboBox.getSelectionModel().select(0);
                    break;
                case "15":
                    minuteFinishComboBox.getSelectionModel().select(1);
                    break;
                case "30":
                    minuteFinishComboBox.getSelectionModel().select(2);
                    break;
                case "45":
                    minuteFinishComboBox.getSelectionModel().select(3);
                    break;
                default:
                    minuteFinishComboBox.getSelectionModel().select(0);
                    break;
            }
        }
    }

    /**
     * Вывод печатать чек ККТ или нет
     */
    private void getPrintKKT() {
        String str = category.getNote();
        String kkt = "";
        String checkKKTPrint = "0";
        int indexStartPrintKKT = str.indexOf("/2: ");
        int indexFinishPrintKKT = str.indexOf(" :2|");

        if (indexStartPrintKKT == -1) {
//                    System.out.println("Вывод: " + category.getNote());
        } else {
            System.out.println("Вывод_2: " + str.substring(0, indexStartPrintKKT));
            System.out.println("Вывод_2: " + str.substring(indexStartPrintKKT, indexFinishPrintKKT + 4));
            kkt = str.substring(indexStartPrintKKT, indexFinishPrintKKT + 4);
        }

        if (!"".equals(kkt)) {
            System.out.println("Вывод_печати_чека: " + kkt.substring(4, 6));
            checkKKTPrint = kkt.substring(4, 6);
        } else {
        }

        if (null != checkKKTPrint) {
            switch (checkKKTPrint) {
                case "tr":
                    printCheckKKTCheckBox.setSelected(true);
                    break;
                case "fa":
                    printCheckKKTCheckBox.setSelected(false);
                    break;
                default:
                    printCheckKKTCheckBox.setSelected(false);
                    break;
            }
        }

    }

}
