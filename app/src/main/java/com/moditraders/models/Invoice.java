package com.moditraders.models;

import java.math.BigDecimal;
import java.util.Collection;

/**
 *
 */
public class Invoice {

    private String invoiceId;
    private String invoiceDate;
    private Customer customer;
    private Consignee consignee;
    private Collection<InvoiceItem> invoiceItemDetails;
    private BigDecimal grandTotal;
    private BigDecimal totalTax;
    private BigDecimal amountReceived;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Consignee getConsignee() {
        return consignee;
    }

    public void setConsignee(Consignee consignee) {
        this.consignee = consignee;
    }

    public Collection<InvoiceItem> getInvoiceItemDetails() {
        return invoiceItemDetails;
    }

    public void setInvoiceItemDetails(Collection<InvoiceItem> invoiceItemDetails) {
        this.invoiceItemDetails = invoiceItemDetails;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }
}
