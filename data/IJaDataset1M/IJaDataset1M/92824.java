package fr.xebia.springframework.jms.support.converter;

import java.lang.reflect.Method;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.xml.bind.Marshaller;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.jms.support.converter.MessageConversionException;

/**
 * <p>
 * Tibco Enterprise Messaging Service (EMS) implementation of the <code>JaxbMessageConverter</code>. Keeps generated XML encoding in sync
 * with {@link com.tibco.tibjms.TibjmsMessage#_encoding} field.
 * </p>
 * <p>
 * We use reflection to load {@link com.tibco.tibjms.Tibjms#setMessageEncoding(Message, String)} method to be able to compile without Tibco
 * EMS jar.
 * </p>
 * 
 * @see com.tibco.tibjms.Tibjms#setMessageEncoding(Message, String)
 * @author <a href="mailto:cyrille.leclerc@pobox.com">Cyrille Le Clerc</a>
 */
public class JaxbMessageConverterTibjmsImpl extends JaxbMessageConverter {

    private static final String TIBJMS_CLASS = "com.tibco.tibjms.Tibjms";

    private static final String TIBJMS_SET_MESSAGE_ENCODING_METHOD = "setMessageEncoding";

    protected Method setMessageEncodingMethod;

    /**
     * @throws MessageConversionException
     *             if a problem occurs loading {@link com.tibco.tibjms.Tibjms#setMessageEncodingMethod} method.
     */
    public JaxbMessageConverterTibjmsImpl() {
        super();
        try {
            Class<?> tibJmsClass = Class.forName(TIBJMS_CLASS);
            this.setMessageEncodingMethod = tibJmsClass.getMethod(TIBJMS_SET_MESSAGE_ENCODING_METHOD, new Class[] { Message.class, String.class });
        } catch (Exception e) {
            throw new MessageConversionException(NestedExceptionUtils.buildMessage("Exception loading Tibjms class", e), e);
        }
    }

    /**
     * Set given <code>message</code> encoding with Tibco proprietary APIs.
     * 
     * @see com.tibco.tibjms.Tibjms#setMessageEncoding(javax.jms.Message, String)
     * @see fr.xebia.springframework.jms.support.converter.JaxbMessageConverter#postProcessResponseMessage(Message)
     */
    @Override
    protected void postProcessResponseMessage(Message textMessage) throws JMSException {
        super.postProcessResponseMessage(textMessage);
        String encoding = this.marshallerProperties == null ? null : (String) this.marshallerProperties.get(Marshaller.JAXB_ENCODING);
        encoding = encoding == null ? "UTF-8" : encoding;
        try {
            this.setMessageEncodingMethod.invoke(null, new Object[] { textMessage, encoding });
        } catch (Exception e) {
            MessageConversionException jmse = new MessageConversionException(NestedExceptionUtils.buildMessage("Exception setting message encoding", e), e);
            throw jmse;
        }
    }
}
