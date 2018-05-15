package external.letiuka.persistence.entities;

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
