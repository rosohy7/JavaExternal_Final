package external.letiuka.modelviewcontroller.controller.concrete;


import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.model.dto.AccountListDTO;
import external.letiuka.modelviewcontroller.model.dto.PaginationDTO;
import external.letiuka.service.BankOperationsService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller responsible for listing  a patricular user`s accounts.
 */
public class ListUserAccountsController implements HttpController {
    private static final Logger logger = Logger.getLogger(ListUserAccountsController.class);
    private static final long PER_PAGE=5;
    private final BankOperationsService service;

    public ListUserAccountsController(BankOperationsService service) {
        this.service = service;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        long targetPage=1;
        String role= (String)req.getSession().getAttribute("role");
        String targetLogin, myLogin;
        PaginationDTO pagination = new PaginationDTO();
        try{
            targetPage=Long.valueOf(req.getParameter("page"));
        }
        catch(Exception e){
            logger.log(Level.DEBUG,"Pagination request without page number (setting 1)");
        }

        targetLogin=req.getParameter("login");
        myLogin = (String)req.getSession().getAttribute("login");

        pagination.setPerPage(PER_PAGE);
        pagination.setTargetPage(targetPage);

        try{
            if("ADMIN".equals(role) || targetLogin.equals(myLogin)){
            AccountListDTO accountList = service.getUserAccounts(targetLogin,pagination);
            req.setAttribute("accountList",accountList);
            RequestDispatcher disp = req.getRequestDispatcher("/WEB-INF/user-accounts.jsp");
            disp.forward(req,resp);}
            else{
                req.getSession().setAttribute("message","You cannot view another user`s accounts");
                resp.sendRedirect("/bankapp/main/home.jsp");
            }

        } catch (IOException | ServiceException | ServletException e) {
            logger.log(Level.DEBUG, e.getStackTrace());
            try {
                resp.sendError(404);
            } catch (IOException e1) {
            }
        }
    }
}
