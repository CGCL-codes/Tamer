    public Bot spawn(Class<? extends Bot> botClass, String name, List<BoticelliPlugin> plugins) throws Exception {
        Bot child = botClass.newInstance();
        child.setPlugins(plugins);
        child.setBeanName(name);
        child.setSessionFactory(getSessionFactory());
        child.setServer(this.server);
        child.setPort(this.port);
        child.setChannelName(getChannelName());
        child.setNick(name);
        child.afterPropertiesSet();
        return child;
    }
