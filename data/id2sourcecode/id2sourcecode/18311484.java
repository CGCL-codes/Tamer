    protected void setUp() throws Exception {
        super.setUp();
        if (bus != null) return;
        String properties = null;
        String multicastIP = null;
        if ((properties == null) && (multicastIP == null)) multicastIP = DEFAULT_MULTICAST_IP;
        if (properties == null) properties = DEFAULT_CHANNEL_PROPERTIES_PRE + multicastIP.trim() + DEFAULT_CHANNEL_PROPERTIES_POST; else properties = properties.trim();
        if (log.isInfoEnabled()) log.info("Starting a new JavaGroups broadcasting listener with properties=" + properties);
        bus = new NotificationBus(BUS_NAME, properties);
        bus.start();
        bus.getChannel().setOpt(Channel.LOCAL, new Boolean(false));
        bus.setConsumer(this);
        CacheManager.create(Thread.currentThread().getContextClassLoader().getResource("/ehcache.xml"));
        CacheManager.getInstance().addCache("cacheTest");
        log.info("JavaGroups clustering support started successfully");
    }
