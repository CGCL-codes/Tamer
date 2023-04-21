    public void testChannelNames() throws Exception {
        final String anotherChannel = "Mozilla";
        DeviceMessageAttachment ma = new DeviceMessageAttachment(VALUE, MIME, VALUE_TYPE, DEVICE_NAME, CHANNEL_NAME);
        String retrieved = ma.getChannelName();
        assertEquals("Values should match", CHANNEL_NAME, retrieved);
        try {
            ma.setChannelName(null);
            fail("Previous call should have caused an exception");
        } catch (MessageException me) {
        }
        ma.setChannelName(anotherChannel);
        retrieved = ma.getChannelName();
        assertEquals("Values should match", anotherChannel, retrieved);
    }
