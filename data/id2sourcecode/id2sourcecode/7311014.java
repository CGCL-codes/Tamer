    public String getChannelFactoryName() {
        if (gravityConfig.getChannelFactory() != null) return gravityConfig.getChannelFactory().getClass().getName();
        return null;
    }
