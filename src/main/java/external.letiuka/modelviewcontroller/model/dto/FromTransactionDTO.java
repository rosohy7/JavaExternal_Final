package external.letiuka.modelviewcontroller.model.dto;


/**
 * DTO for representing transactions that take money from account.
 */
public class FromTransactionDTO extends TransactionDTO {
    protected String receiverNumber;

    public FromTransactionDTO() {
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }
}
