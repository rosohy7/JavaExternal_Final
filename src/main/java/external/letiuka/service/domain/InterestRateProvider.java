package external.letiuka.service.domain;

/**
 * Responsible for providing services with interest rates to assign to new
 * credit and deposit accounts
 */
public interface InterestRateProvider {
    double getCreditInterestRate(long userId);
    double getDepositInterestRate(long userId);
}
