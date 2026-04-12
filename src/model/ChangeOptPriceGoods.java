package ml.model;
// Generated 17.01.2017 17:00:10 by Hibernate Tools 4.3.1

import java.math.BigDecimal;

public class ChangeOptPriceGoods implements java.io.Serializable {

    private String name;
    private String code;
    private BigDecimal price;
    private BigDecimal oldPriceOpt;
    private BigDecimal newPriceOpt;

    public ChangeOptPriceGoods() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOldPriceOpt() {
        return oldPriceOpt;
    }

    public void setOldPriceOpt(BigDecimal oldPriceOpt) {
        this.oldPriceOpt = oldPriceOpt;
    }

    public BigDecimal getNewPriceOpt() {
        return newPriceOpt;
    }

    public void setNewPriceOpt(BigDecimal newPriceOpt) {
        this.newPriceOpt = newPriceOpt;
    }

}
