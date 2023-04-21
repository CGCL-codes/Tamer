    @Override
    protected void validate() {
        if (isValid()) return;
        super.validate();
        if (getAdapter() == null) {
            String defaultAdapterId = getService().getDefaultAdapter();
            if (defaultAdapterId != null) {
                createAdapter(defaultAdapterId);
            } else {
                invalidate();
                ConfigurationException ex = new ConfigurationException();
                ex.setMessage(ConfigurationConstants.DEST_NEEDS_ADAPTER, new Object[] { getId() });
                throw ex;
            }
        }
        if (channelIds != null) {
            List<String> brokerChannelIds = getService().getMessageBroker().getChannelIds();
            for (Iterator<String> iter = channelIds.iterator(); iter.hasNext(); ) {
                String id = iter.next();
                if (brokerChannelIds == null || !brokerChannelIds.contains(id)) {
                    iter.remove();
                    if (Log.isWarn()) {
                        Log.getLogger(getLogCategory()).warn("No channel with id '{0}' is known by the MessageBroker." + " Removing the channel.", new Object[] { id });
                    }
                }
            }
        }
        if (channelIds == null) {
            List<String> defaultChannelIds = getService().getDefaultChannels();
            if (defaultChannelIds != null && defaultChannelIds.size() > 0) {
                setChannels(defaultChannelIds);
            } else {
                invalidate();
                ConfigurationException ex = new ConfigurationException();
                ex.setMessage(ConfigurationConstants.DEST_NEEDS_CHANNEL, new Object[] { getId() });
                throw ex;
            }
        }
        MessageBroker broker = getService().getMessageBroker();
        if (securityConstraint == null && securityConstraintRef != null) {
            securityConstraint = broker.getSecurityConstraint(securityConstraintRef);
        }
        ClusterManager cm = broker.getClusterManager();
        if (getNetworkSettings().getClusterId() != null || cm.getDefaultClusterId() != null) {
            cm.clusterDestination(this);
        }
    }
