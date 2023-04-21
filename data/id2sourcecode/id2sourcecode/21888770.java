    public void config(QSP qsp, Node node) throws ConfigurationException {
        LogEvent evt = new LogEvent(qsp, "config-filter");
        Node parent;
        if ((parent = node.getParentNode()) == null) throw new ConfigurationException("orphan filter");
        ISOChannel c = ConfigChannel.getChannel(parent);
        if (c == null) throw new ConfigurationException("null parent channel");
        if (!(c instanceof FilteredChannel)) throw new ConfigurationException("not a filtered channel");
        FilteredChannel channel = (FilteredChannel) c;
        NamedNodeMap attr = node.getAttributes();
        String className = attr.getNamedItem("class").getNodeValue();
        String direction = attr.getNamedItem("direction").getNodeValue();
        ISOFilter filter = (ISOFilter) ConfigUtil.newInstance(className);
        if (filter instanceof Configurable) {
            try {
                ((Configurable) filter).setConfiguration(new SimpleConfiguration(ConfigUtil.addProperties(node, null, evt)));
            } catch (ISOException e) {
                throw new ConfigurationException(e);
            }
        }
        if (filter instanceof NodeConfigurable) {
            try {
                ((NodeConfigurable) filter).setConfiguration(node);
            } catch (ISOException e) {
                throw new ConfigurationException(e);
            }
        }
        if (direction.equals("incoming")) channel.addIncomingFilter(filter); else if (direction.equals("outgoing")) channel.addOutgoingFilter(filter); else channel.addFilter(filter);
        evt.addMessage("parent-channel=" + channel.getName());
        Logger.log(evt);
    }
