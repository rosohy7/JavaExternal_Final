package external.letiuka.service;

import external.letiuka.mvc.model.AccountType;
import external.letiuka.mvc.model.dto.CreditBankAccountDTO;
import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.dal.dao.BankAccountDAO;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.entities.UserEntity;
import external.letiuka.persistence.transaction.TransactionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.Timestamp;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public abstract class BankAccountServiceTest {
    @Mock
    protected TransactionManager manager;
    @Mock
    protected TimeProvider timeProvider;
    @Mock
    protected InterestRateProvider interestRateProvider;
    @Mock
    protected AccountNumberGenerator numberGenerator;
    @Mock
    protected UserDAO userDAO;
    @Mock
    protected BankAccountDAO accountDAO;

    protected abstract BankAccountService getBankAccountService();
    @Before
    public void init() throws DAOException {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        when(numberGenerator.getNewAccountNumber()).thenReturn("1111222233334444");
        when(timeProvider.getCurrentTimestamp()).thenReturn(new Timestamp(1000));
        when(userDAO.readUser(anyString())).thenReturn(userEntity);

    }

    @Test(timeout = 1000)
    public void registersCreditAccount() throws ServiceException {

        CreditBankAccountDTO accountDTO = new CreditBankAccountDTO();
        accountDTO.setCreditLimit(100.0);
        accountDTO.setType(AccountType.CREDIT);
        accountDTO.setHolder("john");
        BankAccountService service = getBankAccountService();
        service.registerBankAccount(accountDTO);
    }
}
