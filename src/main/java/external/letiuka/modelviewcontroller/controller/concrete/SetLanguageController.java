package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller responsible for setting UI language.
 */
@Controller
public class SetLanguageController implements HttpController {
    private static final Logger logger = Logger.getLogger(SetLanguageController.class);
    private static final String CONTENT_PATH = "external.letiuka.modelviewcontroller.view.content";

    @RequestMapping(value = "dispatcher", params = "action=set-language", method = RequestMethod.POST)
    public ModelAndView setLanguage(@RequestParam String lang,
                                    HttpSession session,
                                    @SessionAttribute("latest-get-uri") String latestGetUri) {
        ModelAndView mav = new ModelAndView();
        switch (lang) {
            case "ru":
                session.setAttribute("bundle", CONTENT_PATH + ".RuContent");
                break;
            case "en":
                session.setAttribute("bundle", CONTENT_PATH + ".EnContent");
                break;
            default:
                logger.log(Level.WARN, "Attempt to select unsupported language");
                mav.setStatus(HttpStatus.BAD_REQUEST);
                return mav;
        }

        session.setAttribute("contentLanguage", lang);
        mav.setView(new RedirectView(latestGetUri));
        return mav;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        // Stub
    }

}
