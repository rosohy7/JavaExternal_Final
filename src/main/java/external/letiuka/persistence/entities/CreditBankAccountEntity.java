package external.letiuka.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Represents credit bank account table in a database.
 */
@Entity(name = "CreditBankAccount")
@Table(name = "credit_bank_account")
public class CreditBankAccountEntity extends BankAccountEntity {
    @Column(name="credit_limit")
    protected double creditLimit;

    @Column(name="interest_rate")
    protected double interestRate;

    @Column(name="accrued_interest")
    protected double accruedInterest;

    public CreditBankAccountEntity() {
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getAccruedInterest() {
        return accruedInterest;
    }

    public void setAccruedInterest(double accruedInterest) {
        this.accruedInterest = accruedInterest;
    }
}
