    public void testResolveChannelName() throws Exception {
        MessageRecipient messageRecipient = new MessageRecipient();
        PrivateAccessor.setField(messageRecipient, "messageRecipientInfo", new MessageRecipientInfoTestHelper());
        messageRecipient.resolveChannelName(false);
        assertEquals("Resolved channel not equal to default", "smtp", messageRecipient.getChannelName());
        messageRecipient.setChannelName("aardvark");
        messageRecipient.resolveChannelName(false);
        assertEquals("Resolved channel overrode current value", "aardvark", messageRecipient.getChannelName());
        messageRecipient.resolveChannelName(true);
        assertEquals("Resolved channel did not override current value", "smtp", messageRecipient.getChannelName());
    }
