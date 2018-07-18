package external.letiuka.service;

import external.letiuka.modelviewcontroller.model.AccountType;
import external.letiuka.modelviewcontroller.model.TransactionType;
import external.letiuka.modelviewcontroller.model.dto.*;
import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.dal.dao.BankAccountDAO;
import external.letiuka.persistence.dal.dao.TransactionDAO;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.entities.*;
import external.letiuka.persistence.transaction.TransactionException;
import external.letiuka.persistence.transaction.TransactionManager;
import external.letiuka.service.domain.AccountNumberGenerator;
import external.letiuka.service.domain.InterestRateProvider;
import external.letiuka.service.domain.TimeProvider;
import external.letiuka.service.domain.TransactionFeeProvider;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultBankOperationsService implements BankOperationsService {
    private static final Logger logger = Logger.getLogger(DefaultBankOperationsService.class);

    private final TransactionManager manager;
    private final TimeProvider timeProvider;
    private final InterestRateProvider interestRateProvider;
    private final AccountNumberGenerator numberGenerator;
    private final UserDAO userDAO;
    private final BankAccountDAO accountDAO;
    private final TransactionFeeProvider feeProvider;
    private final TransactionDAO transactionDAO;


    public DefaultBankOperationsService(
            TransactionManager manager, TimeProvider timeProvider,
            InterestRateProvider interestRateProvider, AccountNumberGenerator numberGenerator,
            UserDAO userDAO, BankAccountDAO accountDAO, TransactionFeeProvider feeProvider,
            TransactionDAO transactionDAO) {
        this.manager = manager;
        this.timeProvider = timeProvider;
        this.interestRateProvider = interestRateProvider;
        this.numberGenerator = numberGenerator;
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
        this.feeProvider = feeProvider;
        this.transactionDAO = transactionDAO;
    }


    @Override
    public void registerBankAccount(BankAccountDTO accountDTO) throws ServiceException {
        BankAccountEntity entity = null;
        logger.log(Level.TRACE, "Trying to register bank account");
        switch (accountDTO.getType()) {
            case "CREDIT":
                CreditBankAccountEntity credit = new CreditBankAccountEntity();
                entity = credit;
                CreditBankAccountDTO creditDTO = (CreditBankAccountDTO) accountDTO;
                credit.setAccruedInterest(0);
                credit.setCreditLimit(creditDTO.getCreditLimit());
                credit.setType(AccountType.CREDIT);
                break;
            case "DEPOSIT":
                DepositBankAccountEntity deposit = new DepositBankAccountEntity();
                entity = deposit;
                deposit.setAccruedInterest(0);
                deposit.setType(AccountType.DEPOSIT);
                break;
        }

        entity.setAccountBalance(0);
        entity.setConfirmed(false);
        entity.setExpires(new Date(timeProvider.getMillisDaysLater(365)));
        entity.setLatestUpdate(new Date(timeProvider.getCurrentMillis()));
        String holder = accountDTO.getHolder();
        try {
            logger.log(Level.TRACE, "Inserting new bank account into database");
            manager.beginTransaction();
            entity.setAccountNumber(numberGenerator.getNewAccountNumber());
            UserEntity user = userDAO.readUser(holder);
            if (user == null) throw new ServiceException("Cannot find card holder");
            entity.setUser(user);
            switch (accountDTO.getType()) {
                case "CREDIT":
                    ((CreditBankAccountEntity) entity).setInterestRate(
                            interestRateProvider.getCreditInterestRate(user.getId()));
                    break;
                case "DEPOSIT":
                    ((DepositBankAccountEntity) entity).setInterestRate(
                            interestRateProvider.getDepositInterestRate(user.getId()));
                    break;
            }
            manager.getSession().save(entity);
            manager.commit();
            logger.log(Level.TRACE, "Successfully inserted new bank account into database");
        } catch (Exception e) {
            manager.rollback();
            throw new ServiceException(e);
        }
    }

    @Override
    public AccountListDTO getUnconfirmedAccounts
            (PaginationDTO paginationDTO) throws ServiceException {
        long entryCount, lastPage;
        long targetPage = paginationDTO.getTargetPage();
        long perPage = paginationDTO.getPerPage();
        List<BankAccountDTO> accountDTOList = new ArrayList<BankAccountDTO>();
        List<BankAccountEntity> accountEntityList;
        AccountListDTO accountList = new AccountListDTO();
        accountList.setPagination(paginationDTO);

        try {
            manager.getSession();
            entryCount = accountDAO.getUnconfirmedAccountCount();
            lastPage = (entryCount - 1) / perPage + 1;
            accountList.getPagination().setLastPage(lastPage);
            if (targetPage <= lastPage) {
                accountEntityList = accountDAO.readUnconfirmedAccounts(
                        perPage * (targetPage - 1), perPage);
                for (BankAccountEntity entity : accountEntityList) {
                    accountDTOList.add(accountEntityToDTO(entity));
                }
            } else throw new ServiceException("Page " + targetPage + " does not exist");
            accountList.setAccounts(accountDTOList);
            manager.commit();
            return accountList;
        } catch (Exception e) {
            manager.rollback();
            throw new ServiceException(e);
        }
    }

    @Override
    public AccountListDTO getUserAccounts
            (String login, PaginationDTO paginationDTO) throws ServiceException {
        long entryCount, lastPage;
        long targetPage = paginationDTO.getTargetPage();
        long perPage = paginationDTO.getPerPage();
        List<BankAccountDTO> accountDTOList = new ArrayList<BankAccountDTO>();
        List<BankAccountEntity> accountEntityList;
        AccountListDTO accountList = new AccountListDTO();
        accountList.setPagination(paginationDTO);
        try {
            manager.beginTransaction();
            entryCount = accountDAO.getUserAccountCount(login);
            lastPage = (entryCount - 1) / perPage + 1;
            accountList.getPagination().setLastPage(lastPage);
            if (targetPage <= lastPage) {
                accountEntityList = accountDAO.readUserAccounts(
                        login, perPage * (targetPage - 1), perPage);
                for (BankAccountEntity entity : accountEntityList) {
                    BankAccountDTO dto = accountEntityToDTO(entity);
                    dto.setHolder(login);
                    accountDTOList.add(dto);
                }
            } else throw new ServiceException("Page " + targetPage + " does not exist");
            accountList.setAccounts(accountDTOList);
            manager.commit();
            return accountList;
        } catch (Exception e) {
            manager.rollback();
            throw new ServiceException(e);
        }

    }

    @Override
    public BankAccountDTO getAccount(String accountNumber, PaginationDTO pagination) throws ServiceException {
        BankAccountDTO accountDTO;
        BankAccountEntity accountEntity;
        UserEntity user;
        long accountId;
        long transactionCount;
        long lastPage, perPage, targetPage;
        perPage = pagination.getPerPage();
        targetPage = pagination.getTargetPage();
        List<TransactionEntity> transactionsList;
        List<TransactionDTO> transactionsDTOList = new ArrayList<>();

        try {
            manager.beginTransaction();
            Session session = manager.getSession();
            accountEntity = accountDAO.readAccount(accountNumber);
            accountDTO = accountEntityToDTO(accountEntity);
            accountId = accountEntity.getId();
            transactionCount = transactionDAO.getAccountTransactionsCount(accountId);
            lastPage = (transactionCount - 1) / perPage + 1;
            pagination.setLastPage(lastPage);
            if (targetPage <= lastPage) {
                transactionsList = transactionDAO.readAccountTransactions(accountId,
                        perPage * (targetPage - 1), perPage);
                for (TransactionEntity tr : transactionsList) {
                    transactionsDTOList.add(transactionEntityToDTO(tr));
                }
            }
            accountDTO.getHistory().setPagination(pagination);
            accountDTO.getHistory().setTransactions(transactionsDTOList);
            user = session.get(UserEntity.class, accountEntity.getUser().getId());
            accountDTO.setHolder(user.getLogin());
            manager.commit();
            return accountDTO;


        } catch (Exception e) {
            manager.rollback();
            throw new ServiceException(e);
        }
    }

    @Override
    public void confirmAccount(String accountNumber) throws ServiceException {
        Date expires = new Date(timeProvider.getMillisDaysLater(365));
        try {
            manager.beginTransaction();
            BankAccountEntity accountEntity = accountDAO.readAccount(accountNumber);
            accountEntity.setExpires(expires);
            accountEntity.setConfirmed(true);
            if(accountEntity!=null)
            manager.getSession().update(accountEntity);
            manager.commit();
        } catch (Exception e) {
            manager.rollback();
            throw new ServiceException(e);
        }
    }

    @Override
    public void denyAccount(String accountNumber) throws ServiceException {
        BankAccountEntity account;
        long id;
        try {
            manager.beginTransaction();
            account = accountDAO.readAccount(accountNumber);

            if (account.isConfirmed()) {
                logger.log(Level.WARN, "Tried to deny account that is already confirmed");
            }
            if(account!=null)
            manager.getSession().delete(account);
            manager.commit();
        } catch (Exception e) {
            manager.rollback();
            throw new ServiceException(e);
        }
    }

    private BankAccountDTO accountEntityToDTO(BankAccountEntity entity) {
        BankAccountDTO dto = null;
        switch (entity.getType()) {
            case CREDIT:
                CreditBankAccountDTO creditDTO = new CreditBankAccountDTO();
                dto = creditDTO;
                CreditBankAccountEntity creditEntity = (CreditBankAccountEntity) entity;
                creditDTO.setType(AccountType.CREDIT.toString());
                creditDTO.setCreditLimit(creditEntity.getCreditLimit());
                creditDTO.setAccruedInterest(creditEntity.getAccruedInterest());
                creditDTO.setInterestRate(creditEntity.getInterestRate());
                break;
            case DEPOSIT:
                DepositBankAccountDTO depositDTO = new DepositBankAccountDTO();
                dto = depositDTO;
                DepositBankAccountEntity depositEntity = (DepositBankAccountEntity) entity;
                depositDTO.setType(AccountType.DEPOSIT.toString());
                depositDTO.setAccruedInterest(depositEntity.getAccruedInterest());
                depositDTO.setInterestRate(depositEntity.getInterestRate());
                break;
        }
        dto.setAccountBalance(entity.getAccountBalance());
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setConfirmed(entity.isConfirmed());
        dto.setExpires(entity.getExpires());
        return dto;

    }

    private TransactionDTO transactionEntityToDTO(TransactionEntity entity) {
        TransactionDTO dto;
        switch (entity.getType()) {
            case TRANSFER_FROM:
                FromTransactionDTO from = new FromTransactionDTO();
                from.setReceiverNumber(((FromTransactionEntity) entity).getReceiverNumber());
                dto = from;
                break;
            case TRANSFER_TO:
                ToTransactionDTO to = new ToTransactionDTO();
                to.setSenderNumber(((ToTransactionEntity) entity).getSenderNumber());
                dto = to;
                break;
            default:
                logger.log(Level.ERROR, "Transaction type not supported");
                return null;
        }
        dto.setBalanceChange(entity.getBalanceChange());
        dto.setBankFee(entity.getBankFee());
        dto.setType(entity.getType().toString());
        dto.setTimestamp(entity.getTimeStamp());
        return dto;

    }

    @Override
    public String getAccountHolder(String accountNumber) throws ServiceException {
        BankAccountEntity accountEntity;
        UserEntity user;
        try {
            manager.beginTransaction();
            accountEntity = accountDAO.readAccount(accountNumber);
            String login = accountEntity.getUser().getLogin();
            manager.commit();
            return login;
        } catch (Exception e) {
            manager.rollback();
            throw new ServiceException(e);
        }
    }

    @Override
    public void transferMoney(String numberFrom, String numberTo, double amount) throws ServiceException {
        BankAccountEntity accountFrom, accountTo;
        ToTransactionEntity transactionTo;
        FromTransactionEntity transactionFrom;
        double availableMoney;
        double fee;
        double resultAmount = amount;
        Timestamp now = timeProvider.getCurrentTimestamp();

        UserEntity user;
        try {
            manager.beginTransaction();
            Session session = manager.getSession();
            accountFrom = accountDAO.readAccount(numberFrom);
            if (accountFrom == null) {
                manager.rollback();
                throw new ServiceException("Sender account does not exist");
            }
            if (accountFrom.getExpires().before(now)) {
                manager.rollback();
                throw new ServiceException("Sender account is expired");
            }
            if (!accountFrom.isConfirmed()) {
                manager.rollback();
                throw new ServiceException("Sender account is not confirmed");
            }
            accountTo = accountDAO.readAccount(numberTo);

            if (accountTo == null) {
                manager.rollback();
                throw new ServiceException("Receiver account does not exist");
            }
            if (accountTo.getExpires().before(now)) {
                manager.rollback();
                throw new ServiceException("Receiver account is expired");
            }
            if (!accountTo.isConfirmed()) {
                manager.rollback();
                throw new ServiceException("Receiver account is not confirmed");
            }
            availableMoney = accountFrom.getAccountBalance();
            if (accountFrom.getType() == AccountType.CREDIT)
                availableMoney += ((CreditBankAccountEntity) accountFrom).getCreditLimit();
            if (availableMoney < amount) {
                manager.rollback();
                throw new ServiceException("Not enough money for transfer");
            }
            fee = feeProvider.getSenderFee() * amount / 100.0;
            resultAmount = amount - fee;
            accountFrom.setAccountBalance(accountFrom.getAccountBalance() - amount);
            accountTo.setAccountBalance(accountTo.getAccountBalance() + resultAmount);

            transactionFrom = new FromTransactionEntity();
            transactionFrom.setReceiverNumber(numberTo);
            transactionFrom.setBankAccount(accountFrom);
            transactionFrom.setBalanceChange(-amount);
            transactionFrom.setBankFee(fee);
            transactionFrom.setType(TransactionType.TRANSFER_FROM);
            transactionFrom.setTimeStamp(now);

            transactionTo = new ToTransactionEntity();
            transactionTo.setSenderNumber(numberFrom);
            transactionTo.setBankAccount(accountTo);
            transactionTo.setBalanceChange(resultAmount);
            transactionTo.setBankFee(fee);
            transactionTo.setType(TransactionType.TRANSFER_TO);
            transactionTo.setTimeStamp(now);

            session.update(accountFrom);
            session.update(accountTo);
            transactionFrom.setPair(transactionTo);
            transactionTo.setPair(transactionFrom);
            session.save(transactionFrom);
            session.save(transactionTo);


            manager.commit();


        } catch (PersistenceException e) {
            manager.rollback();
            throw new ServiceException(e);
        }
    }

    @Override
    public void withdrawMoney(String accountNumber, double amount) throws ServiceException {
        BankAccountEntity accountEntity;
        double availableMoney, fee;
        FromTransactionEntity transaction = new FromTransactionEntity();
        fee = feeProvider.getWithdrawalFee() * amount / 100.0;
        try {
            manager.beginTransaction();
            Session session = manager.getSession();
            accountEntity = accountDAO.readAccount(accountNumber);
            if (accountEntity == null) {
                manager.rollback();
                throw new ServiceException("Account does not exist");
            }
            if (!accountEntity.isConfirmed()) {
                manager.rollback();
                throw new ServiceException("Account is not confirmed");
            }
            availableMoney = accountEntity.getAccountBalance();
            if (accountEntity.getType() == AccountType.CREDIT)
                availableMoney += ((CreditBankAccountEntity) accountEntity).getCreditLimit();
            if (availableMoney < amount + fee) {
                manager.rollback();
                throw new ServiceException("Not enough money on account");
            }
            accountEntity.setAccountBalance(accountEntity.getAccountBalance() - amount - fee);
            session.update(accountEntity);
            transaction.setTimeStamp(timeProvider.getCurrentTimestamp());
            transaction.setType(TransactionType.TRANSFER_FROM);
            transaction.setBankFee(fee);
            transaction.setBalanceChange(-amount - fee);
            transaction.setBankAccount(accountEntity);
            session.save(transaction);
            manager.commit();
        } catch (PersistenceException e) {
            manager.rollback();
            throw new ServiceException(e);
        }
    }

    @Override
    public void depositMoney(String accountNumber, double amount) throws ServiceException {
        ToTransactionEntity transaction = new ToTransactionEntity();
        Timestamp now = timeProvider.getCurrentTimestamp();
        BankAccountEntity accountEntity;
        try {
            manager.beginTransaction();
            Session session = manager.getSession();
            accountEntity = accountDAO.readAccount(accountNumber);
            if (accountEntity == null) {
                manager.rollback();
                throw new ServiceException("Account does not exist");
            }
            if (accountEntity.getExpires().before(now)) {
                manager.rollback();
                throw new ServiceException("Account is expired");
            }
            if (!accountEntity.isConfirmed()) {
                manager.rollback();
                throw new ServiceException("Account is not confirmed");
            }
            accountEntity.setAccountBalance(accountEntity.getAccountBalance() + amount);
            session.update(accountEntity);
            transaction.setTimeStamp(now);
            transaction.setBalanceChange(amount);
            transaction.setBankAccount(accountEntity);
            transaction.setBankFee(0);
            transaction.setType(TransactionType.TRANSFER_TO);
            session.save(transaction);
            manager.commit();
        }catch (PersistenceException e) {
            manager.rollback();
            throw new ServiceException(e);
        }
    }
}
