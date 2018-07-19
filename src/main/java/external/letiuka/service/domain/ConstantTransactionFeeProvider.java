package external.letiuka.service.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantTransactionFeeProvider implements TransactionFeeProvider {
    private final double sender, withdrawal;


    public ConstantTransactionFeeProvider(@Value("1.0") double sender,@Value("2.0") double withdrawal) {
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
