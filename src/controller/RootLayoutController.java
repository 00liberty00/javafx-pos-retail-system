/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import ml.authentication.GrantedAuth;
import ml.exit.ExitApp;
import ml.json.JsonCategory;
import ml.json.JsonGoods;
import ml.kkt.KKT;
import ml.model.CategoryFavorite;
import ml.model.CategoryGoods;
import ml.model.Favorite;
import ml.model.Goods;
import ml.model.UserSwing;
import ml.modelLicense.Comp;
import ml.query.accounting.TruncateBackup;
import ml.query.accounting.TruncateGoodsAccounting;
import ml.query.categoryGood.CategoryGoodList;
import ml.query.compCard.CompMessage;
import ml.query.favorite.CategoryFavoriteList;
import ml.query.favorite.FavoriteList;
import ml.query.goods.QueryAllGoodsList;
import ml.query.user.IdUserByName;
import ml.telegram.Bot;
import ml.telegram.BotClient;
import ml.util.CheckConnection;
import ml.util.CheckInternetConnection;
import ml.util.NewThread;
import ml.window.AccountingByCategoryWindow;
import ml.window.AccountingWindow;
import ml.window.AllTelegramSettingsWindow;
import ml.window.ArrivalDishWindow;
import ml.window.ArrivalReportsWindow;
import ml.window.ArrivalWindow;
import ml.window.CancelReportsWindow;
import ml.window.CancelWindow;
import ml.window.CashInWindow;
import ml.window.CashOutWindow;
import ml.window.CategoryWindow;
import ml.window.CheckReportsWindow;
import ml.window.CheckWindow;
import ml.window.ClientsReportsWindow;
import ml.window.CreateFavorWindow;
import ml.window.CreateUserWindow;
import ml.window.DayReportsWindow;
import ml.window.DishesWindow;
import ml.window.EndShiftWindow;
import ml.window.GeneralsReportsWindow;
import ml.window.IndebtednessWindow;
import ml.window.LoyaltyCardWindow;
import ml.window.NewSessionWindow;
import ml.window.SendMessageClientWindow;
import ml.window.SettingsAppWindow;
import ml.window.SettingsPrintCheckWindow;
import ml.window.SettingsUserWindow;
import ml.window.TechMapWindow;
import ml.window.ViewAllGoodsWindow;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * FXML Controller class
 *
 * @author dave
 */
public class RootLayoutController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private Pane sellPane;
    @FXML
    private Pane moneyPane;
    @FXML
    private Pane ordersPane;
    @FXML
    private Pane goodsPane;
    @FXML
    private Pane reportsPane;
    @FXML
    private Pane connectPane;
    @FXML
    private Pane endShiftPane;
    @FXML
    private Pane settingsPane;
    @FXML
    private Pane exitPane;
    @FXML
    private Button sellMenu;
    @FXML
    private Button moneyMenu;
    @FXML
    private Button ordersMenu;
    @FXML
    private Button goodsMenu;
    @FXML
    private Button reportsMenu;
    @FXML
    private Button connectMenu;
    @FXML
    private Button endShiftMenu;
    @FXML
    private Button settingsMenu;
    @FXML
    private Button exitMenu;
    @FXML
    private Button arrivalDishButton;
    @FXML
    private Button cashIn;
    @FXML
    private Button cashOut;
    @FXML
    private Button indebtedness;
    @FXML
    private Button dayReports;
    @FXML
    private Button generalsReports;
    @FXML
    private Button checkReports;
    @FXML
    private Button arrivalReports;
    @FXML
    private Button cancelReports;
    @FXML
    private Button endShift;
    @FXML
    private Button newSess;
    @FXML
    private Button exit;
    @FXML
    private VBox vBox;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button viewGoods;
    @FXML
    private Button accounting;
    @FXML
    private Button techMap;
    @FXML
    private Label message;
    @FXML
    private MenuItem deleteAccAndBackUpMenu;
    @FXML
    private MenuItem accountingByCategoryMenu;
    @FXML
    private Button xReports;
    @FXML
    private Button zReports;
    @FXML
    private Button clientsReports;
    @FXML
    private Button categoryGoods;
    @FXML
    private Button telegramSettings;
    @FXML
    private Button sendMessageClient;
    @FXML
    private Button dishes;

    private ExitApp app = new ExitApp();
    private GrantedAuth grantedAuth = new GrantedAuth();
    private Object auth = grantedAuth.role();
    private Timeline timeline = new Timeline();
    private CheckConnection checkConnection = new CheckConnection();
    private QueryAllGoodsList allGoodsList = new QueryAllGoodsList();

    private CompMessage compMessage = new CompMessage();
    private Comp comp = new Comp();
    private KKT kkt;
    private Bot bot;
    private BotClient botClient;
    boolean firstState = false;
    boolean lastState = false;
    private JsonGoods jg = new JsonGoods();
    private JsonCategory jc = new JsonCategory();

    private List<Goods> goodsList;
    private CategoryGoods category = new CategoryGoods();
    private List<CategoryGoods> categoryList;
    private CategoryGoodList categoryGoodList = new CategoryGoodList();
    private List<CategoryFavorite> catFavorList;
    private CategoryFavoriteList favoriteList;
    private List<Favorite> favList;
    private FavoriteList allFavorList;

    private static final Logger log = Logger.getLogger(RootLayoutController.class);

    private UserSwing userSwing = new UserSwing();

    /**
     * Открывает меню движения товара.
     *
     * @param event
     */
    @FXML
    private void okSellMenuClicked(ActionEvent event) {

        sellPane.setVisible(true);
        moneyPane.setVisible(false);
        ordersPane.setVisible(false);
        goodsPane.setVisible(false);
        reportsPane.setVisible(false);
        //Pane.setVisible(false);
        connectPane.setVisible(false);
        endShiftPane.setVisible(false);
        settingsPane.setVisible(false);
        exitPane.setVisible(false);

        log.info("Открывает меню движения товара.");

    }

    /**
     * Открывает меню движения наличных денег.
     *
     * @param event
     */
    @FXML
    private void okMoneyMenuClicked(ActionEvent event) {

        sellPane.setVisible(false);
        moneyPane.setVisible(true);
        ordersPane.setVisible(false);
        goodsPane.setVisible(false);
        reportsPane.setVisible(false);
        //Pane.setVisible(false);
        connectPane.setVisible(false);
        endShiftPane.setVisible(false);
        settingsPane.setVisible(false);
        exitPane.setVisible(false);

        log.info("Открывает меню движения наличных денег");

    }

    /**
     * Открывает меню продуктов.
     *
     * @param event
     */
    @FXML
    private void okGoodsMenuClicked(ActionEvent event) {

        sellPane.setVisible(false);
        moneyPane.setVisible(false);
        ordersPane.setVisible(false);
        goodsPane.setVisible(true);
        reportsPane.setVisible(false);
        //Pane.setVisible(false);
        connectPane.setVisible(false);
        endShiftPane.setVisible(false);
        settingsPane.setVisible(false);
        exitPane.setVisible(false);

        log.info("Открывает меню продуктов.");

    }

    /**
     * Открывает меню заказов.
     *
     * @param event
     */
    @FXML
    private void okOrdersMenuClicked(ActionEvent event) {

        sellPane.setVisible(false);
        moneyPane.setVisible(false);
        ordersPane.setVisible(true);
        goodsPane.setVisible(false);
        reportsPane.setVisible(false);
        //Pane.setVisible(false);
        connectPane.setVisible(false);
        endShiftPane.setVisible(false);
        settingsPane.setVisible(false);
        exitPane.setVisible(false);

        log.info("Открывает меню заказов.");

    }

    /**
     * Открывает меню отчетов.
     *
     * @param event
     */
    @FXML
    private void okReportsMenuClicked(ActionEvent event) {

        sellPane.setVisible(false);
        moneyPane.setVisible(false);
        ordersPane.setVisible(false);
        goodsPane.setVisible(false);
        reportsPane.setVisible(true);
        //Pane.setVisible(false);
        connectPane.setVisible(false);
        endShiftPane.setVisible(false);
        settingsPane.setVisible(false);
        exitPane.setVisible(false);

        log.info("Открывает меню отчетов.");

    }

    /**
     * Открывает меню связи с клиентами
     *
     * @param event
     */
    @FXML
    private void okConnectMenuClicked(ActionEvent event) {
        sellPane.setVisible(false);
        moneyPane.setVisible(false);
        ordersPane.setVisible(false);
        goodsPane.setVisible(false);
        reportsPane.setVisible(false);
        //Pane.setVisible(false);
        connectPane.setVisible(true);
        endShiftPane.setVisible(false);
        settingsPane.setVisible(false);
        exitPane.setVisible(false);
    }

    /**
     * Открывает меню конец смены.
     *
     * @param event
     */
    @FXML
    private void okEndShiftMenuClicked(ActionEvent event) {

        sellPane.setVisible(false);
        moneyPane.setVisible(false);
        ordersPane.setVisible(false);
        goodsPane.setVisible(false);
        reportsPane.setVisible(false);
        //Pane.setVisible(false);
        connectPane.setVisible(false);
        endShiftPane.setVisible(true);
        settingsPane.setVisible(false);
        exitPane.setVisible(false);

        log.info("Открывает меню конец смены.");

    }

    /**
     * Открывает меню настроек.
     *
     * @param event
     */
    @FXML
    private void okSettingsMenuClicked(ActionEvent event) {

        sellPane.setVisible(false);
        moneyPane.setVisible(false);
        ordersPane.setVisible(false);
        goodsPane.setVisible(false);
        reportsPane.setVisible(false);
        //Pane.setVisible(false);
        connectPane.setVisible(false);
        endShiftPane.setVisible(false);
        settingsPane.setVisible(true);
        exitPane.setVisible(false);

        log.info("Открывает меню настроек.");

    }

    /**
     * Открывает меню выход/новая сессия.
     *
     * @param event
     */
    @FXML
    private void okExitMenuClicked(ActionEvent event) {
        sellPane.setVisible(false);
        moneyPane.setVisible(false);
        ordersPane.setVisible(false);
        goodsPane.setVisible(false);
        reportsPane.setVisible(false);
        //Pane.setVisible(false);
        connectPane.setVisible(false);
        endShiftPane.setVisible(false);
        settingsPane.setVisible(false);
        exitPane.setVisible(true);
        log.info("Открывает меню выход/новая сессия.");

    }

    /**
     * Продажа товара.
     *
     * @param event
     */
    @FXML
    private void okSellClicked(ActionEvent event) {

        log.info("Нажата кнопка /Продажа товара/");
        // bot.checkButtonBot("");
        new CheckWindow(comp, kkt, bot, botClient, goodsList, categoryList, userSwing, catFavorList, favList);
    }

    /**
     * Приход товара.
     *
     * @param event
     */
    @FXML

    private void arrivalClicked(ActionEvent event) {

        log.info("Нажата кнопка /Приход товара/");

        new ArrivalWindow(kkt, bot);
    }

    /**
     * Приход продуктов для приготовления.
     *
     * @param event
     */
    @FXML
    private void arrivalDishClicked(ActionEvent event) {

        log.info("Нажата кнопка /Приход продуктов/");

        new ArrivalDishWindow();
    }

    /**
     * Списание товара.
     */
    @FXML
    private void cancelClicked(ActionEvent event) {

        log.info("Нажата кнопка /Списание товара/");

        new CancelWindow(bot);
    }

    /**
     * Ввод денежных средств.
     */
    @FXML
    private void cashInClicked(ActionEvent event) {

        log.info("Нажата кнопка /Ввод денежных средств/");

//        new CashInWindow();
//        new CashInWindow(kkt);
        new CashInWindow(kkt, bot);

    }

    /**
     * Вывод денежных средств.
     */
    @FXML
    private void cashOutClicked(ActionEvent event) {

        log.info("Нажата кнопка /Вывод денежных средств/");

//        new CashOutWindow();
        new CashOutWindow(kkt, bot);
    }

    /**
     * Просмотр задолжности.
     */
    @FXML
    private void indebtednessClicked(ActionEvent event) {

        log.info("Нажата кнопка /Просмотр задолжности/");

//        new IndebtednessWindow();
        new IndebtednessWindow(kkt, bot);

    }

    /**
     * Просмотр товара - общий список.
     */
    @FXML
    private void viewGoodsClicked(ActionEvent event) {

        if ("ROLE_ADMIN".equals(auth.toString())) {
            log.info("Нажата кнопка /Просмотр товара - общий список/");

            new ViewAllGoodsWindow();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Сообщение:");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Требуются права администратора!");
            alert.showAndWait();
            log.info("/Просмотр товара - общий список/ - Ошибка /Требуются права администратора/");

        }
    }

    /**
     * Учет товара
     *
     * @param event
     */
    @FXML
    private void accountingClicked(ActionEvent event) {

        if ("ROLE_ADMIN".equals(auth.toString())) {
            log.info("Нажата кнопка /Учет товара/");

            new AccountingWindow();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Сообщение:");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Требуются права администратора!");
            alert.showAndWait();

            log.info("/Учет товара/ - Ошибка /Требуются права администратора/");

        }
    }

    /**
     * Технологическая карта.
     */
    @FXML
    private void techmapClicked(ActionEvent event) {

        if ("ROLE_ADMIN".equals(auth.toString())) {
            log.info("Нажата кнопка /Технологическая карта/");

            new TechMapWindow();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Сообщение:");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Требуются права администратора!");
            alert.showAndWait();
            log.info("/Технологическая карта/ - Ошибка /Требуются права администратора/");

        }
    }

    /**
     * Список блюд.
     */
    @FXML
    private void dishesClicked(ActionEvent event) {

        if ("ROLE_ADMIN".equals(auth.toString())) {
            log.info("Нажата кнопка /Блюда/");

            new DishesWindow();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Сообщение:");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Требуются права администратора!");
            alert.showAndWait();
            log.info("/Блюда/ - Ошибка /Требуются права администратора/");

        }
    }

    /**
     * Дневные отчеты.
     *
     * @param event
     */
    @FXML
    private void dayReportsClicked(ActionEvent event) {

        log.info("Нажата кнопка /Дневные отчеты/");

        new DayReportsWindow();
    }

    /**
     * Общие отчеты.
     *
     * @param event
     */
    @FXML
    private void generalsReportsClicked(ActionEvent event) {

        if ("ROLE_ADMIN".equals(auth.toString())) {
            log.info("Нажата кнопка /Общие отчеты/");

            new GeneralsReportsWindow();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Сообщение:");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Требуются права администратора!");
            alert.showAndWait();
            log.info("/Общие отчеты/ - Ошибка /Требуются права администратора/");

        }
    }

    /**
     * Отчеты продаж.
     *
     * @param event
     */
    @FXML
    private void checkReportsClicked(ActionEvent event) {

        log.info("Нажата кнопка /Отчеты продаж/");
        bot.checkButtonBot("Отчеты продаж");

        new CheckReportsWindow();
    }

    /**
     * Отчеты прихода.
     *
     * @param event
     */
    @FXML
    private void arrivalReportsClicked(ActionEvent event) {

        log.info("Нажата кнопка /Отчеты прихода/");
        bot.checkButtonBot("Отчеты прихода");

        new ArrivalReportsWindow();
    }

    /**
     * Отчеты списания.
     *
     * @param event
     */
    @FXML
    private void cancelReportsClicked(ActionEvent event) {

        log.info("Нажата кнопка /Отчеты списания/");

        new CancelReportsWindow();
    }

    /**
     * Х-Отчет
     *
     * @param event
     */
    @FXML
    private void xReportsClicked(ActionEvent event) {
        log.info("Нажата кнопка /Х-Отчет/");
        bot.checkButtonBot("X-Отчет");

        kkt.xReport();
        //kkt.destroyDriver();

    }

    /**
     * Z-Отчет
     *
     * @param event
     */
    @FXML
    private void zReportsClicked(ActionEvent event) {
        log.info("Нажата кнопка /Z-Отчет/");
        bot.checkButtonBot("Z-Отчет");
        kkt.zReport();
        //kkt.destroyDriver();
    }

    /**
     * Отчеты по клиентам
     */
    @FXML
    private void clientsReportsClicked(ActionEvent event) {
        log.info("Нажата кнопка /Отчеты по клиентам/");

        new ClientsReportsWindow();

    }

    /**
     * Конец смены.
     *
     * @param event
     */
    @FXML
    private void endShiftClicked(ActionEvent event) {
        log.info("Нажата кнопка /Конец смены/");

//        new EndShiftWindow();
        new EndShiftWindow(kkt, bot);

    }

    /**
     * Новый пользователь.
     *
     * @param event
     */
    @FXML
    private void newUserClicked(ActionEvent event) {

        if ("ROLE_ADMIN".equals(auth.toString())) {
            log.info("Нажата кнопка /Новый пользователь/");

            new CreateUserWindow();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Сообщение:");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Требуются права администратора!");
            alert.showAndWait();
            log.info("/Новый пользователь/ - Ошибка /Требуются права администратора/");

        }
    }

    /**
     * Настройки пользователя
     *
     * @param event
     */
    @FXML
    private void settingsUser(ActionEvent event) {
        log.info("Нажата кнопка /Настройки пользователя/");

        new SettingsUserWindow();
    }

    /**
     * Создание избранного.
     *
     * @param event
     */
    @FXML
    private void createLikedClicked(ActionEvent event) {

        log.info("Нажата кнопка /Создание избранного/");

        new CreateFavorWindow();
    }

    /**
     * Настройки печати чека.
     *
     * @param event
     */
    @FXML
    private void settingsPrintCheckClicked(ActionEvent event) {

        if ("ROLE_ADMIN".equals(auth.toString())) {
            log.info("Нажата кнопка /Настройки печати чека/");

            new SettingsPrintCheckWindow();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Сообщение:");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Требуются права администратора!");
            alert.showAndWait();
            log.info("/Настройки печати чека/ - Ошибка /Требуются права администратора/");

        }
    }

    /**
     * Настройки приложения.
     *
     * @param event
     */
    @FXML
    private void settingsAppClicked(ActionEvent event) {

        if ("ROLE_ADMIN".equals(auth.toString())) {
            log.info("Нажата кнопка /Настройки приложения/");

            new SettingsAppWindow();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Сообщение:");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Требуются права администратора!");
            alert.showAndWait();
            log.info("/Настройки приложения/ - Ошибка /Требуются права администратора/");

        }
    }

    /**
     * Создание карты лояльности.
     *
     * @param event
     */
    @FXML
    private void loyaltyCardClicked(ActionEvent event) {

        new LoyaltyCardWindow();
        log.info("Нажата кнопка /Создание карты лояльности/");

        /*if ("ROLE_ADMIN".equals(auth.toString())) {
         log.info("Нажата кнопка /Создание карты лояльности/");
         
         new LoyaltyCardWindow();
         } else {
         Alert alert = new Alert(AlertType.INFORMATION);
         alert.setTitle("Сообщение:");
         alert.setHeaderText("Внимание!");
         alert.setContentText("Требуются права администратора!");
         alert.showAndWait();
         log.info("/Создание карты лояльности/ - Ошибка /Требуются права администратора/");
         
         }*/
    }

    /**
     * Редактирование категории товара.
     *
     * @param event
     */
    @FXML
    private void categoryGoodsClicked(ActionEvent event) {

        new CategoryWindow();
        log.info("Нажата кнопка /Создание карты лояльности/");

    }

    /**
     * Настройки telegram.
     *
     * @param event
     */
    @FXML
    private void telegramSettingsClicked(ActionEvent event) {

        if ("ROLE_ADMIN".equals(auth.toString())) {
            new AllTelegramSettingsWindow();
            log.info("Нажата кнопка /Настройки TELEGRAM/");
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Сообщение:");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Требуются права администратора!");
            alert.showAndWait();
            log.info("/Настройки TELEGRAM/ - Ошибка /Требуются права администратора/");

        }
    }
    
    
    /**
     * Отправить сообщение клиенту telegram.
     *
     * @param event
     */
    @FXML
    private void sendMessageClientClicked(ActionEvent event) {

        if ("ROLE_ADMIN".equals(auth.toString())) {
            new SendMessageClientWindow(comp);
            log.info("Нажата кнопка /Отправить сообщение клиенту/");
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Сообщение:");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Требуются права администратора!");
            alert.showAndWait();
            log.info("/Отправить сообщение клиенту/ - Ошибка /Требуются права администратора/");

        }
    }

    /**
     * Новая сессия.
     *
     * @param event
     */
    @FXML
    private void newSessClicked(ActionEvent event) {

        log.info("Нажата кнопка /Новая сессия/");

        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
        new NewSessionWindow();
    }

    /**
     * Выход из приложения.
     *
     * @param event
     */
    @FXML
    private void exitClicked(ActionEvent event) {

        log.info("Нажата кнопка /Выход из приложения/");

        closeApp();
    }

    @FXML
    private void handleClose() {
        closeApp();

    }

    /**
     * Контекстное меню Удаление предыдущего учета и бекапа
     *
     * @param event
     */
    @FXML
    private void deleteAccAndBackUp(ActionEvent event) {

        if ("ROLE_ADMIN".equals(auth.toString())) {
            TruncateGoodsAccounting ta = new TruncateGoodsAccounting();
            TruncateBackup tb = new TruncateBackup();
            ta.truncateAccounting();
            tb.truncateBackup();

            log.info("Нажата кнопка /Удаление предыдущего учета и бекапа/");
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Сообщение:");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Требуются права администратора!");
            alert.showAndWait();
            log.info("/Удаление предыдущего учета и бекапа/ - Ошибка /Требуются права администратора/");

        }

    }

    /**
     * Контекстное меню Учет по категории
     *
     * @param event
     */
    @FXML
    private void accountingByCategory(ActionEvent event) {

        if ("ROLE_ADMIN".equals(auth.toString())) {

            new AccountingByCategoryWindow();

            log.info("Нажата кнопка /Учет по категории/");
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Сообщение:");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Требуются права администратора!");
            alert.showAndWait();
            log.info("/Учет по категории/ - Ошибка /Требуются права администратора/");

        }

    }

    //Подтверждение закрытия окна
    private void closeApp() {
        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Вы уверены, что хотите выйти?"
        );
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.OK
        );
        Button cancelButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.CANCEL
        );
        exitButton.setText("Да");
        cancelButton.setText("Нет");
        closeConfirmation.setHeaderText("Выход из программы");

        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeResponse.get())) {
        } else {
            kkt.destroyDriver();
            app.close();

        }
    }

    public void setCompCard(Comp comp, KKT kkt, Bot b) {
        this.comp = comp;
        this.kkt = kkt;
        this.bot = b;
        System.out.println("");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        CheckInternetConnection connection = new CheckInternetConnection();

        sellPane.setVisible(false);
        moneyPane.setVisible(false);
        ordersPane.setVisible(false);
        goodsPane.setVisible(false);
        reportsPane.setVisible(false);
        //Pane.setVisible(false);
        connectPane.setVisible(false);
        endShiftPane.setVisible(false);
        settingsPane.setVisible(false);
        exitPane.setVisible(false);

//        long m = System.currentTimeMillis();
        jg.inJsonFile(allGoodsList.listGoods());

        jg.getJsonFile();

        jc.inJsonFile(categoryGoodList.getList());
        jc.getJsonFile();

        goodsList = jg.displayResultAllGoods();
        categoryList = jc.displayResultAllCategory();
        favoriteList = new CategoryFavoriteList();
        catFavorList = favoriteList.listFavoriteCategory();
        //Список избранного товара для кнопок
        allFavorList = new FavoriteList();
        favList = allFavorList.listFavoriteCategory();
        getUserSwing();

//        System.out.println("Время создания файла: " + (double) (System.currentTimeMillis() - m));
        /*TestTrial testTrial = new TestTrial();
        // Created c = new Created();
        AddUserLicense addUserLicense = new AddUserLicense();
        License license = testTrial.license();*/
        //Сообщение
        timeline = new Timeline(new KeyFrame(Duration.seconds(30), ev -> {

            //compMessage.setComp(comp);
            //message.setText(compMessage.displayResult().getMessage());
            //Проверка связи с бд
            if ("true".equals(connection.call())) {

                firstState = true;

            } else {
                //checkConnection.closeConnection();
                firstState = false;
                lastState = true;
            }

            if ((firstState == true) && (lastState == true)) {
                firstState = false;
                lastState = false;
                checkConnection.restartConnection();
            }

        }));

        timeline.setCycleCount(1000);
        timeline.play();

        sellMenu.setStyle("-fx-background-color: #a0a0a0; ");
        moneyMenu.setStyle("-fx-background-color: #a0a0a0; ");
//        ordersMenu.setStyle("-fx-background-color: #a0a0a0; ");;
        goodsMenu.setStyle("-fx-background-color: #a0a0a0; ");
        reportsMenu.setStyle("-fx-background-color: #a0a0a0; ");
        connectMenu.setStyle("-fx-background-color: #a0a0a0; ");
        endShiftMenu.setStyle("-fx-background-color: #a0a0a0; ");
        settingsMenu.setStyle("-fx-background-color: #a0a0a0; ");
        exitMenu.setStyle("-fx-background-color: #a0a0a0; ");

        //allGoodsList.listGoods();
        //executor.shutdownNow();
        new NewThread("Do it!").start();

        //Запуск бота для клиентов
        botClient = new BotClient();

//        new ThreadSearchCheckDuplicat("Do it!").start();
    }

    private UserSwing getUserSwing() {
        IdUserByName idUserByName = new IdUserByName();
        Authentication authentication = SecurityContextHolder.getContext().
                getAuthentication();
        String login = authentication.getName();
        idUserByName.setLoginUser(login);   //метод для idUser по логину

        this.userSwing = idUserByName.displayResult(); //Возвращает пользователя
        return userSwing;
    }

}
