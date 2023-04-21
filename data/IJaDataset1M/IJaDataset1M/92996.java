package de.juwimm.cms.cocoon.generation.helper;

import java.util.Enumeration;
import javax.servlet.http.Cookie;
import org.apache.log4j.Logger;
import de.juwimm.cms.plugins.server.Request;

/**
 * This is the CocoonRequest implementation for conquest plugins
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id: RequestImpl.java 8 2009-02-15 08:54:54Z skulawik $
 */
public class RequestImpl implements Request {

    private static Logger log = Logger.getLogger(RequestImpl.class);

    private org.apache.cocoon.environment.Request delegateRequest = null;

    public RequestImpl(org.apache.cocoon.environment.Request delegateRequest) {
        this.delegateRequest = delegateRequest;
    }

    public String getParameter(String parameter) {
        return delegateRequest.getParameter(parameter);
    }

    @SuppressWarnings("unchecked")
    public Enumeration<String> getParameterNames() {
        return delegateRequest.getParameterNames();
    }

    public Cookie[] getCookies() {
        if (log.isDebugEnabled()) log.debug("ENTERING GET COOKIES");
        org.apache.cocoon.environment.Cookie[] cookies = this.delegateRequest.getCookies();
        Cookie[] realCookies = new Cookie[cookies.length];
        if (log.isDebugEnabled()) log.debug("FOUND " + cookies.length + " COOKIES");
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            Cookie cook = new javax.servlet.http.Cookie(name, value);
            realCookies[i] = cook;
        }
        if (log.isDebugEnabled()) log.debug("LEAVING GET COOKIES");
        return realCookies;
    }
}
