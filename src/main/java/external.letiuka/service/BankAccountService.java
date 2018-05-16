package external.letiuka.service;

import external.letiuka.mvc.model.dto.AccountListDTO;
import external.letiuka.mvc.model.dto.BankAccountDTO;
import external.letiuka.mvc.model.dto.LongPaginationDTO;

public interface BankAccountService {
    void registerBankAccount(BankAccountDTO accountDTO) throws ServiceException;
    AccountListDTO GetUnconfirmedAccounts(LongPaginationDTO paginationDTO) throws ServiceException;

}
