package external.letiuka.service;

import external.letiuka.modelviewcontroller.model.dto.AccountListDTO;
import external.letiuka.modelviewcontroller.model.dto.BankAccountDTO;
import external.letiuka.modelviewcontroller.model.dto.PaginationDTO;

/**
 * Responsible for managing bank operations
 */
public interface BankOperationsService {
    /*
    Registers bank account in a database that is only visible to admin until he confirms it.
     */
    void registerBankAccount(BankAccountDTO accountDTO) throws ServiceException;

    /*
    Reads a list of unconfirmed accounts from database according to pagination data.
     */
    AccountListDTO getUnconfirmedAccounts(PaginationDTO paginationDTO) throws ServiceException;

    /*
    Reads a list of a user`s accounts from database according to pagination data.
     */
    AccountListDTO getUserAccounts(String login, PaginationDTO paginationDTO) throws ServiceException;

    /*
    Reads a list of a user`s accounts from database according to pagination data.
     */
    BankAccountDTO getAccount(String accountNumber, PaginationDTO pagination) throws ServiceException;

    /*
    Used to confirm new bank account by admin.
     */
    void confirmAccount(String accountNumber) throws ServiceException;

    /*
    Used to deny new bank account by admin deleting request from database.
     */
    void denyAccount(String accountNumber) throws ServiceException;

    /*
     Used to read account`s holder login
    */
    String getAccountHolder(String accountNumber) throws ServiceException;

    /*
    Used to transfer money between accounts.
     */
    void transferMoney(String numberFrom, String numberToo, double amount) throws ServiceException;

    /*
    Used to withdraw money from account (by admin).
     */
    void withdrawMoney(String accountNumber, double amount) throws ServiceException;

    /*
    Used to deposit money to account (by admin).
     */
    void depositMoney(String accountNumber, double amount) throws ServiceException;
}
