package external.letiuka;

import external.letiuka.exception.ServiceException;
import external.letiuka.service.MyService;
import external.letiuka.service.ServiceFactory;

public class Main {

    public static void main(String[] args) {
        ServiceFactory factory = ServiceFactory.getInstatice();
        MyService myService = factory.getMyService();
        try {
            myService.serviceMethod();
        } catch (ServiceException e) {

        }


    }
}
