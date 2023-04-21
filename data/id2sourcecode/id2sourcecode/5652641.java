    @Override
    public void initialiseThis(Channel channel) throws ConfigurationException {
        logger.debug("initialising channel: " + channel.getChannelName());
        if (channel.supportsInbound()) {
            logger.debug(channel.getChannelName() + " supports inbound so setting up JMS listener.");
            JMSMessageListener listener = new JMSMessageListener(channel, this);
            try {
                Destination destination = _bootstrapJMS.getJmsDestination(channel.getChannelName());
                _bootstrapJMS.registerMessageHandler(listener, destination);
            } catch (NamingException e) {
                throw new ConfigurationException("Failed to find channel [" + channel.getChannelName() + "]. " + e.getMessage(), e);
            } catch (JMSException e) {
                throw new ConfigurationException("Failed registed channel [" + channel.getChannelName() + "] with JMS server.");
            }
        } else {
            logger.warn(channel.getChannelName() + " does not support inbound so not setting up a listener.");
        }
    }
