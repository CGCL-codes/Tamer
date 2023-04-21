package org.openacs.message;

import org.openacs.Message;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

/**
 *
 * @author Administrator
 */
public class Upload extends Message {

    /** Creates a new instance of Upload */
    public Upload() {
        name = "Upload";
        Username = "";
        Password = "";
        DelaySeconds = 0;
        FileType = FT_CONFIG;
        URL = "http://192.168.1.1:8080/acs-war/upload/tst.cfg";
        CommandKey = "default.command.key";
    }

    protected void createBody(SOAPBodyElement body, SOAPFactory spf) throws SOAPException {
        body.addChildElement(COMMAND_KEY).setValue(CommandKey);
        body.addChildElement("FileType").setValue(FileType);
        body.addChildElement("URL").setValue(URL);
        body.addChildElement("Username").setValue(Username);
        body.addChildElement("Password").setValue(Password);
        body.addChildElement("DelaySeconds").setValue(String.valueOf(DelaySeconds));
    }

    protected void parseBody(SOAPBodyElement body, SOAPFactory f) throws SOAPException {
    }

    public String CommandKey;

    public String FileType;

    public String URL;

    public String Username;

    public String Password;

    public int DelaySeconds;

    public static final String FT_CONFIG = "1 Vendor Configuration File";

    public static final String FT_LOG = "2 Vendor Log File";
}
