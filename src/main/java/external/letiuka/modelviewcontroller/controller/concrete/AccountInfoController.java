package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.model.dto.BankAccountDTO;
import external.letiuka.modelviewcontroller.model.dto.PaginationDTO;
import external.letiuka.service.BankOperationsService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller responsible for showing account information.
 */
@Controller
public class AccountInfoController implements HttpController {
    private static final Logger logger = Logger.getLogger(AccountInfoController.class);
    private final long PER_PAGE = 5;

    private final BankOperationsService service;

    public AccountInfoController(BankOperationsService service) {
        this.service = service;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        // I am a stub to keep interface
    }

    @RequestMapping(value = "dispatcher", params = "action=account-page", method = RequestMethod.GET)
    public ModelAndView getAccountInfo(@RequestParam(value = "page", defaultValue = "1") long targetPage,
                                                   @RequestParam("account-number") String accountNumber,
                                                   HttpSession session,
                                                   @SessionAttribute String login,
                                                   @SessionAttribute String role) {
        ModelAndView mav = new ModelAndView();
        PaginationDTO pagination = new PaginationDTO();
        pagination.setPerPage(PER_PAGE);
        pagination.setTargetPage(targetPage);
        try {

            BankAccountDTO account = service.getAccount(accountNumber, pagination);
            if (role == "ADMIN" || account.getHolder().equals(login)) {
                mav.addObject("account", account);
                mav.setViewName("/WEB-INF/account-page.jsp");
                return mav;
            } else {
                logger.log(Level.WARN, login + " tried to see "
                        + account.getHolder() + "`s bank account information");
                mav.setStatus(HttpStatus.FORBIDDEN);
                return mav;

            }
        } catch (ServiceException e) {
            logger.log(Level.DEBUG, e);
            session.setAttribute("message", "Request failed");
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;

        }
    }
}
