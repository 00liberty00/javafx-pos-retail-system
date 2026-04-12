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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ml.exit.ExitApp;
import ml.ipmac.IpMac;
import ml.model.UserSwing;
import ml.modelLicense.Comp;
import ml.modelLicense.License;
import ml.modelLicense.User;
import ml.query.license.AddMac;
import ml.query.license.ChooseLicense;
import ml.query.license.NewUser;
import ml.query.license.UpdateLicense;
import ml.query.trial.ChangeCreatedTrial;
import ml.query.trial.TrialInfo;
import ml.query.user.AddUser;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * FXML Controller class Создание администратора
 *
 * @author dave
 */
public class CreateAdminController implements Initializable {

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
    private User userLicense = new User();
    private AddUser addAdmin = new AddUser();
    private boolean fail = false;
   // private ExitApp app = new ExitApp();
    private Comp comp = new Comp();
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
    private void handleOkAdmin() {

        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        if (pass.getText().equals(rePass.getText())) {
            us.setLogin(admin.getText());
            us.setAdmin(1);
            us.setPassword(encoder.encodePassword(pass.getText(), null));
            us.setPass(pass.getText());
            us.setActive(true);
            us.setName(name.getText());
            us.setPhone(phone.getText());
            us.setEmail(mail.getText());
            us.setLastLogin(date);

            userLicense.setEmail(mail.getText());
            userLicense.setName(name.getText());
            userLicense.setPhone(phone.getText());

            validate(us, validator);
            if (fail == false) {
//Написать сообщения об ошибках
                
                //Запись пользователя в локальную бд
                addAdmin.add(us);
                
                recordAboutUser();
                message.setText("Администратор создан");
                message.setTextFill(Color.rgb(21, 117, 84));

                //Закрывает окно
                Stage stage = (Stage) ok.getScene().getWindow();
                stage.close();
                ExitApp app = new ExitApp();
                app.close();
            }

        } else {
            message.setText("Пароли не совпадают");
            message.setTextFill(Color.rgb(210, 39, 30));
        }

    }

    @FXML
    private void handleCancelButton() {
        ExitApp app = new ExitApp();
        app.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Запись информации о пользователе в бд лицензии
     */
    private void recordAboutUser() {
        Date date = new Date();
        //Запись в бд-лицензии
        ChooseLicense chooseLicense = new ChooseLicense();
        UpdateLicense updateLicense = new UpdateLicense();
        License license = new License();
        NewUser newUser = new NewUser();
        AddMac addMac = new AddMac();
        IpMac ipMac = new IpMac();
        ipMac.getIp();
        //String macString = ipMac.getMac().replaceAll("-", "");
        String[] macAddressParts = ipMac.getMac().split("-");

        byte[] macAddressBytes = new byte[6];
        for (int i = 0; i < 6; i++) {
            Integer hex = Integer.parseInt(macAddressParts[i], 16);
            macAddressBytes[i] = hex.byteValue();
        }
        comp.setMac(macAddressBytes);
        comp.setBlocking(false);
        comp.setDateCreate(date);
        comp.setName(ipMac.getName());
        //Выбирает любой номер лицензии
        chooseLicense.get();
        license = chooseLicense.displayResult();
        comp.setLicense(license);
        //Запись MAC-адреса в БД
        addMac.add(comp);
        //Запись пользователя в БД-лицензии
        userLicense.setLicense(license);
        newUser.add(userLicense);
        //Обновление поля лицензии на использованный номер лицензии
        license.setCreated(true);
        updateLicense.update(license);

        //Добавление номера лицензии в таблицу Trial 
        TrialInfo trialInfo = new TrialInfo();
        ChangeCreatedTrial cct = new ChangeCreatedTrial();
        String numLicense = Long.toString(license.getLicense());
        trialInfo.getTrial().setLicense(numLicense);
        trialInfo.getTrial().setCreated(true);
        cct.update(trialInfo.getTrial());

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleOkUser(ActionEvent event) {
    }

}
