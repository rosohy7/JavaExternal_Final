package external.letiuka.modelviewcontroller.model.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for representing bank transactions in account history.
 */
public abstract class TransactionDTO implements Serializable {
    protected double balanceChange;
    protected double bankFee;
    protected String type;
    protected Date timestamp;

    public TransactionDTO() {
    }

    public double getBalanceChange() {
        return balanceChange;
    }

    public void setBalanceChange(double balanceChange) {
        this.balanceChange = balanceChange;
    }

    public double getBankFee() {
        return bankFee;
    }

    public void setBankFee(double bankFee) {
        this.bankFee = bankFee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
