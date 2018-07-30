package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.model.dto.AccountListDTO;
import external.letiuka.modelviewcontroller.model.dto.PaginationDTO;
import external.letiuka.service.BankOperationsService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller responsible for listing bank accounts requested by users to be confirmed.
 */
@Controller
public class ListUnconfirmedController{
    private static final Logger logger = Logger.getLogger(ListUnconfirmedController.class);
    private static final long PER_PAGE=10;
    private final BankOperationsService service;

    public ListUnconfirmedController(BankOperationsService service) {
        this.service = service;
    }

    @RequestMapping(value = "dispatcher", params = "action=list-unconfirmed", method = RequestMethod.GET)
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {

        long targetPage=1;
        long lastPage;
        PaginationDTO pagination = new PaginationDTO();
        try{
            targetPage=Long.valueOf(req.getParameter("page"));

        }
        catch(NumberFormatException e){
            logger.log(Level.DEBUG,"Pagination request without page number (setting 1)");
        }
        pagination.setPerPage(PER_PAGE);
        pagination.setTargetPage(targetPage);
        try{
            AccountListDTO accountList = service.getUnconfirmedAccounts(pagination);
            req.setAttribute("accountList",accountList);
            RequestDispatcher disp = req.getRequestDispatcher("/WEB-INF/unconfirmed-accounts.jsp");

            disp.forward(req,resp);

        } catch (IOException | ServiceException | ServletException e) {
            logger.log(Level.DEBUG, e.getStackTrace());
            try {
                resp.sendError(404);
            } catch (IOException e1) {
            }
        }
    }
}
