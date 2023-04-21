    public void testSetChannelName() throws Exception {
        MessageRecipient messageRecipient = new MessageRecipient();
        String channelName = "SMTP";
        messageRecipient.setChannelName(channelName);
        if (!messageRecipient.getChannelName().equals(channelName)) {
            fail("ChannelName not equal");
        }
    }
