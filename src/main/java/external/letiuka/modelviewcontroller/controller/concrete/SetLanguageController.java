package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller responsible for setting UI language.
 */
public class SetLanguageController implements HttpController {
    private static final Logger logger = Logger.getLogger(SetLanguageController.class);

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        String lang = req.getParameter("lang");
        HttpSession session = req.getSession();

        switch(lang){
            case "ru":
                session.setAttribute("bundle","external.letiuka.modelviewcontroller.view.content.RuContent");
                break;
            case "en":
                session.setAttribute("bundle","external.letiuka.modelviewcontroller.view.content.EnContent");
                break;
            default:
                logger.log(Level.WARN,"Attempt to select unsupported language");
                try {
                    resp.sendError(404,"Language is not supported");
                } catch (IOException e) {
                    logger.log(Level.DEBUG,e.getMessage());
                }
                return;
        }
        session.setAttribute("contentLanguage",lang);
        try{
            String uri = (String)session.getAttribute("latest-get-uri");
            resp.sendRedirect((uri==null)? "/bankapp/" : uri);

        } catch (IOException e) {
            logger.log(Level.DEBUG,e.getMessage());
            try{resp.sendError(404);} catch(IOException e1) {}
        }
    }

}
