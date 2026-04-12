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
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ml.model.UserSwing;
import ml.query.user.AddUser;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * FXML Controller class Создание администратора
 *
 * @author dave
 */
public class CreateUserController implements Initializable {

    @FXML
    private PasswordField pass;
    @FXML
    private PasswordField rePass;
    @FXML
    private TextField admin;
    @FXML
    private TextField phone;
    @FXML
    private TextField mail;
    @FXML
    private TextField name;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;
    @FXML
    private Label message;

    private final ShaPasswordEncoder encoder = new ShaPasswordEncoder();
    private Stage dialogStage;
    private UserSwing us = new UserSwing();
    private AddUser addAdmin = new AddUser();
    private boolean fail = false;
    String failMessage;

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
            message.setText(failMessage);
            message.setTextFill(Color.rgb(210, 39, 30));
        }
    }

    /**
     * Создание данных администратора.
     */
    @FXML
    private void handleOkUser() {

        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        if (pass.getText().equals(rePass.getText())) {
            us.setLogin(admin.getText());
            us.setAdmin(3);
            us.setPassword(encoder.encodePassword(pass.getText(), null));
            us.setPass(pass.getText());
            us.setActive(true);
            us.setName(name.getText());
            us.setPhone(phone.getText());
            us.setEmail(mail.getText());
            us.setLastLogin(date);

            validate(us, validator);
            if (fail == false) {
//Написать сообщения об ошибках
                addAdmin.add(us);
                message.setText("Пользователь создан");
                message.setTextFill(Color.rgb(21, 117, 84));
                
                //Закрывает окно
                Stage stage = (Stage) ok.getScene().getWindow();
                stage.close();
            }

        } else {
            message.setText("Пароли не совпадают");
            message.setTextFill(Color.rgb(210, 39, 30));
        }

    }

    @FXML
    private void handleCancelButton() {
        //Закрывает окно
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
