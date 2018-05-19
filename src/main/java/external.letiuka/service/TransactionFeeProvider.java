package external.letiuka.service;

public interface TransactionFeeProvider {
    double getSenderFee();
    double getWithdrawalFee();

}

