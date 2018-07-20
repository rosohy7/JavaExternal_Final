package external.letiuka.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Represents transfer_from_transaction table in a database which is response for transactions
 * that remove money from account.
 */

@Entity(name = "FromTransaction")
@Table(name = "transfer_from_transaction")
public class FromTransactionEntity extends TransactionEntity {
    @Column(name = "receiver_account_number")
    protected String receiverNumber;

    public FromTransactionEntity() {
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }
}
