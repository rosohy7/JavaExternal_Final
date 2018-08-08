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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller responsible for listing bank accounts requested by users to be confirmed.
 */
@Controller
public class ListUnconfirmedController implements HttpController {
    private static final Logger logger = Logger.getLogger(ListUnconfirmedController.class);
    private static final long PER_PAGE = 10;
    private final BankOperationsService service;

    public ListUnconfirmedController(BankOperationsService service) {
        this.service = service;
    }


    @RequestMapping(value = "/dispatcher", params = "action=list-unconfirmed", method = RequestMethod.GET)
    public ModelAndView listUnconfirmed(@RequestParam(value = "page", defaultValue = "1") long targetPage) {
        ModelAndView mav = new ModelAndView();
        AccountListDTO accountList;
        PaginationDTO pagination = new PaginationDTO();
        pagination.setTargetPage(targetPage);
        pagination.setPerPage(PER_PAGE);

        try {
            accountList = service.getUnconfirmedAccounts(pagination);
        } catch (ServiceException e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        mav.addObject("accountList", accountList);
        mav.setViewName("/WEB-INF/unconfirmed-accounts.jsp");
        return mav;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {

        // Stub
    }
}
