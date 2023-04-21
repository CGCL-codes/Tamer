    private static void destroyClassListener(SGSServer server, String name) {
        try {
            ChannelManager manager = AppContext.getChannelManager();
            Channel channel = null;
            try {
                channel = manager.getChannel(name);
                channel.close();
            } catch (NameNotBoundException ex) {
            }
            server.removeChannel(name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
