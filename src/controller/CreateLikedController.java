/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import ml.barcode.BarcodeConv;
import ml.model.CategoryFavorite;
import ml.model.Favorite;
import ml.model.Goods;
import ml.query.favorite.AddFavoriteButton;
import ml.query.favorite.AddFavoriteCategory;
import ml.query.favorite.CategoryByName;
import ml.query.favorite.CategoryFavoriteList;
import ml.query.favorite.FavoriteByName;
import ml.query.favorite.FavoriteList;
import ml.query.favorite.UpdateCategoryFavorite;
import ml.query.favorite.UpdateFavoriteButton;
import ml.query.goods.QueryAllGoodsList;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class CreateLikedController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
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
    private Button favButt_1;
    @FXML
    private Button favButt_2;
    @FXML
    private Button favButt_3;
    @FXML
    private Button favButt_4;
    @FXML
    private Button favButt_5;
    @FXML
    private Button favButt_6;
    @FXML
    private Button favButt_7;
    @FXML
    private Button favButt_8;
    @FXML
    private Button favButt_9;
    @FXML
    private Button favButt_10;
    @FXML
    private Button favButt_11;
    @FXML
    private Button favButt_12;
    @FXML
    private Button favButt_13;
    @FXML
    private Button favButt_14;
    @FXML
    private Button favButt_15;
    @FXML
    private Button favButt_16;
    @FXML
    private Button favButt_17;
    @FXML
    private Button favButt_18;
    @FXML
    private Button favButt_19;
    @FXML
    private Button favButt_20;
    @FXML
    private Button favButt_21;
    @FXML
    private Button favButt_22;
    @FXML
    private Button favButt_23;
    @FXML
    private Button favButt_24;
    @FXML
    private Button favButt_25;
    @FXML
    private TextField codeTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField textFieldFavor1;
    @FXML
    private TextField textFieldFavor2;
    @FXML
    private TextField textFieldFavor3;
    @FXML
    private TextField textFieldFavor4;
    @FXML
    private TextField textFieldFavor5;
    @FXML
    private TextField textFieldFavor6;
    @FXML
    private Label goodNameLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private Label chooseCatLabel;
    @FXML
    private Label chooseButtonLabel;
    @FXML
    private Label chooseGoodLabel;

    private List<CategoryFavorite> catFavorList;
    private CategoryFavoriteList favoriteList = new CategoryFavoriteList();
    private CategoryFavorite cf;
    private AddFavoriteCategory addFavoriteCategory = new AddFavoriteCategory();
    private UpdateCategoryFavorite updateCategoryFavorite = new UpdateCategoryFavorite();
    private CategoryByName categoryByName = new CategoryByName();
    private List<Goods> goodsList;
    private List<Favorite> favList;
    private QueryAllGoodsList allGoodsList = new QueryAllGoodsList();
    private FavoriteList allFavorList = new FavoriteList();
    private BarcodeConv barcodeConv = new BarcodeConv();
    private String weight = "1";
    private Goods goods;
    private String idButtonCategFav;
    private String nameCategory;

    @FXML
    private void clickCategFavor1() {

        chooseCatLabel.setTextFill(Color.web("#000000"));
        chooseGoodLabel.setTextFill(Color.web("#FF0000"));

        firstFavorite.setStyle("-fx-base: #b6e7c9;");
        secondFavorite.setStyle("");
        thirdFavorite.setStyle("");
        fourthFavorite.setStyle("");
        fifthFavorite.setStyle("");
        sixthFavorite.setStyle("");

        nameCategory = firstFavorite.getText();
        listButtonName(nameCategory);

        codeTextField.setDisable(false);
        nameTextField.setDisable(false);

        favButtonWithOutText();
        listFavCateg(cf);
    }

    @FXML
    private void clickCategFavor2() {

        chooseCatLabel.setTextFill(Color.web("#000000"));
        chooseGoodLabel.setTextFill(Color.web("#FF0000"));

        firstFavorite.setStyle("");
        secondFavorite.setStyle("-fx-base: #b6e7c9;");
        thirdFavorite.setStyle("");
        fourthFavorite.setStyle("");
        fifthFavorite.setStyle("");
        sixthFavorite.setStyle("");
        nameCategory = secondFavorite.getText();
        listButtonName(nameCategory);
        codeTextField.setDisable(false);
        nameTextField.setDisable(false);

        favButtonWithOutText();
        listFavCateg(cf);
    }

    @FXML
    private void clickCategFavor3() {

        chooseCatLabel.setTextFill(Color.web("#000000"));
        chooseGoodLabel.setTextFill(Color.web("#FF0000"));

        firstFavorite.setStyle("");
        secondFavorite.setStyle("");
        thirdFavorite.setStyle("-fx-base: #b6e7c9;");
        fourthFavorite.setStyle("");
        fifthFavorite.setStyle("");
        sixthFavorite.setStyle("");
        nameCategory = thirdFavorite.getText();
        listButtonName(nameCategory);
        codeTextField.setDisable(false);
        nameTextField.setDisable(false);

        favButtonWithOutText();
        listFavCateg(cf);
    }

    @FXML
    private void clickCategFavor4() {

        chooseCatLabel.setTextFill(Color.web("#000000"));
        chooseGoodLabel.setTextFill(Color.web("#FF0000"));

        firstFavorite.setStyle("");
        secondFavorite.setStyle("");
        thirdFavorite.setStyle("");
        fourthFavorite.setStyle("-fx-base: #b6e7c9;");
        fifthFavorite.setStyle("");
        sixthFavorite.setStyle("");
        nameCategory = fourthFavorite.getText();
        listButtonName(nameCategory);
        codeTextField.setDisable(false);
        nameTextField.setDisable(false);

        favButtonWithOutText();
        listFavCateg(cf);
    }

    @FXML
    private void clickCategFavor5() {

        chooseCatLabel.setTextFill(Color.web("#000000"));
        chooseGoodLabel.setTextFill(Color.web("#FF0000"));

        firstFavorite.setStyle("");
        secondFavorite.setStyle("");
        thirdFavorite.setStyle("");
        fourthFavorite.setStyle("");
        fifthFavorite.setStyle("-fx-base: #b6e7c9;");
        sixthFavorite.setStyle("");
        nameCategory = fifthFavorite.getText();
        listButtonName(nameCategory);
        codeTextField.setDisable(false);
        nameTextField.setDisable(false);

        favButtonWithOutText();
        listFavCateg(cf);
    }

    @FXML
    private void clickCategFavor6() {

        chooseCatLabel.setTextFill(Color.web("#000000"));
        chooseGoodLabel.setTextFill(Color.web("#FF0000"));

        firstFavorite.setStyle("");
        secondFavorite.setStyle("");
        thirdFavorite.setStyle("");
        fourthFavorite.setStyle("");
        fifthFavorite.setStyle("");
        sixthFavorite.setStyle("-fx-base: #b6e7c9;");
        nameCategory = sixthFavorite.getText();
        listButtonName(nameCategory);
        codeTextField.setDisable(false);
        nameTextField.setDisable(false);

        favButtonWithOutText();
        listFavCateg(cf);
    }

    /**
     * Ввод или обновление названия кнопки категории избранного
     *
     */
    @FXML
    private void enterCategFavor1() {

        cf = new CategoryFavorite();

        if (firstFavorite.getText().equals("Введите название категории")) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Описание категории");
            dialog.setContentText("Введите описание категории:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                cf.setNote(result.get());
                cf.setName(textFieldFavor1.getText());

                addFavoriteCategory.add(cf);
                textFieldFavor1.setVisible(false);
                firstFavorite.setVisible(true);
                firstFavorite.setText(cf.getName());
                
                nameCategory = textFieldFavor1.getText();
            }
        } else {
            String name = textFieldFavor1.getText();
            categoryByName.setName(firstFavorite.getText());
            cf = categoryByName.displayResult();

            TextInputDialog dialog = new TextInputDialog(cf.getNote());
            dialog.setTitle("Описание категории");
            dialog.setContentText("Введите описание категории:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {

                cf.setNote(result.get());
                updateCategoryFavorite.update(cf, name);

                textFieldFavor1.setVisible(false);
                firstFavorite.setVisible(true);
                firstFavorite.setText(cf.getName());
                
                nameCategory = textFieldFavor1.getText();
            }
        }
// The Java 8 way to get the response value (with lambda expression).
        // result.ifPresent(name -> System.out.println("Your name: " + name));
    }

    /**
     * Ввод или обновление названия кнопки категории избранного
     *
     */
    @FXML
    private void enterCategFavor2() {

        cf = new CategoryFavorite();

        if (secondFavorite.getText().equals("Введите название категории")) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Описание категории");
            dialog.setContentText("Введите описание категории:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                cf.setNote(result.get());
                cf.setName(textFieldFavor2.getText());

                addFavoriteCategory.add(cf);
                textFieldFavor2.setVisible(false);
                secondFavorite.setVisible(true);
                secondFavorite.setText(cf.getName());
                
                nameCategory = textFieldFavor2.getText();
            }
        } else {
            String name = textFieldFavor2.getText();
            categoryByName.setName(secondFavorite.getText());
            cf = categoryByName.displayResult();

            TextInputDialog dialog = new TextInputDialog(cf.getNote());
            dialog.setTitle("Описание категории");
            dialog.setContentText("Введите описание категории:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {

                cf.setNote(result.get());
                updateCategoryFavorite.update(cf, name);

                textFieldFavor2.setVisible(false);
                secondFavorite.setVisible(true);
                secondFavorite.setText(cf.getName());
                
                nameCategory = textFieldFavor2.getText();
            }
        }
    }

    /**
     * Ввод или обновление названия кнопки категории избранного
     *
     */
    @FXML
    private void enterCategFavor3() {

        cf = new CategoryFavorite();

        if (thirdFavorite.getText().equals("Введите название категории")) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Описание категории");
            dialog.setContentText("Введите описание категории:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                cf.setNote(result.get());
                cf.setName(textFieldFavor3.getText());

                addFavoriteCategory.add(cf);
                textFieldFavor3.setVisible(false);
                thirdFavorite.setVisible(true);
                thirdFavorite.setText(cf.getName());
                
                nameCategory = textFieldFavor3.getText();
            }
        } else {
            String name = textFieldFavor3.getText();
            categoryByName.setName(thirdFavorite.getText());
            cf = categoryByName.displayResult();

            TextInputDialog dialog = new TextInputDialog(cf.getNote());
            dialog.setTitle("Описание категории");
            dialog.setContentText("Введите описание категории:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {

                cf.setNote(result.get());
                updateCategoryFavorite.update(cf, name);

                textFieldFavor3.setVisible(false);
                thirdFavorite.setVisible(true);
                thirdFavorite.setText(cf.getName());
                
                nameCategory = textFieldFavor3.getText();
            }
        }
    }

    /**
     * Ввод или обновление названия кнопки категории избранного
     *
     */
    @FXML
    private void enterCategFavor4() {

        cf = new CategoryFavorite();

        if (fourthFavorite.getText().equals("Введите название категории")) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Описание категории");
            dialog.setContentText("Введите описание категории:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                cf.setNote(result.get());
                cf.setName(textFieldFavor4.getText());

                addFavoriteCategory.add(cf);
                textFieldFavor4.setVisible(false);
                fourthFavorite.setVisible(true);
                fourthFavorite.setText(cf.getName());
                
                nameCategory = textFieldFavor4.getText();
            }
        } else {
            String name = textFieldFavor4.getText();
            categoryByName.setName(fourthFavorite.getText());
            cf = categoryByName.displayResult();

            TextInputDialog dialog = new TextInputDialog(cf.getNote());
            dialog.setTitle("Описание категории");
            dialog.setContentText("Введите описание категории:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {

                cf.setNote(result.get());
                updateCategoryFavorite.update(cf, name);

                textFieldFavor4.setVisible(false);
                fourthFavorite.setVisible(true);
                fourthFavorite.setText(cf.getName());
                
                nameCategory = textFieldFavor4.getText();
            }
        }
    }

    /**
     * Ввод или обновление названия кнопки категории избранного
     *
     */
    @FXML
    private void enterCategFavor5() {

        cf = new CategoryFavorite();

        if (fifthFavorite.getText().equals("Введите название категории")) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Описание категории");
            dialog.setContentText("Введите описание категории:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                cf.setNote(result.get());
                cf.setName(textFieldFavor5.getText());

                addFavoriteCategory.add(cf);
                textFieldFavor5.setVisible(false);
                fifthFavorite.setVisible(true);
                fifthFavorite.setText(cf.getName());
                
                nameCategory = textFieldFavor5.getText();
            }
        } else {
            String name = textFieldFavor5.getText();
            categoryByName.setName(fifthFavorite.getText());
            cf = categoryByName.displayResult();

            TextInputDialog dialog = new TextInputDialog(cf.getNote());
            dialog.setTitle("Описание категории");
            dialog.setContentText("Введите описание категории:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {

                cf.setNote(result.get());
                updateCategoryFavorite.update(cf, name);

                textFieldFavor5.setVisible(false);
                fifthFavorite.setVisible(true);
                fifthFavorite.setText(cf.getName());
                
                nameCategory = textFieldFavor5.getText();
            }
        }
    }

    /**
     * Ввод или обновление названия кнопки категории избранного
     *
     */
    @FXML
    private void enterCategFavor6() {

        cf = new CategoryFavorite();

        if (sixthFavorite.getText().equals("Введите название категории")) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Описание категории");
            dialog.setContentText("Введите описание категории:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                cf.setNote(result.get());
                cf.setName(textFieldFavor6.getText());

                addFavoriteCategory.add(cf);
                textFieldFavor6.setVisible(false);
                sixthFavorite.setVisible(true);
                sixthFavorite.setText(cf.getName());
                
                nameCategory = textFieldFavor6.getText();
            }
        } else {
            String name = textFieldFavor6.getText();
            categoryByName.setName(sixthFavorite.getText());
            cf = categoryByName.displayResult();

            TextInputDialog dialog = new TextInputDialog(cf.getNote());
            dialog.setTitle("Описание категории");
            dialog.setContentText("Введите описание категории:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {

                cf.setNote(result.get());
                updateCategoryFavorite.update(cf, name);

                textFieldFavor6.setVisible(false);
                sixthFavorite.setVisible(true);
                sixthFavorite.setText(cf.getName());
                
                nameCategory = textFieldFavor6.getText();
            }
        }
    }

    /**
     * При нажатии ESC появляется кнопка категории избранного
     *
     * @param key
     */
    @FXML
    private void returnButton(KeyEvent key) {
        switch (key.getCode()) {
            case ESCAPE:
                textFieldFavor1.setVisible(false);
                textFieldFavor2.setVisible(false);
                textFieldFavor3.setVisible(false);
                textFieldFavor4.setVisible(false);
                textFieldFavor5.setVisible(false);
                textFieldFavor6.setVisible(false);
                firstFavorite.setVisible(true);
                secondFavorite.setVisible(true);
                thirdFavorite.setVisible(true);
                fourthFavorite.setVisible(true);
                fifthFavorite.setVisible(true);
                sixthFavorite.setVisible(true);
                firstFavorite.setStyle("");
                secondFavorite.setStyle("");
                thirdFavorite.setStyle("");
                fourthFavorite.setStyle("");
                fifthFavorite.setStyle("");
                sixthFavorite.setStyle("");

        }
    }

    /**
     * Запись избранного товара к конкретной кнопке
     *
     * @param evt
     */
    @FXML
    private void addButtonFav(Event evt) {

        FavoriteByName favoriteByName = new FavoriteByName();

        Object o = evt.getSource();
        Button b = null;
        String buttonText = "";

        if (o instanceof Button) {
            b = (Button) o;
            buttonText = b.getText();
        }

        if ("".equals(goodNameLabel.getText())) {
            messageLabel.setText("Выберите товар!!!");
        } else {

            AddFavoriteButton addFavoriteButton = new AddFavoriteButton();
            Favorite fav = new Favorite();

            fav.setCategoryFavorite(cf);
            fav.setGoods(goods);
            fav.setButtonName(((Control) evt.getSource()).getId());

            // Проверка, есть ли название у кнопки
            if (!"".equals(buttonText)) {
                UpdateFavoriteButton updateFavoriteButton = new UpdateFavoriteButton();
                favoriteByName.setFav(cf, ((Control) evt.getSource()).getId());
                Favorite f = favoriteByName.displayResult();
                updateFavoriteButton.update(f, goods);
                buttonText = "";
            } else {
                addFavoriteButton.add(fav);
            }
        }
        //Обновление списка избранного товара для кнопок
        favList = allFavorList.listFavoriteCategory();
        listButtonName(nameCategory);
        listFavCateg(cf);
    }

    @FXML
    private void getGoodCode() {
        String code = codeTextField.getText();
        boolean checkGood = false;

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
                code = barcodeConv.sixLetter(code);
                weight = barcodeConv.fourLetter(code);
                if ("0.001".equals(weight)) {
                    weight = "1";
                }
                //residueGoodsTextField.setText(weight);
            }
            for (Goods gg1 : goodsList) {
                if (gg1.getCode().equals(code)) {
                    goods = gg1;
                }
            }
        }
        if (goods != null) {
            goodNameLabel.setText(goods.getName());
        }
        favButt_1.setDisable(false);
        favButt_2.setDisable(false);
        favButt_3.setDisable(false);
        favButt_4.setDisable(false);
        favButt_5.setDisable(false);
        favButt_6.setDisable(false);
        favButt_7.setDisable(false);
        favButt_8.setDisable(false);
        favButt_9.setDisable(false);
        favButt_10.setDisable(false);
        favButt_11.setDisable(false);
        favButt_12.setDisable(false);
        favButt_13.setDisable(false);
        favButt_14.setDisable(false);
        favButt_15.setDisable(false);
        favButt_16.setDisable(false);
        favButt_17.setDisable(false);
        favButt_18.setDisable(false);
        favButt_19.setDisable(false);
        favButt_20.setDisable(false);
        favButt_21.setDisable(false);
        favButt_22.setDisable(false);
        favButt_23.setDisable(false);
        favButt_24.setDisable(false);
        favButt_25.setDisable(false);

        chooseGoodLabel.setTextFill(Color.web("#000000"));
        chooseButtonLabel.setTextFill(Color.web("#FF0000"));
    }

    @FXML
    private void getGoodName() {
        String name = nameTextField.getText();
        boolean checkGood = false;

        //qg.runQueryCodeGoods(code);
        //goods = qg.displayResultCode();
        for (Goods gg1 : goodsList) {
            if (gg1.getName().equals(name)) {
                goods = gg1;
                checkGood = true;
            } else {
                checkGood = false;
            }
        }

        if (goods != null) {
            goodNameLabel.setText(goods.getName());
        }
        favButt_1.setDisable(false);
        favButt_2.setDisable(false);
        favButt_3.setDisable(false);
        favButt_4.setDisable(false);
        favButt_5.setDisable(false);
        favButt_6.setDisable(false);
        favButt_7.setDisable(false);
        favButt_8.setDisable(false);
        favButt_9.setDisable(false);
        favButt_10.setDisable(false);
        favButt_11.setDisable(false);
        favButt_12.setDisable(false);
        favButt_13.setDisable(false);
        favButt_14.setDisable(false);
        favButt_15.setDisable(false);
        favButt_16.setDisable(false);
        favButt_17.setDisable(false);
        favButt_18.setDisable(false);
        favButt_19.setDisable(false);
        favButt_20.setDisable(false);
        favButt_21.setDisable(false);
        favButt_22.setDisable(false);
        favButt_23.setDisable(false);
        favButt_24.setDisable(false);
        favButt_25.setDisable(false);

        chooseGoodLabel.setTextFill(Color.web("#000000"));
        chooseButtonLabel.setTextFill(Color.web("#FF0000"));
    }

    /**
     * Обнуляет текст на кнопках
     */
    private void favButtonWithOutText() {
        favButt_1.setText("");
        favButt_2.setText("");
        favButt_3.setText("");
        favButt_4.setText("");
        favButt_5.setText("");
        favButt_6.setText("");
        favButt_7.setText("");
        favButt_8.setText("");
        favButt_9.setText("");
        favButt_10.setText("");
        favButt_11.setText("");
        favButt_12.setText("");
        favButt_13.setText("");
        favButt_14.setText("");
        favButt_15.setText("");
        favButt_16.setText("");
        favButt_17.setText("");
        favButt_18.setText("");
        favButt_19.setText("");
        favButt_20.setText("");
        favButt_21.setText("");
        favButt_22.setText("");
        favButt_23.setText("");
        favButt_24.setText("");
        favButt_25.setText("");
    }

    /**
     * Возвращает название кнопок в зависимости от выбранной категории
     *
     * @param cf
     */
    private void listFavCateg(CategoryFavorite cf) {

        for (Favorite f : favList) {
            if (cf != null) {
                if (Objects.equals(f.getCategoryFavorite().getIdCategoryFavorite(), cf.getIdCategoryFavorite())) {
                    if (f.getButtonName().equals("favButt_1")) {

                        favButt_1.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_2")) {

                        favButt_2.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_3")) {

                        favButt_3.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_4")) {

                        favButt_4.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_5")) {

                        favButt_5.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_6")) {

                        favButt_6.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_7")) {

                        favButt_7.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_8")) {

                        favButt_8.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_9")) {

                        favButt_9.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_10")) {

                        favButt_10.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_11")) {

                        favButt_11.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_12")) {

                        favButt_12.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_13")) {

                        favButt_13.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_14")) {

                        favButt_14.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_15")) {

                        favButt_15.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_16")) {

                        favButt_16.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_17")) {

                        favButt_17.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_18")) {

                        favButt_18.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_19")) {

                        favButt_19.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_20")) {

                        favButt_20.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_21")) {

                        favButt_21.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_22")) {

                        favButt_22.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_23")) {

                        favButt_23.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_24")) {

                        favButt_24.setText(f.getGoods().getName());
                    }
                    if (f.getButtonName().equals("favButt_25")) {

                        favButt_25.setText(f.getGoods().getName());
                    }
                }
            }
        }
    }

    @FXML
    private String idButtonCategFav(Event evt) {

        idButtonCategFav = ((Control) evt.getSource()).getId();

        return idButtonCategFav;
    }

    /**
     * Список имен для кнопок
     *
     * @param name
     */
    private void listButtonName(String name) {
        cf = new CategoryFavorite();

        categoryByName.setName(name);
        cf = categoryByName.displayResult();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        textFieldFavor1.setVisible(false);
        textFieldFavor2.setVisible(false);
        textFieldFavor3.setVisible(false);
        textFieldFavor4.setVisible(false);
        textFieldFavor5.setVisible(false);
        textFieldFavor6.setVisible(false);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                chooseCatLabel.setTextFill(Color.web("#FF0000"));
                //Список всего товара
                goodsList = allGoodsList.listGoods();

                //Список избранного товара для кнопок
                favList = allFavorList.listFavoriteCategory();

                //Список товара по названию в поле nameGood
                String[] sName = new String[goodsList.size()];
                for (int i = 0; i < goodsList.size(); i++) {
                    sName[i] = (String) goodsList.get(i).getName();
                }
                TextFields.bindAutoCompletion(nameTextField, sName);

                //Список товара по коду в поле codeGood
                String[] sCode = new String[goodsList.size()];
                for (int i = 0; i < goodsList.size(); i++) {
                    sCode[i] = (String) goodsList.get(i).getCode();
                }
                TextFields.bindAutoCompletion(codeTextField, sCode);

                //Список категорий избранного
                catFavorList = favoriteList.listFavoriteCategory();

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

                //Редактирование названий категории при нажатии F3
                anchorPane.setOnKeyPressed(
                        event -> {
                            switch (event.getCode()) {
                                case F3:
                                    if ("firstFavorite".equals(idButtonCategFav)) {
                                        textFieldFavor1.setVisible(true);
                                        textFieldFavor1.requestFocus();
                                        firstFavorite.setVisible(false);
                                        idButtonCategFav = "";
                                    }
                                    if ("secondFavorite".equals(idButtonCategFav)) {
                                        textFieldFavor2.setVisible(true);
                                        textFieldFavor2.requestFocus();
                                        secondFavorite.setVisible(false);
                                        idButtonCategFav = "";
                                    }
                                    if ("thirdFavorite".equals(idButtonCategFav)) {
                                        textFieldFavor3.setVisible(true);
                                        textFieldFavor3.requestFocus();
                                        thirdFavorite.setVisible(false);
                                        idButtonCategFav = "";
                                    }
                                    if ("fourthFavorite".equals(idButtonCategFav)) {
                                        textFieldFavor4.setVisible(true);
                                        textFieldFavor4.requestFocus();
                                        fourthFavorite.setVisible(false);
                                        idButtonCategFav = "";
                                    }
                                    if ("fifthFavorite".equals(idButtonCategFav)) {
                                        textFieldFavor5.setVisible(true);
                                        textFieldFavor5.requestFocus();
                                        fifthFavorite.setVisible(false);
                                        idButtonCategFav = "";
                                    }
                                    if ("sixthFavorite".equals(idButtonCategFav)) {
                                        textFieldFavor6.setVisible(true);
                                        textFieldFavor6.requestFocus();
                                        sixthFavorite.setVisible(false);
                                        idButtonCategFav = "";
                                    }
                                    break;
                            }
                        });

            }
        });

        // TODO
    }

}
