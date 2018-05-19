package external.letiuka.modelviewcontroller.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransactionListDTO implements Serializable {
    private List<TransactionDTO> transactions;
    private PaginationDTO pagination;

    public TransactionListDTO(){
        pagination=new PaginationDTO();
        transactions=new ArrayList<>();
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }

    public PaginationDTO getPagination() {
        return pagination;
    }

    public void setPagination(PaginationDTO pagination) {
        this.pagination = pagination;
    }
}
