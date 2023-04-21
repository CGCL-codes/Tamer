package com.sun.mail.dsn;

import java.io.IOException;
import java.io.OutputStream;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import at.dasz.KolabDroid.glue.DataFlavor;

/**
 * DataContentHandler for message/disposition-notification MIME type.
 * Applications should not use this class directly, it's used indirectly
 * through the JavaBeans Activation Framework.
 *
 * @since	JavaMail 1.4.2
 */
public class message_dispositionnotification implements DataContentHandler {

    ActivationDataFlavor ourDataFlavor = new ActivationDataFlavor(DispositionNotification.class, "message/disposition-notification", "Disposition Notification");

    /**
     * return the DataFlavors for this <code>DataContentHandler</code>
     * @return The DataFlavors.
     */
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { ourDataFlavor };
    }

    /**
     * return the Transfer Data of type DataFlavor from InputStream
     * @param df The DataFlavor.
     * @param ins The InputStream corresponding to the data.
     * @return a Message object
     */
    public Object getTransferData(DataFlavor df, DataSource ds) throws IOException {
        if (ourDataFlavor.equals(df)) return getContent(ds); else return null;
    }

    /**
     * Return the content.
     */
    public Object getContent(DataSource ds) throws IOException {
        try {
            return new DispositionNotification(ds.getInputStream());
        } catch (MessagingException me) {
            throw new IOException("Exception creating DispositionNotification in " + "message/disposition-notification DataContentHandler: " + me.toString());
        }
    }

    /**
     */
    public void writeTo(Object obj, String mimeType, OutputStream os) throws IOException {
        if (obj instanceof DispositionNotification) {
            DispositionNotification dn = (DispositionNotification) obj;
            try {
                dn.writeTo(os);
            } catch (MessagingException me) {
                throw new IOException(me.toString());
            }
        } else {
            throw new IOException("unsupported object");
        }
    }
}
