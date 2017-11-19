package com.moditraders.models;

import com.moditraders.types.TaxType;

import java.math.BigDecimal;

/**
 * Created by Hitesh on 19-11-2017.
 */
public class TaxItem {
    private TaxType taxType;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
}
