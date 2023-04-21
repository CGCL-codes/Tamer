package org.mobicents.servlet.sip.catalina;

import java.lang.reflect.Method;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.deploy.SecurityConstraint;
import org.apache.catalina.startup.SetNextNamingRule;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.NodeCreateRule;
import org.apache.tomcat.util.digester.Rule;
import org.apache.tomcat.util.digester.RuleSetBase;
import org.mobicents.servlet.sip.catalina.rules.MatchingRuleParser;
import org.mobicents.servlet.sip.core.descriptor.MatchingRule;
import org.mobicents.servlet.sip.core.descriptor.MobicentsSipServletMapping;
import org.mobicents.servlet.sip.core.security.MobicentsSipLoginConfig;
import org.mobicents.servlet.sip.startup.loading.SipServletMapping;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;

/**
 * <p><strong>SipRuleSet</strong> for processing the contents of a sip application
 * deployment descriptor (<code>/WEB-INF/sip.xml</code>) resource.</p>
 * @author Jean Deruelle
 */
public class SipRuleSet extends RuleSetBase {

    /**
     * The matching pattern prefix to use for recognizing our elements.
     */
    protected String prefix = null;

    /**
     * The <code>SetSessionConfig</code> rule used to parse the sip.xml
     */
    protected SetSessionConfig sessionConfig;

    /**
     * The <code>SetProxyConfig</code> rule used to parse the sip.xml
     */
    protected SetProxyConfig proxyConfig;

    /**
     * The <code>SetLoginConfig</code> rule used to parse the sip.xml
     */
    protected SetLoginConfig loginConfig;

    /**
     * The <code>SetServletSelection</code> rule used to parse the sip.xml
     */
    protected SetServletSelection servletSelection;

    /**
     * Construct an instance of this <code>RuleSet</code> with the default
     * matching pattern prefix.
     */
    public SipRuleSet() {
        this("");
    }

    /**
     * Construct an instance of this <code>RuleSet</code> with the specified
     * matching pattern prefix.
     *
     * @param prefix Prefix for matching pattern rules (including the
     *  trailing slash character)
     */
    public SipRuleSet(String prefix) {
        super();
        this.namespaceURI = null;
        this.prefix = prefix;
    }

    /**
     * <p>Add the set of Rule instances defined in this RuleSet to the
     * specified <code>Digester</code> instance, associating them with
     * our namespace URI (if any).  This method should only be called
     * by a Digester instance.</p>
     *
     * @param digester Digester instance to which the new Rule instances
     *  should be added.
     */
    public void addRuleInstances(Digester digester) {
        sessionConfig = new SetSessionConfig();
        proxyConfig = new SetProxyConfig();
        loginConfig = new SetLoginConfig();
        servletSelection = new SetServletSelection();
        digester.addRule(prefix + "sip-app", new SetPublicIdRule("setPublicId"));
        digester.addCallMethod(prefix + "sip-app/app-name", "setApplicationName", 0);
        digester.addCallMethod(prefix + "sip-app/icon/small-icon", "setSmallIcon", 0);
        digester.addCallMethod(prefix + "sip-app/icon/large-icon", "setLargeIcon", 0);
        digester.addCallMethod(prefix + "sip-app/display-name", "setDisplayName", 0);
        digester.addCallMethod(prefix + "sip-app/description", "setDescription", 0);
        digester.addRule(prefix + "sip-app/distributable", new SetDistributableRule());
        digester.addCallMethod(prefix + "sip-app/context-param", "addParameter", 2);
        digester.addCallParam(prefix + "sip-app/context-param/param-name", 0);
        digester.addCallParam(prefix + "sip-app/context-param/param-value", 1);
        digester.addCallMethod(prefix + "sip-app/listener/listener-class", "addSipApplicationListener", 0);
        digester.addRule(prefix + "sip-app/servlet-selection/main-servlet", servletSelection);
        digester.addCallMethod(prefix + "sip-app/servlet-selection/main-servlet", "setMainServlet", 0);
        digester.addRule(prefix + "sip-app/servlet", new WrapperCreateRule());
        digester.addSetNext(prefix + "sip-app/servlet", "addChild");
        digester.addCallMethod(prefix + "sip-app/servlet/icon/smallIcon", "setSmallIcon", 0);
        digester.addCallMethod(prefix + "sip-app/servlet/icon/largeIcon", "setLargeIcon", 0);
        digester.addCallMethod(prefix + "sip-app/servlet/servlet-name", "setName", 0);
        digester.addCallMethod(prefix + "sip-app/servlet/servlet-name", "setServletName", 0);
        digester.addCallMethod(prefix + "sip-app/servlet/display-name", "setDisplayName", 0);
        digester.addCallMethod(prefix + "sip-app/servlet/servlet-class", "setServletClass", 0);
        digester.addCallMethod(prefix + "sip-app/servlet/init-param", "addInitParameter", 2);
        digester.addCallParam(prefix + "sip-app/servlet/init-param/param-name", 0);
        digester.addCallParam(prefix + "sip-app/servlet/init-param/param-value", 1);
        digester.addCallMethod(prefix + "sip-app/servlet/load-on-startup", "setLoadOnStartupString", 0);
        digester.addCallMethod(prefix + "sip-app/servlet/run-as/role-name", "setRunAs", 0);
        digester.addCallMethod(prefix + "sip-app/servlet/security-role-ref", "addSecurityReference", 2);
        digester.addCallParam(prefix + "sip-app/servlet/security-role-ref/role-name", 0);
        digester.addCallParam(prefix + "sip-app/servlet/security-role-ref/role-link", 1);
        digester.addRule(prefix + "sip-app/servlet-selection/servlet-mapping", servletSelection);
        digester.addObjectCreate(prefix + "sip-app/servlet-selection/servlet-mapping", SipServletMapping.class.getName());
        digester.addSetNext(prefix + "sip-app/servlet-selection/servlet-mapping", "addSipServletMapping", MobicentsSipServletMapping.class.getName());
        digester.addCallMethod(prefix + "sip-app/servlet-selection/servlet-mapping/servlet-name", "setServletName", 0);
        try {
            digester.addRule("sip-app/servlet-selection/servlet-mapping/pattern", new PatternRule());
        } catch (Throwable e) {
            throw new IllegalArgumentException("Impossible to parse the pattern", e);
        }
        digester.addRule(prefix + "sip-app/proxy-config", proxyConfig);
        digester.addCallMethod(prefix + "sip-app/proxy-config/proxy-timeout", "setProxyTimeout", 1, new Class[] { Integer.TYPE });
        digester.addCallParam(prefix + "sip-app/proxy-config/proxy-timeout", 0);
        digester.addRule(prefix + "sip-app/session-config", sessionConfig);
        digester.addCallMethod(prefix + "sip-app/session-config/session-timeout", "setSipApplicationSessionTimeout", 1, new Class[] { Integer.TYPE });
        digester.addCallParam(prefix + "sip-app/session-config/session-timeout", 0);
        digester.addObjectCreate(prefix + "sip-app/resource-env-ref", "org.apache.catalina.deploy.ContextResourceEnvRef");
        digester.addRule(prefix + "sip-app/resource-env-ref", new SetNextNamingRule("addResourceEnvRef", "org.apache.catalina.deploy.ContextResourceEnvRef"));
        digester.addCallMethod(prefix + "sip-app/resource-env-ref/resource-env-ref-name", "setName", 0);
        digester.addCallMethod(prefix + "sip-app/resource-env-ref/resource-env-ref-type", "setType", 0);
        digester.addObjectCreate(prefix + "sip-app/resource-ref", "org.apache.catalina.deploy.ContextResource");
        digester.addRule(prefix + "sip-app/resource-ref", new SetNextNamingRule("addResource", "org.apache.catalina.deploy.ContextResource"));
        digester.addCallMethod(prefix + "sip-app/resource-ref/res-ref-name", "setName", 0);
        digester.addCallMethod(prefix + "sip-app/resource-ref/res-type", "setType", 0);
        digester.addCallMethod(prefix + "sip-app/resource-ref/res-auth", "setAuth", 0);
        digester.addCallMethod(prefix + "sip-app/resource-ref/res-sharing-scope", "setScope", 0);
        digester.addObjectCreate(prefix + "sip-app/security-constraint", SipSecurityConstraint.class.getName());
        digester.addSetNext(prefix + "sip-app/security-constraint", "addConstraint");
        digester.addCallMethod(prefix + "sip-app/security-constraint/display-name", "setDisplayName", 0);
        digester.addObjectCreate(prefix + "sip-app/security-constraint/resource-collection", SipSecurityCollection.class.getName());
        digester.addSetNext(prefix + "sip-app/security-constraint/resource-collection", "addCollection");
        digester.addCallMethod(prefix + "sip-app/security-constraint/resource-collection/resource-name", "setName", 0);
        digester.addCallMethod(prefix + "sip-app/security-constraint/resource-collection/servlet-name", "addServletName", 0);
        digester.addCallMethod(prefix + "sip-app/security-constraint/resource-collection/sip-method", "addSipMethod", 0);
        digester.addCallMethod(prefix + "sip-app/security-constraint/proxy-authentication", "setProxyAuthentication", 0);
        digester.addRule(prefix + "sip-app/security-constraint/auth-constraint", new SetAuthConstraintRule());
        digester.addCallMethod(prefix + "sip-app/security-constraint/auth-constraint/description", "setDescription", 0);
        digester.addCallMethod(prefix + "sip-app/security-constraint/auth-constraint/role-name", "addAuthRole", 0);
        digester.addCallMethod(prefix + "sip-app/security-constraint/user-data-constraint/transport-guarantee", "setUserConstraint", 0);
        digester.addRule(prefix + "sip-app/login-config", loginConfig);
        digester.addObjectCreate(prefix + "sip-app/login-config", SipLoginConfig.class.getName());
        digester.addSetNext(prefix + "sip-app/login-config", "setSipLoginConfig", MobicentsSipLoginConfig.class.getName());
        digester.addCallMethod(prefix + "sip-app/login-config/auth-method", "setAuthMethod", 0);
        digester.addCallMethod(prefix + "sip-app/login-config/realm-name", "setRealmName", 0);
        digester.addCallMethod(prefix + "sip-app/login-config/identity-assertion", "addIdentityAssertion", 2);
        digester.addCallParam(prefix + "sip-app/login-config/identity-assertion/identity-assertion-scheme", 0);
        digester.addCallParam(prefix + "sip-app/login-config/identity-assertion/identity-assertion-support", 1);
        digester.addCallMethod(prefix + "sip-app/security-role/role-name", "addSecurityRole", 0);
        digester.addObjectCreate(prefix + "sip-app/env-entry", "org.apache.catalina.deploy.ContextEnvironment");
        digester.addRule(prefix + "sip-app/env-entry", new SetNextNamingRule("addEnvironment", "org.apache.catalina.deploy.ContextEnvironment"));
        digester.addCallMethod(prefix + "sip-app/env-entry/description", "setDescription", 0);
        digester.addCallMethod(prefix + "sip-app/env-entry/env-entry-name", "setName", 0);
        digester.addCallMethod(prefix + "sip-app/env-entry/env-entry-type", "setType", 0);
        digester.addCallMethod(prefix + "sip-app/env-entry/env-entry-value", "setValue", 0);
        digester.addObjectCreate(prefix + "sip-app/ejb-ref", "org.apache.catalina.deploy.ContextEjb");
        digester.addRule(prefix + "sip-app/ejb-ref", new SetNextNamingRule("addEjb", "org.apache.catalina.deploy.ContextEjb"));
        digester.addCallMethod(prefix + "sip-app/ejb-ref/description", "setDescription", 0);
        digester.addCallMethod(prefix + "sip-app/ejb-ref/ejb-link", "setLink", 0);
        digester.addCallMethod(prefix + "sip-app/ejb-ref/ejb-ref-name", "setName", 0);
        digester.addCallMethod(prefix + "sip-app/ejb-ref/ejb-ref-type", "setType", 0);
        digester.addCallMethod(prefix + "sip-app/ejb-ref/home", "setHome", 0);
        digester.addCallMethod(prefix + "sip-app/ejb-ref/remote", "setRemote", 0);
        digester.addObjectCreate(prefix + "sip-app/ejb-local-ref", "org.apache.catalina.deploy.ContextLocalEjb");
        digester.addRule(prefix + "sip-app/ejb-local-ref", new SetNextNamingRule("addLocalEjb", "org.apache.catalina.deploy.ContextLocalEjb"));
        digester.addCallMethod(prefix + "sip-app/ejb-local-ref/description", "setDescription", 0);
        digester.addCallMethod(prefix + "sip-app/ejb-local-ref/ejb-link", "setLink", 0);
        digester.addCallMethod(prefix + "sip-app/ejb-local-ref/ejb-ref-name", "setName", 0);
        digester.addCallMethod(prefix + "sip-app/ejb-local-ref/ejb-ref-type", "setType", 0);
        digester.addCallMethod(prefix + "sip-app/ejb-local-ref/local", "setLocal", 0);
        digester.addCallMethod(prefix + "sip-app/ejb-local-ref/local-home", "setHome", 0);
    }

    /**
     * Reset counter used for validating the web.xml file.
     */
    public void recycle() {
        proxyConfig.isProxyConfigSet = false;
        sessionConfig.isSessionConfigSet = false;
        loginConfig.isLoginConfigSet = false;
        servletSelection.isMainServlet = false;
        servletSelection.isServletMapping = false;
    }
}

/**
 * Rule to check that the <code>login-config</code> is occuring 
 * only 1 time within the web.xml
 */
final class SetLoginConfig extends Rule {

    boolean isLoginConfigSet = false;

    public SetLoginConfig() {
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        if (isLoginConfigSet) {
            throw new IllegalArgumentException("<login-config> element is limited to 1 occurance");
        }
        isLoginConfigSet = true;
    }
}

/**
 * Rule to check that the <code>session-config</code> is occuring 
 * only 1 time within the web.xml
 */
final class SetSessionConfig extends Rule {

    boolean isSessionConfigSet = false;

    public SetSessionConfig() {
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        if (isSessionConfigSet) {
            throw new IllegalArgumentException("<session-config> element is limited to 1 occurance");
        }
        isSessionConfigSet = true;
    }
}

/**
 * Rule to check that the <code>proxy-config</code> is occuring 
 * only 1 time within the web.xml
 */
final class SetProxyConfig extends Rule {

    boolean isProxyConfigSet = false;

    public SetProxyConfig() {
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        if (isProxyConfigSet) {
            throw new IllegalArgumentException("<proxy-config> element is limited to 1 occurance");
        }
        isProxyConfigSet = true;
    }
}

/**
 * Rule to check that only one of the <code>servlet-mapping</code> or <code>main-servlet</code> is occuring 
 * within the sip.xml
 */
final class SetServletSelection extends Rule {

    boolean isServletMapping;

    boolean isMainServlet;

    public SetServletSelection() {
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        if (name.equals("servlet-mapping")) {
            isServletMapping = true;
        }
        if (name.equals("main-servlet")) {
            isMainServlet = true;
        }
        if (isMainServlet && isServletMapping) {
            throw new IllegalArgumentException("only one of the <servlet-mapping> or <main-servlet> can be present in the sip.xml");
        }
    }
}

/**
 * A Rule that calls the <code>setAuthConstraint(true)</code> method of
 * the top item on the stack, which must be of type
 * <code>org.apache.catalina.deploy.SecurityConstraint</code>.
 */
final class SetAuthConstraintRule extends Rule {

    public SetAuthConstraintRule() {
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        SecurityConstraint securityConstraint = (SecurityConstraint) digester.peek();
        securityConstraint.setAuthConstraint(true);
    }
}

/**
 * Class that calls <code>setDistributable(true)</code> for the top object
 * on the stack, which must be a <code>org.apache.catalina.Context</code>.
 */
final class SetDistributableRule extends Rule {

    public SetDistributableRule() {
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        Context context = (Context) digester.peek();
        context.setDistributable(true);
    }
}

/**
 * Class that calls a property setter for the top object on the stack,
 * passing the public ID of the entity we are currently processing.
 */
final class SetPublicIdRule extends Rule {

    public SetPublicIdRule(String method) {
        this.method = method;
    }

    private String method = null;

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        digester.peek(digester.getCount() - 1);
        Object top = digester.peek();
        Class paramClasses[] = new Class[1];
        paramClasses[0] = "String".getClass();
        String paramValues[] = new String[1];
        paramValues[0] = digester.getPublicId();
        Method m = null;
        try {
            m = top.getClass().getMethod(method, paramClasses);
        } catch (NoSuchMethodException e) {
            return;
        }
        m.invoke(top, (Object[]) paramValues);
    }
}

/**
 * A Rule that calls the factory method on the specified Context to
 * create the object that is to be added to the stack.
 */
final class WrapperCreateRule extends Rule {

    public WrapperCreateRule() {
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        Context context = (Context) digester.peek(digester.getCount() - 1);
        Wrapper wrapper = context.createWrapper();
        digester.push(wrapper);
    }

    public void end(String namespace, String name) throws Exception {
        digester.pop();
    }
}

final class PatternRule extends NodeCreateRule {

    public PatternRule() throws Exception {
        super();
    }

    public void end() throws Exception {
        Element e = (Element) super.digester.pop();
        Node pattern = (Node) e;
        NodeList list = pattern.getChildNodes();
        MatchingRule rule = MatchingRuleParser.buildRule((Element) list.item(0));
        MobicentsSipServletMapping sipServletMapping = (MobicentsSipServletMapping) digester.peek();
        sipServletMapping.setMatchingRule(rule);
    }
}
