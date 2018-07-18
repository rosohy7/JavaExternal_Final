package external.letiuka.modelviewcontroller.model.dto;

/**
 * DTO for representing transactions that deposit money to account.
 */
public class ToTransactionDTO extends TransactionDTO {
    protected String senderNumber;

    public ToTransactionDTO() {
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }
}
