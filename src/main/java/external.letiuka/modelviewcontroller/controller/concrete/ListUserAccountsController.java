package external.letiuka.modelviewcontroller.controller.concrete;


import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.model.dto.AccountListDTO;
import external.letiuka.modelviewcontroller.model.dto.PaginationDTO;
import external.letiuka.service.BankAccountService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListUserAccountsController implements HttpController {
    private static final Logger logger = Logger.getLogger(ListUserAccountsController.class);
    private static final long PER_PAGE=5;
    private final BankAccountService service;

    public ListUserAccountsController(BankAccountService service) {
        this.service = service;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        long targetPage=1;
        long lastPage;
        String login;
        PaginationDTO pagination = new PaginationDTO();
        try{
            targetPage=Long.valueOf(req.getParameter("page"));
        }
        catch(Exception e){
            logger.log(Level.DEBUG,"Pagination request without page number (setting 1)");
        }

        login=req.getParameter("login");

        pagination.setPerPage(PER_PAGE);
        pagination.setTargetPage(targetPage);
        try{
            AccountListDTO accountList = service.getUserAccounts(login,pagination);
            req.setAttribute("accountList",accountList);
            RequestDispatcher disp = req.getRequestDispatcher("/WEB-INF/user-accounts.jsp");
            disp.forward(req,resp);

        } catch (IOException | ServiceException | ServletException e) {
            e.printStackTrace();
            logger.log(Level.ERROR, "Could not redirect");
        }
    }
}
