    @PostConstruct
    public void init() {
        logger.info("Initializing CacheUpdater ...");
        lChannel = this.getCache().getChannelList();
    }
