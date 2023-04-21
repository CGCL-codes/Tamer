package org.opencms.xml;

import org.opencms.cache.CmsVfsMemoryObjectCache;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.i18n.CmsMessages;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsMacroResolver;
import org.opencms.util.CmsStringUtil;
import org.opencms.xml.content.CmsXmlContent;
import org.opencms.xml.content.CmsXmlContentFactory;
import java.util.Locale;
import org.apache.commons.logging.Log;

/**
 * The xml messages overwrite some methods of the general CmsMessages class to get keys from an individual configuration file.<p>
 * 
 * As fallback if no file was specified or no value was found for the desired key,
 * a common CmsMessages object is used to get the localized value.<p>
 * 
 * @author Andreas Zahner
 * 
 * @version $Revision: 1.4 $ 
 * 
 * @since 6.5.4 
 */
public class CmsXmlMessages extends CmsMessages {

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsXmlMessages.class);

    /** The locale to use for getting localized String values. */
    private Locale m_locale;

    /** The content holding the localized values. */
    private CmsXmlContent m_localizationContent;

    /** The initialized messages used as fallback if no value was found in the configuration file. */
    private CmsMessages m_messages;

    /** The (optional) xPath prefix to the element nodes. */
    private String m_pathPrefix;

    /**
     * Constructor, with parameters.<p>
     * 
     * Creates the necessary member objects using the passed arguments.<p>
     * 
     * @param messages the messages object to use as fallback
     * @param configurationFileName the absolute path (including site root!) to the configuration file containing localized keys
     * @param pathPrefix the (optional) xPath prefix to the element nodes
     * @param locale the locale to use for localization
     */
    public CmsXmlMessages(CmsMessages messages, String configurationFileName, String pathPrefix, Locale locale) {
        m_messages = messages;
        initLocalizationContent(configurationFileName);
        initPathPrefix(pathPrefix);
        m_locale = locale;
    }

    /**
     * Constructor, with parameters.<p>
     * 
     * Creates the necessary member objects using the passed arguments.<p>
     * 
     * @param bundleName the name of the ResourceBundle to use
     * @param configurationFileName the absolute path (including site root!) to the configuration file containing localized keys
     * @param pathPrefix the (optional) xPath prefix to the element nodes
     * @param locale the locale to use for localization
     */
    public CmsXmlMessages(String bundleName, String configurationFileName, String pathPrefix, Locale locale) {
        m_messages = new CmsMessages(bundleName, locale);
        initLocalizationContent(configurationFileName);
        initPathPrefix(pathPrefix);
        m_locale = locale;
    }

    /**
     * Returns the messages.<p>
     *
     * @return the messages
     */
    public CmsMessages getMessages() {
        return m_messages;
    }

    /**
     * Returns the localized resource String from the configuration file, if not found or set from the resource bundle.<p>
     * 
     * @see org.opencms.i18n.CmsMessages#key(java.lang.String)
     */
    public String key(String keyName) {
        if (hasConfigValue(keyName)) {
            return getConfigValue(keyName);
        }
        return m_messages.key(keyName);
    }

    /**
     * Returns the localized resource String from the configuration file, if not found or set from the resource bundle.<p>
     * 
     * @see org.opencms.i18n.CmsMessages#key(java.lang.String, java.lang.Object)
     */
    public String key(String key, Object arg0) {
        if (hasConfigValue(key)) {
            return getConfigValue(key, new Object[] { arg0 });
        }
        return m_messages.key(key, arg0);
    }

    /**
     * Returns the localized resource String from the configuration file, if not found or set from the resource bundle.<p>
     * 
     * @see org.opencms.i18n.CmsMessages#key(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public String key(String key, Object arg0, Object arg1) {
        if (hasConfigValue(key)) {
            return getConfigValue(key, new Object[] { arg0, arg1 });
        }
        return m_messages.key(key, arg0, arg1);
    }

    /**
     * Returns the localized resource String from the configuration file, if not found or set from the resource bundle.<p>
     * 
     * @see org.opencms.i18n.CmsMessages#key(java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public String key(String key, Object arg0, Object arg1, Object arg2) {
        if (hasConfigValue(key)) {
            return getConfigValue(key, new Object[] { arg0, arg1, arg2 });
        }
        return m_messages.key(key, arg0, arg1, arg2);
    }

    /**
     * Returns the localized resource String from the configuration file, if not found or set from the resource bundle.<p>
     * 
     * @see org.opencms.i18n.CmsMessages#key(java.lang.String, java.lang.Object[])
     */
    public String key(String key, Object[] args) {
        if (hasConfigValue(key)) {
            return getConfigValue(key, args);
        }
        return m_messages.key(key, args);
    }

    /**
     * Returns the localized resource String from the configuration file, if not found or set from the resource bundle.<p>
     * 
     * @see org.opencms.i18n.CmsMessages#keyDefault(java.lang.String, java.lang.String)
     */
    public String keyDefault(String keyName, String defaultValue) {
        if (hasConfigValue(keyName)) {
            return getConfigValue(keyName);
        }
        return m_messages.keyDefault(keyName, defaultValue);
    }

    /**
     * Sets the messages.<p>
     *
     * @param messages the messages
     */
    public void setMessages(CmsMessages messages) {
        m_messages = messages;
    }

    /**
     * Returns the value for the given key from the configuration file.<p>
     * @param key the key to get the value for
     * @return the value for the given key
     */
    protected String getConfigValue(String key) {
        if (m_localizationContent != null) {
            try {
                return m_localizationContent.getStringValue(null, m_pathPrefix + key, m_locale);
            } catch (NullPointerException e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(Messages.get().getBundle().key(Messages.ERR_NULL_CMSOBJECT_0));
                }
            }
        }
        return null;
    }

    /**
     * Returns the substituted value for the given key and arguments from the configuration file.<p>
     * 
     * @param key the key to get the value for
     * @param args the arguments that should be substituted
     * @return the substituted value for the given key and arguments
     */
    protected String getConfigValue(String key, Object[] args) {
        String value = getConfigValue(key);
        CmsMacroResolver resolver = CmsMacroResolver.newInstance();
        for (int i = 0; i < args.length; i++) {
            resolver.addMacro(String.valueOf(i), args[i].toString());
        }
        return resolver.resolveMacros(value);
    }

    /**
     * Checks if the given key is provided in the configuration file.<p>
     * 
     * @param key the key to check
     * @return true if the given key is provided in the configuration file, otherwise false
     */
    protected boolean hasConfigValue(String key) {
        return CmsStringUtil.isNotEmptyOrWhitespaceOnly(getConfigValue(key));
    }

    /**
     * Initializes the content used for localizing the output.<p>
     * 
     * @param configurationFileName the absolute path including site root to the configuration file containing localized keys
     */
    protected void initLocalizationContent(String configurationFileName) {
        CmsObject cms = null;
        try {
            cms = OpenCms.initCmsObject(OpenCms.getDefaultUsers().getUserGuest());
        } catch (CmsException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(Messages.get().getBundle().key(Messages.ERR_INVALID_INIT_USER_1, OpenCms.getDefaultUsers().getUserGuest()));
            }
        }
        if (cms != null && CmsStringUtil.isNotEmptyOrWhitespaceOnly(configurationFileName)) {
            Object o = CmsVfsMemoryObjectCache.getVfsMemoryObjectCache().getCachedObject(cms, configurationFileName);
            if (o != null) {
                m_localizationContent = (CmsXmlContent) o;
                return;
            }
            try {
                CmsFile configFile = cms.readFile(configurationFileName);
                m_localizationContent = CmsXmlContentFactory.unmarshal(cms, configFile);
                CmsVfsMemoryObjectCache.getVfsMemoryObjectCache().putCachedObject(cms, configurationFileName, m_localizationContent);
            } catch (CmsException e) {
            }
        }
    }

    /**
     * Initializes the (optional) xPath prefix to the element nodes.<p>
     * 
     * @param pathPrefix the (optional) xPath prefix to the element nodes
     */
    protected void initPathPrefix(String pathPrefix) {
        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(pathPrefix)) {
            m_pathPrefix = pathPrefix;
        } else {
            m_pathPrefix = "";
        }
    }
}
