    public void config(QSP qsp, Node node) throws ConfigurationException {
        ISOChannel channel;
        LogEvent evt = new LogEvent(qsp, "config-channel");
        String name = node.getAttributes().getNamedItem("name").getNodeValue();
        try {
            channel = getChannel(name);
        } catch (NameRegistrar.NotFoundException e) {
            channel = createChannel(name, node, evt);
            channel.setName(name);
        }
        try {
            qsp.registerMBean(channel, "type=channel,name=" + name);
        } catch (NotCompliantMBeanException e) {
            evt.addMessage(e.getMessage());
        } catch (Exception e) {
            evt.addMessage(e);
            throw new ConfigurationException(e);
        } finally {
            Logger.log(evt);
        }
    }
