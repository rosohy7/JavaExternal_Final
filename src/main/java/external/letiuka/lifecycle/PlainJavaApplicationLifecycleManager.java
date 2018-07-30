package external.letiuka.lifecycle;

import external.letiuka.springconfig.RootSpringConfig;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContext;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;

public class PlainJavaApplicationLifecycleManager implements ApplicationLifecycleManager {
    private static final Logger logger = Logger.getLogger(PlainJavaApplicationLifecycleManager.class);

    protected AnnotationConfigApplicationContext springContext;

    protected ServletContext servletContext;


    public PlainJavaApplicationLifecycleManager(ServletContext servletContext) {
        if (servletContext == null)
            throw new IllegalArgumentException("Null dependency");
        this.servletContext = servletContext;

    }

    @Override
    public final void build() {
        try {
            //setupSpring();
            logger.log(Level.TRACE, "Successfully scheduled jobs");
        } catch (Exception e) {
            String message = "Failed to build application object graph. ";

            logger.log(Level.FATAL, message,e);
            throw new Error(message, e);
        }
    }

    private void setupSpring() {

        springContext = new AnnotationConfigApplicationContext();
        springContext.getBeanFactory().registerSingleton("servletContext",servletContext);
        springContext.register(RootSpringConfig.class);
        springContext.refresh();
    }


    @Override
    public void shutdown() {
//        springContext.close();
    }
}
