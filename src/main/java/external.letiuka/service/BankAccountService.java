package external.letiuka.service;

import external.letiuka.modelviewcontroller.model.dto.AccountListDTO;
import external.letiuka.modelviewcontroller.model.dto.BankAccountDTO;
import external.letiuka.modelviewcontroller.model.dto.PaginationDTO;

public interface BankAccountService {
    void registerBankAccount(BankAccountDTO accountDTO) throws ServiceException;
    AccountListDTO getUnconfirmedAccounts(PaginationDTO paginationDTO) throws ServiceException;
    AccountListDTO getUserAccounts(String login, PaginationDTO paginationDTO) throws ServiceException;
    BankAccountDTO getAccount(String accountNumber, PaginationDTO pagination) throws ServiceException;
    void confirmAccount(String accountNumber) throws ServiceException;
    String getAccountHolder(String accountNumber) throws ServiceException;
    void transferMoney(String numberFrom, String numberToo, double amount) throws ServiceException;
    void withdrawMoney(String accountNumber, double amount) throws ServiceException;
    void depositMoney(String accountNumber, double amount)throws ServiceException;
}
