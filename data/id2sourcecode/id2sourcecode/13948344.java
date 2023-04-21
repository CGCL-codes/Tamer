    private static void join(SGSServer server, SGSChannelData d, String a) {
        try {
            ClientSession session = server.getClientSession(a);
            Channel channel = d.getChannel();
            channel.join(session, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
