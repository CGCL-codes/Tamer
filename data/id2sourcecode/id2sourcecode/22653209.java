    public static ISOChannel getChildChannel(Node node) throws ConfigurationException {
        ISOChannel channel = null;
        NodeList childs = node.getChildNodes();
        for (int i = 0; i < childs.getLength() && channel == null; i++) {
            Node n = childs.item(i);
            if (n.getNodeName().equals("channel")) channel = ConfigChannel.getChannel(n);
        }
        if (channel == null) throw new ConfigurationException("could not find channel");
        return channel;
    }
