    public void start(String configuration) {
        try {
            String properties = getProperties(configuration);
            bus = new NotificationBus(BUS_NAME, properties);
            bus.start();
            bus.getChannel().setOpt(Channel.LOCAL, new Boolean(false));
            bus.setConsumer(this);
            log.info("JavaGroups clustering support started successfully");
        } catch (Exception e) {
            throw new ClusterCacheException("Initialization failed: ", e);
        }
    }
