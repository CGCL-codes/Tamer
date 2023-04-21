    public void testKeepAliveCount() throws Exception {
        System.out.println("Setting keep alive count to 0");
        for (int i = 0; i < channels.length; i++) {
            ReplicationTransmitter t = (ReplicationTransmitter) channels[0].getChannelSender();
            t.getTransport().setKeepAliveCount(0);
        }
        sendMessages(1000, 15000);
    }
