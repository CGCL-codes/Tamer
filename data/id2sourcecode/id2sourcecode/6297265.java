    public void unregister(DataSession dataSession, ControlSession controlSession, Service service, Protocol protocol, MBeanServer server, String domain) throws Exception {
        domain = domain.replace(':', '-');
        JGroupsDataSession jgroupsDataSession = (JGroupsDataSession) dataSession;
        JChannel channel = jgroupsDataSession.getChannel(service);
        String clusterName = channel.getClusterName();
        String name = domain + ":type=channel,cluster=" + clusterName;
        JmxConfigurator.unregisterChannel(server, name);
        String tmp = domain + ":type=protocol,cluster=" + clusterName;
        JmxConfigurator.unregisterProtocols(server, channel, tmp);
    }
