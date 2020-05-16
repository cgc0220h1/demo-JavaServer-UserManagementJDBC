package dao;/*
 * @project demo-JavaServer-UserManagementJDBC
 * @author Duc on 5/15/2020
 */

import model.User;

import java.util.List;

public interface IUserDao {
    void insertUser(User user);

    User selectUser(int id);

    List<User> selectAllUsers();

    boolean deleteUser(int id);

    boolean updateUser(User user);
}
