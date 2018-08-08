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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller responsible for withdrawing money from account.
 */

@Controller
public class WithdrawController implements HttpController {
    private static final Logger logger = Logger.getLogger(WithdrawController.class);

    private final BankOperationsService accountService;

    public WithdrawController(BankOperationsService accountService) {
        this.accountService = accountService;
    }


    @RequestMapping(value = "dispatcher", params = "action=withdraw", method = RequestMethod.POST)
    public ModelAndView withdraw(@RequestParam("account-number") String accountNumber,
                                 @RequestParam double amount,
                                 @SessionAttribute("latest-get-uri") String latestGetUri) {
        ModelAndView mav = new ModelAndView();
        if (amount < 0) {
            mav.setStatus(HttpStatus.BAD_REQUEST);
            return mav;
        }
        try {
            accountService.withdrawMoney(accountNumber, amount);
        } catch (ServiceException e) {
            logger.log(Level.WARN, "Failed to withdraw money");
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        mav.setView(new RedirectView(latestGetUri));
        return mav;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        // Stub
    }
}
