package external.letiuka.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Represents deposit bank account table in a database.
 */
@Entity(name = "DepositBankAccount")
@Table(name = "deposit_bank_account")
public class DepositBankAccountEntity extends BankAccountEntity {
    @Column(name = "interest_rate")
    protected double interestRate;

    @Column(name = "accrued_interest")
    protected double accruedInterest;

    public DepositBankAccountEntity() {

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
