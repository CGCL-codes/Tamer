    protected Node getChannelNode(Document doc) {
        NodeList childs = doc.getChildNodes();
        Node channel = childs.item(0).getChildNodes().item(0);
        return channel;
    }
