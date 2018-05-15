package external.letiuka.service;

public interface InterestRateProvider {
    double getCreditInterestRate(long userId);
    double getDepositInterestRate(long userId);
}
