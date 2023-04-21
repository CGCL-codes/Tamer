    public void getChannelsList(Document d) throws IOException, ParserConfigurationException {
        NodeList list = d.getElementsByTagName("tv");
        Element root = (Element) list.item(0);
        if (root == null) {
            root = d.createElement("tv");
            d.appendChild(root);
        }
        Set channels = read();
        Iterator it = channels.iterator();
        while (it.hasNext()) {
            Channel channel = (Channel) it.next();
            root.appendChild(channel.createElement(d));
        }
    }
