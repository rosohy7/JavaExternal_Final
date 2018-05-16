package external.letiuka.mvc.controller.concrete;

import external.letiuka.mvc.controller.HttpController;
import external.letiuka.mvc.model.AccountType;
import external.letiuka.mvc.model.dto.BankAccountDTO;
import external.letiuka.mvc.model.dto.CreditBankAccountDTO;
import external.letiuka.mvc.model.dto.DepositBankAccountDTO;
import external.letiuka.service.BankAccountService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegisterBankAccountController implements HttpController {
    private static final Logger logger = Logger.getLogger(RegisterBankAccountController.class);
    private final BankAccountService accountService;

    public RegisterBankAccountController(BankAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        logger.log(Level.TRACE, "Entered " + this.getClass().getName());

        String type = null;
        try {
            type = req.getParameter("type");
        } catch (IllegalArgumentException e) {
            try {
                resp.sendError(404);
            } finally {
                return;
            }

        }

        switch (type) {
            case "CREDIT":
                registerCredit(req, resp);
                break;
            case "DEPOSIT":
                registerDeposit(req, resp);
                break;
        }
        try {
            resp.sendRedirect("/bankapp/main/home.jsp");
        } catch (IOException e) {
            logger.log(Level.ERROR, "Could not redirect to home page after successful log in");
        }
    }

    private void registerCredit(HttpServletRequest req, HttpServletResponse resp) {
        double limit = 0;
        try {
            limit = Double.valueOf(req.getParameter("limit"));
        } catch (NumberFormatException e) {
            try {
                resp.sendError(404);
            } finally {
                return;
            }
        }
        HttpSession session = req.getSession();
        CreditBankAccountDTO accountDTO = new CreditBankAccountDTO();
        accountDTO.setHolder((String) session.getAttribute("login"));
        accountDTO.setCreditLimit(limit);
        accountDTO.setType("CREDIT");
        try {
            accountService.registerBankAccount(accountDTO);
        } catch (ServiceException e) {
            try {
                resp.sendError(404);
            } finally {
                return;
            }
        }
    }

    private void registerDeposit(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        DepositBankAccountDTO accountDTO = new DepositBankAccountDTO();
        accountDTO.setHolder((String) session.getAttribute("login"));
        accountDTO.setType("DEPOSIT");
        try {
            accountService.registerBankAccount(accountDTO);
        } catch (ServiceException e) {
            try {
                resp.sendError(404);
            } finally {
                return;
            }
        }
    }
}
