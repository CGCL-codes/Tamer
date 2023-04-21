    public void connect() {
        if (isConnected()) {
            return;
        }
        String protocolString = (String) registration.getReference().getProperty(JGroupsConstants.JGROUPS_PROTOCOLSTRING);
        if (protocolString == null) protocolString = JGroupsActivator.INSTANCE.getBundleContext().getProperty(JGroupsConstants.JGROUPS_PROTOCOLSTRING);
        if (protocolString == null) protocolString = JGroupsConstants.DEFAULT_PROTOCOLSTRING;
        try {
            log.log(LogService.LOG_INFO, "JGroups protocol = " + protocolString);
            bus = new NotificationBus(topic, protocolString);
            bus.getChannel().addChannelListener(listener);
            bus.setConsumer(consumer);
            bus.getChannel().setOpt(JChannel.AUTO_RECONNECT, Boolean.TRUE);
            bus.start();
            localPeer = new BusPeerImpl(bus.getLocalAddress(), this);
        } catch (Exception e) {
            e.printStackTrace();
            log.log(LogService.LOG_ERROR, e.getMessage(), e);
        }
    }
