    public void start() throws ServiceException {
        if (isLocalMode()) {
            if (logger.isInfoEnabled()) {
                logger.info("[INFO] We are in local mode, we do not start this service. Current service state " + this.state.get());
                logger.info("[INFO] In local mode, we do not create the JGroups channel.");
                logger.info("[INFO] If you plan to use this service in a cluster environment (integration or production), " + "please go to your JMX console and change to false the local mode [setLocalMode(false)] " + "and invoke start method.");
            }
            return;
        }
        if (this.jgProperties != null && this.jgProperties.startsWith(DEFAULT_CHANNEL_PROPERTIES_PRE)) {
            int indexPre = this.jgProperties.indexOf(DEFAULT_CHANNEL_PROPERTIES_PRE);
            int indexPost = this.jgProperties.indexOf(DEFAULT_CHANNEL_PROPERTIES_POST);
            if (indexPost > -1) {
                this.multicastIP = this.jgProperties.substring(indexPre, indexPost);
            }
        }
        if (this.multicastIP != null && this.jgProperties == null) {
            StringBuffer sb = new StringBuffer(DEFAULT_CHANNEL_PROPERTIES_PRE);
            sb.append(this.multicastIP.trim()).append(DEFAULT_CHANNEL_PROPERTIES_POST);
            this.jgProperties = sb.toString();
        }
        if (this.jgProperties == null) {
            StringBuffer sb = new StringBuffer(DEFAULT_CHANNEL_PROPERTIES_PRE);
            sb.append(DEFAULT_MULTICAST_IP).append(DEFAULT_CHANNEL_PROPERTIES_POST);
            this.jgProperties = sb.toString();
        }
        try {
            this.notificationBus = new NotificationBus(this.clusterName, this.jgProperties);
            this.notificationBus.setConsumer(this);
            this.notificationBus.getChannel().setOpt(Channel.LOCAL, Boolean.TRUE);
            this.notificationBus.start();
            if (this.persistenceManager != null) {
                this.persistenceManager.create();
                this.persistenceManager.start();
            }
            this.registry = new ServiceStateRegistry();
            if (this.notificationBus.isCoordinator()) {
                if (this.persistenceManager != null) {
                    Map states = this.persistenceManager.retrieveAll();
                    notifyServiceStates(states);
                } else {
                    if (logger.isInfoEnabled()) {
                        logger.info("JGroups Persistence Manager not available. " + "No synchronization with persistence storage available");
                    }
                }
            }
            Serializable cache = this.notificationBus.getCacheFromCoordinator(timeout, 1);
            if (cache instanceof Map) {
                Map states = (Map) cache;
                notifyServiceStates(states);
            }
        } catch (InstanceNotFoundException e) {
            if (logger.isInfoEnabled()) {
                logger.info("JGroups NotificationBus exception at start :: JMX Instance not found", e);
            }
        } catch (Exception e) {
            if (logger.isInfoEnabled()) {
                logger.info("JGroups NotificationBus exception at start of this service", e);
            }
            throw new ServiceException("Problems statring the Notification Bus", e);
        }
        if (isJmxPresent) {
            registerChannel();
        }
        this.state.set(Service.SERVICE_STARTED);
    }
