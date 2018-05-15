package external.letiuka.service;

import external.letiuka.mvc.model.dto.CreditBankAccountDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBankAccountServiceTest extends BankAccountServiceTest {



    @Override
    protected BankAccountService getBankAccountService() {
        return new DefaultBankAccountService(
                manager,timeProvider,interestRateProvider,
                numberGenerator,userDAO,accountDAO);
    }


}


