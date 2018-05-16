package external.letiuka.mvc.controller.concrete;

import external.letiuka.mvc.controller.HttpController;
import external.letiuka.mvc.model.dto.AccountListDTO;
import external.letiuka.mvc.model.dto.LongPaginationDTO;
import external.letiuka.service.BankAccountService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListUnconfirmedController implements HttpController {
    private static final Logger logger = Logger.getLogger(ListUnconfirmedController.class);
    private static final long PER_PAGE=10;
    private final BankAccountService service;

    public ListUnconfirmedController(BankAccountService service) {
        this.service = service;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        long targetPage=1;
        long lastPage;
        LongPaginationDTO pagination = new LongPaginationDTO();
        try{
            targetPage=Long.valueOf(req.getParameter("page"));

        }
        catch(Exception e){
            logger.log(Level.DEBUG,"Pagination request without page number (setting 1)");
        }
        pagination.setPerPage(PER_PAGE);
        pagination.setTargetPage(targetPage);
        try{
            AccountListDTO accountList = service.GetUnconfirmedAccounts(pagination);
            logger.log(Level.DEBUG, accountList.toString());
            logger.log(Level.DEBUG, accountList.getPagination().toString());
            logger.log(Level.DEBUG, accountList.getAccounts().toString());
            req.setAttribute("accountList",accountList);
            RequestDispatcher disp = req.getRequestDispatcher("/WEB-INF/unconfirmed-accounts.jsp");
            disp.forward(req,resp);

        } catch (IOException | ServiceException | ServletException e) {
            e.printStackTrace();
            logger.log(Level.ERROR, "Could not redirect to home page");
        }
    }
}
