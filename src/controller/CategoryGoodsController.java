/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import ml.Ml_FX;
import ml.dialog.DialogAlert;
import ml.model.CategoryGoods;
import ml.query.categoryGood.AddCategoryGood;
import ml.query.categoryGood.CategoryGoodList;
import ml.query.categoryGood.CategoryGoodsByName;
import ml.query.categoryGood.UpdateCategoryGood;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class CategoryGoodsController implements Initializable {

    @FXML
    private ComboBox<String> categoryGoodComboBox;
    @FXML
    private BorderPane borderPane;

    private Stage primaryStage;
    private String textComboBox;
    private ObservableList<String> options = FXCollections.observableArrayList();
    private ObservableList<String> optionsTime = FXCollections.observableArrayList();
    private ObservableList<String> optionsMinute = FXCollections.observableArrayList();

    private CategoryGoodList categoryGoodList = new CategoryGoodList();
    private List<CategoryGoods> categoryList;
    private CategoryGoods category = new CategoryGoods();
    private DialogAlert dialogAlert = new DialogAlert();

    /**
     * Берет текст из ComboBox
     */
    @FXML
    private void getTextCategory() {
        textComboBox = categoryGoodComboBox.getValue();
        choose(textComboBox);

    }

    /**
     * Обновление название и описание категории
     *
     * @param event
     */
    @FXML
    private void changeCategoryButton(ActionEvent event) {
        CategoryGoodsByName categoryGoodsByName = new CategoryGoodsByName();
        CategoryGoods cg = new CategoryGoods();
        categoryGoodsByName.setName(categoryGoodComboBox.getValue());   //категори товара по названию
        cg = categoryGoodsByName.displayResult();
        if (cg == null) {
            dialogAlert.alert("Внимание!", "Сообщение:", "Выберите категорию!!!");

        } else {
            dialogForUpdateCategoryGood(cg);
        }
    }

    /**
     * Удаление категории
     *
     * @param event
     */
    @FXML
    private void deleteCategoryButton(ActionEvent event) {

        CategoryGoodsByName categoryGoodsByName = new CategoryGoodsByName();
        CategoryGoods cg = new CategoryGoods();
        categoryGoodsByName.setName(categoryGoodComboBox.getValue());   //категори товара по названию
        cg = categoryGoodsByName.displayResult();
        if (cg == null) {
            dialogAlert.alert("Внимание!", "Сообщение:", "Выберите категорию!!!");

        } else {
            dialogForDeleteCategoryGood(cg);
        }

    }

    /**
     * Ограничения для категории
     *
     * @param event
     */
    @FXML
    private void restrictCategoryButton(ActionEvent event) {
        CategoryGoodsByName categoryGoodsByName = new CategoryGoodsByName();
        CategoryGoods cg = new CategoryGoods();
        categoryGoodsByName.setName(categoryGoodComboBox.getValue());   //категори товара по названию
        cg = categoryGoodsByName.displayResult();
        if (cg == null) {
            dialogAlert.alert("Внимание!", "Сообщение:", "Выберите категорию!!!");

        } else {
            dialogForRestrictCategoryGood(cg);
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
            categoryGoodComboBox.setItems(options);
        });
        /*result.ifPresent(pair -> {
        
        category.setName(pair.getKey());
        category.setNote(pair.getValue());
        acg.add(category);
        
        });*/
    }

    /**
     * Диалог для обновления категории товара
     */
    private void dialogForUpdateCategoryGood(CategoryGoods cg) {

        UpdateCategoryGood update = new UpdateCategoryGood();

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
        from.setText(cg.getName());
        TextField to = new TextField();
        Label restrict = new Label();

        //удаление из строки ограничения, показывает только описание
        String str = cg.getNote();
        int index = str.indexOf("/");
        if (index == -1) {
            to.setText(cg.getNote());
        } else {
            to.setText(str.substring(0, index));
            restrict.setText(str.substring(index));
        }
        
        
        gridPane.add(new Label("Название:"), 0, 0);
        gridPane.add(from, 1, 0);
        gridPane.add(new Label("Описание:"), 2, 0);
        gridPane.add(to, 3, 0);
        gridPane.add(restrict, 4, 0);
        
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
            cg.setName(pair.getKey());
            cg.setNote(pair.getValue()+restrict.getText());
            update.update(cg);
        });
        /*result.ifPresent(pair -> {
        
        category.setName(pair.getKey());
        category.setNote(pair.getValue());
        acg.add(category);
        
        });*/
    }

    /**
     * Диалог для удаления категории товара
     */
    private void dialogForDeleteCategoryGood(CategoryGoods cg) {

        UpdateCategoryGood update = new UpdateCategoryGood();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление категории товара!");
        alert.setHeaderText("Удалить категорию?");
        alert.setContentText(cg.getName() + " | " + cg.getNote());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            //Добавление категории в БД
            cg.setName("Удаленная категория '" + cg.getName() + "'");
            cg.setNote(cg.getNote());
            update.update(cg);
        } else {
            // ... user chose CANCEL or closed the dialog
        }

    }

    /**
     * Диалог для ограничения категории товара
     */
    private void dialogForRestrictCategoryGood(CategoryGoods cg) {
        UpdateCategoryGood update = new UpdateCategoryGood();

        boolean okClicked = false;

        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Ml_FX.class.getResource("/ml/view/RestrictCategoryGood.fxml"));
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ограничения категории товаров");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            BorderPane page = (BorderPane) loader.load();
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            RestrictCategoryGoodController restrictCategoryGoodController = loader.getController();
            restrictCategoryGoodController.setCategoryGoods(cg);
            restrictCategoryGoodController.setDialogStage(dialogStage);

//
//            sumInvoiceController.setChoose(crList, bg2, kkt);
//            //Список оплат за сегоняшний день
//            List<CaseRecord> crList;
//            DateCaseRecord dateCaseRecord = new DateCaseRecord();
//            dateCaseRecord.setDate(LocalDate.now());
//            crList = dateCaseRecord.displayResult();
//            ChooseSumInvoiceController sumInvoiceController = loader.getController();
//
//            sumInvoiceController.setChoose(crList, bg2, kkt);
//
            dialogStage.showAndWait();
//            this.sumInvoice = sumInvoiceController.displaySum();
//            okClicked = sumInvoiceController.displayOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return okClicked;

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
//            allowanceTextField.requestFocus();
//            allowanceTextField.setEditable(true);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        //Список всего товара
        categoryList = categoryGoodList.getList();
        for (CategoryGoods cg : categoryList) {
            options.add(cg.getName());
        }
        options.add("Создать категорию");

        //Выделяет красным цветом СОЗДАТЬ КАТЕГОРИЮ
        categoryGoodComboBox.setCellFactory(
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

        categoryGoodComboBox.setItems(options);
    }

}
