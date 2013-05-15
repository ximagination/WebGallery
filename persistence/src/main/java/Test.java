import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import persistence.dao.interfaces.BaseDAO;

import java.io.File;
import java.util.ArrayList;

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
//
//        testInsert();
//
//        testInsertIncorrect();
//
//        testUpdate();
//
//        testFetchByPrimary();
//
//        //see all
//        testFetch();
//
//        testDelete();
//
//        //again see all
//        testFetch();

//        for (Image each : ImageDAOFactory.getDao(DataSource.JDBC).fetch()) {
//            System.out.println(each);
//        }
    }

    private static void init() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("");
    }


    //    private static void testInsertIncorrect() {
//        User user1 = new User();
//        user1.setLogin("test1");
//        user1.setPassword("test1");
//
//        // insert user. User with this login already exists. Exception must be thrown
//        UserDAO dao = UserDAOFactory.getDao(DataSource.JDBC);
//        try {
//            dao.insert(user1);
//        } catch (ValidationException e) {
//            System.out.println("Error on insert user :" + user1 + " because " + e.getMessage());
//        }
//    }
//
//    private static void testDelete() throws ValidationException {
//        int id = 1;
//        System.out.println("---start delete user " + id + "---");
//        UserDAOFactory.getDao(DataSource.JDBC).delete(id);
//    }
//
    private static void create() {
        ArrayList<BaseDAO<?, ?>> tables = new ArrayList<>();


        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-persistence-config.xml");

        String type = "raw";
        if ("raw".equals(type)) {
            tables.add(ctx.getBean(persistence.dao.rawJdbcImpl.UserDAOImpl.class));
            tables.add(ctx.getBean(persistence.dao.rawJdbcImpl.ImageDAOImpl.class));

        } else if ("template".equals(type)) {
            tables.add(ctx.getBean(persistence.dao.jdbcTemplateImpl.UserDAOImpl.class));
            tables.add(ctx.getBean(persistence.dao.jdbcTemplateImpl.ImageDAOImpl.class));

        } else {
            throw new UnsupportedOperationException("unknown type of profile " + type);
        }

        for (BaseDAO<?, ?> each : tables) {
            each.initScheme();
        }
    }
//
//    private static void testInsert() throws ValidationException {
//        User user1 = new User();
//        user1.setLogin("test1");
//        user1.setPassword("test1");
//
//        User user2 = new User();
//        user2.setLogin("test2");
//        user2.setPassword("test2");
//
//        // insert two users
//        UserDAO dao = UserDAOFactory.getDao(DataSource.JDBC);
//        //
//        System.out.println("insert " + user1);
//        dao.insert(user1);
//        //
//        System.out.println("insert " + user2);
//        dao.insert(user2);
//    }
//
//    private static void testUpdate() throws ValidationException {
//        User user = new User();
//        user.setId(1);
//        user.setLogin("test_updated");
//        user.setPassword("test_updated");
//
//        System.out.println("update user with id=1 " + user);
//
//        UserDAOFactory.getDao(DataSource.JDBC).update(user);
//    }
//
//    private static void testFetch() {
//        System.out.println();
//        System.out.println(" --- getting all user from database --- ");
//        for (User each : UserDAOFactory.getDao(DataSource.JDBC).fetch()) {
//            System.out.println("User " + each.getId() + " | login " + each.getLogin() + " | password " + each.getPassword());
//        }
//        System.out.println();
//    }
//
//    private static void testFetchByPrimary() throws ValidationException {
//        System.out.println();
//
//        int id = 1;
//
//        User each = UserDAOFactory.getDao(DataSource.JDBC).fetchByPrimary(id);
//        if (each == null) {
//            System.out.println("User not found by Id  " + id);
//        } else {
//            System.out.println("User by Primary " + each.getId() + " | login " + each.getLogin() + " | password " + each.getPassword());
//        }
//        System.out.println();
//    }
}
