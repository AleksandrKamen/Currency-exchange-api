package util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Properties;

@UtilityClass
public class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();
static {
    loadProperties();
}
    private static void loadProperties() {
        try (var stream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(stream);
    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
public static String get(String key){
    return PROPERTIES.getProperty(key);
}


}
