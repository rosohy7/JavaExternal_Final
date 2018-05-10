package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.dto.User;
import external.letiuka.persistence.transaction.TransactionException;
import external.letiuka.persistence.transaction.TransactionManager;

import java.sql.*;

public class DefaultUserDAO implements UserDAO {
    private TransactionManager manager;

    public DefaultUserDAO(TransactionManager manager) {
        this.manager = manager;
    }

    @Override
    public void InsertUser(User user) throws DAOException {
        
        String sql = "INSERT INTO `user`(`role`) VALUES(?)";
        Connection cn;
        PreparedStatement ps;
        ResultSet generated;
        boolean success = true;
        int rows;

        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,user.getRole().toString());
            rows = ps.executeUpdate();
            if (rows < 1) throw new DAOException("Failed to add an entity");
            generated = ps.getGeneratedKeys();
            if(generated.next())
                user.setId(generated.getInt(1));
        } catch (Exception e) {
            success = false;
            throw new DAOException(e);
        } finally {
            try {
                manager.returnConnection(success);       // If not in a transaction then commit or rollback
            } catch (Exception e) {
                if (success) throw new DAOException(e);  // Do not override original exception if any
            }
        }


    }
}
