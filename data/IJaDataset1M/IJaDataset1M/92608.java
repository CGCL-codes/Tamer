package net.jxta.edutella.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * EduConfig is responsible for configuring objects from properties
 * or command line arguments. Command line arguments have a higher precedence
 * than property file entries.<br>
 * 
 * The process works as follows: 
 * 
 * <ul>
 * 	 <li>An object that wants to be configured needs to implement
 *       the {@link Configurable} interface. Thus, is it able to 
 *       provide information about which properties and command line
 *       switches it understands.
 *   <li>Each Configurable needs to be registered with the EduConfig
 *       object using {@link #register}.
 *   <li>After registering the objects, {@link finishConfig} is called,
 *       which configures all registered objects accordingly.
 * </ul>
 * 
 * See {@link Configurable} for details on how objects register themselves.
 * 
 * 
 * @author schm4704@jxta.org
 */
public class Configurator {

    private List configurables = new ArrayList();

    private String[] args;

    private int argCount = 1;

    private Properties properties;

    private Map options = new HashMap();

    public String appInfo = "";

    private boolean initialized = false;

    private static Logger log = null;

    /**
	 * @param clArgs command line args as given in <code>main()</code>
	 */
    public Configurator(String[] clArgs) {
        this("edutella.properties", clArgs);
    }

    /**
	 * @param propFile name of the property file
	 * @param clArgs command line args as given in <code>main()</code>
	 */
    public Configurator(String propFile, String[] clArgs) {
        Properties props = new Properties();
        try {
            FileInputStream fis = new FileInputStream(propFile);
            props.load(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            try {
                InputStream fis = getClass().getResourceAsStream("/" + propFile);
                props.load(fis);
                fis.close();
            } catch (NullPointerException e2) {
            } catch (IOException e2) {
                System.err.println("I/O error reading edutella.properties");
                e.printStackTrace(System.err);
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("I/O error reading edutella.properties");
            e.printStackTrace(System.err);
            System.exit(1);
        }
        init(props, clArgs);
    }

    /**
	 * @param propFile name of the property file
	 * @param clArgs command line args as given in <code>main()</code>
	 */
    public Configurator(Properties props, String[] clArgs) {
        init(props, clArgs);
    }

    public void init(Properties props, String[] clArgs) {
        properties = props;
        args = clArgs;
        PropertyConfigurator.configure(".logconfig");
        getLogger().info("Log4j configured based on file \".logconfig\"");
        addOption(new Option('h', "help", "print this help", false, "false", true));
    }

    /**
	 * Register a configurable with this EduConfigOld so that it
	 * can be configured automatically by {@link #finishConfig}.
	 * 
	 * @param c the Configurable to be registered
	 */
    public void register(Configurable c) {
        configurables.add(c);
    }

    /**
	 * Set a string describing this application
	 * 
	 * @param appInfo application description
	 */
    public void setAppInfo(String appInfo) {
        this.appInfo = appInfo;
    }

    private void collectOptions() {
        Iterator iter = configurables.iterator();
        while (iter.hasNext()) {
            Configurable c = (Configurable) iter.next();
            Option[] opts = c.getOptions();
            if (opts != null) {
                for (int i = 0; i < opts.length; i++) {
                    addOption(opts[i]);
                }
            }
        }
    }

    /**
	 * reads properties and sets/adds options accordingly
	 */
    private void setProperties() {
        String regex = "\\$\\{[a-zA-Z0-9.]*}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        for (Enumeration i = properties.propertyNames(); i.hasMoreElements(); ) {
            String propName = (String) i.nextElement();
            Option o = (Option) options.get(propName);
            if (o == null) {
                o = new Option(' ', propName, "");
                addOption(o);
            }
            String propertyValue = properties.getProperty(propName);
            matcher = pattern.matcher(propertyValue);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String group = matcher.group();
                matcher.appendReplacement(sb, System.getProperty(group.substring("${".length(), group.length() - "}".length())));
            }
            matcher.appendTail(sb);
            propertyValue = sb.toString();
            log.debug("Property '" + propName + "', required '" + o.isRequired() + "', value '" + propertyValue + "'");
            o.setValue(propertyValue);
        }
    }

    private void initConfig() {
        if (!initialized) {
            collectOptions();
            setProperties();
            setArgs(args);
            initialized = true;
        }
    }

    public void addOption(Option opt) {
        if (options.containsKey(opt.longName)) {
            throw new IllegalArgumentException("option name '" + opt.longName + "' used twice");
        }
        options.put(opt.longName, opt);
        if (opt.hasShortName()) {
            if (options.containsKey(opt.shortName)) {
                throw new IllegalArgumentException("option name '" + opt.shortName + "' used twice");
            }
            options.put(opt.shortName, opt);
        }
    }

    public void configureAll() {
        for (Iterator iter = configurables.iterator(); iter.hasNext(); ) {
            Configurable conf = (Configurable) iter.next();
            configureObject(conf);
        }
    }

    private void retrieveOptionValues() {
        for (Iterator iter = configurables.iterator(); iter.hasNext(); ) {
            Configurable conf = (Configurable) iter.next();
            retrieveObjectProperties(conf, conf.getPropertyPrefix());
        }
    }

    private void initAll() {
        for (Iterator iter = configurables.iterator(); iter.hasNext(); ) {
            Configurable conf = (Configurable) iter.next();
            initObject(conf);
        }
    }

    /**
	 * Finish configuration, i. e., call the appropriate setter
	 * methods for all {@link Configurable}s and configured options.
	 */
    public void finishConfig() {
        initConfig();
        configureAll();
        retrieveOptionValues();
        if (getMissingOptions().size() > 0) {
            ConfigDialog cd = new ConfigDialog("Configuration information", "Please supply configuration information", this);
            cd.showDialog();
            configureAll();
        }
        Iterator optIter = options.values().iterator();
        while (optIter.hasNext()) {
            Option o = (Option) optIter.next();
            if (o.isRequired() && o.getValue() == null) {
                exit("Required option " + o.getLongName() + " (-" + o.getShortName() + ")  not set. ");
            }
        }
        initAll();
    }

    public Map getArguments() {
        Map props = new HashMap(options.size());
        for (Iterator i = options.values().iterator(); i.hasNext(); ) {
            Option opt = (Option) i.next();
            if (opt.value != null && !props.containsKey(opt.longName)) {
                props.put(opt.longName, opt.value);
            }
        }
        return props;
    }

    public Collection getOptions() {
        initConfig();
        List optList = new ArrayList(options.size());
        for (Iterator i = options.values().iterator(); i.hasNext(); ) {
            Object opt = i.next();
            if (!optList.contains(opt)) {
                optList.add(opt);
            }
        }
        return optList;
    }

    public Collection getMissingOptions() {
        initConfig();
        List optList = new ArrayList(options.size());
        for (Iterator i = options.values().iterator(); i.hasNext(); ) {
            Option opt = (Option) i.next();
            if (!optList.contains(opt) && opt.isRequired() && (opt.getValue() == null || opt.getValue() == "")) {
                optList.add(opt);
            }
        }
        return optList;
    }

    /**
	 * writes all properties to an output stream
	 * @param out the stream
	 */
    public void write(OutputStream out) {
        try {
            properties.store(out, "Edutella Configuration Properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                String optName = arg.substring(args[i].lastIndexOf("-") + 1);
                Option opt = (Option) options.get(optName);
                if (opt == null) {
                    exit("option '" + optName + "' is undefined");
                }
                if (opt.isFlag) {
                    opt.value = Boolean.TRUE.toString();
                } else {
                    i++;
                    String nextArg = args[i];
                    if (i == args.length || args[i].startsWith("-")) {
                        exit("missing value for option '" + optName + "'");
                    }
                    opt.value = nextArg;
                }
            } else {
                String optName = new Integer(argCount).toString();
                argCount++;
                Option opt = (Option) options.get(optName);
                if (opt == null) {
                    exit("unexpected argument '" + arg + "'");
                }
                opt.value = arg;
            }
        }
        if (getFlag("help")) {
            System.out.println(appInfo);
            System.out.println(getHelp());
            System.exit(0);
        }
    }

    public String getHelp() {
        String cmdLine = "";
        String opts = "";
        String delim = "\nArguments: ";
        for (int i = 1; i < argCount; i++) {
            String optName = "" + i;
            Option opt = (Option) options.get(optName);
            cmdLine += delim + opt.longName;
            delim = " ";
            String option = opt.longName;
            while (option.length() < 16) {
                option += " ";
            }
            option += opt.description + "\n";
            opts += option;
        }
        for (char i = 'a'; i <= 'z'; i++) {
            String optName = new Character(i).toString();
            Option opt = (Option) options.get(optName);
            if (opt != null) {
                String option = "[-" + opt.shortName;
                if (!opt.isFlag) {
                    option += " <" + opt.longName + ">";
                }
                option += "]";
                cmdLine += delim + option;
                delim = " ";
                option = "-" + opt.shortName + " (-" + opt.longName + ")";
                while (option.length() < 16) {
                    option += " ";
                }
                option += opt.description;
                if (!opt.required && opt.defaultValue != "") {
                    option += "(default: " + opt.defaultValue + ")";
                }
                option += "\n";
                opts += option;
            }
        }
        return cmdLine + "\n\n" + opts;
    }

    public boolean getFlag(String flagName) {
        String strVal = getValue(flagName);
        if (strVal == null) {
            return false;
        }
        return Boolean.valueOf(strVal).booleanValue();
    }

    public Integer getIntValue(String optName) {
        String strVal = getValue(optName);
        if (strVal == null) {
            return null;
        }
        return Integer.valueOf(strVal);
    }

    public String getValue(String optName) {
        Option opt = (Option) options.get(optName);
        if (opt == null) {
            throw new IllegalArgumentException("unknown option '" + optName + "'");
        }
        if (opt.value != null) {
            return opt.value;
        }
        if (opt.required) {
            throw new RuntimeException("no value set for required argument '" + optName + "'");
        } else {
            return opt.defaultValue;
        }
    }

    /**
	 * sets the value of a property
	 * @param propertyName name of the property
	 * @return String new value 
	 */
    public void setValue(String propertyName, String value) {
        properties.put(propertyName, value);
    }

    public void updateValue(String propertyName, String value) {
        Option o = (Option) options.get(propertyName);
        o.setValue(value);
    }

    public boolean isValueAvailable(String optName) {
        return options.containsKey(optName);
    }

    /**
	 * Sets object attributes from properties.
	 * This method extracts the attribute names from the
	 * object BeanInfo and looks for properties with the
	 * same names. If the prefix argument is not empty,
	 * it looks for properties with name 
	 * &lt;prefix&gt;.&lt;attributename&gt;.
	 * For all matching properties the attribute value is 
	 * set using the setter methods.
	 * Finally the method <code>init()</code> is called 
	 * on the object if it exists.
	 *
	 * @param object object to store
	 * @param prefix property name prefix
	 */
    public void configureObject(Configurable object) {
        getLogger().debug("configure " + object.getClass());
        String prefix = object.getPropertyPrefix();
        if (prefix != "" && !prefix.endsWith(".")) {
            prefix += ".";
        }
        PropertyDescriptor propertyDescriptors[] = null;
        try {
            BeanInfo beaninfo = Introspector.getBeanInfo(object.getClass());
            propertyDescriptors = beaninfo.getPropertyDescriptors();
        } catch (IntrospectionException ie) {
            getLogger().error("could not get bean info", ie);
        }
        if (propertyDescriptors != null) {
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor pd = propertyDescriptors[i];
                String attribute = pd.getName();
                Method setter = pd.getWriteMethod();
                Class type = pd.getPropertyType();
                if (setter != null) {
                    String propertyName = prefix + attribute;
                    String value = null;
                    Option option = null;
                    try {
                        if (isValueAvailable(propertyName)) {
                            option = (Option) options.get(propertyName);
                        } else if (isValueAvailable(propertyName.toLowerCase())) {
                            option = (Option) options.get(propertyName.toLowerCase());
                        }
                        if (option != null) {
                            value = option.getValue();
                        }
                        if (value != null) {
                            Object parameter = null;
                            if (type.equals(String.class)) {
                                parameter = value;
                            } else if (type.equals(int.class)) {
                                parameter = Integer.valueOf(value);
                            } else if (type.equals(boolean.class)) {
                                parameter = Boolean.valueOf(value);
                            }
                            if (parameter != null) {
                                setter.invoke(object, new Object[] { parameter });
                                getLogger().debug("set attribute '" + attribute + "' to '" + value + "'");
                            }
                        }
                    } catch (IllegalAccessException e) {
                        getLogger().error("access rights prohibit to set attribute '" + attribute + "'", e);
                    } catch (InvocationTargetException e) {
                        getLogger().error("error invoking setter for '" + attribute + "' on object" + object, e);
                    } catch (NumberFormatException e) {
                        getLogger().error("invalid value for attribute '" + attribute + "': " + value, e);
                    }
                }
            }
        }
    }

    public void initObject(Configurable object) {
        boolean initInvoked = false;
        try {
            Method initMethod = object.getClass().getMethod("init", new Class[] { Configurator.class });
            initMethod.invoke(object, new Object[] { this });
            initInvoked = true;
            getLogger().debug("init(EduConfig) invoked");
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
            getLogger().error("access rights prohibit invoking init(EduConfig)", e);
        } catch (InvocationTargetException e) {
            getLogger().error("error invoking init(EduConfig) on object " + object, e);
        }
        if (!initInvoked) {
            try {
                Method initMethod = object.getClass().getMethod("init", new Class[] {});
                initMethod.invoke(object, new Object[] {});
                initInvoked = true;
                getLogger().debug("init(Configurator) invoked");
            } catch (NoSuchMethodException e) {
            } catch (IllegalAccessException e) {
                getLogger().error("access rights prohibit invoking init()", e);
            } catch (InvocationTargetException e) {
                getLogger().error("error invoking init() on object " + object, e);
            }
        }
    }

    /**
	 * Sets object attributes from properties.
	 * This method extracts the attribute names from the
	 * object BeanInfo and looks for properties with the
	 * same names. If the prefix argument is not empty,
	 * it looks for properties with name 
	 * &lt;prefix&gt;.&lt;attributename&gt;.
	 * For all matching properties the attribute value is 
	 * set using the setter methods.
	 * Finally the method <code>init()</code> is called 
	 * on the object if it exists.
	 *
	 * @param object object to store
	 * @param prefix property name prefix
	 */
    public void retrieveObjectProperties(Object object, String prefix) {
        getLogger().debug("retrieve values from " + object.getClass());
        if (prefix != "" && !prefix.endsWith(".")) {
            prefix += ".";
        }
        PropertyDescriptor propertyDescriptors[] = null;
        try {
            BeanInfo beaninfo = Introspector.getBeanInfo(object.getClass());
            propertyDescriptors = beaninfo.getPropertyDescriptors();
        } catch (IntrospectionException ie) {
            getLogger().error("could not get bean info", ie);
        }
        if (propertyDescriptors != null) {
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor pd = propertyDescriptors[i];
                String attribute = pd.getName();
                Method getter = pd.getReadMethod();
                if (getter != null) {
                    String propertyName = prefix + attribute;
                    Option option = null;
                    try {
                        if (isValueAvailable(propertyName)) {
                            option = (Option) options.get(propertyName);
                        } else if (isValueAvailable(propertyName.toLowerCase())) {
                            option = (Option) options.get(propertyName.toLowerCase());
                        }
                        if (option != null) {
                            Object value = getter.invoke(object, new Object[] {});
                            if (value != null) {
                                option.setValue(value.toString());
                            }
                        }
                    } catch (IllegalAccessException e) {
                        getLogger().error("access rights prohibit to get attribute '" + attribute, e);
                    } catch (InvocationTargetException e) {
                        getLogger().error("error invoking '" + attribute, e);
                    }
                }
            }
        }
    }

    /**
	 * writes all options to a file.
	 * @param filename name of properties file
	 */
    public void write(String filename) {
        try {
            getLogger().info("store config in " + filename);
            FileOutputStream out = new FileOutputStream(filename);
            Properties outProps = new Properties();
            Iterator optIter = options.values().iterator();
            while (optIter.hasNext()) {
                Option o = (Option) optIter.next();
                if (o.getValue() != null) {
                    outProps.setProperty(o.getLongName(), o.getValue());
                }
            }
            properties.store(out, "Edutella Configuration Properties");
            out.close();
        } catch (FileNotFoundException e) {
            getLogger().error("configuration file " + filename + " not found");
            throw new IllegalArgumentException();
        } catch (Exception e) {
            getLogger().error("error writing config file " + filename, e);
            throw new IllegalArgumentException();
        }
    }

    /**
	 * Stores object attributes.
	 * This method extracts the attribute names from the
	 * object BeanInfo and sets properties using this names. 
	 * If the prefix argument is not empty,
	 * it uses names of the form &lt;prefix&gt;.&lt;attributename&gt;.
	 * Note that to store the properties in a file you need
	 * to call @see write()
	 *
	 * @param object object to store
	 * @param prefix property name prefix
	 */
    public void store(Configurable object) {
        String prefix = object.getPropertyPrefix();
        if (prefix != "" && !prefix.endsWith(".")) {
            prefix += ".";
        }
        PropertyDescriptor propertyDescriptors[] = null;
        try {
            BeanInfo beaninfo = Introspector.getBeanInfo(object.getClass());
            propertyDescriptors = beaninfo.getPropertyDescriptors();
        } catch (IntrospectionException ie) {
            System.err.println("could not get bean info: " + ie.getMessage());
        }
        if (propertyDescriptors != null) {
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor pd = propertyDescriptors[i];
                String attribute = pd.getName();
                Method getter = pd.getReadMethod();
                Method setter = pd.getWriteMethod();
                if (getter != null && setter != null) {
                    try {
                        Object value = getter.invoke(object, null);
                        if (value != null) {
                            properties.setProperty(prefix + attribute, value.toString());
                        }
                    } catch (IllegalAccessException iae) {
                    } catch (InvocationTargetException ite) {
                    } catch (NullPointerException npe) {
                    }
                }
            }
        }
    }

    private void exit(String message) {
        System.out.println(message);
        System.out.println("use -h to get help");
        System.exit(-1);
    }

    /**
	 * to avoid using Log4J before it is initialized we do lazy creation here
	 */
    private static Logger getLogger() {
        if (log == null) {
            log = Logger.getLogger(Configurator.class);
        }
        return log;
    }
}
