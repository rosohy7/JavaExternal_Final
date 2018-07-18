package external.letiuka.service.domain;

/**
 * Responsible for providing services with fee taken by bank on transactions
 */
public interface TransactionFeeProvider {
    double getSenderFee();
    double getWithdrawalFee();

}

