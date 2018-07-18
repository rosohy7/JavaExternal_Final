package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.service.BankOperationsService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller responsible confirming bank accounts requested by users.
 */
@Controller
public class ConfirmAccountController implements HttpController {
    private static final Logger logger= Logger.getLogger(AccountInfoController.class);
    private final BankOperationsService service;

    public ConfirmAccountController(BankOperationsService service) {
        this.service = service;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession session = req.getSession();
        String accountNumber=req.getParameter("account-number");
        try{
            service.confirmAccount(accountNumber);
            String uri = (String)session.getAttribute("latest-get-uri");
            resp.sendRedirect((uri==null)? "/bankapp/" : uri);

        } catch (IOException | ServiceException e) {
            logger.log(Level.DEBUG, e.getStackTrace());
            try{resp.sendError(404);} catch(IOException e1) {}
            logger.log(Level.ERROR, "Failed to confirm account",e);
        }
    }
}
