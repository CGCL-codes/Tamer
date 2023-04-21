    public List<NodeChannel> getChannels() {
        if (engine != null) {
            IConfigurationService configService = (IConfigurationService) engine.getApplicationContext().getBean(Constants.CONFIG_SERVICE);
            return configService.getChannelsFor(true);
        } else {
            return new ArrayList<NodeChannel>(0);
        }
    }
