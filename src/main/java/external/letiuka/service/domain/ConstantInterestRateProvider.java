package external.letiuka.service.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantInterestRateProvider implements InterestRateProvider {
    private final double creditRate;
    private final double depositRate;

    public ConstantInterestRateProvider(@Value("25") double creditRate, @Value("15") double depositRate) {
        this.creditRate = creditRate;
        this.depositRate = depositRate;
    }

    @Override
    public double getCreditInterestRate(long userId) {
        return creditRate;
    }

    @Override
    public double getDepositInterestRate(long userId) {
        return depositRate;
    }
}
