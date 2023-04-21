    public void testKeepAliveTime() throws Exception {
        System.out.println("Setting keep alive count to 1 second");
        for (int i = 0; i < channels.length; i++) {
            ReplicationTransmitter t = (ReplicationTransmitter) channels[0].getChannelSender();
            t.getTransport().setKeepAliveTime(1000);
        }
        sendMessages(2000, 15000);
    }
