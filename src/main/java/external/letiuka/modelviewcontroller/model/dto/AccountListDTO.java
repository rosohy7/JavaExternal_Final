package external.letiuka.modelviewcontroller.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for transfering bank account lists with pagination data.
 */
public class AccountListDTO implements Serializable {
    private List<BankAccountDTO> accounts;
    private PaginationDTO pagination;

    public AccountListDTO() {

        accounts = new ArrayList<>();
        pagination = new PaginationDTO();
    }

    public List<BankAccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BankAccountDTO> accounts) {
        this.accounts = accounts;
    }

    public PaginationDTO getPagination() {
        return pagination;
    }

    public void setPagination(PaginationDTO pagination) {
        this.pagination = pagination;
    }
}
