package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.service.BankOperationsService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.jws.WebParam;
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

    @RequestMapping(value = "dispatcher", params = "action=transfer-money", method = RequestMethod.POST)
    public ModelAndView transferMoney(@RequestParam("from") String fromNumber,
                                      @RequestParam("to") String toNumber,
                                      @RequestParam double amount,
                                      HttpSession session,
                                      @SessionAttribute String login,
                                      @SessionAttribute("latest-get-uri") String latestGetUri) {
        ModelAndView mav = new ModelAndView();
        if (amount < 0) {
            mav.setStatus(HttpStatus.BAD_REQUEST);
            return mav;
        }
        String holder;
        try {
            holder = accountService.getAccountHolder(fromNumber);
            if (login == null || !login.equals(holder)) {
                logger.log(Level.WARN, login + " attempted to transfer money from " + holder + "`s card");
                mav.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
                return mav;
            }
            accountService.transferMoney(fromNumber, toNumber, amount);
            mav.setView(new RedirectView(latestGetUri));
            return mav;
        } catch (ServiceException e) {
            logger.log(Level.WARN, "Failed to transfer money", e);
            session.setAttribute("message", "failed to transfer money");
            mav.setView(new RedirectView(latestGetUri));
            return mav;
        }

    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        // Stub
    }
}
