package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.model.dto.BankAccountDTO;
import external.letiuka.modelviewcontroller.model.dto.PaginationDTO;
import external.letiuka.service.BankOperationsService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

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
public class AccountInfoController implements HttpController{
    private static final Logger logger= Logger.getLogger(AccountInfoController.class);
    private final long PER_PAGE=5;

    private final BankOperationsService service;

    public AccountInfoController(BankOperationsService service) {
        this.service = service;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        long targetPage;
        try{
            targetPage=Long.valueOf(req.getParameter("page"));
        }
        catch(Exception e){
            targetPage=1;
        }
        String accountNumber = req.getParameter("account-number");
        HttpSession session = req.getSession();
        String login = (String) session.getAttribute("login");
        String role = (String) session.getAttribute("role");
        PaginationDTO pagination = new PaginationDTO();
        pagination.setPerPage(PER_PAGE);
        pagination.setTargetPage(targetPage);
        try{

            BankAccountDTO account = service.getAccount(accountNumber, pagination);
            if(role == "ADMIN" || account.getHolder().equals(login)){
                req.setAttribute("account",account);
                RequestDispatcher disp = req.getRequestDispatcher("/WEB-INF/account-page.jsp");
                disp.forward(req,resp);
            }
            else{
                logger.log(Level.WARN,login+" tried to see "
                        +account.getHolder()+"`s bank account information");
                resp.sendError(405,"You can not view another user`s bank account information");
            }
        } catch (IOException | ServiceException | ServletException e) {
            logger.log(Level.DEBUG, "Exception",e);
        session.setAttribute("message","Request failed");
        try {
        resp.sendError(404);
        } catch (IOException e1) {}
        }
        }
        }
