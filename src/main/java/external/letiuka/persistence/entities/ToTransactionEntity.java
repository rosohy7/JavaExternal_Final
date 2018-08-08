package external.letiuka.persistence.entities;

/**
 * Represents transfer_to_transaction table in a database that is
 * responsible for transactions that put money in account.
 */
public class ToTransactionEntity extends TransactionEntity {
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
