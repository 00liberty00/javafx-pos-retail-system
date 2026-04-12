/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.model;

import java.math.BigDecimal;

/**
 *
 * @author Dave
 */
public class GeneralReportsModel {

    private String nameColumn;
    private BigDecimal sumPriceAmountColumn;
    private BigDecimal sumProfitColumn;
    private BigDecimal quantitySoldColumn;
    private BigDecimal quantityAfterAccountingColumn;
    private BigDecimal quantityArrivalColumn;
    private BigDecimal quantityCancelColumn;
    private BigDecimal balanceColumn;

    public GeneralReportsModel() {
    }

    public GeneralReportsModel(String nameColumn, BigDecimal sumPriceAmountColumn, BigDecimal sumProfitColumn, BigDecimal quantitySoldColumn, BigDecimal quantityArrivalColumn, BigDecimal quantityCancelColumn, BigDecimal balanceColumn) {
        this.nameColumn = nameColumn;
        this.sumPriceAmountColumn = sumPriceAmountColumn;
        this.sumProfitColumn = sumProfitColumn;
        this.quantitySoldColumn = quantitySoldColumn;
        this.quantityArrivalColumn = quantityArrivalColumn;
        this.quantityCancelColumn = quantityCancelColumn;
        this.balanceColumn = balanceColumn;
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public BigDecimal getSumPriceAmountColumn() {
        return sumPriceAmountColumn;
    }

    public void setSumPriceAmountColumn(BigDecimal sumPriceAmountColumn) {
        this.sumPriceAmountColumn = sumPriceAmountColumn;
    }

    public BigDecimal getSumProfitColumn() {
        return sumProfitColumn;
    }

    public void setSumProfitColumn(BigDecimal sumProfitColumn) {
        this.sumProfitColumn = sumProfitColumn;
    }

    public BigDecimal getQuantitySoldColumn() {
        return quantitySoldColumn;
    }

    public void setQuantitySoldColumn(BigDecimal quantitySoldColumn) {
        this.quantitySoldColumn = quantitySoldColumn;
    }

    public BigDecimal getQuantityArrivalColumn() {
        return quantityArrivalColumn;
    }

    public void setQuantityArrivalColumn(BigDecimal quantityArrivalColumn) {
        this.quantityArrivalColumn = quantityArrivalColumn;
    }

    public BigDecimal getQuantityCancelColumn() {
        return quantityCancelColumn;
    }

    public void setQuantityCancelColumn(BigDecimal quantityCancelColumn) {
        this.quantityCancelColumn = quantityCancelColumn;
    }

    public BigDecimal getBalanceColumn() {
        return balanceColumn;
    }

    public void setBalanceColumn(BigDecimal balanceColumn) {
        this.balanceColumn = balanceColumn;
    }

    public BigDecimal getQuantityAfterAccountingColumn() {
        return quantityAfterAccountingColumn;
    }

    public void setQuantityAfterAccountingColumn(BigDecimal quantityAfterAccountingColumn) {
        this.quantityAfterAccountingColumn = quantityAfterAccountingColumn;
    }

    
}
