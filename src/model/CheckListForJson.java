/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Dave
 */
public class CheckListForJson {

    //Check
    private String idCheckList;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private String userSwing;
    private String check;
    private String goods;
    private String amount;
    private String price;
    private String profit;
    private String checkNewPrice;
    private String marking;

    public CheckListForJson() {
    }

    public String getIdCheckList() {
        return idCheckList;
    }

    public void setIdCheckList(String idCheckList) {
        this.idCheckList = idCheckList;
    }

    public String getUserSwing() {
        return userSwing;
    }

    public void setUserSwing(String userSwing) {
        this.userSwing = userSwing;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getCheckNewPrice() {
        return checkNewPrice;
    }

    public void setCheckNewPrice(String checkNewPrice) {
        this.checkNewPrice = checkNewPrice;
    }

    public String getMarking() {
        return marking;
    }

    public void setMarking(String marking) {
        this.marking = marking;
    }

}
