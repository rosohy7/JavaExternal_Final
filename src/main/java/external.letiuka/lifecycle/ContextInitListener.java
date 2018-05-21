package external.letiuka.lifecycle;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/*
    Interecepts ServletContextInit and ServletContextDestroy events to call lifecycle manager.
 */
@WebListener
public final class ContextInitListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(ContextInitListener.class);

    private ApplicationLifecycleManager lifecycle;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.log(Level.INFO,"Deployed web application");
        ServletContext context=servletContextEvent.getServletContext();
        lifecycle = new PlainJavaApplicationLifecycleManager(context);
        lifecycle.build();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        lifecycle.shutdown();
    }
}
