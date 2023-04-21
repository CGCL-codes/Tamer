    public void register(DataSession dataSession, ControlSession controlSession, Service service, Protocol protocol, MBeanServer server, String domain) throws Exception {
        domain = domain.replace(':', '-');
        JGroupsDataSession jgroupsDataSession = (JGroupsDataSession) dataSession;
        JChannel channel = jgroupsDataSession.getChannel(service);
        org.jgroups.jmx.JmxConfigurator.registerChannel(channel, server, domain, channel.getClusterName(), true);
    }
