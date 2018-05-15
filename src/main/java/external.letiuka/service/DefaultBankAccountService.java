package external.letiuka.service;

import external.letiuka.mvc.model.AccountType;
import external.letiuka.mvc.model.dto.BankAccountDTO;
import external.letiuka.mvc.model.dto.CreditBankAccountDTO;
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

public class DefaultBankAccountService implements BankAccountService {
    private TransactionManager manager;
    private TimeProvider timeProvider;
    private InterestRateProvider interestRateProvider;
    private AccountNumberGenerator numberGenerator;
    private UserDAO userDAO;
    private BankAccountDAO accountDAO;

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
    public void registerBankAccount(BankAccountDTO accountDTO) throws ServiceException{
        BankAccountEntity entity=null;
        logger.log(Level.TRACE,"Trying to register bank account");
        switch(accountDTO.getType()){
            case CREDIT:
                CreditBankAccountEntity credit = new CreditBankAccountEntity();
                entity = credit;
                CreditBankAccountDTO creditDTO= (CreditBankAccountDTO) accountDTO;
                credit.setAccruedInterest(0);
                credit.setCreditLimit(creditDTO.getCreditLimit());
                credit.setType(AccountType.CREDIT);
                break;
            case DEPOSIT:
                DepositBankAccountEntity deposit = new DepositBankAccountEntity();
                entity = deposit;
                deposit.setAccruedInterest(0);
                deposit.setType(AccountType.DEPOSIT);
                break;
        }

        entity.setAccountBalance(0);
        entity.setAccountNumber(numberGenerator.getNewAccountNumber());
        entity.setConfirmed(false);
        entity.setExpires(new Date(timeProvider.getMillisDaysLater(30)));
        entity.setLatestUpdate(new Date(timeProvider.getCurrentMillis()));
        String holder = accountDTO.getHolder();
        try{
            logger.log(Level.TRACE,"Inserting new bank account into database");
            manager.beginTransaction();
            UserEntity user = userDAO.readUser(holder);
            if(user==null) throw new ServiceException("Cannot find card holder");
            entity.setUserId(user.getId());
            switch(accountDTO.getType()){
                case CREDIT:
                    ((CreditBankAccountEntity) entity).setInterestRate(
                            interestRateProvider.getCreditInterestRate(user.getId()));
                    break;
                case DEPOSIT:
                    ((DepositBankAccountEntity) entity).setInterestRate(user.getId());
                    break;
            }
            accountDAO.insertAccount(entity);
            manager.commit();
            logger.log(Level.TRACE,"Successfully inserted new bank account into database");
        }catch(TransactionException e){
            try {
                manager.rollback();
            } catch (TransactionException e1) {}
            throw new ServiceException(e);
        }
        catch(DAOException e)
        {
            try {
                manager.rollback();
            } catch (TransactionException e1) {}
            throw new ServiceException(e);
        }


    }
}
