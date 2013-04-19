import persistence.connectAndSource.DataSource;
import persistence.dao.UserDAO;
import persistence.dao.factory.UserDAOFactory;
import persistence.exception.ValidationException;
import persistence.struct.User;

import java.io.File;

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

        // insert user. User with this login already exists. Exception must be thrown
        UserDAO dao = UserDAOFactory.getDao(DataSource.JDBC);
        try {
            dao.insert(user1);
        } catch (ValidationException e) {
            System.out.println("Error on insert user :" + user1 + " because " + e.getMessage());
        }
    }

    private static void testDelete() throws ValidationException {
        int id = 1;
        System.out.println("---start delete user " + id + "---");
        UserDAOFactory.getDao(DataSource.JDBC).delete(id);
    }

    private static void create() {
        UserDAO dao = UserDAOFactory.getDao(DataSource.JDBC);
        dao.create();
    }

    private static void testInsert() throws ValidationException {
        User user1 = new User();
        user1.setLogin("test1");
        user1.setPassword("test1");

        User user2 = new User();
        user2.setLogin("test2");
        user2.setPassword("test2");

        // insert two users
        UserDAO dao = UserDAOFactory.getDao(DataSource.JDBC);
        //
        System.out.println("insert " + user1);
        dao.insert(user1);
        //
        System.out.println("insert " + user2);
        dao.insert(user2);
    }

    private static void testUpdate() throws ValidationException {
        User user = new User();
        user.setId(1);
        user.setLogin("test_updated");
        user.setPassword("test_updated");

        System.out.println("update user with id=1 " + user);

        UserDAOFactory.getDao(DataSource.JDBC).update(user);
    }

    private static void testFetch() {
        System.out.println();
        System.out.println(" --- getting all user from database --- ");
        for (User each : UserDAOFactory.getDao(DataSource.JDBC).fetch()) {
            System.out.println("User " + each.getId() + " | login " + each.getLogin() + " | password " + each.getPassword());
        }
        System.out.println();
    }

    private static void testFetchByPrimary() throws ValidationException {
        System.out.println();

        int id = 1;

        User each = UserDAOFactory.getDao(DataSource.JDBC).fetchByPrimary(id);
        if (each == null) {
            System.out.println("User not found by Id  " + id);
        } else {
            System.out.println("User by Primary " + each.getId() + " | login " + each.getLogin() + " | password " + each.getPassword());
        }
        System.out.println();
    }
}
