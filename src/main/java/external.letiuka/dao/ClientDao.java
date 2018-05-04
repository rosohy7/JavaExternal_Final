package external.letiuka.dao;

import external.letiuka.dto.ClientDto;
import external.letiuka.exception.DAOException;
import external.letiuka.exception.TransactionException;
import external.letiuka.transaction.WrappedConnection;
import external.letiuka.transaction.TransactionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientDao {


    public void insertClient(ClientDto client) throws DAOException, TransactionException {
        WrappedConnection con = TransactionManager.getConnection();
        try {
            String sql = "";
            PreparedStatement ps = con.prepareStatement(sql);
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            TransactionManager.returnConnection();
        }
    }
}
