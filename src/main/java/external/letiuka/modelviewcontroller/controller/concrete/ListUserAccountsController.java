package external.letiuka.modelviewcontroller.controller.concrete;


import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.model.dto.AccountListDTO;
import external.letiuka.modelviewcontroller.model.dto.PaginationDTO;
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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller responsible for listing  a patricular user`s accounts.
 */
@Controller
public class ListUserAccountsController implements HttpController {
    private static final Logger logger = Logger.getLogger(ListUserAccountsController.class);
    private static final long PER_PAGE = 5;
    private final BankOperationsService service;

    public ListUserAccountsController(BankOperationsService service) {
        this.service = service;
    }

    @RequestMapping(value = "dispatcher", params = "action=list-accounts", method = RequestMethod.GET)
    public ModelAndView listAccounts(@RequestParam(value = "page", defaultValue = "1") long targetPage,
                                     @SessionAttribute("role") String role,
                                     @SessionAttribute("login") String myLogin,
                                     @RequestParam("login") String targetLogin,
                                     HttpSession session) {
        ModelAndView mav = new ModelAndView();
        AccountListDTO accountList;
        PaginationDTO pagination = new PaginationDTO();
        pagination.setPerPage(PER_PAGE);
        pagination.setTargetPage(targetPage);

        if ("ADMIN".equals(role) || targetLogin.equals(myLogin)) {
            try {
                accountList = service.getUserAccounts(targetLogin, pagination);
            } catch (ServiceException e) {
                mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                return mav;
            }
            mav.addObject("accountList", accountList);
            mav.setViewName("/WEB-INF/user-accounts.jsp");
            return mav;
        } else {
            session.setAttribute("message", "You cannot view another user`s accounts");
           mav.setViewName("redirect:/main/home.jsp");
            return mav;
        }

    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        // Stub
    }
}
