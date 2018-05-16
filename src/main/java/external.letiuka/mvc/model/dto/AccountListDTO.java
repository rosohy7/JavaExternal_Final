package external.letiuka.mvc.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountListDTO implements Serializable{
    private List<BankAccountDTO> accounts;
    private LongPaginationDTO pagination;

    public AccountListDTO() {

        accounts = new ArrayList<>();
        pagination=new LongPaginationDTO();
    }

    public List<BankAccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BankAccountDTO> accounts) {
        this.accounts = accounts;
    }

    public LongPaginationDTO getPagination() {
        return pagination;
    }

    public void setPagination(LongPaginationDTO pagination) {
        this.pagination = pagination;
    }
}
