/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import ml.dialog.DialogAlert;
import ml.model.Barcode;
import ml.model.CategoryGoods;
import ml.model.Dish;
import ml.model.Goods;
import ml.model.TechMap;
import ml.query.barcode.AddBarcode;
import ml.query.barcode.LastBarcode;
import ml.query.categoryGood.AddCategoryGood;
import ml.query.categoryGood.CategoryGoodList;
import ml.query.categoryGood.CategoryGoodsByName;
import ml.query.dish.AllDishList;
import ml.query.dish.DishByCode;
import ml.query.goods.GoodByCode;
import ml.query.goods.NewGood;
import ml.query.goods.UpdateGood;
import ml.query.techmap.AddTechMap;
import ml.query.techmap.DeletePosTechMap;
import ml.query.techmap.TechMapById;
import ml.query.techmap.TechMapListWhereGoodNotNull;
import ml.query.techmap.UpdateTechMap;
import ml.table.TechMapTable;
import ml.util.RegexpNameGoods;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class TechMapController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField nameDishTextField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField nameTextField;
    @FXML
    private TableView<TechMapTable> tableTechMap;
    @FXML
    private TableColumn<TechMapTable, Long> idTechMapColumn;
    @FXML
    private TableColumn<TechMapTable, String> codeColumn;
    @FXML
    private TableColumn<TechMapTable, String> nameColumn;
    @FXML
    private TableColumn<TechMapTable, BigDecimal> grossColumn;
    @FXML
    private TableColumn<TechMapTable, BigDecimal> optPriceGoodColumn;
    @FXML
    private TableColumn<TechMapTable, Boolean> newPosTechMapColumn;
    @FXML
    private Text sumLabel;
    @FXML
    private TextField priceDishTextField;
    @FXML
    private Button newCardButton;
    @FXML
    private Button addTechMapButton;
    @FXML
    private TextField grossTextfield;
    @FXML
    private TextField codeGoodTextField;

    private CategoryGoodList categoryGoodList = new CategoryGoodList();
    private List<CategoryGoods> categoryList;
    private List<Dish> dishList;
    private AllDishList allDishList = new AllDishList();
    private int selectRow = -1;
    private ObservableList<String> options = FXCollections.observableArrayList();
    private ObservableList<TechMapTable> techMapData = FXCollections.observableArrayList();
    private String textCategoryDish;
    private CategoryGoods category = new CategoryGoods();
    private DialogAlert dialogAlert = new DialogAlert();
    private TechMapListWhereGoodNotNull tmlwgnn = new TechMapListWhereGoodNotNull();
    private Goods goodNotNull;
    private TechMap techMapForUpdate;
    private List<TechMap> listTechMap;
    private List<TechMap> newListTechMap;

    /**
     * Берет текст из ComboBox
     */
    @FXML
    private void getTextCategory() {
        textCategoryDish = categoryComboBox.getValue();
        choose(textCategoryDish);

    }

    @FXML
    private void newCardAction(ActionEvent event) {
        //удаление строк в таблице
        for (int i = -1; i < tableTechMap.getItems().size(); i++) {
            tableTechMap.getItems().clear();

        }

        grossTextfield.setText("");
        nameTextField.setText("");
        nameDishTextField.setText("");
    }

    @FXML
    private void addTechMap(ActionEvent event) {

        if (!"".equals(categoryComboBox.getValue())) {

            Goods goods = new Goods();
            DishByCode dishByCode = new DishByCode();
            Dish dish = new Dish();
            AddTechMap addTechMap = new AddTechMap();
            UpdateGood updateGood = new UpdateGood();
            int countRow = tableTechMap.getItems().size();   // кол-во строк в таблице
            BigDecimal sumOpt = new BigDecimal(0);
            newListTechMap = new ArrayList();

            if (codeGoodTextField.getText().isEmpty()) {
                if (!priceDishTextField.getText().isEmpty()) {

                    for (int i = 0; i < countRow; i++) {
                        TechMap techMap = new TechMap();
//Товар по коду из таблицы
                        dishByCode.setCode(codeColumn.getCellData(i));

                        dish = dishByCode.displayResult();
                        techMap.setDish(dish);
                        //goods.getTechmap().add(techMap);
                        techMap.setGross(grossColumn.getCellData(i));

                        addTechMap.add(techMap);
                        newListTechMap.add(techMap);
                        sumOpt = sumOpt.add(optPriceGoodColumn.getCellData(i));
                    }

                    setNewGood(goods, newListTechMap, sumOpt);
                } else {
                    dialogAlert.alert("Внимание!", "Сообщение:", "Поле 'Цена за блюдо' пустое");
                }
            } else {
                Goods g = new Goods();
                UpdateTechMap upTM = new UpdateTechMap();
                for (int i = 0; i < countRow; i++) {
                    TechMap techMap = new TechMap();

                    if (i < listTechMap.size()) {
                        g = listTechMap.get(i).getGoods();
                        listTechMap.get(i).setGross(grossColumn.getCellData(i));
                        upTM.update(listTechMap.get(i));
                    } else {
//                        TechMap techMap = new TechMap();
                        dishByCode.setCode(codeColumn.getCellData(i));
                        dish = dishByCode.displayResult();
                        techMap.setDish(dish);
                        techMap.setGoods(g);
                        techMap.setGross(grossColumn.getCellData(i));
                        listTechMap.add(techMap);
                        System.out.println("Появилась новая строка: " + techMap.getDish().getName());
                        upTM.update(listTechMap.get(i));
                    }
//                    if (countRow > listTechMap.size()) {
//                        TechMap techMap = new TechMap();
//                        dishByCode.setCode(codeColumn.getCellData(i));
//                        dish = dishByCode.displayResult();
//                        techMap.setDish(dish);
//                        techMap.setGross(grossColumn.getCellData(i));
//
//                        System.out.println("Появилась новая строка: " + techMap.getDish().getName());
//
////                        l.add(techMap);
//                    }

//                     upTM.update(l.get(i));
                }

                goodNotNull.setPriceOpt(new BigDecimal(sumLabel.getText()));
                goodNotNull.setPrice(new BigDecimal(priceDishTextField.getText()));

                UpdateGood upG = new UpdateGood();
                upG.update(goodNotNull);

                dialogAlert.alert("Внимание!", "Сообщение:", "Товар создан или обновлен");
                closeWindow();

            }
        } else {
            dialogAlert.alert("Внимание!", "Сообщение:", "Выберите категорию блюда");
        }
    }

    /**
     * Поиск товара по наименованию
     *
     * @param event
     */
    @FXML
    private void nameAction(ActionEvent event) {
        String name = nameTextField.getText();

        getGoodName(name, true);
        nameTextField.setEditable(true);
        grossTextfield.setEditable(true);
        grossTextfield.requestFocus();
        tableTechMap.refresh();

    }

    /**
     * Берет значение из поля Кол-во товара
     */
    @FXML
    private void grossAction(ActionEvent event) {
        tableTechMap.refresh();
        String s = grossTextfield.getText();
        //Замена , на .
        String gr = s.replace(',', '.');
        BigDecimal gross = decimal("###.###", Double.parseDouble(gr));

        int i = tableTechMap.getItems().size();   // кол-во строк в таблице
        //если кол-во строк не 0
        if (i != 0) {
            int row = i - 1;                        // последняя строка
            //Если выбрана строка то изменить в ней значение кол-ва
            if (selectRow >= 0) {
                //Вставка данных в выбранную строку в ячейку "кол-во"
                techMapData.get(selectRow).setGross(gross);
                //arrivalData.get(row).setNewPrice(allowance.multiply(invoice));s
                tableTechMap.getItems().set(selectRow, techMapData.get(selectRow));

                tableTechMap.getSelectionModel().clearSelection();
                selectRow = -1;
            } else {
                //Вставка данных в последнюю ячейку "кол-во" и "сумма строки" таблицы
                techMapData.get(row).setGross(gross);
                //arrivalData.get(row).setNewPrice(allowance.multiply(invoice));s
                tableTechMap.getItems().set(row, techMapData.get(row));
            }

            tableTechMap.getSelectionModel().clearSelection();

        }

        sumTechMap();

        nameTextField.requestFocus();
        nameTextField.selectAll();
        addTechMapButton.setDisable(false);
    }

    /**
     * Значение номера выбранной строки
     */
    @FXML
    private void getRow() {

        List<TablePosition> positions = tableTechMap.getSelectionModel().getSelectedCells();
        if (!positions.isEmpty()) {

            int i = tableTechMap.getItems().size();   // кол-во строк в таблице
            if (i > 0) {

                TablePosition pos = tableTechMap.getSelectionModel().getSelectedCells().get(0);
                selectRow = pos.getRow();

//                            tableArrival.getSelectionModel().clearSelection();
            }
        }
    }

    /**
     * Выводит на экран код товара из Goods
     *
     * @param event
     */
    @FXML
    private void getNameDish(ActionEvent event) {
        goodNotNull = new Goods();
        String name = nameDishTextField.getText();

        TechMapListWhereGoodNotNull gNoNu = new TechMapListWhereGoodNotNull();
        goodNotNull = gNoNu.getGoodFromListTechMapWhereGoodNotNullByName(name);
        if (goodNotNull != null) {
            codeGoodTextField.setText(goodNotNull.getCode());
            priceDishTextField.setText(goodNotNull.getPrice().toString());
            sumLabel.setText(goodNotNull.getPriceOpt().toString());
            listTechMap = gNoNu.getIdDishFromListTechMapWhereGoodNotNull(goodNotNull);
            for (int i = 0; i < listTechMap.size(); i++) {
                System.out.println("Ответ: " + listTechMap.get(i).getDish().getName());
                displayResult(listTechMap.get(i).getDish(), listTechMap.get(i), false);
            }

            addTechMapButton.setDisable(false);
            nameTextField.setEditable(true);
            grossTextfield.setEditable(true);
            nameTextField.requestFocus();

        } else {

            nameTextField.setEditable(true);
            grossTextfield.setEditable(true);
            nameTextField.requestFocus();
        }
    }

    public void setTechMap(List<TechMap> tm) {
        for (int i = 0; i < tm.size(); i++) {
            displayResultById(tm.get(i));
        }

        nameDishTextField.setText(tm.get(0).getGoods().getName());
        codeGoodTextField.setText(tm.get(0).getGoods().getCode());
        nameTextField.setEditable(true);
        grossTextfield.setEditable(true);
        nameTextField.requestFocus();

    }

    /**
     * Закрывает окно
     */
    private void closeWindow() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
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
            //nameTextField.requestFocus();
            //nameTextField.setEditable(true);
            nameDishTextField.requestFocus();
            nameDishTextField.setEditable(true);

        }
    }

    /**
     * Открывает окно для создания нового товара
     */
    private void setNewGood(Goods goods, List<TechMap> listTechMap, BigDecimal sumOpt) {
        CategoryGoodsByName categoryGoodsByName = new CategoryGoodsByName();
        CategoryGoods cg = new CategoryGoods();
        NewGood newGood = new NewGood();
        Date date = new Date();
        RegexpNameGoods regexpNameGoods = new RegexpNameGoods();
        Long newBarcode = null;
        String price = priceDishTextField.getText();
        String newName = nameDishTextField.getText();
        UpdateTechMap updateTechMap = new UpdateTechMap();

        //Проверка первого символа
        if (regexpNameGoods.nameGoods(newName) == true) {

            if (!"".equals(newName) && !("".equals(price))) {

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
                goods.setName(nameDishTextField.getText());
                goods.setCode(newBarcode.toString());
                goods.setPriceOpt(new BigDecimal(sumLabel.getText()));
                goods.setPrice(new BigDecimal(priceDishTextField.getText()));
                goods.setResidue(new BigDecimal(0));
                goods.setDate(date);
                goods.setMarking(Boolean.FALSE);

                //Если поле нового товара не пустое, тогда создается новая запись в БД и новый штрих-код
                if (!(newName == null) && !("".equals(newName))) {
                    GoodByCode goodByCode = new GoodByCode();
                    Goods goodNewBarcode = new Goods();
                    categoryGoodsByName.setName(categoryComboBox.getValue());   //категори товара по названию
                    cg = categoryGoodsByName.displayResult();
                    goods.setCategoryGoods(cg);
                    //добавляет новый товар
                    newGood.add(goods);

                    goodByCode.setCode(newBarcode.toString());
                    goodNewBarcode = goodByCode.displayResult();
                    barcode.setGoods(goodNewBarcode);
                    addBarcode.add(barcode);

                    nameDishTextField.setText("");
                    grossTextfield.setText("");
                    nameTextField.setText("");
                    priceDishTextField.setText("");

                    for (int i = 0; i < listTechMap.size(); i++) {
                        listTechMap.get(i).setGoods(goods);
                        updateTechMap.update(listTechMap.get(i));
                    }

                    //удаление строк в таблице
                    for (int i = -1; i < tableTechMap.getItems().size(); i++) {
                        tableTechMap.getItems().clear();

                    }
                }

            }
        } else {
            dialogAlert.alert("Внимание!", "Сообщение:", "Название товара должно начинаться с буквы");

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
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
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
            categoryComboBox.setItems(options);
        });
        /*result.ifPresent(pair -> {
        
        category.setName(pair.getKey());
        category.setNote(pair.getValue());
        acg.add(category);
        
        });*/
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
     * Вовзращает данные о товаре по наименованию
     *
     * @param code
     */
    private void getGoodName(String name, Boolean newPos) {
        Dish dish = null;
        for (Dish gg1 : dishList) {
            if (gg1.getName().equals(name)) {
                dish = gg1;
            }
        }

        if (dish != null) {
            displayResult(dish, null, newPos);
        }
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayResult(Dish dish, TechMap techMap, Boolean newPos) {

        idTechMapColumn.setCellValueFactory(new PropertyValueFactory<TechMapTable, Long>("idTechMap"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<TechMapTable, String>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<TechMapTable, String>("name"));
        grossColumn.setCellValueFactory(new PropertyValueFactory<TechMapTable, BigDecimal>("gross"));
        optPriceGoodColumn.setCellValueFactory(new PropertyValueFactory<TechMapTable, BigDecimal>("optPrice"));
        newPosTechMapColumn.setCellValueFactory(new PropertyValueFactory<TechMapTable, Boolean>("newPos"));

        TechMapTable techmapTable = new TechMapTable();
        for (int i = 0; i < 1; i++) {
            techmapTable.setCode(dish.getCode());
            techmapTable.setName(dish.getName());
            if (techMap != null) {
                techmapTable.setIdTechMap(techMap.getIdTechMap());
                techmapTable.setGross(techMap.getGross());
            } else {
                techmapTable.setGross(new BigDecimal(1));

            }
            techmapTable.setOptPrice(dish.getPriceOpt());
            techmapTable.setNewPos(newPos);
            // заполняем таблицу данными
            if (!"".equals(dish.getName())) {
                techMapData.add(techmapTable);
            }
        }

        tableTechMap.setItems(techMapData);
        sumTechMap();

    }

    private void sumTechMap() {
        BigDecimal sumOpt = new BigDecimal(0);
        int countRow = tableTechMap.getItems().size();   // кол-во строк в таблице

        for (int j = 0; j < countRow; j++) {

            sumOpt = sumOpt.add(grossColumn.getCellData(j).multiply(optPriceGoodColumn.getCellData(j)));
        }

        sumLabel.setText(sumOpt.toString());
    }

    private void displayResultById(TechMap techMap) {

        idTechMapColumn.setCellValueFactory(new PropertyValueFactory<TechMapTable, Long>("idTechMap"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<TechMapTable, String>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<TechMapTable, String>("name"));
        grossColumn.setCellValueFactory(new PropertyValueFactory<TechMapTable, BigDecimal>("gross"));
        optPriceGoodColumn.setCellValueFactory(new PropertyValueFactory<TechMapTable, BigDecimal>("optPrice"));

        TechMapTable techmapTable = new TechMapTable();
        for (int i = 0; i < 1; i++) {
            techmapTable.setCode(techMap.getDish().getCode());
            techmapTable.setName(techMap.getDish().getName());
            if (techMap != null) {
                techmapTable.setIdTechMap(techMap.getIdTechMap());
                techmapTable.setGross(techMap.getGross());
            } else {
                techmapTable.setGross(new BigDecimal(1));

            }
            techmapTable.setOptPrice(techMap.getDish().getPriceOpt());
            // заполняем таблицу данными
            if (!"".equals(techMap.getDish().getName())) {
                techMapData.add(techmapTable);
            }
        }

        tableTechMap.setItems(techMapData);

    }

    private void deletePosition(TechMap tm) {

        DeletePosTechMap posTechMap = new DeletePosTechMap();
        posTechMap.delete(tm);

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//По нажатию клавиш
        anchorPane.setOnKeyPressed(
                event -> {
                    switch (event.getCode()) {

                        case DELETE:
                            TechMapById idTechMap = new TechMapById();
                            TechMapTable table = tableTechMap.getSelectionModel().getSelectedItem();
                            List<TechMap> tm = new ArrayList<>();
                            Long id = table.getIdTechMap();
                            String code = table.getCode();
                            String name = table.getName();

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Удаление позиции!");
                            alert.setHeaderText("Удалить данную позицию?");
                            alert.setContentText(code + " | " + name);

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                //Удаление строки
                                TablePosition pos = tableTechMap.getSelectionModel().getSelectedCells().get(0);
                                int row = pos.getRow();
                                Boolean b = newPosTechMapColumn.getCellData(row);
                                if (b == true) {
                                    techMapData.remove(row);
                                    nameTextField.requestFocus();
                                    tableTechMap.getSelectionModel().clearSelection();

                                } else {
                                    listTechMap.remove(row);
                                    techMapData.remove(row);
                                    nameTextField.requestFocus();
                                    //считает сумму техкарты
                                    sumTechMap();
                                    idTechMap.setId(id);
                                    tm = idTechMap.displayResult();
                                    deletePosition(tm.get(0));
                                    tableTechMap.getSelectionModel().clearSelection();

                                }

                            } else {

                            }

                            break;
                    }
                });

        //Список всего товара
        categoryList = categoryGoodList.getList();
        for (CategoryGoods cg : categoryList) {
            options.add(cg.getName());
        }

        options.add("Создать категорию");

        //Выделяет красным цветом СОЗДАТЬ КАТЕГОРИЮ
        categoryComboBox.setCellFactory(
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

        categoryComboBox.setItems(options);

        //Список всего товара
        dishList = allDishList.listDish();
        //Список товара по названию в поле nameGood
        String[] sName = new String[dishList.size()];
        for (int i = 0; i < dishList.size(); i++) {
            sName[i] = (String) dishList.get(i).getName();
        }
        Arrays.sort(sName);
        TextFields.bindAutoCompletion(nameTextField, sName);

        //Список товара в тех.карте
        List<Goods> listTechMapOnlyGoods = tmlwgnn.listTechMapWhereGoodNotNull();
        String[] nameGoodFromTechMap = new String[listTechMapOnlyGoods.size()];
        for (int i = 0; i < listTechMapOnlyGoods.size(); i++) {
            nameGoodFromTechMap[i] = (String) listTechMapOnlyGoods.get(i).getName();
        }

        Arrays.sort(nameGoodFromTechMap);
        TextFields.bindAutoCompletion(nameDishTextField, nameGoodFromTechMap);

        //Ввод поле ВЕС_БРУТТО только цифр точки или запятой
        grossTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*([.,]\\d*)?")) {
                    grossTextfield.setText(oldValue);
                }
            }
        });

        //Ввод поле ЦЕНА_ЗА_БЛЮДО только цифр точки или запятой
        priceDishTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*([.,]\\d*)?")) {
                    priceDishTextField.setText(oldValue);
                }
            }
        });
    }

}
