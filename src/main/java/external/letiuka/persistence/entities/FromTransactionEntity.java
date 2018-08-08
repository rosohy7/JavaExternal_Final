package external.letiuka.persistence.entities;

/**
 * Represents transfer_from_transaction table in a database which is response for transactions
 * that remove money from account.
 */
public class FromTransactionEntity extends TransactionEntity {
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
