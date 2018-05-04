package external.letiuka.service;

public class ServiceFactory {
    private final static ServiceFactory serviceFactory = new ServiceFactory();
    private final MyService myService =new MyService();

    public static ServiceFactory getInstatice(){
        return serviceFactory;
    }

    public MyService getMyService(){
        return myService;
    }


}
