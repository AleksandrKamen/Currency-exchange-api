import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.createSessionFactory();
            Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.getTransaction().commit();

        }
    }
}
