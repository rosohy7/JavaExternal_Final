package external.letiuka.modelviewcontroller.model.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for transfering bank account data.
 */
public abstract class BankAccountDTO implements Serializable {
    protected Date expires;
    protected String accountNumber;
    protected String type;
    protected double accountBalance;
    protected boolean confirmed;
    protected String holder;
    protected TransactionListDTO history;

    public BankAccountDTO() {
        history=new TransactionListDTO();
    }


    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public TransactionListDTO getHistory() {
        return history;
    }

    public void setHistory(TransactionListDTO history) {
        this.history = history;
    }
}
