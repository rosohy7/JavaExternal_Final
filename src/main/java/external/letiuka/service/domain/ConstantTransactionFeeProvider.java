package external.letiuka.service.domain;

public class ConstantTransactionFeeProvider implements TransactionFeeProvider {
    private final double sender, withdrawal;

    public ConstantTransactionFeeProvider(double sender, double withdrawal) {
        this.sender = sender;
        this.withdrawal = withdrawal;
    }

    @Override
    public double getSenderFee() {
        return sender;
    }

    @Override
    public double getWithdrawalFee() {
        return withdrawal;
    }

}
