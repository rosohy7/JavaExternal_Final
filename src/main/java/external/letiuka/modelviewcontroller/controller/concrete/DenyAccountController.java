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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller responsible denying bank accounts requested by users.
 */
@Controller
public class DenyAccountController implements HttpController {
    private static final Logger logger= Logger.getLogger(AccountInfoController.class);
    private final BankOperationsService service;

    public DenyAccountController(BankOperationsService service) {
        this.service = service;
    }

    @RequestMapping(value = "dispatcher",params = "action=deny-account", method = RequestMethod.POST)
    public ModelAndView denyAccount(@RequestParam("account-number") String accountNumber){
        ModelAndView mav = new ModelAndView();
        try{
            service.denyAccount(accountNumber);
        }
        catch (ServiceException e){
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            logger.log(Level.ERROR, "Failed to deny account",e);
            return mav;
        }
        mav.setViewName("redirect:/dispatcher?action=list-unconfirmed");
        return mav;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        //Stub
    }
}
