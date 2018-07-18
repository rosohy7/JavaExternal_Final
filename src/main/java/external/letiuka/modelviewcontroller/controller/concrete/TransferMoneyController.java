package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.service.BankOperationsService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller transferring money between accounts.
 */
@Controller
public class TransferMoneyController implements HttpController {
    private static final Logger logger = Logger.getLogger(TransferMoneyController.class);

    private final BankOperationsService accountService;

    public TransferMoneyController(BankOperationsService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        String fromNumber = req.getParameter("from");
        String toNumber = req.getParameter("to");
        HttpSession session = req.getSession();
        String login = (String) session.getAttribute("login");
        String holder;

        String uri = (String)session.getAttribute("latest-get-uri");
        try {
            double amount = Double.valueOf(req.getParameter("amount"));
            if(amount<0) throw new IllegalArgumentException("Amount can not be negative");
            holder = accountService.getAccountHolder(fromNumber);
            if (login == null || !login.equals(holder)) {
                logger.log(Level.WARN, login + " attempted to transfer money from " + holder + "`s card");
                try {
                    resp.sendError(405,"Cannot transfer money from somebody else`s card");
                } catch (IOException e) {
                }
                return;
            }
            accountService.transferMoney(fromNumber, toNumber, amount);

            resp.sendRedirect((uri==null)? "/bankapp/" : uri);
        } catch (IOException | ServiceException | IllegalArgumentException e) {
            logger.log(Level.WARN, "Failed to transfer money",e);
            String message = "failed to transfer money";
            session.setAttribute("message",message);
            try {
                resp.sendRedirect((uri==null)? "/bankapp/" : uri);
            } catch (IOException e1) {

            }
    }
    }
}
