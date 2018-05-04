package external.letiuka.dao;

public class DAOFactory {
    private static final DAOFactory factory = new DAOFactory();

    private ClientDao clientDao =new ClientDao();

    public static DAOFactory getInstance(){
        return factory;
    }

    public ClientDao getClientDao(){
        return clientDao;
    }
}
