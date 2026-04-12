/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ml.mail.NewMail;
import ml.model.Discount;
import ml.model.LoyaltyCard;
import ml.query.discount.AddDiscount;
import ml.query.discount.LastDiscount;
import ml.wallet.ResourceDefinitions;
import ml.wallet.Start;
import org.apache.commons.lang3.ArrayUtils;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class LoyaltyCardController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button okButton;
    @FXML
    private TextField idCardTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField percentTextField;
    @FXML
    private TextField noteTextField;
    @FXML
    private Text messageText;
    @FXML
    private CheckBox createLoyaltyCardCheckBox;
    @FXML
    private DatePicker datePicker;

    private Discount discount = new Discount();
    private LoyaltyCard card = new LoyaltyCard();
    private boolean fail = false;
    private String failMessage;
    private String clientText = "";
    private Stage dialogStage;
    private ResourceDefinitions resourceDefinitions = ResourceDefinitions.getInstance();
    private boolean okClicked = false;

    ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
    Validator validator = vf.getValidator();

    /**
     * Валидация полей.
     */
    private void validate(Object object, Validator validator) {
        Set<ConstraintViolation<Object>> constraintViolations = validator
                .validate(object);

        System.out.println(object);
        System.out.println(String.format("Кол-во ошибок: %d",
                constraintViolations.size()));

        for (ConstraintViolation<Object> cv : constraintViolations) {
            System.out.println(String.format(
                    "Внимание, ошибка! property: [%s], value: [%s], message: [%s]",
                    cv.getPropertyPath(), cv.getInvalidValue(), cv.getMessage()));
            failMessage = cv.getMessage();
        }
        if (constraintViolations.size() == 0) {
            fail = false;
        } else {
            fail = true;
            messageText.setText(failMessage);
//            messageText.setTextFill(Color.rgb(210, 39, 30));
        }
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
//                dialogStage.setAlwaysOnTop(true);

                Discount d = new Discount();
                LastDiscount lastDiscount = new LastDiscount();

                lastDiscount.lastDiscount();
                d = lastDiscount.displayResult();
                if (d == null) {
                    idCardTextField.setText("10000001");
                } else {
                    idCardTextField.setText((new BigDecimal(d.getNumcard()).add(new BigDecimal(1))).toString());
                }

                datePicker.setValue(LocalDate.now());

                phoneTextField.setText(clientText);
            }
        });

    }

    @FXML
    private boolean getOk(ActionEvent event) {

        Date date = new Date();
        LocalDate ld = datePicker.getValue();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date d = Date.from(ld.atStartOfDay(defaultZoneId).toInstant());

        discount.setName(firstNameTextField.getText());
        discount.setNumcard(idCardTextField.getText());

        discount.setLastname(lastNameTextField.getText());
        discount.setPhone(phoneTextField.getText());
        discount.setEmail(emailTextField.getText());
        discount.setPercent(percentTextField.getText());
        discount.setNote(noteTextField.getText());

        discount.setDateRegister(date);
        discount.setDateUsed(d);
        discount.setSumChecks(BigDecimal.ZERO);
        validate(discount, validator);

        card.setIdCard(idCardTextField.getText());
        card.setName(firstNameTextField.getText());
        card.setPercent(percentTextField.getText());

        if (fail == false) {
            AddDiscount addDiscount = new AddDiscount();

            //Google Pay Card
            if (createLoyaltyCardCheckBox.isSelected()) {

                resourceDefinitions.setLoyaltyCard(card);

                Start s = new Start();
                s.main();

                String lc = s.getLinkOnLoyaltyCard();
                discount.setClassId(s.getClassId());
                discount.setObjectId(s.getObjectId());
                String messageEmail = "<p>Здравствуйте, " + firstNameTextField.getText() + " на Вашу почту выслана ссылка на карту лояльности!<br>"
                        + "<p>Пожалуйста, добавьте ее в свой кошелек<br>\n"
                        + "<a href=\"" + lc + "\">Ссылка на карту лояльности</a>";
                String email = emailTextField.getText();
                NewMail mail = new NewMail();
                mail.sendLoyaltyCard(email, messageEmail);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Информация");
                alert.setHeaderText("ВНИМАНИЕ");
                alert.setContentText("Карта создана и отправлена клиенту!");

                alert.showAndWait();

            }

            addDiscount.add(discount);

            okClicked = true;

            closeWindow();
        }
        return okClicked;
    }

    /**
     * Закрывает окно
     */
    private boolean closeWindow() {
//        okClicked = false;

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
        return okClicked;
    }

    public void setController(Stage dialogStage, String clientText) {
        this.dialogStage = dialogStage;
        this.clientText = clientText;
    }

    public Discount getDiscount() {
        return this.discount;
    }

    public boolean displayOkClicked() {

        return okClicked;
    }
}
