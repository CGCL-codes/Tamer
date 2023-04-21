    public void afterPropertiesSet() throws Exception {
        channel = bayeuxServer.getBayeux().getChannel(name, true);
        for (BayeuxFilter filter : filters) {
            channel.addDataFilter(new DataFilterAdapter(filter));
        }
    }
