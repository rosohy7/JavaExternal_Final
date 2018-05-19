package external.letiuka.init;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public final class ContextInitListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(ContextInitListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context=servletContextEvent.getServletContext();
        System.setProperty("rootPath", context.getRealPath("/"));
        logger.log(Level.INFO,"Deployed web application");
        new PlainJavaApplicationBuilder(context).build();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
