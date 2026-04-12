/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import ml.util.JsonObjectListDeserializer;

/**
 *
 * @author Dave
 */
//@JsonFormat(shape = JsonFormat.Shape.ARRAY)
//@JsonPropertyOrder({ "idCheck", "sum", "nal","bnal","money","date","note","bank","discount","checkDiscount","checkListNewPrice","checkList"})
public class CheckForJson {

    //Check
//    @JsonDeserialize(using = JsonObjectListDeserializer.class)
//    private String idCheck;
//    private String sum;
//    private String nal;
//    private String bnal;
//    private String money;
//    private String date;
//    private String note;
//    private String bank;
//    private String checkDiscount;
    private Check check;
    private Discount discount;
    private CheckDiscount checkDiscount;
    private CheckListNewPrice checkListNewPrice;
//    private List<CheckList> checkList;

    //CheckList
//    private String goods;
//    private String amount;
//    private String price;
//    private String profit;
//    private String checkNewPrice;
//    private String marking;
    public CheckForJson() {
    }

    public Check getCheck() {
        return check;
    }

    public void setCheck(Check check) {
        this.check = check;
    }

    public CheckDiscount getCheckDiscount() {
        return checkDiscount;
    }

    public void setCheckDiscount(CheckDiscount checkDiscount) {
        this.checkDiscount = checkDiscount;
    }

//    public List<CheckList> getCheckList() {
//        return checkList;
//    }
//
//    public void setCheckList(List<CheckList> checkList) {
//        this.checkList = checkList;
//    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public CheckListNewPrice getCheckListNewPrice() {
        return checkListNewPrice;
    }

    public void setCheckListNewPrice(CheckListNewPrice checkListNewPrice) {
        this.checkListNewPrice = checkListNewPrice;
    }

}
