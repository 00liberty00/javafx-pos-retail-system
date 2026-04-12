/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.kkt;

import java.math.BigDecimal;
import java.util.List;
import ml.model.Check;
import ml.model.CheckList;
import ml.model.Discount;
import ml.model.UserSwing;
import ru.atol.drivers10.fptr.IFptr;

/**
 *
 * @author Dave
 */
public class KKTPrintFiscalCheck {

    private boolean checkBank;
    private boolean checkPrint;
    private String phoneOrEmail;
    private List<CheckList> checkListArrayList;
    private List<Check> checkArrayList;
    private Discount discount;
    private BigDecimal sumChecktText;
    private BigDecimal discountText;
    private BigDecimal cash;
    private BigDecimal shortChange;
    private Integer rz = 0;
    private long validationResult;
    private UserSwing userSwing;
//    private final IFptr fptr = new Fptr();

    public KKTPrintFiscalCheck() {
    }

    public void setPrintCheck(IFptr fptr, List<CheckList> checkListArrayList, List<Check> checkArrayList,
            Discount discount, BigDecimal sumChecktText, BigDecimal discountText,
            BigDecimal cash, BigDecimal shortChange, boolean checkBank, boolean checkPrint, String phoneOrEmail, UserSwing us) {

        this.userSwing = us;
        this.checkBank = checkBank;
        this.checkPrint = checkPrint;
        this.checkListArrayList = checkListArrayList;
        this.checkArrayList = checkArrayList;
        this.discount = discount;
        this.sumChecktText = sumChecktText;
        this.discountText = discountText;
        this.cash = cash;
        this.shortChange = shortChange;
        this.phoneOrEmail = phoneOrEmail;

//        if (checkPrint == false) {
        openFiscalReceipt(fptr);

//            fptr.setParam(IFptr.LIBFPTR_PARAM_FN_DATA_TYPE, IFptr.LIBFPTR_FNDT_FFD_VERSIONS);
//            fptr.fnQueryData();
//
//            long deviceFfdVersion = fptr.getParamInt(IFptr.LIBFPTR_PARAM_DEVICE_FFD_VERSION);
//            long fnFfdVersion = fptr.getParamInt(IFptr.LIBFPTR_PARAM_FN_FFD_VERSION);
//            long maxFnFfdVersion = fptr.getParamInt(IFptr.LIBFPTR_PARAM_FN_MAX_FFD_VERSION);
//            long ffdVersion = fptr.getParamInt(IFptr.LIBFPTR_PARAM_FFD_VERSION);
//            long maxFfdVersion = fptr.getParamInt(IFptr.LIBFPTR_PARAM_DEVICE_MAX_FFD_VERSION);
//            long minFfdVersion = fptr.getParamInt(IFptr.LIBFPTR_PARAM_DEVICE_MIN_FFD_VERSION);
//            long versionKKT = fptr.getParamInt(IFptr.LIBFPTR_PARAM_VERSION);
//
//            System.out.println("Версия ФФД ККТ        " + deviceFfdVersion);
//            System.out.println("Версия ФФД ФН      " + fnFfdVersion);
//            System.out.println("Максимальная версия ФФД ФН     " + maxFnFfdVersion);
//            System.out.println("Версия ФФД	" + ffdVersion);
//            System.out.println("Максимальная версия ФФД ККТ	" + maxFfdVersion);
//            System.out.println("Минимальная версия ФФД ККТ	" + minFfdVersion);
//            System.out.println("Версия модели ККТ	" + versionKKT);
//        } else {
//            openFiscalElectronReceipt(fptr);
//        }
    }

    /**
     * Открытие печатного чека
     */
    private void openFiscalReceipt(IFptr fptr) {
//        System.out.println("Ошиб_1: " + fptr.errorCode());
//        System.out.println("Ошиб_1: " + fptr.errorDescription());
        fptr.setParam(1021, userSwing.getName());
//        fptr.setParam(1055, IFptr.LIBFPTR_TT_USN_INCOME);
        //fptr.setParam(1203, "910100021654");

        //fptr.setParam(1021, "Кассир Гусейнов Д.Х.");
        //fptr.setParam(1203, "910100021580");
        fptr.operatorLogin();
//        System.out.println("Ошиб_2: " + fptr.errorCode());
//        System.out.println("Ошиб_2: " + fptr.errorDescription());
        fptr.setParam(IFptr.LIBFPTR_PARAM_RECEIPT_TYPE, IFptr.LIBFPTR_RT_SELL);
        //Печать или не печать фиск чека
        if (checkPrint) {
            fptr.setParam(IFptr.LIBFPTR_PARAM_RECEIPT_ELECTRONICALLY, false);
//            System.out.println("Ошиб_3: " + fptr.errorCode());
//            System.out.println("Ошиб_3: " + fptr.errorDescription());
        } else {
            fptr.setParam(IFptr.LIBFPTR_PARAM_RECEIPT_ELECTRONICALLY, true);
        }
        if (!phoneOrEmail.isEmpty()) {
            fptr.setParam(1008, phoneOrEmail);
        }
//        System.out.println("Ошиб_4: " + fptr.errorCode());
//        System.out.println("Ошиб_4: " + fptr.errorDescription());
        fptr.openReceipt();
//        System.out.println("Ошиб_5: " + fptr.errorCode());
//        System.out.println("Ошиб_5: " + fptr.errorDescription());
        fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_CENTER);
//        System.out.println("Ошиб_5_1: " + fptr.errorCode());
//        System.out.println("Ошиб_5_1: " + fptr.errorDescription());
        fptr.printText();
//        System.out.println("Ошиб_5_2: " + fptr.errorCode());
//        System.out.println("Ошиб_5-2: " + fptr.errorDescription());

        //Если есть дисконт!!!!
        if (discount.getNumcard() != null) {
            // Блок ТОВАР_______ЦЕНА * КОЛ-ВО = СУММА
            for (CheckList check1 : checkListArrayList) {

                BigDecimal priceWithDiscount = check1.getPrice()
                        .subtract((check1.getPrice()
                                .multiply(new BigDecimal(discount.getPercent())).divide(new BigDecimal(100))));

                fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "Цена без скидки: " + check1.getPrice());
                fptr.printText();

                registrationCheck(fptr, check1.getGoods().getName(), priceWithDiscount, check1.getAmount(), check1.getMarking());
            }

        } else {

            // Блок ТОВАР_______ЦЕНА * КОЛ-ВО = СУММА
            for (CheckList check1 : checkListArrayList) {

                registrationCheck(fptr, check1.getGoods().getName(), check1.getPrice(),
                        check1.getAmount(), check1.getMarking());

            }
        }
//        System.out.println("Ошиб_6: " + fptr.errorCode());
//        System.out.println("Ошиб_6: " + fptr.errorDescription());
        //Блок СУММА ЧЕКА
        paymentReceipt(fptr, cash, sumChecktText, checkBank);
//        System.out.println("Ошиб_7: " + fptr.errorCode());
//        System.out.println("Ошиб_7: " + fptr.errorDescription());
//        receiptTax(fptr, sumChecktText);
        receiptTotal(fptr, cash);

        if (discount.getNumcard() != null) {

            fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "Скидка " + discount.getPercent() + "% : " + sumChecktText.subtract(discountText));
            fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_RIGHT);
            fptr.printText();
            fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "Сумма без скидки: " + sumChecktText);
            fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_RIGHT);
            fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "Прим: " + discount.getNote());
            fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_RIGHT);
            fptr.printText();
        }
//        System.out.println("Ошиб_8: " + fptr.errorCode());
//        System.out.println("Ошиб_8: " + fptr.errorDescription());
        closeReceipt(fptr);

//        if (checkBank == true) {
//            copyLastDocument(fptr);
//        }
    }

    /**
     * Открытие чека по безналу
     *
     * @param fptr
     */
    private void openFiscalElectronReceipt(IFptr fptr) {
        fptr.operatorLogin();

        fptr.setParam(IFptr.LIBFPTR_PARAM_RECEIPT_TYPE, IFptr.LIBFPTR_RT_SELL);
        fptr.setParam(IFptr.LIBFPTR_PARAM_RECEIPT_ELECTRONICALLY, false);
        fptr.openReceipt();

        fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_CENTER);
        fptr.printText();

        //Если есть дисконт!!!!
        if (discount.getNumcard() != null) {
            // Блок ТОВАР_______ЦЕНА * КОЛ-ВО = СУММА
            for (CheckList check1 : checkListArrayList) {

                BigDecimal priceWithDiscount = check1.getPrice()
                        .subtract((check1.getPrice()
                                .multiply(new BigDecimal(discount.getPercent())).divide(new BigDecimal(100))));

                fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "Цена без скидки: " + check1.getPrice());
                fptr.printText();

                registrationCheck(fptr, check1.getGoods().getName(), priceWithDiscount, check1.getAmount(), check1.getMarking());
            }

        } else {

            // Блок ТОВАР_______ЦЕНА * КОЛ-ВО = СУММА
            checkListArrayList.forEach((check1) -> {
                registrationCheck(fptr, check1.getGoods().getName(), check1.getPrice(), check1.getAmount(), check1.getMarking());
            });
        }

        //Блок СУММА ЧЕКА
        paymentReceipt(fptr, cash, sumChecktText, checkBank);
//        receiptTax(fptr, sumChecktText);
        receiptTotal(fptr, cash);

        if (discount.getNumcard() != null) {

            fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "Скидка " + discount.getPercent() + "% : " + sumChecktText.subtract(discountText));
            fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_RIGHT);
            fptr.printText();
            fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "Сумма без скидки: " + sumChecktText);
            fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_RIGHT);
            fptr.printText();
        }

        closeReceipt(fptr);

//        if (checkBank == true) {
//            copyLastDocument(fptr);
//        }
    }

    /**
     * Отмена чека
     */
    private void cancelReceipt(IFptr fptr) {
        fptr.cancelReceipt();
    }

    /**
     * Регистрация позиции без указания суммы налога
     */
    private void registrationCheck(IFptr fptr, String name, BigDecimal price, BigDecimal quantity, String marking) {
//        System.out.println("Ошиб_9: " + fptr.errorCode());
//        System.out.println("Ошиб_9: " + fptr.errorDescription());
        //Провести вставку спецсимволов в маркировку
        if (!marking.equals("Б/М")) {
            markingValidPosition(fptr, marking, quantity);
//            System.out.println("Ошиб_10: " + fptr.errorCode());
//            System.out.println("Ошиб_10: " + fptr.errorDescription());
        }

        if (!marking.equals("Б/М")) {
            markingRegistrPosition(fptr, name, price, quantity, marking);
//            System.out.println("Ошиб_11: " + fptr.errorCode());
//            System.out.println("Ошиб_11: " + fptr.errorDescription());

        } else {
            fptr.setParam(IFptr.LIBFPTR_PARAM_COMMODITY_NAME, name);
            fptr.setParam(IFptr.LIBFPTR_PARAM_PRICE, price.doubleValue());
            fptr.setParam(IFptr.LIBFPTR_PARAM_QUANTITY, quantity.doubleValue());
            fptr.setParam(IFptr.LIBFPTR_PARAM_MEASUREMENT_UNIT, IFptr.LIBFPTR_IU_PIECE);
            fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_TYPE, IFptr.LIBFPTR_TAX_NO);
//            fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_TYPE, IFptr.LIBFPTR_TAX_VAT5);
            
        }
        fptr.registration();
        
    }

    /**
     * Оплата чека
     *
     */
    private void paymentReceipt(IFptr fptr, BigDecimal sum, BigDecimal sumChecktText, Boolean checkBank) {

        if (checkBank == true) {
            if (discount.getNumcard() != null) {

                fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_TYPE, IFptr.LIBFPTR_PT_ELECTRONICALLY);
                fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_SUM, discountText.doubleValue());
                fptr.payment();
            } else {

                fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_TYPE, IFptr.LIBFPTR_PT_ELECTRONICALLY);
                fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_SUM, sumChecktText.doubleValue());
                fptr.payment();
            }

            fptr.setParam(IFptr.LIBFPTR_PARAM_DATA_TYPE, IFptr.LIBFPTR_DT_RECEIPT_STATE);
            fptr.queryData();

            long receiptType = fptr.getParamInt(IFptr.LIBFPTR_PARAM_RECEIPT_TYPE);
            long receiptNumber = fptr.getParamInt(IFptr.LIBFPTR_PARAM_RECEIPT_NUMBER);
            long documentNumber = fptr.getParamInt(IFptr.LIBFPTR_PARAM_DOCUMENT_NUMBER);
            double summ = fptr.getParamDouble(IFptr.LIBFPTR_PARAM_RECEIPT_SUM);
            double remainder = fptr.getParamDouble(IFptr.LIBFPTR_PARAM_REMAINDER);
            double change = fptr.getParamDouble(IFptr.LIBFPTR_PARAM_CHANGE);

//            System.out.println("Тип чека        " + receiptType);
//            System.out.println("Номер чека      " + receiptNumber);
//            System.out.println("Номер документа     " + documentNumber);
//            System.out.println("Текущая сумма чека	" + summ);
//            System.out.println("Неоплаченный остаток	" + remainder);
//            System.out.println("Сдача по чеку	" + change);
        } else {
            fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_TYPE, IFptr.LIBFPTR_PT_CASH);
            fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_SUM, sum.doubleValue());
            fptr.payment();

            fptr.setParam(IFptr.LIBFPTR_PARAM_DATA_TYPE, IFptr.LIBFPTR_DT_RECEIPT_STATE);
            fptr.queryData();
            long receiptType = fptr.getParamInt(IFptr.LIBFPTR_PARAM_RECEIPT_TYPE);
            long receiptNumber = fptr.getParamInt(IFptr.LIBFPTR_PARAM_RECEIPT_NUMBER);
            long documentNumber = fptr.getParamInt(IFptr.LIBFPTR_PARAM_DOCUMENT_NUMBER);
            double summ = fptr.getParamDouble(IFptr.LIBFPTR_PARAM_RECEIPT_SUM);
            double remainder = fptr.getParamDouble(IFptr.LIBFPTR_PARAM_REMAINDER);
            double change = fptr.getParamDouble(IFptr.LIBFPTR_PARAM_CHANGE);

//            System.out.println("Тип чека        " + receiptType);
//            System.out.println("Номер чека      " + receiptNumber);
//            System.out.println("Номер документа     " + documentNumber);
//            System.out.println("Текущая сумма чека	" + summ);
//            System.out.println("Неоплаченный остаток	" + remainder);
//            System.out.println("Сдача по чеку	" + change);
        }


        /*
        if (checkBank == true) {
        fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_TYPE, IFptr.LIBFPTR_PT_ELECTRONICALLY);
        } else {
        fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_TYPE, IFptr.LIBFPTR_PT_CASH);
        }
        fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_SUM, sum.doubleValue());
        fptr.payment();*/
    }

    /**
     * Регистрация налога на чек
     */
    private void receiptTax(IFptr fptr, BigDecimal sumCheck) {
        fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_TYPE, IFptr.LIBFPTR_TAX_NO);
//        fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_TYPE, IFptr.LIBFPTR_TAX_VAT5);
        fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_SUM, sumCheck.doubleValue());
        fptr.receiptTax();
    }

    /**
     * Регистрация итога чека
     */
    private void receiptTotal(IFptr fptr, BigDecimal sum) {

        fptr.setParam(IFptr.LIBFPTR_PARAM_SUM, sum.doubleValue());
        fptr.receiptTotal();
    }

    /**
     * Закрытие полностью оплаченного чека
     */
    private void closeReceipt(IFptr fptr) {

        //Проверка версии ФФД
        fptr.setParam(IFptr.LIBFPTR_PARAM_FN_DATA_TYPE, IFptr.LIBFPTR_FNDT_FFD_VERSIONS);
        fptr.fnQueryData();

        long ffdVersion = fptr.getParamInt(IFptr.LIBFPTR_PARAM_FFD_VERSION);
        System.out.println("Версия ФФД	" + ffdVersion);

        // Перед закрытием проверяем, что все КМ отправились (на случай, если были проверки КМ без ожидания результата
        //Если версия ФФД 1.2
        if (ffdVersion == 120) {
            while (true) {
                fptr.checkMarkingCodeValidationsReady();
                if (fptr.getParamBool(IFptr.LIBFPTR_PARAM_MARKING_CODE_VALIDATION_READY)) {
                    break;
                }
            }
        }

        fptr.closeReceipt();

        //Проверка закрытия документа (на примере закрытия фискального чека)
        while (fptr.checkDocumentClosed() < 0) {
            // Не удалось проверить состояние документа. Вывести пользователю текст ошибки, попросить устранить неполадку и повторить запрос
            System.out.println(fptr.errorDescription());
            break;
        }

        if (!fptr.getParamBool(IFptr.LIBFPTR_PARAM_DOCUMENT_CLOSED)) {
            // Документ не закрылся. Требуется его отменить (если это чек) и сформировать заново
            fptr.cancelReceipt();
            return;
        }

        if (!fptr.getParamBool(IFptr.LIBFPTR_PARAM_DOCUMENT_PRINTED)) {
            // Можно сразу вызвать метод допечатывания документа, он завершится с ошибкой, если это невозможно
            while (fptr.continuePrint() < 0) {
                // Если не удалось допечатать документ - показать пользователю ошибку и попробовать еще раз.
                System.out.println(String.format("Не удалось напечатать документ (Ошибка \"%s\"). Устраните неполадку и повторите.", fptr.errorDescription()));
                continue;
            }
        }
    }

    /**
     * Копия последнего документа
     *
     */
    private void copyLastDocument(IFptr fptr) {

        fptr.setParam(IFptr.LIBFPTR_PARAM_REPORT_TYPE, IFptr.LIBFPTR_RT_LAST_DOCUMENT);
        fptr.report();
    }

    /**
     * Работа с маркировкой (ВАЛИДАЦИЯ)
     */
    private void markingValidPosition(IFptr fptr, String mark, BigDecimal quantity) {
//        String mark = "014494550435306821QXYXSALGLMYQQ\u001D91EE06\u001D92YWCXbmK6SN8vvwoxZFk7WAY8WoJNMGGr6Cgtiuja04c=";

//        String marks = "0104680017612715215%RKtF\u001D93upHn";
        long status = 2;

// Запускаем проверку КМ
        fptr.setParam(IFptr.LIBFPTR_PARAM_MARKING_CODE_TYPE, IFptr.LIBFPTR_MCT12_AUTO);
        fptr.setParam(IFptr.LIBFPTR_PARAM_MARKING_CODE, mark);
        fptr.setParam(IFptr.LIBFPTR_PARAM_MARKING_CODE_STATUS, status);
        fptr.setParam(IFptr.LIBFPTR_PARAM_QUANTITY, quantity.doubleValue());
        fptr.setParam(IFptr.LIBFPTR_PARAM_MEASUREMENT_UNIT, IFptr.LIBFPTR_IU_PIECE);
//        fptr.setParam(IFptr.LIBFPTR_PARAM_MARKING_WAIT_FOR_VALIDATION_RESULT, true);
        fptr.setParam(IFptr.LIBFPTR_PARAM_MARKING_PROCESSING_MODE, 0);
        fptr.setParam(IFptr.LIBFPTR_PARAM_MARKING_FRACTIONAL_QUANTITY, "1/2");
        fptr.beginMarkingCodeValidation();

// Дожидаемся окончания проверки и запоминаем результат
        while (true) {
            fptr.getMarkingCodeValidationStatus();
            if (fptr.getParamBool(IFptr.LIBFPTR_PARAM_MARKING_CODE_VALIDATION_READY)) {
                break;
            }
        }
        this.validationResult = fptr.getParamInt(IFptr.LIBFPTR_PARAM_MARKING_CODE_ONLINE_VALIDATION_RESULT);

        System.out.println("!!! validationResult = " + this.validationResult + " : " + mark);

        // Подтверждаем реализацию товара с указанным КМ
        fptr.acceptMarkingCode();
    }

    /**
     * Работа с маркировкой (Регистрация)
     */
    private void markingRegistrPosition(IFptr fptr, String name, BigDecimal price, BigDecimal quantity, String mark) {
        long status = 2;

//        String marks = "0104680017612715215%RKtF\u001D93upHn";
// ... Проверяем остальные КМ
// Формируем чек
        fptr.setParam(IFptr.LIBFPTR_PARAM_COMMODITY_NAME, name);
        fptr.setParam(IFptr.LIBFPTR_PARAM_PRICE, price.doubleValue());
        fptr.setParam(IFptr.LIBFPTR_PARAM_QUANTITY, quantity.doubleValue());
        fptr.setParam(IFptr.LIBFPTR_PARAM_MEASUREMENT_UNIT, IFptr.LIBFPTR_IU_PIECE);
        fptr.setParam(IFptr.LIBFPTR_PARAM_MARKING_FRACTIONAL_QUANTITY, "1/2");
        fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_TYPE, IFptr.LIBFPTR_TAX_NO);
//        fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_TYPE, IFptr.LIBFPTR_TAX_VAT5);
       

        fptr.setParam(1212, 33);
        fptr.setParam(1214, 4);
        fptr.setParam(IFptr.LIBFPTR_PARAM_MARKING_CODE, mark);
        fptr.setParam(IFptr.LIBFPTR_PARAM_MARKING_CODE_STATUS, status);
        fptr.setParam(IFptr.LIBFPTR_PARAM_MARKING_CODE_ONLINE_VALIDATION_RESULT, validationResult);
        fptr.setParam(IFptr.LIBFPTR_PARAM_MARKING_PROCESSING_MODE, 0);
        

// ... Регистрируем остальные позиции
    }

}
