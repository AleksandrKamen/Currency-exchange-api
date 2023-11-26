package util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JSPUtil {

    String JSP_FORMAT = "/WEB-INF/jsp/%s.jsp";
    public static String getPath(String jspName){
        return String.format(JSP_FORMAT,jspName);

    }

}
