package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.UserEntity;
import external.letiuka.persistence.transaction.TransactionException;
import external.letiuka.persistence.transaction.TransactionManager;
import external.letiuka.security.Role;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.*;

public class DefaultUserDAO implements UserDAO {
    private TransactionManager manager;
    private static final Logger logger = Logger.getLogger(DefaultUserDAO.class);

    public DefaultUserDAO(TransactionManager manager) {
        this.manager = manager;
    }

    @Override
    public void insertUser(UserEntity user) throws DAOException {
        String sql = "INSERT INTO `user`(`login`,`password_hash`,`role`) VALUES(?,?,?)";
        Connection cn;
        PreparedStatement ps;
        ResultSet generated;
        boolean success = true;
        int rows;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole().toString());
            rows = ps.executeUpdate();
            if (rows < 1) throw new DAOException("Failed to add an entity");
            generated = ps.getGeneratedKeys();
            if (generated.next())
                user.setId(generated.getLong(1));
        } catch (SQLException | TransactionException e) {
            logger.log(Level.WARN, "Failed to add entity to database");
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

    @Override
    public UserEntity readUser(long id) throws DAOException {
        String sql = "SELECT * FROM `user` WHERE `user_id` = ?";
        UserEntity user = new UserEntity();
        Connection cn;
        PreparedStatement ps;
        ResultSet rs;
        boolean success = true;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                user.setId(id);
                user.setLogin(rs.getString("login"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRole(Role.valueOf(rs.getString("role")));
            } else {
                throw new DAOException("Failed to read an entity");
            }

            return user;
        } catch (SQLException | TransactionException e) {
            logger.log(Level.WARN, "Failed to read entity from database");
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

    @Override
    public UserEntity readUser(String login) throws DAOException {
        String sql = "SELECT * FROM `user` WHERE `login` = ?";
        UserEntity user = new UserEntity();
        Connection cn;
        PreparedStatement ps;
        ResultSet rs;
        boolean success = true;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sql);
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                user.setId(rs.getLong("user_id"));
                user.setLogin(login);
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRole(Role.valueOf(rs.getString("role")));
            } else {
                throw new DAOException("Failed to read an entity");
            }

            return user;
        } catch (SQLException | TransactionException e) {
            logger.log(Level.WARN, "Failed to read entity from database");
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
    @Override
    public void updateUser(UserEntity user) throws DAOException {
        String sql = "UPDATE `user` SET `login` = ?," +
                " `password_hash` = ?, `role` = ? WHERE `user_id` = ?";
        Connection cn;
        PreparedStatement ps;
        int rows;
        boolean success = true;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sql);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole().toString());
            ps.setLong(4, user.getId());
            rows = ps.executeUpdate();
            if (rows < 1) {
                throw new DAOException("Failed to update an entity");
            }
        } catch (SQLException | TransactionException e) {
            logger.log(Level.WARN, "Failed to update entity in database");
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

    @Override
    public void deleteUser(long id) throws DAOException {
        String sql = "DELETE FROM `user` WHERE `user_id` = ?";
        Connection cn;
        PreparedStatement ps;
        boolean success = true;
        int rows;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, id);
            rows = ps.executeUpdate();
            if (rows < 1) throw new DAOException("Failed to delete an entity");
        } catch (SQLException | TransactionException e) {
            logger.log(Level.WARN, "Failed to remove entity from database");
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
