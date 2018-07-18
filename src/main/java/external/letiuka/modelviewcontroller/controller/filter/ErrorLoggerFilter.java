package external.letiuka.modelviewcontroller.controller.filter;

import external.letiuka.modelviewcontroller.controller.concrete.AccountInfoController;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class ErrorLoggerFilter  implements Filter {
    private static final Logger logger= Logger.getLogger(AccountInfoController.class);
    private ServletRequest request;
    private ServletResponse response;
    private FilterChain chain;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.request = request;
        this.response = response;
        this.chain = chain;
        try {
            chain.doFilter(request, response);
        } catch (Throwable e) {
            logger.log(Level.ERROR,"An exception called in exception filter", e);
            if (e instanceof IOException) {
                throw (IOException) e;
            } else if (e instanceof ServletException) {
                throw (ServletException) e;
            } else if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                //This should never be hit
                throw new RuntimeException("Unexpected Exception", e);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
