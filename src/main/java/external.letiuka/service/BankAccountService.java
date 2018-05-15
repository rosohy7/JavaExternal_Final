package external.letiuka.service;

import external.letiuka.mvc.model.dto.BankAccountDTO;

public interface BankAccountService {
    void registerBankAccount(BankAccountDTO accountDTO) throws ServiceException;

}
