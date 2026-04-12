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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ml.Ml_FX;
import ml.dialog.DialogAlert;
import ml.dialog.DialogTextInput;
import ml.dialog.DialogTextInputDontHidden;
import ml.model.ArrivalDish;
import ml.model.ArrivalDishList;
import ml.model.Barcode;
import ml.model.CaseRecord;
import ml.model.CategoryGoods;
import ml.model.ChangeOptPriceGoods;
import ml.model.Dish;
import ml.model.Goods;
import ml.model.TechMap;
import ml.model.UserSwing;
import ml.query.arrivaldish.AddArrivalDish;
import ml.query.arrivaldish.AddArrivalDishList;
import ml.query.arrivaldish.LastArrivalDish;
import ml.query.barcode.AddBarcode;
import ml.query.barcode.LastBarcode;
import ml.query.caserecord.AddCaseRecord;
import ml.query.categoryGood.CategoryGoodsByName;
import ml.query.dish.AllDishList;
import ml.query.dish.DishByCode;
import ml.query.dish.NewDish;
import ml.query.dish.UpdateDish;
import ml.query.goods.GoodById;
import ml.query.goods.UpdateGood;
import ml.query.techmap.TechMapByDish;
import ml.query.techmap.TechMapListByGood;
import ml.query.techmap.UpdateTechMap;
import ml.query.techmap.UpdateTechMapByGood;
import ml.query.user.IdUserByName;
import ml.table.ArrivalDishTable;
import ml.util.PlayAudio;
import ml.util.RegexpNameGoods;
import ml.util.SearchBarcode;
import org.apache.hc.core5.http.ParseException;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class ArrivalDishController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField codeTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TableView<ArrivalDishTable> tableDish;
    @FXML
    private TableColumn<ArrivalDishTable, String> codeColumn;
    @FXML
    private TableColumn<ArrivalDishTable, String> nameColumn;
    @FXML
    private TableColumn<ArrivalDishTable, BigDecimal> weightColumn;
    @FXML
    private TableColumn<ArrivalDishTable, BigDecimal> priceOptColumn;
    @FXML
    private TextField weightTextField;
    @FXML
    private TextField priceOptTextField;
    @FXML
    private Button okButton;
    @FXML
    private TextField numderInvoiceTextField;
    @FXML
    private Text sumInvoice;

    private ObservableList<ArrivalDishTable> arrivalDishData = FXCollections.observableArrayList();
    private List<Dish> dishList;
    private List<Dish> dishListForChange = new ArrayList<>();
    private List<ChangeOptPriceGoods> goodList = new ArrayList<>();
    private Boolean checkChangeDish = false;
    private AllDishList allDishList = new AllDishList();
    private Stage primaryStage;
    private DialogAlert dialogAlert = new DialogAlert();
    private DialogTextInput dialogTextInput = new DialogTextInput();
    private int selectRow = -1;
    private BigDecimal sumInv = new BigDecimal("0.00");

    /**
     * Поиск по коду
     *
     * @param event
     */
    @FXML
    private void getCodeAction(ActionEvent event) {
        String code = codeTextField.getText();

        weightTextField.requestFocus();
        codeTextField.setText("");
        getDishCode(code);
        sumInvoice();
    }

    @FXML
    private void getNameAction(ActionEvent event) {
        String name = nameTextField.getText();

        nameTextField.setText("");
        if (!name.equals("")) {
            getDishName(name);
            sumInvoice();
        }
    }

    @FXML
    private void getWeightAction(ActionEvent event) {
        String s = weightTextField.getText();

        //Замена , на .
        String gr = s.replace(',', '.');

        int i = tableDish.getItems().size();   // кол-во строк в таблице
        //если кол-во строк не 0
        if (i != 0) {
            int row = i - 1;                        // последняя строка
            BigDecimal weight = decimal("###.###", Double.parseDouble(gr));
            //Если выбрана строка то изменить в ней значение кол-ва
            if (selectRow >= 0) {
                //Вставка данных в выбранную строку в ячейку "кол-во"
                arrivalDishData.get(selectRow).setWeight(weight);
                //arrivalData.get(row).setNewPrice(allowance.multiply(invoice));s
                tableDish.getItems().set(selectRow, arrivalDishData.get(selectRow));

                tableDish.getSelectionModel().clearSelection();
                selectRow = -1;
            } else {
                //Вставка данных в последнюю ячейку "кол-во" и "сумма строки" таблицы
                arrivalDishData.get(row).setWeight(weight);
                //arrivalData.get(row).setNewPrice(allowance.multiply(invoice));s
                tableDish.getItems().set(row, arrivalDishData.get(row));
            }

            tableDish.getSelectionModel().clearSelection();

        }
        weightTextField.setText("");
        codeTextField.requestFocus();
        sumInvoice();
    }

    /**
     * Значение номера выбранной строки
     */
    @FXML
    private void getRow() {

        List<TablePosition> positions = tableDish.getSelectionModel().getSelectedCells();
        if (!positions.isEmpty()) {

            int i = tableDish.getItems().size();   // кол-во строк в таблице
            if (i > 0) {

                TablePosition pos = tableDish.getSelectionModel().getSelectedCells().get(0);
                selectRow = pos.getRow();

//                            tableArrival.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    private void okAction(ActionEvent event) {
        Dish dish = new Dish();
        ArrivalDish arrivalDish = new ArrivalDish();
        AddArrivalDish addArrivalDish = new AddArrivalDish();
        AddArrivalDishList addArrivalDishList = new AddArrivalDishList();
        CaseRecord caseRecord = new CaseRecord();
        AddCaseRecord addCaseRecord = new AddCaseRecord();
        UpdateDish updateDish = new UpdateDish();
        DishByCode dishByCode = new DishByCode();
        LastArrivalDish lastArrivalDish = new LastArrivalDish();
        IdUserByName idUserByName = new IdUserByName();
        Authentication authentication = SecurityContextHolder.getContext().
                getAuthentication();
        UserSwing userSwing = new UserSwing();
        String login = authentication.getName();
        ////////////
        idUserByName.setLoginUser(login);   //метод для idUser по логину
        userSwing = idUserByName.displayResult(); //Возвращает пользователя

        Date date = new Date();

//        arrivalDish.s
        int countRow = tableDish.getItems().size();   // кол-во строк в таблице

        arrivalDish.setNumberWaybill(numderInvoiceTextField.getText());
        arrivalDish.setSumInvoice(new BigDecimal(sumInvoice.getText()));
        arrivalDish.setDate(date);
//        userSwing.getArrivalsDish().add(arrivalDish);
        arrivalDish.setUserSwing(userSwing);

        addArrivalDish.add(arrivalDish);

        lastArrivalDish.get();

        for (int i = 0; i < countRow; i++) {

            ArrivalDishList arrivalDishList = new ArrivalDishList();

//Товар по коду из таблицы
            dishByCode.setCode(codeColumn.getCellData(i));
            dish = dishByCode.displayResult();

            arrivalDishList.setDish(dish);
            dish.getArrivalDishLists().add(arrivalDishList);
            arrivalDishList.setAmount(weightColumn.getCellData(i));
//            arrivalListArrayList.add(arrivalList);
            arrivalDishList.setArrivalDish(lastArrivalDish.displayResult());
            arrivalDishList.setPriceOpt(priceOptColumn.getCellData(i));
            arrivalDish.getArrivalDishLists().add(arrivalDishList);
            addArrivalDishList.add(arrivalDishList);

            dish = dishByCode.displayResult();

            BigDecimal dishPriceOptForCheck = dish.getPriceOpt();

            dish.setName(nameColumn.getCellData(i));
            //goods.getTechmap().add(techMap);
            dish.setPriceOpt(priceOptColumn.getCellData(i));
            dish.setWeight(weightColumn.getCellData(i).add(dish.getWeight()));

            updateDish.update(dish);

            //Проверка различия цен
            checkChangePriceOpt(dish, priceOptColumn.getCellData(i), dishPriceOptForCheck);

        }

        arrivalDish.getCaseRecord().add(arrivalDish);
        caseRecord.setArrivalDish(arrivalDish);
        caseRecord.setDate(date);

        caseRecord.setUserSwing(userSwing);
        addCaseRecord.add(caseRecord);
        closeWindow();

        if (checkChangeDish == true) {
            for (int i = 0; i < dishListForChange.size(); i++) {
                changePriceOpt(dishListForChange.get(i));
            }
            changePriceDialog(goodList);
        }

        dialogAlert.alert("Внимание!", "Сообщение:", "Продукт создан или обновлен");
    }

    /**
     * Проверка на совпадение оптовой цены
     *
     * @param dish
     * @param arrivalDish
     * @param priceOpt
     */
    private void checkChangePriceOpt(Dish dish, BigDecimal priceOpt, BigDecimal dishPriceOptForCheck) {
        int res;
        res = dishPriceOptForCheck.compareTo(priceOpt); // compare bg1 with bg2
        switch (res) {
            case 0:
                System.out.println("Цена совпадает");
                break;
            case 1:
                dishListForChange.add(dish);
                checkChangeDish = true;
                System.out.println("Цена не совпадает");
                break;
            case -1:

                dishListForChange.add(dish);
                checkChangeDish = true;

                System.out.println("Цена не совпадает");
                break;
            default:
                break;
        }

    }

    /**
     * Замена оптовой цены
     *
     * @param dish
     * @param priceOpt
     */
    private void changePriceOpt(Dish dish) {
        Dish d = new Dish();
        TechMapByDish tmbIdDish = new TechMapByDish();
        tmbIdDish.setDish(dish);
        List<TechMap> tm = tmbIdDish.displayResult();
        UpdateTechMapByGood upTMbyGood = new UpdateTechMapByGood();
        UpdateTechMap updateTechMap = new UpdateTechMap();
        UpdateDish upDish = new UpdateDish();

        for (int i = 0; i < tm.size(); i++) {
            ChangeOptPriceGoods changeOptPriceGoods = new ChangeOptPriceGoods();
            BigDecimal sumOpt = new BigDecimal(0);
            UpdateGood upGood = new UpdateGood();
            GoodById gbi = new GoodById();
            TechMapListByGood techMapByG = new TechMapListByGood();

            gbi.setId(tm.get(i).getGoods().getIdGoods());

            Goods goods = gbi.displayResult();

            changeOptPriceGoods.setCode(goods.getCode());
            changeOptPriceGoods.setName(goods.getName());
            changeOptPriceGoods.setOldPriceOpt(goods.getPriceOpt());
            changeOptPriceGoods.setPrice(goods.getPrice());

            List<TechMap> t = techMapByG.listTechMap(goods.getIdGoods());
            for (int j = 0; j < t.size(); j++) {
                sumOpt = sumOpt.add(t.get(j).getGross().multiply(t.get(j).getDish().getPriceOpt()));
            }

            changeOptPriceGoods.setNewPriceOpt(sumOpt);
            goods.setPriceOpt(sumOpt);
            upGood.update(goods);
            goodList.add(changeOptPriceGoods);
            System.out.println("");
        }

    }

    /**
     * Диалог об изменении оптовой цены
     */
    private void changePriceDialog(List<ChangeOptPriceGoods> goodList) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Ml_FX.class.getResource("/ml/view/ChangePrice.fxml"));

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Изменение оптовой цены");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            BorderPane page = (BorderPane) loader.load();
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ChangePriceController controller = loader.getController();

//            controller.setSumText(convertSum, convertSumWithDiscount);
            //controller.setSumText("1000", "10");
            controller.setDialogStage(dialogStage, goodList);

//            controller.setAddCheck(tableCheck, checkArrayList, checkListArrayList, discount, checkNewPrice, newPrice);
//            controller.setTechMap(checkTechMap);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getPriceOptAction(ActionEvent event) {
        String s = priceOptTextField.getText();

        //Замена , на .
        String gr = s.replace(',', '.');

        int i = tableDish.getItems().size();   // кол-во строк в таблице
        //если кол-во строк не 0
        if (i != 0) {
            int row = i - 1;                        // последняя строка
            BigDecimal priceOpt = decimal("###.###", Double.parseDouble(gr));
            //Если выбрана строка то изменить в ней значение кол-ва
            if (selectRow >= 0) {
                //Вставка данных в выбранную строку в ячейку "кол-во"
                arrivalDishData.get(selectRow).setPriceOpt(priceOpt);
                //arrivalData.get(row).setNewPrice(allowance.multiply(invoice));s
                tableDish.getItems().set(selectRow, arrivalDishData.get(selectRow));

                tableDish.getSelectionModel().clearSelection();
                selectRow = -1;
            } else {
                //Вставка данных в последнюю ячейку "кол-во" и "сумма строки" таблицы
                arrivalDishData.get(row).setPriceOpt(priceOpt);
                //arrivalData.get(row).setNewPrice(allowance.multiply(invoice));s
                tableDish.getItems().set(row, arrivalDishData.get(row));
            }

            tableDish.getSelectionModel().clearSelection();

        }
        priceOptTextField.setText("");
        codeTextField.requestFocus();
        sumInvoice();

    }

    /**
     * Закрывает окно
     */
    private void closeWindow() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
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
     * Вовзращает данные о товаре по коду
     *
     * @param code
     */
    private void getDishCode(String code) {
        boolean checkDish = false;
        Dish dish = null;
        PlayAudio pa = new PlayAudio();

        String codeFromTable = "";
        String codeFromTable1 = "";

        int numberRow = 0;
        boolean b = false;
        boolean bb = false;

        int r = tableDish.getItems().size();   // кол-во строк в таблице
        for (int i = 0; i < r; i++) {
            codeFromTable1 = (String) arrivalDishData.get(i).getCode();
            if (code.equals(codeFromTable1)) {
                bb = true;
            }
        }

        if (dishList.isEmpty()) {
            checkDish = bb == true;
        } else {

            for (Dish gg1 : dishList) {
                if ((gg1.getCode().equals(code)) || (bb == true)) {
                    dish = gg1;
                    checkDish = true;
                    break;
                } else {
                    checkDish = false;
                }
            }
        }
        if (checkDish == false) {
            NewDish newDish = new NewDish();
            SearchBarcode searchBarcode = new SearchBarcode();
            String answer = "";

            try {
                answer = searchBarcode.setBarcode(code);
            } catch (ParseException ex) {
                Logger.getLogger(ArrivalController.class.getName()).log(Level.SEVERE, null, ex);
            }

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
                    dish = new Dish();
                    dish.setName(newName);
                    dish.setCode(code);
                    dish.setPriceOpt(new BigDecimal(0));
                    dish.setWeight(new BigDecimal(0));

                    newDish.add(dish);
                }
            } else {
                dialogAlert.alert("Внимание!", "Сообщение:", "Название товара должно начинаться с буквы!!!");
                codeTextField.requestFocus();
            }

            for (Dish gg1 : dishList) {
                if (gg1.getCode().equals(code)) {
                    dish = gg1;
                }
            }

        }

        //Сравнивает введеный код со списком кодов в таблице
        /*if (i > 0) {
        TablePosition pos = tableArrival.getSelectionModel().getSelectedCells().get(0);
        selectRow = pos.getRow();
        }*/
        int rowCount = tableDish.getItems().size();   // кол-во строк в таблице
        for (int i = 0; i < rowCount; i++) {
            codeFromTable = (String) arrivalDishData.get(i).getCode();
            if (code.equals(codeFromTable)) {
                b = true;
                numberRow = i;
            }
        }

        if (dish != null) {
            displayResult(dish);
        }

    }

    /**
     * Вовзращает данные о продукта по наименованию
     *
     * @param code
     */
    private void getDishName(String name) {
        Dish dish = null;
        String nameFromTable = "";
        int numberRow = 0;
        boolean b = false;
        for (Dish gg1 : dishList) {
            if (gg1.getName().equals(name)) {
                dish = gg1;
            }
        }
        //Сравнивает введеное наименование со списком наименований в таблице
        int rowCount = tableDish.getItems().size();   // кол-во строк в таблице
        for (int i = 0; i < rowCount; i++) {
            nameFromTable = (String) arrivalDishData.get(i).getName();
            if (name.equals(nameFromTable)) {
                b = true;
                numberRow = i;
            }
        }

        displayResult(dish);

//        //считает сумму прихода
//        sumCheckWithAllowance();
//        //считает сумму накладной
//        sumCheck();
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayResult(Dish dish) {
        BigDecimal priceOpt;

        nameColumn.setCellValueFactory(new PropertyValueFactory<ArrivalDishTable, String>("name"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<ArrivalDishTable, String>("code"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<ArrivalDishTable, BigDecimal>("weight"));
        priceOptColumn.setCellValueFactory(new PropertyValueFactory<ArrivalDishTable, BigDecimal>("priceOpt"));

        ArrivalDishTable arrivalTable = new ArrivalDishTable();

        if (dish != null) {
            for (int i = 0; i < 1; i++) {
                //BigDecimal price = goods.getPrice();
                //BigDecimal amount = decimal("###.###", Double.parseDouble(weight));
                if (dish.getPriceOpt() == null) {
                    priceOpt = BigDecimal.ZERO;
                } else {
                    priceOpt = dish.getPriceOpt();
                }
                arrivalTable.setCode(dish.getCode());
                arrivalTable.setName(dish.getName());
                arrivalTable.setPriceOpt(dish.getPriceOpt());
                arrivalTable.setWeight(BigDecimal.ZERO);

                // заполняем таблицу данными
                if (!"".equals(dish.getName())) {
                    arrivalDishData.add(arrivalTable);
                }

            }

            tableDish.setItems(arrivalDishData);
        }
        //Переход курсора на кол-во товара
        //amountGood.requestFocus();
        //amountGood.setText("1");
        //amountGood.selectAll(); //Выделяет текст в поле
    }

    /**
     * Открывает окно для создания нового товара
     */
    private void getDialogNewGood() {
        CategoryGoodsByName categoryGoodsByName = new CategoryGoodsByName();
        CategoryGoods cg = new CategoryGoods();
        NewDish newDish = new NewDish();
        Date date = new Date();
        RegexpNameGoods regexpNameGoods = new RegexpNameGoods();
        Dish dish = null;
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

                dish = new Dish();
                dish.setName(newName);
                dish.setCode(newBarcode.toString());
                dish.setPriceOpt(new BigDecimal(0));
                dish.setWeight(new BigDecimal(0));

                //Если поле нового товара не пустое, тогда создается новая запись в БД и новый штрих-код
                if (!(newName == null) && !("".equals(newName))) {
                    DishByCode dishByCode = new DishByCode();
                    Dish dishNewBarcode = new Dish();

                    //добавляет новый товар
                    newDish.add(dish);

                    dishByCode.setCode(newBarcode.toString());
                    dishNewBarcode = dishByCode.displayResult();
                    barcode.setDish(dishNewBarcode);
                    addBarcode.add(barcode);
                }
                codeTextField.requestFocus();

                //String input = Dialogs.showInputDialog(stage, "Please enter your name:", "Input Dialog", "title");
                for (Dish gg1 : dishList) {
                    if (gg1.getCode().equals(newBarcode.toString())) {
                        dish = gg1;
                    }
                }

                if (dish != null) {
                    displayResult(dish);
                }
            }
        } else {
            dialogAlert.alert("Внимание!", "Сообщение:", "Название товара должно начинаться с буквы!!!");
            codeTextField.requestFocus();
            codeTextField.selectAll();
        }

    }

    /**
     * Список товара по названию в поле nameGood
     *
     * Список товара по коду в поле codeGood
     */
    private void listGoodsByNameAndCode(List<Dish> someList) {

        //Список товара по названию в поле nameGood
        String[] sName = new String[someList.size()];
        for (int i = 0; i < someList.size(); i++) {
            sName[i] = (String) someList.get(i).getName();
        }
        Arrays.sort(sName);
        TextFields.bindAutoCompletion(nameTextField, sName);

        //Список товара по коду в поле codeGood
        String[] sCode = new String[someList.size()];
        for (int i = 0; i < someList.size(); i++) {
            sCode[i] = (String) someList.get(i).getCode();
        }
        Arrays.sort(sCode);
        TextFields.bindAutoCompletion(codeTextField, sCode);

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Список всего товара
        dishList = allDishList.listDish();

        //Выводит список товара по названию и коду
        listGoodsByNameAndCode(dishList);

        //По нажатию клавиш
        anchorPane.setOnKeyPressed(
                event -> {
                    switch (event.getCode()) {

                        case F7:
//                                    if (event.isControlDown()) {
                            getDialogNewGood();
                            System.out.println("КНОПКА F7 НАЖАТА!!!");
//                                    }
                            break;
                        case DELETE:
                            //Удаление строки
                            TablePosition pos = tableDish.getSelectionModel().getSelectedCells().get(0);
                            int row = pos.getRow();
                            arrivalDishData.remove(row);
                            codeTextField.requestFocus();
//                                    //считает сумму прихода
//                                    sumCheckWithAllowance();
//                                    //считает сумму накладной
//                                    sumCheck();
                            break;
                    }
                });

        //При нажатии ALT_GRAPH в поле Код товара - переход на кол-во товара
        codeTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case ALT_GRAPH:
                        weightTextField.requestFocus();
                        weightTextField.selectAll();
                        break;
                    case F7:
//                                    if (event.isControlDown()) {
                        getDialogNewGood();
                        System.out.println("КНОПКА F7 НАЖАТА!!!");
//                                    }
                        break;
                }
            }
        });

        //При нажатии F7 в поле Название товара - диалог новый товар
        nameTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case ALT_GRAPH:
                        weightTextField.requestFocus();
                        weightTextField.selectAll();
                        break;
                    case F7:
//                                    if (event.isControlDown()) {
                        getDialogNewGood();
                        System.out.println("КНОПКА F7 НАЖАТА!!!");
//                                    }
                        break;
                }
            }
        });

    }

    /**
     * Суммирует накладную
     */
    private void sumInvoice() {
        sumInv = sumInv.add(sumInv);

        BigDecimal sum = new BigDecimal(0);
        BigDecimal price = new BigDecimal(0.00);
        BigDecimal amount = new BigDecimal(0.00);

        for (int i = 0; i < tableDish.getItems().size(); i++) {
            price = new BigDecimal(arrivalDishData.get(i).getPriceOpt().toString());
            amount = new BigDecimal(arrivalDishData.get(i).getWeight().toString());

            sum = decimal("###.###", Double.parseDouble(price.multiply(amount).add(sum).toString()));
//amount = decimal("###.###", Double.parseDouble(am));
        }

        //Выводит на экран сумму прихода
        sumInvoice.setText(sum.toString());
    }

}
