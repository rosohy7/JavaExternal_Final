package external.letiuka.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Represents transfer_to_transaction table in a database that is
 * responsible for transactions that put money in account.
 */
@Entity(name = "ToTransaction")
@Table(name = "transfer_to_transaction")
@PrimaryKeyJoinColumn(name = "transaction_id", referencedColumnName = "transaction_id")
public class ToTransactionEntity extends TransactionEntity {
    @Column(name = "sender_account_number")
    protected String senderNumber;

    public ToTransactionEntity() {
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }
}
