package external.letiuka.persistence.entities;

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
