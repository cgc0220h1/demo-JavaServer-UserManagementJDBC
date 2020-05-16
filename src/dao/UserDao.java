package dao;/*
 * @project demo-JavaServer-UserManagementJDBC
 * @author Duc on 5/15/2020
 */

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements IUserDao {
    private static final String INSERT_USER_SQL = "INSERT INTO demo.users(name,email,country)"
            + "VALUES(?,?,?);";
    private static final String SELECT_USER_BY_ID = "SELECT id,name,email,country from demo.users where id = ?;";
    private static final String SELECT_ALL_USERS = "SELECT * FROM demo.users;";
    private static final String DELETE_USERS_SQL = "DELETE from demo.users WHERE id = ?;";
    private static final String UPDATE_USERS_SQL = "UPDATE demo.users SET name = ?,email = ?, country = ? WHERE id = ?;";
    private Connection connection = getConnection();
    private PreparedStatement statement;
    private ResultSet resultSet;

    public UserDao() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        String jdbcURL = "jdbc:mysql://localhost:3306/demo";
        String jdbcUsername = "root";
        String jdbcPassword = "anhnam420";

        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return connection;
    }

    @Override
    public void insertUser(User user) {
        System.out.println(INSERT_USER_SQL);
        try {
            statement = connection.prepareStatement(INSERT_USER_SQL);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            System.out.println(statement);
            statement.executeUpdate();
        } catch (SQLException ex) {
            handleSQLException(ex);
        }
    }

    @Override
    public User selectUser(int id) {
        User user = null;
        try {
            statement = connection.prepareStatement(SELECT_USER_BY_ID);
            statement.setInt(1, id);
            System.out.println(statement);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return user;
    }

    @Override
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();

        try {
            statement = connection.prepareStatement(SELECT_ALL_USERS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                users.add(new User(name, email, country));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return users;
    }

    @Override
    public boolean deleteUser(int id) {
        boolean result = false;
        try {
            statement = connection.prepareStatement(DELETE_USERS_SQL);
            statement.setInt(1, id);
            result = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean updateUser(User user) {
        boolean result = false;
        int id = user.getId();
        String name = user.getName();
        String email = user.getEmail();
        String country = user.getCountry();
        try {
            statement = connection.prepareStatement(UPDATE_USERS_SQL);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, country);
            statement.setInt(4, id);
            result = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return result;
    }

    private void handleSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
