    public void testSendImpl() throws Exception {
        NowSMSWAPPushChannelAdapter channel = new NowSMSWAPPushChannelAdapter(CHANNEL_NAME, createChannelInfo()) {

            protected boolean sendAsGetRequest(String paramString) throws IOException, MalformedURLException {
                return !(paramString.startsWith("PhoneNumber=%2B44failure"));
            }
        };
        MultiChannelMessage multiChannelMessage = new MultiChannelMessageImpl();
        multiChannelMessage.setMessageURL(new URL("http://test.message/file"));
        multiChannelMessage.setSubject("Test Message");
        MessageRecipients messageRecipients = createTestRecipients();
        MessageRecipient messageSender = new MessageRecipient(new InternetAddress("mps@volantis.com"), "Master");
        MessageRecipients failures = channel.sendImpl(multiChannelMessage, messageRecipients, messageSender);
        assertNotNull("Should be a failure list, even if it is empty", failures);
        assertTrue("Expected some failures", failures.getIterator().hasNext());
        for (Iterator i = failures.getIterator(); i.hasNext(); ) {
            MessageRecipient recipient = (MessageRecipient) i.next();
            System.out.println("Failure:\n");
            assertEquals("Failed recipient MSISDN should match", "failure", recipient.getMSISDN());
            assertEquals("Failed recipient channel name should match", CHANNEL_NAME, recipient.getChannelName());
        }
    }
