    public void testClone() throws Exception {
        MessageRecipient messageRecipient = new MessageRecipient();
        InternetAddress internetAddressOrig = new InternetAddress("ianw@volantis.com");
        InternetAddress internetAddressNew = new InternetAddress("mat@volantis.com");
        String channelNameOrig = "smtp";
        String channelNameNew = "smsc";
        String deviceNameOrig = "Outlook";
        String deviceNameNew = "SMS Hamdset";
        String msISDNOrig = "12345678";
        String msISDNNew = "87654321";
        messageRecipient.setAddress(internetAddressOrig);
        messageRecipient.setChannelName(channelNameOrig);
        messageRecipient.setDeviceName(deviceNameOrig);
        messageRecipient.setMSISDN(msISDNOrig);
        MessageRecipient clone = (MessageRecipient) messageRecipient.clone();
        assertEquals("Cloned address not equal", messageRecipient.getAddress(), clone.getAddress());
        assertEquals("Cloned channel name not equal", messageRecipient.getChannelName(), clone.getChannelName());
        assertEquals("Cloned device name not equal", messageRecipient.getDeviceName(), clone.getDeviceName());
        assertEquals("Cloned MSISDN not equal", messageRecipient.getMSISDN(), clone.getMSISDN());
        messageRecipient.setAddress(internetAddressNew);
        messageRecipient.setChannelName(channelNameNew);
        messageRecipient.setDeviceName(deviceNameNew);
        messageRecipient.setMSISDN(msISDNNew);
        assertFalse("Cloned address equal", messageRecipient.getAddress().equals(clone.getAddress()));
        assertFalse("Cloned channel name equal", messageRecipient.getChannelName().equals(clone.getChannelName()));
        assertFalse("Cloned device name equal", messageRecipient.getDeviceName().equals(clone.getDeviceName()));
        assertFalse("Cloned MSISDN equal", messageRecipient.getMSISDN().equals(clone.getDeviceName()));
    }
