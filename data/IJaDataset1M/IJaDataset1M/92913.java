package org.mobicents.protocols.smpp.util;

import static org.testng.Assert.assertNotNull;
import java.util.MissingResourceException;
import org.mobicents.protocols.smpp.util.APIConfigFactory;
import org.mobicents.protocols.smpp.util.APIMessages;
import org.mobicents.protocols.smpp.util.PropertiesAPIConfig;
import org.testng.annotations.Test;

@Test
public class APIMessagesTest {

    public void testAPIMessagesWorksWithNoBundle() throws Exception {
        try {
            PropertiesAPIConfig cfg = new PropertiesAPIConfig();
            cfg.initialise();
            cfg.setProperty(APIMessages.BUNDLE_PROPERTY, "non_existent");
            APIConfigFactory.setCachedConfig(cfg);
            APIMessages messages = new APIMessages();
            assertNotNull(messages.getPacketStatus(8));
        } finally {
            APIConfigFactory.reset();
        }
    }

    public void testGetPacketStatusReturnsValidValue() throws Exception {
        APIMessages messages = new APIMessages();
        assertNotNull(messages.getPacketStatus(2));
    }

    @Test(expectedExceptions = MissingResourceException.class)
    public void testGetPacketStatusThrowsExceptionOnUnrecognizedStatus() throws Exception {
        APIMessages messages = new APIMessages();
        messages.getPacketStatus(Integer.MAX_VALUE);
    }
}
