package external.letiuka.lifecycle;

/**
 * Initializes and shuts down the application.
 */
public interface ApplicationLifecycleManager {

    /*
    Builds dependencies, registers controllers, maps controllers, sets up authorization table and schedules jobs.
    Should be attached to ServletContextInit event.
     */
    void build();

     /*
    Shuts down scheduled jobs and closes resources.
    Should be attached to ServletContextDestroy event.
     */
    void shutdown();
}
