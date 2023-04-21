    private void defaultChannel(Node chan, ServiceSettings serviceSettings) {
        String ref = getAttributeOrChildElement(chan, REF_ATTR);
        if (ref.length() > 0) {
            ChannelSettings channel = config.getChannelSettings(ref);
            if (channel != null) {
                serviceSettings.addDefaultChannel(channel);
            } else {
                ConfigurationException e = new ConfigurationException();
                e.setMessage(REF_NOT_FOUND, new Object[] { CHANNEL_ELEMENT, ref });
                throw e;
            }
        } else {
            ConfigurationException ex = new ConfigurationException();
            ex.setMessage(INVALID_DEFAULT_CHANNEL, new Object[] { serviceSettings.getId() });
            throw ex;
        }
    }
