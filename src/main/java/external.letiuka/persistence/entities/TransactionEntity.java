package external.letiuka.persistence.entities;

import external.letiuka.modelviewcontroller.model.TransactionType;

import java.sql.Timestamp;
/**
 * Responsible for bank transaction table in a database.
 */
public abstract class TransactionEntity extends BaseEntity {
    protected Timestamp timeStamp;
    protected double balanceChange;
    protected double bankFee;
    protected TransactionType type;
    protected long accountId;
    protected Long pairId;

    public TransactionEntity() {
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
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

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Long getPairId() {
        return pairId;
    }

    public void setPairId(Long pairId) {
        this.pairId = pairId;
    }
}
