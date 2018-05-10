package external.letiuka.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public final class ContextInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ApplicationBuilder builder = new PlainJavaApplicationBuilder(
                servletContextEvent.getServletContext());
        builder.build();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
