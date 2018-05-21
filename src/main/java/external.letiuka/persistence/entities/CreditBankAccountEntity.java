package external.letiuka.persistence.entities;

/**
 * Represents credit bank account table in a database.
 */
public class CreditBankAccountEntity extends BankAccountEntity {
    protected double creditLimit;
    protected double interestRate;
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
