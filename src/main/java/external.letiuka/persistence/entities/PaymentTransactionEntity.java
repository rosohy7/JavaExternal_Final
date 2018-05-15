package external.letiuka.persistence.entities;

public class PaymentTransactionEntity extends TransactionEntity {
    protected String paymentNumber;

    public PaymentTransactionEntity() {
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }
}
