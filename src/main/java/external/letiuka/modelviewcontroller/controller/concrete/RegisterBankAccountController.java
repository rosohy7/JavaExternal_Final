package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.model.dto.CreditBankAccountDTO;
import external.letiuka.modelviewcontroller.model.dto.DepositBankAccountDTO;
import external.letiuka.service.BankOperationsService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller responsible for requesting new bank accounts to be created.
 */
@Controller
public class RegisterBankAccountController implements HttpController {
    private static final Logger logger = Logger.getLogger(RegisterBankAccountController.class);
    private final BankOperationsService accountService;

    public RegisterBankAccountController(BankOperationsService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "dispatcher", params = "action=register-account", method = RequestMethod.POST)
    public ModelAndView registerAccount(@RequestParam String type,
                                        @RequestParam(defaultValue = "0") double limit,
                                        @SessionAttribute String login) {
        switch (type) {
            case "CREDIT":
                return registerCredit(login, limit);
            case "DEPOSIT":
                return registerDeposit(login);
            default:
                ModelAndView mav = new ModelAndView();
                mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                return mav;
        }
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        // stub

    }

    private ModelAndView registerCredit(String login, double limit) {
        ModelAndView mav = new ModelAndView();

        if (limit < 0) {
            mav.setStatus(HttpStatus.BAD_REQUEST);
            logger.log(Level.DEBUG, "Failed to request accoutn (limit < 0)");
            return mav;
        }
        CreditBankAccountDTO accountDTO = new CreditBankAccountDTO();
        accountDTO.setHolder(login);
        accountDTO.setCreditLimit(limit);
        accountDTO.setType("CREDIT");
        try {
            accountService.registerBankAccount(accountDTO);
        } catch (ServiceException e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            logger.log(Level.ERROR, "Failed to register account", e);
            return mav;
        }
        mav.setViewName("redirect:/main/home.jsp");
        return mav;
    }

    private ModelAndView registerDeposit(String login) {
        ModelAndView mav = new ModelAndView();
        DepositBankAccountDTO accountDTO = new DepositBankAccountDTO();
        accountDTO.setHolder(login);
        accountDTO.setType("DEPOSIT");
        try {
            accountService.registerBankAccount(accountDTO);
        } catch (ServiceException e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            logger.log(Level.ERROR, "Failed to register account", e);
            return mav;
        }
        mav.setViewName("redirect:/main/home.jsp");
        return mav;

    }
}
