package util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {


    public static SessionFactory createSessionFactory(){
        Configuration configuration = buildConfig();
        configuration.configure();
        var sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }

    public static Configuration buildConfig(){
        Configuration configuration = new Configuration();
        return configuration;
    }

}
