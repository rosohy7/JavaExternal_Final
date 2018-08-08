package external.letiuka.modelviewcontroller.controller.filter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Web filter that remembers last get request used by user for future redirects.
 */
@WebFilter("/*")
public class LatestGetURIFilter implements Filter {
    private static final Logger logger = Logger.getLogger(LatestGetURIFilter.class);
    private ServletContext context;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        logger.log(Level.TRACE, "Entered LatestGetURIFilter after serving request");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        if (req.getMethod() == "GET") {
            String uri = req.getRequestURI();
            String query = req.getQueryString();
            HttpSession session = req.getSession();
            String latestGetURI;
            if (query == null || query == "")
                latestGetURI = uri;
            else
                latestGetURI = uri + "?" + query;
            if(latestGetURI.matches(".*\\.css"))
                return;
            session.setAttribute("latest-get-uri", latestGetURI);
            logger.log(Level.DEBUG, "Set latest-get-uri to " + latestGetURI);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        context = filterConfig.getServletContext();
    }

    @Override
    public void destroy() {

    }
}
