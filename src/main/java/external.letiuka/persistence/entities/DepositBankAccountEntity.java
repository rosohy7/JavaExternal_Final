package external.letiuka.persistence.entities;

/**
 * Represents deposit bank account table in a database.
 */
public class DepositBankAccountEntity extends BankAccountEntity {
    protected double interestRate;
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
