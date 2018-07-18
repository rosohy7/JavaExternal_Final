package external.letiuka.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Represents payment_transaction table in a database.
 */
@Entity(name = "PaymentTransaction")
@Table(name = "payment_transaction")
@PrimaryKeyJoinColumn(name = "transaction_id", referencedColumnName = "transaction_id")
public class PaymentTransactionEntity extends TransactionEntity {
    @Column(name = "payment_number")
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
