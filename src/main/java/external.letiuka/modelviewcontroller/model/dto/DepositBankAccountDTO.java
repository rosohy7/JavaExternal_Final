package external.letiuka.modelviewcontroller.model.dto;

public class DepositBankAccountDTO extends BankAccountDTO {
    protected double interestRate;
    protected double accruedInterest;

    public DepositBankAccountDTO() {
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
