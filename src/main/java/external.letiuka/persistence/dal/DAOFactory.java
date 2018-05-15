package external.letiuka.persistence.dal;

import external.letiuka.persistence.dal.dao.BankAccountDAO;
import external.letiuka.persistence.dal.dao.ScheduledDAO;
import external.letiuka.persistence.dal.dao.TransactionDAO;
import external.letiuka.persistence.dal.dao.UserDAO;

public interface DAOFactory {
    UserDAO getUserDAO();
    BankAccountDAO getBankAccountDAO();
    TransactionDAO getTransactionDAO();
    ScheduledDAO getScheduledDAO();
}
