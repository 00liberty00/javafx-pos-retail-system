/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.kkt;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ml.dialog.DialogAlert;
import ml.model.Check;
import ml.model.CheckList;
import ml.model.Discount;
import ml.model.UserSwing;
import ml.xml.XMLSettings;
import org.apache.log4j.Logger;
import ru.atol.drivers10.fptr.Fptr;
import ru.atol.drivers10.fptr.IFptr;

/**
 * Работа с ККТ
 *
 * @author Dave
 */
public class KKT {

    private XMLSettings xmls = new XMLSettings();
    private String modelName = "";
    private KKTPrintNonFiscalCheck nonFiscalCheck = new KKTPrintNonFiscalCheck();
    private KKTPrintFiscalCheck fiscalCheck = new KKTPrintFiscalCheck();
    private List<Check> checkArrayList = new ArrayList<Check>();
    private List<CheckList> checkListArrayList = new ArrayList<CheckList>();
    private Discount discount = new Discount();
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final Date date = new Date();
    private final String nowDate = dateFormat.format(date);
    //Инициализация драйвера
    private final IFptr fptr = new Fptr();
    private static final Logger log = Logger.getLogger(KKT.class);

    /**
     * Конструктор для работы с рабочим сом-портом
     */
    public KKT() {
        String defaultValue = xmls.getPrinter();
        int i0 = defaultValue.compareTo("Без печати");
        int i1 = defaultValue.compareTo("ККТ (WI-FI)");
        int i2 = defaultValue.compareTo("ККТ (USB)");
        int i3 = defaultValue.compareTo("Принтер чеков");
        if (i0 == 0) {
//            comboPrint.getSelectionModel().select("Без печати");
        }
        if (i1 == 0) {
//            comboPrint.getSelectionModel().select("ККТ (WI-FI)");
            getIpAddressPortKKT();
        }
        if (i2 == 0) {
            getCOMPortKKT();
        }
        if (i3 == 0) {
//            comboPrint.getSelectionModel().select("Принтер чеков");
        }

    }

    /**
     * Конструктор для ввода нового сом порта
     *
     * @param comPort
     */
    public KKT(String comPort) {
        String numberCom = comPort.replaceAll("\\D+", "");
        addSettingsComPort(numberCom);

    }

    /**
     * Конструктор для ввода нового IP адреса и порта
     *
     * @param comPort
     */
    public KKT(String ipAddress, String port) {
        //String numberCom = comPort.replaceAll("\\D+", "");
        addSettingsIpAddressPort(ipAddress, port);

    }

    /**
     * Возвращает название ККТ
     *
     * @return
     */
    public String getNameModel() {
        //Запрос информации о ККТ
        fptr.setParam(IFptr.LIBFPTR_PARAM_DATA_TYPE, IFptr.LIBFPTR_DT_STATUS);
        fptr.queryData();
        modelName = fptr.getParamString(IFptr.LIBFPTR_PARAM_MODEL_NAME);
        return modelName;
    }

    /**
     * Печать фискального чека
     *
     * @param checkListArrayList
     * @param checkArrayList
     * @param discount
     * @param sumChecktText
     * @param discountText
     * @param cash
     * @param shortChange
     */
    public void printFiscalCheck(List<CheckList> checkListArrayList, List<Check> checkArrayList,
            Discount discount, BigDecimal sumChecktText, BigDecimal discountText,
            BigDecimal cash, BigDecimal shortChange, boolean checkBank, boolean checkPrint, String phoneOrEmail, UserSwing us) {

        // if (checkConnectKKT() == true) {
        // printLogo();
        fiscalCheck.setPrintCheck(fptr, checkListArrayList, checkArrayList,
                discount, sumChecktText, discountText, cash, shortChange, checkBank, checkPrint, phoneOrEmail, us);

//        } else {
//            infoStatusChange();
//
//        }
    }

    /**
     * Начало нефискального чека
     */
    public void startNonFiscalCheck() {
        fptr.beginNonfiscalDocument();
    }

    /**
     * Печать нефискального чека
     */
    public void printNonFiscalCheck(List<CheckList> checkListArrayList, List<Check> checkArrayList,
            Discount discount, BigDecimal sumChecktText, BigDecimal discountText,
            BigDecimal cash, BigDecimal shortChange) {
        log.info("Печать нефискального чека ККТ");

//        printLogo();
        nonFiscalCheck.setPrintCheck(fptr, checkListArrayList, checkArrayList, discount,
                sumChecktText, discountText, cash, shortChange);

    }

    /**
     * Конец нефискального чека
     */
    public void endNonFiscalCheck() {

        fptr.endNonfiscalDocument();
    }

    /**
     * Деинициализация драйвера
     */
    public void destroyDriver() {
        if (checkConnectKKT() == true) {

            log.info("Деинициализация драйвера ККТ");

            fptr.close();
            //Деинициализация драйвера
            fptr.destroy();
        }
    }

    /**
     * Внесение средств
     *
     * @param cash
     */
    public void cashIn(String cash) {
        if (checkConnectKKT() == true) {

            fptr.setParam(IFptr.LIBFPTR_PARAM_SUM, cash);
            fptr.cashIncome();
        }
    }

    /**
     * Вывод средств
     *
     * @param cash
     */
    public void cashOut(String cash) {
        if (checkConnectKKT() == true) {

            fptr.setParam(IFptr.LIBFPTR_PARAM_SUM, cash);
            fptr.cashOutcome();
        }
    }

    /**
     * X Отчет
     */
    public void xReport() {
        if (checkConnectKKT() == true) {

            log.info("X-отчет ККТ");
            String version = fptr.version();
            System.out.println("Версия драйвера: " + version);
            fptr.setParam(IFptr.LIBFPTR_PARAM_REPORT_TYPE, IFptr.LIBFPTR_RT_X);
            fptr.report();
        }

    }

    /**
     * Z Отчет (Закрытие смены)
     */
    public void zReport() {

        if (checkConnectKKT() == true) {

            log.info("Z-отчет ККТ");

            //  fptr.setParam(1021, "Кассир Иванов И.");
            //  fptr.setParam(1203, "123456789047");
            fptr.operatorLogin();
//            fptr.setParam(IFptr.LIBFPTR_PARAM_REPORT_TYPE, IFptr.LIBFPTR_RT_X);
            fptr.setParam(IFptr.LIBFPTR_PARAM_REPORT_TYPE, IFptr.LIBFPTR_RT_CLOSE_SHIFT);
            fptr.report();

            fptr.checkDocumentClosed();
        }
    }

    /**
     * Проверка соединения с ККТ
     *
     * @return
     */
    public boolean checkConnectKKT() {

        String defaultValue = xmls.getPrinter();
        boolean b = false;
        //Проверка, каким принтером печатать чек
        int i1 = defaultValue.compareTo("ККТ (USB)");
        int i2 = defaultValue.compareTo("ККТ (WI-FI)");
        if (i1 == 0) {

            if (fptr.printText() < 0) {
                System.out.println(String.format("%d [%s]", fptr.errorCode(), fptr.errorDescription()));
//                log.error("Соединение с ККТ не установлено");
//                System.out.println("Соединение с ККТ не установлено");
                DialogAlert alert = new DialogAlert();
                alert.alert("Внимание", "", "Соединение с ККТ разорвано");
                b = false;
            } else {
//            log.info("Установка соединения с ККТ");
//                System.out.println("Соединение с ККТ установлено");
                b = true;

            }
        }

        if (i2 == 0) {

            if (fptr.printText() < 0) {
                System.out.println(String.format("%d [%s]", fptr.errorCode(), fptr.errorDescription()));
//                log.error("Соединение с ККТ не установлено");
//                System.out.println("Соединение с ККТ не установлено");
                DialogAlert alert = new DialogAlert();
                alert.alert("Внимание", "", "Соединение с ККТ разорвано");
                b = false;
            } else {
//            log.info("Установка соединения с ККТ");
                System.out.println("Соединение с ККТ установлено");
                b = true;

            }
        }

        return b;
    }

    /**
     * Возвращает сом-порт из xml-файла
     */
    private void getCOMPortKKT() {
        String com = xmls.getKKTCom();
        addSettingsComPort(com);
    }

    /**
     * Возвращает ip и порт из xml-файла
     */
    private void getIpAddressPortKKT() {
        String ip = xmls.getIpAddressKKT();
        String port = xmls.getPortKKT();

        addSettingsIpAddressPort(ip, port);
    }

    /**
     * Ввод сом порта в настройки
     *
     * @param comPort
     */
    private void addSettingsComPort(String comPort) {

        //Настройка драйвера
        //fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_LIBRARY_PATH, String.valueOf("java.library.path"));
//        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_LIBRARY_PATH, String.valueOf("src/dll/"));
//        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_MODEL, String.valueOf(IFptr.LIBFPTR_MODEL_ATOL_AUTO));
//        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_PORT, String.valueOf(IFptr.LIBFPTR_PORT_COM));
//        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_COM_FILE, comPort);
//        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_BAUDRATE, String.valueOf(IFptr.LIBFPTR_PORT_BR_115200));
//        fptr.applySingleSettings();
        //String settings = fptr.getSettings();
        String settings = String.format("{\"%s\": %d, \"%s\": %d, \"%s\": \"%s\", \"%s\": %d}",
                IFptr.LIBFPTR_SETTING_MODEL, IFptr.LIBFPTR_MODEL_ATOL_AUTO,
                IFptr.LIBFPTR_SETTING_PORT, IFptr.LIBFPTR_PORT_COM,
                IFptr.LIBFPTR_SETTING_COM_FILE, comPort,
                IFptr.LIBFPTR_SETTING_BAUDRATE, IFptr.LIBFPTR_PORT_BR_115200);


        /*fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_MODEL, String.valueOf(IFptr.LIBFPTR_MODEL_ATOL_AUTO));
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_PORT, String.valueOf(IFptr.LIBFPTR_PORT_TCPIP));
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_IPADDRESS, "192.168.0.62");
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_IPPORT, "5555");
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_BAUDRATE, String.valueOf(IFptr.LIBFPTR_PORT_BR_115200));
        
        
        fptr.applySingleSettings();*/
        fptr.setSettings(settings);
        fptr.open();

    }

    /**
     * Ввод IP адреса и порта в настройки
     *
     * @param comPort
     */
    private void addSettingsIpAddressPort(String ipAddress, String port) {

        //Настройка драйвера
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_MODEL, String.valueOf(IFptr.LIBFPTR_MODEL_ATOL_AUTO));
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_PORT, String.valueOf(IFptr.LIBFPTR_PORT_TCPIP));
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_IPADDRESS, ipAddress);
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_IPPORT, port);
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_BAUDRATE, String.valueOf(IFptr.LIBFPTR_PORT_BR_115200));

        fptr.applySingleSettings();

        fptr.open();

    }

    /**
     * Печать логотипа
     */
    private void printLogo() {
        //печать картинки с диска на компе
        fptr.setParam(IFptr.LIBFPTR_PARAM_FILENAME, "src/ml/resources/image/logo.png");
        fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_CENTER);
        fptr.printPicture();
        fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "");
        fptr.printText();
    }

    /**
     * Запрос статуса информационного обмена
     */
    private void infoStatusChange() {

        fptr.setParam(IFptr.LIBFPTR_PARAM_FN_DATA_TYPE, IFptr.LIBFPTR_FNDT_OFD_EXCHANGE_STATUS);
        fptr.fnQueryData();

        //Статус информационного обмена
        long exchangeStatus = fptr.getParamInt(IFptr.LIBFPTR_PARAM_OFD_EXCHANGE_STATUS);
        //Количество неотправленных документов
        long unsentCount = fptr.getParamInt(IFptr.LIBFPTR_PARAM_DOCUMENTS_COUNT);
        //Номер первого неотправленного документа
        long firstUnsentNumber = fptr.getParamInt(IFptr.LIBFPTR_PARAM_DOCUMENT_NUMBER);
        //Флаг наличия сообщения для ОФД
        boolean ofdMessageRead = fptr.getParamBool(IFptr.LIBFPTR_PARAM_OFD_MESSAGE_READ);
        //Дата и время первого неотправленного документа
        Date dateTime = fptr.getParamDateTime(IFptr.LIBFPTR_PARAM_DATE_TIME);

        System.out.println("Статус информационного обмена " + exchangeStatus);
        System.out.println("Количество неотправленных документов " + unsentCount);
        System.out.println("Номер первого неотправленного документа " + firstUnsentNumber);
        System.out.println("Флаг наличия сообщения для ОФД " + ofdMessageRead);
        System.out.println("Дата и время первого неотправленного документа " + dateTime);
        System.out.println("");
    }
}
