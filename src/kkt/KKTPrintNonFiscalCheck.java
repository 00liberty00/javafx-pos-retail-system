/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.kkt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ml.model.Check;
import ml.model.CheckList;
import ml.model.Discount;
import ru.atol.drivers10.fptr.Fptr;
import ru.atol.drivers10.fptr.IFptr;

/**
 *
 * @author Dave
 */
public class KKTPrintNonFiscalCheck {

    public KKTPrintNonFiscalCheck() {
    }

    public void setPrintCheck(IFptr fptr, List<CheckList> checkListArrayList, List<Check> checkArrayList,
            Discount discount, BigDecimal sumChecktText, BigDecimal discountText,
            BigDecimal cash, BigDecimal shortChange) {

        fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "Нефискальный чек №" + checkArrayList.get(0).getIdCheck());
        fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_CENTER);
        fptr.printText();

        // Блок ТОВАР_______ЦЕНА * КОЛ-ВО = СУММА
        for (CheckList check1 : checkListArrayList) {

            fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, check1.getGoods().getName());
            fptr.printText();

            fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, check1.getGoods().getPrice() + " * "
                    + check1.getAmount() + " = " + check1.getAmount()
                    .multiply(check1.getGoods().getPrice()).setScale(2, RoundingMode.HALF_UP).toString());
            fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_RIGHT);
            fptr.printText();

        }

        //Если есть дисконт
        if (discount.getNumcard() != null) {

            fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "Сумма чека: " + sumChecktText);
            fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_RIGHT);
            fptr.printText();

            fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "Скидка " + discount.getPercent() + "% : " + sumChecktText.subtract(discountText));
            fptr.printText();

            //Блок СУММА ЧЕКА СО СКИДКОЙ
            fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "ИТОГ   =" + discountText);
            fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_LEFT);
            fptr.setParam(IFptr.LIBFPTR_PARAM_FONT, 3);
            fptr.setParam(IFptr.LIBFPTR_PARAM_FONT_DOUBLE_WIDTH, true);
            fptr.setParam(IFptr.LIBFPTR_PARAM_FONT_DOUBLE_HEIGHT, true);
            fptr.printText();

        } else {
            //Блок СУММА ЧЕКА
            fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "ИТОГ   =" + sumChecktText);
            fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_LEFT);
            fptr.setParam(IFptr.LIBFPTR_PARAM_FONT, 3);
            fptr.setParam(IFptr.LIBFPTR_PARAM_FONT_DOUBLE_WIDTH, true);
            fptr.setParam(IFptr.LIBFPTR_PARAM_FONT_DOUBLE_HEIGHT, true);
            fptr.printText();
        }

        //Блок НАЛИЧНЫМИ
        fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "Наличными " + cash);
        fptr.printText();

        //Блок СДАЧА
        fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "Сдача " + shortChange);
        fptr.printText();

    }

}
