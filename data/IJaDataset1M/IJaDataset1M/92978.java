package openrpg2.common.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * ORPGSettingManager offers a simple interface to handle saving and loading of Properties for the current user.
 * @author snowdog
 */
public class ORPGSettingManager {

    private static Logger log = Logger.getLogger(ORPGSettingManager.class.getName());

    /** Creates a new instance of ORPGSetting */
    public ORPGSettingManager() {
    }

    /**
     * Attempts to load properties from a file with the given name from the users OpenRPG settings directory.
     * 
     * @param filename The simple name of the setting file to load (ie 'mysettings.properties')
     * @return Properties Object. Will be empty if file not found.
     * @see ORPGUserDirs
     */
    public static Properties loadSettings(String filename) {
        File pf = new File(ORPGUserDirs.getSettingDir(true) + filename);
        Properties p = loadSettings(pf);
        if (p == null) {
            log.info("Using empty settings for \"" + filename + "\"");
            p = new Properties();
        }
        return p;
    }

    /**
     * Attempts to load properties from a file with the given name from the users OpenRPG settings directory.
     * If the file does not exist then the properties from the supplied File reference will be used instead.
     * @param filename The simple name of the setting file to load (ie 'mysettings.properties')
     * @param template File object representing a default properties template to load if the setting file (filename) does not exist
     * @return Properties Object. If the filename does not exist it will return the properties from the template file instead. If the template file cannot be used an empty properties object will be returned.
     */
    public static Properties loadSettings(String filename, File template) {
        File pf = new File(ORPGUserDirs.getSettingDir(true) + filename);
        Properties p = loadSettings(pf);
        if (p == null) {
            log.info("Using default template file for \"" + filename + "\" settings");
            p = loadSettings(template);
            if (p == null) {
                log.info("Using empty settings for \"" + filename + "\"");
                p = new Properties();
            }
        }
        return p;
    }

    /**
     * Attempts to load properties from a file with the given name from the users OpenRPG settings directory.
     * If the load process fails the supplied template properties object will be returned instead.
     * @param filename The simple name of the setting file to load (ie 'mysettings.properties')
     * @param template Properties object representing a default properties template to use if the setting file (filename) does not exist
     * @return Properties Object. Either the contents of the specified file or the default template properties object.
     */
    public static Properties loadSettings(String filename, Properties template) {
        File pf = new File(ORPGUserDirs.getSettingDir(true) + filename);
        Properties p = loadSettings(pf);
        if (p == null) {
            log.info("Using default settings template for \"" + filename + "\"");
            p = template;
        }
        return p;
    }

    /**
     * Attempts to load properties from the given File.
     * 
     * @param file reference properties file
     * @return Properties Object. Will return null if file not found.
     */
    public static Properties loadSettings(File file) {
        Properties p = new Properties();
        if (file.exists()) {
            try {
                p.load(new FileInputStream(file));
            } catch (FileNotFoundException ex) {
                log.info("Setting file \"" + file.getName() + "\" cannot be found.");
                return null;
            } catch (IOException ex) {
                log.warning("IOException reading setting file \"" + file.getName() + "\" (" + ex.getMessage() + ")");
                ex.printStackTrace();
                return null;
            }
        }
        return p;
    }

    /**
     * Saves the supplied Properties object to the users OpenRPG2 settings directory using the supplied filename. In the event of a filename conflict the existing file will be overwritten.
     * @param p Properties object to save.
     * @param filename Simple filename to use when saving properties (ie. 'mysettings.properties'}
     * @return true on success. false on fail
     */
    public static boolean saveSettings(Properties p, String filename) {
        File pf = new File(ORPGUserDirs.getSettingDir(true) + filename);
        try {
            p.store(new FileOutputStream(pf), null);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
