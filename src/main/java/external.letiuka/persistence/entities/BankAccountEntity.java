package external.letiuka.persistence.entities;

import external.letiuka.modelviewcontroller.model.AccountType;

import java.sql.Date;

public abstract class BankAccountEntity extends BaseEntity {
    protected Date expires;
    protected String accountNumber;
    protected AccountType type;
    protected double accountBalance;
    protected boolean confirmed;
    protected Date latestUpdate;
    protected long userId;

    public BankAccountEntity() {
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

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
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

    public Date getLatestUpdate() {
        return latestUpdate;
    }

    public void setLatestUpdate(Date latestUpdate) {
        this.latestUpdate = latestUpdate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
