    public static ISOChannel getChannel(Node node) {
        Node n = node.getAttributes().getNamedItem("name");
        if (n != null) try {
            return ConfigChannel.getChannel(n.getNodeValue());
        } catch (NameRegistrar.NotFoundException e) {
        }
        return null;
    }
