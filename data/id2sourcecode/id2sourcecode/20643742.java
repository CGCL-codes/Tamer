    public void testResolveChannelNames() throws Exception {
        MessageRecipients messageRecipients = new MessageRecipients();
        String userPrefix = "bozo";
        String userSuffix = "@volantis.com";
        String channelName;
        for (int n = 9; n > 0; n--) {
            InternetAddress address = new InternetAddress(userPrefix + n + userSuffix);
            if (n % 2 == 0) {
                channelName = "Ardvark";
            } else {
                channelName = null;
            }
            MessageRecipient messageRecipient = new MessageRecipient(address, "Outlook");
            messageRecipient.setChannelName(channelName);
            PrivateAccessor.setField(messageRecipient, "messageRecipientInfo", new MessageRecipientInfoTestHelper());
            messageRecipients.addRecipient(messageRecipient);
        }
        messageRecipients.resolveChannelNames(false);
        Iterator i = messageRecipients.getIterator();
        int tot = 0;
        while (i.hasNext()) {
            MessageRecipient messageRecipient = (MessageRecipient) i.next();
            int n;
            if (tot < 4) {
                n = (tot + 1) * 2;
            } else {
                n = (tot - 4) * 2 + 1;
            }
            InternetAddress testAddress = new InternetAddress(userPrefix + n + userSuffix);
            assertEquals("Wrong recipient ", testAddress.getAddress(), messageRecipient.getAddress().getAddress());
            if (n % 2 == 0) {
                assertEquals("Channel name has been overriden", "Ardvark", messageRecipient.getChannelName());
            } else {
                assertEquals("Channel name has been overriden", "smtp", messageRecipient.getChannelName());
            }
            tot++;
        }
        assertEquals("Expecting 9 recipients", tot, 9);
        messageRecipients.resolveChannelNames(true);
        i = messageRecipients.getIterator();
        tot = 0;
        while (i.hasNext()) {
            tot++;
            MessageRecipient messageRecipient = (MessageRecipient) i.next();
            InternetAddress testAddress = new InternetAddress(userPrefix + tot + userSuffix);
            assertEquals("Wrong recipient ", testAddress.getAddress(), messageRecipient.getAddress().getAddress());
            assertEquals("Channel name has not been overriden", messageRecipient.getChannelName(), "smtp");
        }
        assertEquals("Expecting 9 recipients got ", tot, 9);
    }
