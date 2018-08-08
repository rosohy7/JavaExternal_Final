package external.letiuka.service.domain;

public class ConstantInterestRateProvider implements InterestRateProvider {
    private final double creditRate;
    private final double depositRate;

    public ConstantInterestRateProvider(double creditRate, double depositRate) {
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
