    private static boolean createClassListener(SGSServer server, String name, Hashtable<String, Serializable> props) {
        try {
            ChannelManager manager = AppContext.getChannelManager();
            SGSChannelData data = new SGSChannelData(props);
            SGSClassListener l = new SGSClassListener();
            if (server.addChannel(name, data)) {
                try {
                    manager.createChannel(name, l, Delivery.RELIABLE);
                    data.set(SGSChannelData.NAME, name);
                    data.set(SGSChannelData.WHITEBOARD_DATA, new HashSet<AbstractDrawing>());
                    return true;
                } catch (NameExistsException ex) {
                    server.removeChannel(name);
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
