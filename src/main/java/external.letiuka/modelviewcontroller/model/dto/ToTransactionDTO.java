package external.letiuka.modelviewcontroller.model.dto;

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
