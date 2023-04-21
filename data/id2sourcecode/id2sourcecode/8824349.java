    public void stop() {
        if (this.persistenceManager != null) {
            this.persistenceManager.stop();
            this.persistenceManager.destroy();
        }
        if (this.notificationBus != null) {
            try {
                MBeanServer server = MBeanServerLocator.locateMBeanServer();
                Channel channel = this.notificationBus.getChannel();
                ObjectName channelName = this.getObjectName(channel);
                server.unregisterMBean(channelName);
                this.unregisterProtocols();
            } catch (JMException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("JMX Exception in stop, just printing", e);
                }
            } finally {
                this.notificationBus.stop();
                this.notificationBus = null;
            }
        }
        this.state.set(Service.SERVICE_STOPPED);
    }
