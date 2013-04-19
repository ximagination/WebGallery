import persistence.connectAndSource.Connector;
import persistence.connectAndSource.DataSource;
import persistence.dao.UserDAO;
import persistence.dao.factory.UserDAOFactory;
import persistence.dao.implementation.UserDAOImpl;
import persistence.exception.D2DatabasePersistenceException;
import persistence.exception.ValidateException;
import persistence.struct.User;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/17/13
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    private static final String DATABASE_NAME = "/test.h2.db";

    public static void main(String[] args) throws Exception {
        new File(System.getProperty("user.home") + DATABASE_NAME).delete();

        create();

        testInsert();

        testInsertIncorrect();

        testUpdate();

        testFetchByPrimary();

        //see all
        testFetch();

        testDelete();

        //again see all
        testFetch();
    }

    private static void testInsertIncorrect() {
        User user1 = new User();
        user1.setLogin("test1");
        user1.setPassword("test1");

        // insert two users with same login pass
        UserDAO dao = UserDAOFactory.getDao(DataSource.H2);
        try {
            dao.insert(user1);
        } catch (ValidateException e) {
            System.out.println("Error on insert user :" + user1 + " because " + e.getMessage());
        }
    }

    private static void testDelete() throws ValidateException {
        int id = 1;
        System.out.println("---start delete user " + id + "---");
        UserDAOFactory.getDao(DataSource.H2).delete(id);
    }

    private static void create() {
        Connection c = Connector.getInstance().newConnection();
        try {
            c.createStatement().execute(UserDAOImpl.CREATE_TABLE);
        } catch (SQLException e) {
            throw new D2DatabasePersistenceException("CREATE TABLE USER FAILED " + UserDAOImpl.CREATE_TABLE);
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    private static void testInsert() throws ValidateException {
        User user1 = new User();
        user1.setLogin("test1");
        user1.setPassword("test1");

        User user2 = new User();
        user2.setLogin("test2");
        user2.setPassword("test2");

        // insert two users with same login pass
        UserDAO dao = UserDAOFactory.getDao(DataSource.H2);
        System.out.println("insert " + user1);
        dao.insert(user1);
        System.out.println("insert " + user2);
        dao.insert(user2);
    }

    private static void testUpdate() throws ValidateException {
        User user = new User();
        user.setId(1);
        user.setLogin("test_updated");
        user.setPassword("test_updated");

        System.out.println("update user with id=1 " + user);

        UserDAOFactory.getDao(DataSource.H2).update(user);
    }

    private static void testFetch() {
        System.out.println();
        System.out.println(" --- getting all user from database --- ");
        for (User each : UserDAOFactory.getDao(DataSource.H2).fetch()) {
            System.out.println("User " + each.getId() + " | login " + each.getLogin() + " | password " + each.getPassword());
        }
        System.out.println();
    }

    private static void testFetchByPrimary() throws ValidateException {
        System.out.println();

        int _id = 1;

        User each = UserDAOFactory.getDao(DataSource.H2).fetchByPrimary(_id);
        if (each == null) {
            System.out.println("User not found by Id  " + _id);
        } else {
            System.out.println("User by Primary " + each.getId() + " | login " + each.getLogin() + " | password " + each.getPassword());
        }
        System.out.println();
    }
}
