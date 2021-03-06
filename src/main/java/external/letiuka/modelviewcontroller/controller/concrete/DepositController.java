package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.service.BankOperationsService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller responsible for depositing money to account.
 */
public class DepositController implements HttpController {
    private static final Logger logger = Logger.getLogger(DepositController.class);

    private final BankOperationsService accountService;

    public DepositController(BankOperationsService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {

        String accountNumber = req.getParameter("account-number");
        HttpSession session = req.getSession();

        try {
            double amount = Double.valueOf(req.getParameter("amount"));
            if(amount<0) throw new IllegalArgumentException("Amount can not be negative");
            accountService.depositMoney(accountNumber,amount);
            String uri = (String)session.getAttribute("latest-get-uri");
            resp.sendRedirect((uri==null)? "/bankapp/" : uri);
        } catch (IOException | ServiceException | IllegalArgumentException e) {
            logger.log(Level.WARN, "Failed to deposit money "+e.getMessage());
            logger.log(Level.DEBUG, e.getStackTrace());
            try {
                resp.sendError(404);
            } catch (IOException e1) {
            }

        }
    }
}

