package external.letiuka.service;

import external.letiuka.mvc.model.AccountType;
import external.letiuka.mvc.model.dto.*;
import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.dal.dao.BankAccountDAO;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.entities.BankAccountEntity;
import external.letiuka.persistence.entities.CreditBankAccountEntity;
import external.letiuka.persistence.entities.DepositBankAccountEntity;
import external.letiuka.persistence.entities.UserEntity;
import external.letiuka.persistence.transaction.TransactionException;
import external.letiuka.persistence.transaction.TransactionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DefaultBankAccountService implements BankAccountService {
    private final TransactionManager manager;
    private final TimeProvider timeProvider;
    private final InterestRateProvider interestRateProvider;
    private final AccountNumberGenerator numberGenerator;
    private final UserDAO userDAO;
    private final BankAccountDAO accountDAO;

    public DefaultBankAccountService
            (TransactionManager manager, TimeProvider timeProvider,
             InterestRateProvider interestRateProvider, AccountNumberGenerator numberGenerator,
             UserDAO userDAO, BankAccountDAO accountDAO) {
        this.manager = manager;
        this.timeProvider = timeProvider;
        this.interestRateProvider = interestRateProvider;
        this.numberGenerator = numberGenerator;
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
    }

    private static final Logger logger = Logger.getLogger(DefaultBankAccountService.class);

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
        entity.setAccountNumber(numberGenerator.getNewAccountNumber());
        entity.setConfirmed(false);
        entity.setExpires(new Date(timeProvider.getMillisDaysLater(365)));
        entity.setLatestUpdate(new Date(timeProvider.getCurrentMillis()));
        String holder = accountDTO.getHolder();
        try {
            logger.log(Level.TRACE, "Inserting new bank account into database");
            manager.beginTransaction();
            UserEntity user = userDAO.readUser(holder);
            if (user == null) throw new ServiceException("Cannot find card holder");
            entity.setUserId(user.getId());
            switch (accountDTO.getType()) {
                case "CREDIT":
                    ((CreditBankAccountEntity) entity).setInterestRate(
                            interestRateProvider.getCreditInterestRate(user.getId()));
                    break;
                case "DEPOSIT":
                    ((DepositBankAccountEntity) entity).setInterestRate(user.getId());
                    break;
            }
            accountDAO.insertAccount(entity);
            manager.commit();
            logger.log(Level.TRACE, "Successfully inserted new bank account into database");
        } catch (TransactionException e) {
            try {
                manager.rollback();
            } catch (TransactionException e1) {
            }
            throw new ServiceException(e);
        } catch (DAOException e) {
            try {
                manager.rollback();
            } catch (TransactionException e1) {
            }
            throw new ServiceException(e);
        }
    }

    @Override
    public AccountListDTO GetUnconfirmedAccounts
            (LongPaginationDTO paginationDTO) throws ServiceException {
        long entryCount, lastPage;
        long targetPage = paginationDTO.getTargetPage();
        long perPage = paginationDTO.getPerPage();
        List<BankAccountDTO> accountDTOList = new ArrayList<BankAccountDTO>();
        List<BankAccountEntity> accountEntityList;
        AccountListDTO accountList = new AccountListDTO();
        accountList.setPagination(paginationDTO);

        try {
            manager.getConnection();
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
            return accountList;
        } catch (TransactionException e) {
            try {
                manager.rollback();
            } catch (TransactionException e1) {
            }
            throw new ServiceException(e);
        } catch (DAOException e) {
            try {
                manager.rollback();
            } catch (TransactionException e1) {
            }
            throw new ServiceException(e);
        }
    }

    private BankAccountDTO accountEntityToDTO(BankAccountEntity entity) {
        BankAccountDTO dto=null;
        switch (entity.getType()) {
            case CREDIT:
                CreditBankAccountDTO creditDTO=new CreditBankAccountDTO();
                dto=creditDTO;
                CreditBankAccountEntity creditEntity = (CreditBankAccountEntity) entity;
                creditDTO.setType(AccountType.CREDIT.toString());
                creditDTO.setCreditLimit(creditEntity.getCreditLimit());
                creditDTO.setAccruedInterest(creditEntity.getAccruedInterest());
                creditDTO.setInterestRate(creditEntity.getInterestRate());
                break;
            case DEPOSIT:
                DepositBankAccountDTO depositDTO=new DepositBankAccountDTO();
                dto=depositDTO;
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
        dto.setLatestUpdate(entity.getLatestUpdate());
        return dto;

    }

}
